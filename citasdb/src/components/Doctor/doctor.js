import { useState, useEffect } from "react";
import { collection, getDocs, query, where, updateDoc, doc, getDoc, addDoc, onSnapshot } from "firebase/firestore";
import { db } from "../servicios/firebase.js";
import axios from "axios";

import './doctor.css'

const Doctor = () => {
  const [especialidades, setEspecialidades] = useState([]);
  const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("");
  const [doctores, setDoctores] = useState([]);
  const [doctorSeleccionado, setDoctorSeleccionado] = useState("");

  const [citas, setCitas] = useState([]);

  // Datos relacionados para mostrar nombres y horarios
  const [pacientesMap, setPacientesMap] = useState({});
  const [horariosMap, setHorariosMap] = useState({});

  const [editandoCitaId, setEditandoCitaId] = useState(null);
  const [descripcionEdit, setDescripcionEdit] = useState("");
  const [fechaHoraEdit, setFechaHoraEdit] = useState(""); 

  // Estados para validaciones
  const [cargandoValidacion, setCargandoValidacion] = useState(false);
  const [errorValidacion, setErrorValidacion] = useState("");

  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [docId, setDocId] = useState("");
  const [pacientes, setPacientes] = useState([]);
  const [horariosDisponibles, setHorariosDisponibles] = useState([]);
   const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");


  // Cargar datos del usuario logeado
useEffect(() => {
  const storedId = localStorage.getItem("uid");
  if (storedId) {
    setDocId(storedId);
    const cargarDatos = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/citasmedicas/citasmedicas/doctor/uid/${storedId}`);
        
        const datos = response.data;
        setNombre(datos.nombre || "");
        setApellido(datos.apellido || "");
      } catch (error) {
        console.error("Error al obtener datos del doctor desde el backend:", error);
      }
    };
    cargarDatos();
  }
}, []);


  // Función helper para convertir Timestamp a string (Fecha y Hora)
  //Ejemplo: "04/06/2025, 15:30"
  const formatearFecha = (timestamp) => {
    if (!timestamp) return "";

    if (typeof timestamp === 'string') {
      const date = new Date(timestamp);
      if (isNaN(date.getTime())) return ""; 
      return date.toLocaleString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }

    if (timestamp && timestamp.seconds) {
      const fecha = new Date(timestamp.seconds * 1000);
      return fecha.toLocaleString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }

    if (timestamp instanceof Date) {
      return timestamp.toLocaleString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
    return "";
  };

  // Función helper para convertir Timestamp a formato YYYY-MM-DDTHH:MM para input 
  const timestampToDateTimeInput = (timestamp) => {
    if (!timestamp) return "";

    let date;
    if (typeof timestamp === 'string') {
      date = new Date(timestamp);
    } else if (timestamp && timestamp.seconds) {
      date = new Date(timestamp.seconds * 1000);
    } else if (timestamp instanceof Date) {
      date = timestamp;
    } else {
      return "";
    }

    if (isNaN(date.getTime())) return "";

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };

  // Cargar pacientes 
  useEffect(() => {
    const cargarPacientes = async () => {
      const q = query(collection(db, "users"), where("rol", "==", "pacient"));
      const snapshot = await getDocs(q);
      setPacientes(snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() })));
    };
    cargarPacientes();
  }, []);

  // Cargar especialidades
  useEffect(() => {
    const cargarEspecialidades = async () => {
      const snapshot = await getDocs(collection(db, "especialidad"));
      setEspecialidades(snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() })));
    };
    cargarEspecialidades();
  }, []);

  // Cuando cambia especialidad, cargar doctores
  useEffect(() => {
    if (!especialidadSeleccionada) {
      setDoctores([]);
      setDoctorSeleccionado("");
      return;
    }
    const cargarDoctores = async () => {
      const q = query(collection(db, "users"), where("especialidadid", "==", especialidadSeleccionada));
      const snapshot = await getDocs(q);
      setDoctores(snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() })));
      setDoctorSeleccionado("");
      setCitas([]);
      setPacientesMap({});
      setHorariosMap({});
    };
    cargarDoctores();
  }, [especialidadSeleccionada]);



useEffect(() => {
  const storedId = localStorage.getItem("uid");
  if (!storedId) return;

  const cargarCitas = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/citasmedicas/citasmedicas/citas/doctor/${storedId}/conDetalles`);
      const citasConDetalles = response.data;

      // Separar citas, pacientes y horarios
      setCitas(citasConDetalles);

      // Mapear pacientes
      const pacientesMap = {};
      citasConDetalles.forEach(cita => {
        if (cita.paciente) {
          pacientesMap[cita.paciente.idUser] = cita.paciente;
        }
      });
      setPacientesMap(pacientesMap);

      // Mapear horarios
      const horariosMap = {};
      citasConDetalles.forEach(cita => {
        if (cita.horario) {
          horariosMap[cita.horario.idHorario] = cita.horario;
        }
      });
      setHorariosMap(horariosMap);

    } catch (error) {
      console.error("Error al obtener citas del doctor:", error);
    }
  };

  cargarCitas();
}, []);


  // Actualizar estado de cita
