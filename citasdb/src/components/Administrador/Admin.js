import { useState, useEffect } from "react";
import { collection, getDocs, query, where, updateDoc, doc, getDoc, addDoc, onSnapshot } from "firebase/firestore";
import { auth, provider, db } from "../servicios/firebase.js";

import './doctor.css'

const Admin = () => {
  const [especialidades, setEspecialidades] = useState([]);
  const [citas, setCitas] = useState([]);

  // Datos relacionados para mostrar nombres, especialidades y horarios
  const [pacientesMap, setPacientesMap] = useState({});
  const [horariosMap, setHorariosMap] = useState({});
  const [doctoresMap, setDoctoresMap] = useState({});
  const [especialidadesMap, setEspecialidadesMap] = useState({});

  const [editandoCitaId, setEditandoCitaId] = useState(null);
  const [descripcionEdit, setDescripcionEdit] = useState("");
  const [fechaHoraEdit, setFechaHoraEdit] = useState("");

  // Estados para validaciones
  const [cargandoValidacion, setCargandoValidacion] = useState(false);
  const [errorValidacion, setErrorValidacion] = useState("");

  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [docId, setDocId] = useState("");
  const [cedula, setCedula] = useState("");

  const [doctores, setDoctores] = useState([]);
  const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("");
  const [doctorSeleccionado, setDoctorSeleccionado] = useState("");

  // Cargar datos del usuario logeado
  useEffect(() => {
  const cargarDatosDesdeBackend = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/usuarios/me", {
        headers: {
          'Authorization': 'Bearer ' + token
        }
      });

      if (!res.ok) throw new Error("No se pudo obtener el usuario");

      const datos = await res.json();

      // Guardar en estado
      setNombre(datos.nombre || "");
      setApellido(datos.apellido || "");
      setCedula(datos.cedula || "");
      setDocId(datos.idUser); // o setCurrentPatientId si lo estás usando

      // Guardar en localStorage si deseas persistir
      localStorage.setItem("nombre", datos.nombre || "");
      localStorage.setItem("apellido", datos.apellido || "");
      localStorage.setItem("cedula", datos.cedula || "");
      localStorage.setItem("idUser", datos.idUser);
      localStorage.setItem("rol", datos.rol);

    } catch (error) {
      console.error("Error al obtener datos del backend:", error);
    }
  };

  cargarDatosDesdeBackend();
}, []);


  // Función helper para convertir Timestamp a string (Fecha y Hora)
  const formatearFecha = (fechaStr) => {
  if (!fechaStr) return "";

  const date = new Date(fechaStr);

  if (isNaN(date.getTime())) return "";

  return date.toLocaleString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};


  // Función helper para convertir Timestamp a formato YYYY-MM-DDTHH:MM para input datetime-local
  const timestampToDateTimeInput = (fechaStr) => {
  if (!fechaStr) return "";

  const date = new Date(fechaStr);
  if (isNaN(date.getTime())) return "";

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return `${year}-${month}-${day}T${hours}:${minutes}`;
};


  // Cargar todas las especialidades
  useEffect(() => {
  const cargarEspecialidades = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/especialidades", {
        headers: {
          'Authorization': 'Bearer ' + token // si tu backend requiere autenticación
        }
      });

      if (!res.ok) throw new Error("Error al obtener especialidades");

      const especialidadesData = await res.json();

      // Estructura: [{ idEspecialidad: 1, nombre: "Odontología" }, ...]
      const formattedEspecialidades = especialidadesData.map(e => ({
        id: e.idEspecialidad,
        nombre: e.nombre
      }));

      setEspecialidades(formattedEspecialidades);

      // Mapa de búsqueda rápida
      const especialidadesMap = {};
      formattedEspecialidades.forEach(esp => {
        especialidadesMap[esp.id] = esp;
      });
      setEspecialidadesMap(especialidadesMap);

    } catch (error) {
      console.error("Error cargando especialidades:", error);
    }
  };

  cargarEspecialidades();
}, []);


  // Cargar todos los doctores
  useEffect(() => {
  const cargarDoctores = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/doctor", {
        headers: {
          'Authorization': 'Bearer ' + token // solo si tu backend lo requiere
        }
      });

      if (!res.ok) throw new Error("Error al obtener doctores");

      const doctoresData = await res.json();

      // Estructura: [{ idUser, nombre, apellido, especialidad: { idEspecialidad, nombre }, ... }]
      const formatted = doctoresData.map(doc => ({
        ...doc,
        id: doc.idUser // para mantener consistencia si lo usas así
      }));

      setDoctores(formatted);

    } catch (error) {
      console.error("Error cargando doctores:", error);
    }
  };

  cargarDoctores();
}, []);