const actualizarEstadoCita = async (idCita, nuevoEstado, idHorario) => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/citas/${idCita}/estado`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        nuevoEstado,
        idHorario
      })
    });

    if (!response.ok) {
      throw new Error("Error actualizando estado de la cita");
    }

    // Actualizar localmente el estado en React
    setCitas(citas.map(c =>
      c.id === idCita ? { ...c, estado: nuevoEstado } : c
    ));

  } catch (error) {
    console.error("Error al actualizar estado de cita:", error);
  }
};


  // Validar disponibilidad de horario (ahora con fecha y hora)
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

      // Si encuentra horarios en esa fecha y hora
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

    if (!fechaHoraEdit) {
      setErrorValidacion("Debe seleccionar un horario disponible.");
      return;
    }

    const body = {
      idHorarioNuevo: fechaHoraEdit, // ID del horario seleccionado
      descripcion: descripcionEdit
    };
     console.log("Enviando body para editar cita:", body); 

    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/citas/${idCita}/editar`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      throw new Error("Error al guardar la edición de la cita");
    }

    const citaActualizada = await response.json();

    // Actualizar estado local de citas con la respuesta del backend
    setCitas(prev =>
      prev.map(c => c.id === idCita ? citaActualizada : c)
    );

    setEditandoCitaId(null);
    alert("Cita actualizada correctamente");

  } catch (error) {
    console.error("Error al guardar edición:", error);
    setErrorValidacion("Error al actualizar la cita.");
  }
};


  const cargarHorarios = async (id) => {
  try {
    const response = await fetch(
      `http://localhost:8080/citasmedicas/citasmedicas/horario/doctor/${id}/horarios`
    );
    if (!response.ok) throw new Error("Error al cargar horarios");

    const data = await response.json();

    // Convertir fecha y filtrar por disponibilidad
    const lista = data
      .map(h => ({
        ...h,
        fecha: new Date(h.fecha)
      }))
      .filter(h => h.disponible === true);  // <-- Filtrado aquí

    setHorariosDisponibles(lista);
  } catch (error) {
    console.error("Error al cargar horarios:", error);
  }
};


const descargarReporte = async (tipo, fechaInicio, fechaFin) => {


    if (!fechaInicio) {
    alert("Por favor ingrese la fecha de inicio.");
    return;
  }
  if (!fechaFin) {
    alert("Por favor ingrese la fecha de fin.");
    return;
  }

  // Opcional: Validar que fechaFin no sea menor que fechaInicio
  if (new Date(fechaFin) < new Date(fechaInicio)) {
    alert("La fecha fin no puede ser anterior a la fecha inicio.");
    return;
  }
  const endpoint = `http://localhost:8080/citasmedicas/citasmedicas/reportes/${tipo}`;

  const params = { fechaInicio, fechaFin };
  if (tipo === "doctor") {
    const doctorId = parseInt(localStorage.getItem("idUser"), 10);
    params.doctorId = doctorId;

  }

  console.log(params);

  const response = await axios.get(endpoint, {
    params,
    responseType: 'blob'
  });

  const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', `reporte-${tipo}.pdf`);
  document.body.appendChild(link);
  link.click();
};



  return (
    <div>
      
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          
          <div>

          </div>
          <h1>
            Hola, {nombre.split(" ")[0]} {apellido.split(" ")[0]} 
          </h1>

          <div style={{
              display: "flex",
              alignItems: "center",
              gap: "15px",
              background: "#f8f9fa",
              padding: "15px 20px",
              borderRadius: "10px",
              boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
          }}>
            <div style={{ display: "flex", flexDirection: "column" }}>
              <label style={{ fontWeight: "bold", marginBottom: "5px" }}>Fecha Inicio</label>
              <input
                type="date"
                value={fechaInicio}
                onChange={(e) => setFechaInicio(e.target.value)}
                style={{
                  padding: "8px",
                  border: "1px solid #ccc",
                  borderRadius: "5px"
                }}
              />
            </div>

            <div style={{ display: "flex", flexDirection: "column" }}>
              <label style={{ fontWeight: "bold", marginBottom: "5px" }}>Fecha Fin</label>
              <input
                type="date"
                value={fechaFin}
                onChange={(e) => setFechaFin(e.target.value)}
                style={{
                  padding: "8px",
                  border: "1px solid #ccc",
                  borderRadius: "5px"
                }}
              />
            </div>

            <button
              onClick={() => descargarReporte("doctor", fechaInicio, fechaFin)}
              style={{
                backgroundColor: "#4CAF50",
                color: "white",
                fontSize: "16px",
                fontWeight: "bold",
                padding: "10px 20px",
                border: "none",
                borderRadius: "5px",
                cursor: "pointer",
                transition: "0.3s"
              }}
              onMouseOver={(e) => (e.target.style.backgroundColor = "#45a049")}
              onMouseOut={(e) => (e.target.style.backgroundColor = "#4CAF50")}
            >
              📥 Reporte
            </button>
          </div>

        </div>

        <hr />

      <h2>Citas</h2>
      <table>
        <thead>
          <tr>
            <th>Paciente</th>
            <th>Fecha y Hora</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {citas.length > 0 ? (
            citas
              .sort((a, b) => {
                const fechaA = new Date(a.horario?.fecha || 0);
                const fechaB = new Date(b.horario?.fecha || 0);
                return fechaB - fechaA;
              })
              .map(cita => {
                const paciente = cita.paciente;
                const horario = cita.horario;

                return (
                  <tr key={cita.id}>
                    <td>{paciente ? `${paciente.nombre} ${paciente.apellido}` : "Desconocido"}</td>

                    <td>
                      {editandoCitaId === cita.id ? (
                        <div>
                          <select
                              value={fechaHoraEdit}
                              onChange={e => setFechaHoraEdit(e.target.value)}
                            >
                              <option value="">Seleccione un horario</option>
                              {horariosDisponibles.map(h => (
                                <option key={h.idHorario} value={h.idHorario}>
                                  {new Date(h.fecha).toLocaleString()}
                                </option>
                              ))}
                            </select>
                            {errorValidacion && (
                              <div style={{ color: 'red', fontSize: '12px', marginTop: '4px' }}>
                                {errorValidacion}
                              </div>
                            )}

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
                      {editandoCitaId === cita.id ? (
                        <input
                          type="text"
                          value={descripcionEdit}
                          onChange={e => setDescripcionEdit(e.target.value)}
                        />
                      ) : (
                        cita.descripcion || "Sin descripción"
                      )}
                    </td>

                    <td className={
                      cita.estado === "confirmado" ? "texto-confirmado" :
                      cita.estado === "rechazado" ? "texto-rechazado" :
                      cita.estado === "pendiente" ? "texto-pendiente" : ""
                    }>
                      {cita.estado || "pendiente"}
                    </td>

                    <td>
                      {editandoCitaId === cita.id ? (
                        <>
                          <button onClick={() => guardarEdicion(cita.id)} disabled={cargandoValidacion}>
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
                          <button onClick={() => {
                            setEditandoCitaId(cita.id);
                            setDescripcionEdit(cita.descripcion || "");
                            setFechaHoraEdit("");
                            setErrorValidacion("");
                            cargarHorarios(localStorage.getItem("idUser"));
                          }}>Modificar</button>

                          <button onClick={() => actualizarEstadoCita(cita.id, "confirmado", horario?.idHorario)}>Confirmar</button>
                          <button className="botonR" onClick={() => actualizarEstadoCita(cita.id, "rechazado", horario?.idHorario)}>Rechazar</button>
                        </>
                      )}
                    </td>
                  </tr>
                )
              })
          ) : (
            <tr>
              <td colSpan="5">No hay citas para el doctor seleccionado.</td>
            </tr>
          )}
        </tbody>
      </table>

      <hr/>

    </div>
  );
};

export default Doctor;