useEffect(() => {
  const cargarTodasLasCitas = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/citas", {
        headers: {
          'Authorization': 'Bearer ' + token
        }
      });

      if (!res.ok) throw new Error("No se pudieron obtener las citas");

      const citas = await res.json();
      setCitas(citas);
    } catch (error) {
      console.error("Error al cargar las citas:", error);
    }
  };

  // 1. Cargar inicialmente
  cargarTodasLasCitas();

  // 2. Suscribirse a eventos SSE
  const eventSource = new EventSource("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/stream-citas");

  eventSource.addEventListener("nueva-cita", (event) => {
    console.log("Evento nueva-cita recibido:", event.data);
    cargarTodasLasCitas(); // vuelve a cargar cuando llega evento
  });

  return () => {
    eventSource.close();
  };
}, []);





  // Actualizar estado de cita
const actualizarEstadoCita = async (idCita, nuevoEstado, idHorario) => {
  try {
    const user = auth.currentUser;
    const token = user && await user.getIdToken();

    const response = await fetch(`https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/citas/${idCita}/estado`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
      body: JSON.stringify({
        nuevoEstado,
        idHorario
      })
    });

    if (!response.ok) throw new Error("Error al actualizar estado de la cita");

    // Actualizar estado local
    setCitas(prevCitas => 
      prevCitas.map(c => c.idCita === idCita ? { ...c, estado: nuevoEstado } : c)
    );

    console.log("Cita actualizada correctamente");

  } catch (error) {
    console.error("Error al actualizar cita:", error);
  }
};




  // Validar disponibilidad de horario
  const validarDisponibilidadHorario = async (doctorId, nuevaFechaHora, horarioIdAExcluir = null) => {
    try {
      setCargandoValidacion(true);
      setErrorValidacion("");

      const fechaHoraObjetivo = new Date(nuevaFechaHora);
      if (isNaN(fechaHoraObjetivo.getTime())) {
        return { disponible: false, mensaje: "Formato de fecha y hora inválido." };
      }

      if (fechaHoraObjetivo < new Date()) {
        return { disponible: false, mensaje: "La fecha y hora de la cita no pueden ser en el pasado." };
      }

      const horariosQuery = query(
        collection(db, "horarios"),
        where("doctorid", "==", doctorId),
        where("fecha", "==", fechaHoraObjetivo)
      );

      const horariosSnapshot = await getDocs(horariosQuery);

      if (!horariosSnapshot.empty) {
        const horariosOcupados = horariosSnapshot.docs.filter(docSnap => {
          const horario = docSnap.data();
          return docSnap.id !== horarioIdAExcluir && horario.estado === "ocupado";
        });

        if (horariosOcupados.length > 0) {
          return { disponible: false, mensaje: "El doctor ya tiene una cita programada a esa fecha y hora." };
        }
      }

      return { disponible: true, mensaje: "" };
    } catch (error) {
      console.error("Error al validar disponibilidad:", error);
      return { disponible: false, mensaje: "Error al validar disponibilidad." };
    } finally {
      setCargandoValidacion(false);
    }
  };

  // Guardar edición de cita
  const guardarEdicion = async (idCita) => {
  try {
    setErrorValidacion("");
    const citaActual = citas.find(c => c.idCita === idCita);
console.log("Cita actual:", citaActual);
console.log("Horario en cita:", citaActual?.horario);
  if (!citaActual || !citaActual.horario) {
    console.error("No se encontró cita u horario");
    setErrorValidacion("Error: cita u horario no definidos.");
    return;
  }
console.log("Cita actual:", citaActual);
console.log("Horario en cita:", citaActual?.horario);
const horarioActual = citaActual.horario;
    const nuevaFechaHoraObj = new Date(fechaHoraEdit);
    if (isNaN(nuevaFechaHoraObj.getTime())) {
      setErrorValidacion("Formato de fecha y hora inválido.");
      return;
    }

    const fechaActualInput = timestampToDateTimeInput(horarioActual?.fecha);
    if (fechaHoraEdit && fechaHoraEdit !== fechaActualInput) {
      // Validar disponibilidad
      const validacion = await validarDisponibilidadHorario(
        citaActual.doctor.idUser,
        fechaHoraEdit,
        citaActual.horario.idHorario
      );

      if (!validacion.disponible) {
        setErrorValidacion(validacion.mensaje);
        return;
      }
    }

    // Convertir fecha a formato ISO para backend (si necesitas enviar la fecha)
    // Si solo estás enviando el id del nuevo horario, este paso no es necesario.

    const response = await fetch(`https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/citas/${idCita}/editar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        idHorarioNuevo: citaActual.horario.idHorario,
        descripcion: descripcionEdit
      })
    });

    if (!response.ok) {
      throw new Error("Error al actualizar la cita");
    }

    const citaActualizada = await response.json();

    // Actualizar estados locales
    setCitas(citas.map(c =>
      c.id === idCita ? {
        ...c,
        descripcion: descripcionEdit,
        horario: {
          ...citaActual.horario,
          fecha: nuevaFechaHoraObj
        }
      } : c
    ));

    setEditandoCitaId(null);

  } catch (error) {
    console.error("Error al guardar edición:", error);
    setErrorValidacion("Error al actualizar la cita.");
  }
};



  // Filtrar doctores por especialidad seleccionada
const doctoresFiltrados = especialidadSeleccionada 
  ? doctores.filter(d => d.especialidad?.idEspecialidad === parseInt(especialidadSeleccionada))
  : doctores;

// Filtrar citas según los selects
const citasFiltradas = citas.filter(cita => {
  const doctor = cita.doctor;

  // Filtro por especialidad
  if (especialidadSeleccionada && doctor?.especialidad?.idEspecialidad !== parseInt(especialidadSeleccionada)) {
    return false;
  }

  // Filtro por doctor
  if (doctorSeleccionado && doctor?.idUser !== parseInt(doctorSeleccionado)) {
    return false;
  }

  return true;
});




  return (
    <div>
      <h1>Hola {nombre.split(" ")[0]} {apellido.split(" ")[0]} </h1>
      
      <div className="inicioescoger">
        <label>Especialidad:</label>
        <select
          value={especialidadSeleccionada}
          onChange={e => {
            setEspecialidadSeleccionada(e.target.value);
            setDoctorSeleccionado(""); 
          }}
        >
          <option value="">-- Todas las especialidades --</option>
          {especialidades.map(e => (
            <option key={e.id} value={e.id}>{e.nombre}</option>
          ))}
        </select>

        <label>Doctor:</label>
        <select
          value={doctorSeleccionado}
          onChange={e => setDoctorSeleccionado(e.target.value)}
        >
          <option value="">-- Todos los doctores --</option>
          {doctoresFiltrados.map(d => (
            <option key={d.id} value={d.id}>{d.nombre} {d.apellido}</option>
          ))}
        </select>
        
      </div>
      <div className="parrafoMai">
        Por favor, seleccionar primero la especialidad y luego el doctor
      </div>
      
      
      <h2>Citas Médicas {especialidadSeleccionada || doctorSeleccionado ? '(Filtradas)' : '(Todas)'}</h2>
      <table>
        <thead>
          <tr>
            <th>Paciente</th>
            <th>Doctor</th>
            <th>Especialidad</th>
            <th>Fecha y Hora</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
  {citasFiltradas.length > 0 ? (
    citasFiltradas
      .sort((a, b) => {
        const fechaA = new Date(a.horario?.fecha || 0);
        const fechaB = new Date(b.horario?.fecha || 0);
        return fechaB - fechaA;
      })
      .map(cita => {
        const paciente = cita.paciente;
        const doctor = cita.doctor;
        const especialidad = doctor?.especialidad || null;
        const horario = cita.horario;

        return (
          <tr key={cita.idCita}>
            <td>{paciente ? `${paciente.nombre} ${paciente.apellido}` : "Sin paciente"}</td>
            <td>{doctor ? `${doctor.nombre} ${doctor.apellido}` : "Sin doctor"}</td>
            <td>{especialidad ? especialidad.nombre : "Sin especialidad"}</td>
            <td>
              {editandoCitaId === cita.idCita ? (
                <div>
                  <input
                    type="datetime-local"
                    value={fechaHoraEdit}
                    onChange={e => {
                      setFechaHoraEdit(e.target.value);
                      setErrorValidacion("");
                    }}
                  />
                  {errorValidacion && (
                    <div style={{ color: 'red', fontSize: '12px', marginTop: '4px' }}>
                      {errorValidacion}
                    </div>
                  )}
                </div>
              ) : (
                horario ? formatearFecha(horario.fecha) : "Sin horario"
              )}
            </td>
            <td>
              {editandoCitaId === cita.idCita ? (
                <input
                  type="text"
                  value={descripcionEdit}
                  onChange={e => setDescripcionEdit(e.target.value)}
                />
              ) : (
                cita.descripcion
              )}
            </td>
            <td className={
                cita.estado === "confirmado" ? "texto-confirmado" :
                cita.estado === "rechazado" ? "texto-rechazado" :
                cita.estado === "pendiente" ? "texto-pendiente" :
                ""
              }>
              {cita.estado}
            </td>
            <td>
              {editandoCitaId === cita.idCita ? (
                <>
                  <button onClick={() => guardarEdicion(cita.idCita)} disabled={cargandoValidacion}>
                    {cargandoValidacion ? "Validando..." : "Guardar"}
                  </button>
                  <button onClick={() => {
                    setEditandoCitaId(null);
                    setErrorValidacion("");
                  }} disabled={cargandoValidacion}>
                    Cancelar
                  </button>
                </>
              ) : (
                <>


                  <button onClick={() => actualizarEstadoCita(cita.idCita, "confirmado", horario?.idHorario)}>Confirmar</button>
                  <button onClick={() => actualizarEstadoCita(cita.idCita, "rechazado", horario?.idHorario)}>Rechazar</button>
                </>
              )}
            </td>
          </tr>
        )
      })
  ) : (
    <tr>
      <td colSpan="7">No hay citas {especialidadSeleccionada || doctorSeleccionado ? 'que coincidan con los filtros seleccionados' : 'registradas'}.</td>
    </tr>
  )}
</tbody>

      </table>
    </div>
  );
};

export default Admin;