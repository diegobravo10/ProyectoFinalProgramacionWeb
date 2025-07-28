import { useEffect, useState } from "react";
import { collection, query, where, getDocs, updateDoc, doc, deleteDoc, addDoc,onSnapshot  } from "firebase/firestore";
import { auth, provider, db } from "../servicios/firebase.js";
import './horario.css';

const HorarioAdmin = () => {
  const [horarios, setHorarios] = useState([]);
  const [nuevaFechaHora, setNuevaFechaHora] = useState("");
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [especialidades, setEspecialidades] = useState([]);
const [doctores, setDoctores] = useState([]);
const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("");
const [doctorSeleccionado, setDoctorSeleccionado] = useState("");
const [diaSemana, setDiaSemana] = useState("MONDAY");
  const [horaInicio, setHoraInicio] = useState("");
  const [horaFin, setHoraFin] = useState("");
  const [intervalo, setIntervalo] = useState(30);
  const [idDoctor, setIdDoctor] = useState("");

  //cargar especialidades
  useEffect(() => {
  const cargarEspecialidades = async () => {
    try {
      const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/especialidades");
      
      if (!response.ok) {
        throw new Error("Error al obtener especialidades");
      }

      const data = await response.json();
      setEspecialidades(data);  // El backend ya devuelve la lista en formato esperado
    } catch (error) {
      console.error("Error al cargar especialidades:", error);
    }
  };

  cargarEspecialidades();
}, []);

//cargar doctores por su especialidad
useEffect(() => {
  const cargarDoctores = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("http://localhost:8080/citasmedicas/citasmedicas/doctor", {
        headers: {
          'Authorization': 'Bearer ' + token // solo si tu backend lo requiere
        }
      });

      if (!res.ok) throw new Error("Error al obtener doctores");

      const doctoresData = await res.json();

      const formatted = doctoresData.map(doc => ({
        ...doc,
        id: doc.idUser
      }));

      setDoctores(formatted);

    } catch (error) {
      console.error("Error cargando doctores:", error);
    }
  };

  cargarDoctores();
}, []);

// Filtrar doctores por especialidad seleccionada
const doctoresFiltrados = especialidadSeleccionada 
  ? doctores.filter(d => d.especialidad?.nombre === especialidadSeleccionada)
  : doctores;


//obtener los datos del doctor 
 useEffect(() => {
  if (!doctorSeleccionado) return;

  const doc = doctores.find(d => d.id === parseInt(doctorSeleccionado));
  if (doc) {
    setIdDoctor(doc.uid);

  }
}, [doctorSeleccionado, doctores]);



//cargar horarios del doctor
const cargarHorarios = async (doctorId) => {
  try {
    setCargando(true);
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/doctor/${doctorId}/horarios`);

    if (!response.ok) {
      throw new Error("Error al obtener horarios");
    }

    const data = await response.json();

    // Convertir fechas a objetos Date
    const lista = data.map(h => ({
      ...h,
      fecha: new Date(h.fecha),
      
    }));

    lista.sort((a, b) => b.fecha?.getTime() - a.fecha?.getTime());
    setHorarios(lista.slice(0, 30));
  } catch (error) {
    console.error("Error al cargar horarios:", error);
    setError("Error al cargar horarios: " + error.message);
  } finally {
    setCargando(false);
  }
  
};

//funcion para que se muestren los horarios automaticamente
useEffect(() => {
  if (doctorSeleccionado) {
    cargarHorarios(doctorSeleccionado);
  } else {
    setHorarios([]);
  }
}, [doctorSeleccionado]);


//agregar horario
const crearDisponibilidad = async () => {
  if (!horaInicio || !horaFin || !idDoctor) return;

  const body = {
    diaSemana,
    horaInicio,
    horaFin,
    doctorId: idDoctor,
    intervalo: intervalo
  };

  try {
    console.log("Datos enviados:", body);
    const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/disponibilidad/disponibilidades", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) throw new Error("Error al crear disponibilidad");
    alert("Disponibilidad creada correctamente");
    cargarHorarios(localStorage.getItem("idUser"))
  } catch (error) {
    console.error("Error:", error);
    alert("Ocurrió un error");
  }
};



//cambiar el estado
  const cambiarEstado = async (horario) => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/${horario.idHorario}/disponibilidad`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
    
      body: JSON.stringify({ disponible: !horario.disponible }) // objeto con propiedad disponible
    });
    

    if (!response.ok) throw new Error("Error al actualizar disponibilidad");
    cargarHorarios(localStorage.getItem("idUser"));
    cargarHorarios(doctorSeleccionado);
  } catch (error) {
    console.error("Error al cambiar estado:", error);
  }
};


//eliminar un horario
  const eliminarHorario = async (idHorario) => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/${idHorario}`, {
      method: "DELETE"
    });

    if (!response.ok) throw new Error("Error al eliminar el horario");
    alert("Horario eliminado correctamente");
    cargarHorarios(localStorage.getItem("idUser"));
    cargarHorarios(doctorSeleccionado);
  } catch (error) {
    console.error("Error al eliminar horario:", error);
    alert("No se pudo eliminar el horario");
  }
};

  return (
    <div className="contenedorH">
      <h2>Gestión de Horarios</h2>

      {error && (
        <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>
      )}

      <div className="form-selectores">
        <label>
          Especialidad:
          <select
            value={especialidadSeleccionada}
            onChange={(e) => setEspecialidadSeleccionada(e.target.value)}
            disabled={cargando}
          >
            <option value="">Seleccione una especialidad</option>
            {especialidades.map((esp) => (
              <option key={esp.id} value={esp.id}>{esp.nombre}</option>
            ))}
          </select>
        </label>

        <label>
          Doctor:
          <select
            value={doctorSeleccionado}
            onChange={e => setDoctorSeleccionado(e.target.value)}
          >
            <option value="">-- Seleccione doctor --</option>
            {doctoresFiltrados.map(d => (
              <option key={d.id} value={d.id}>{d.nombre} {d.apellido}</option>
            ))}
          </select>
        </label>

          <div>
      <label>Día:</label>
      <select value={diaSemana} onChange={(e) => setDiaSemana(e.target.value)}>
        <option value="MONDAY">Lunes</option>
        <option value="TUESDAY">Martes</option>
        <option value="WEDNESDAY">Miércoles</option>
        <option value="THURSDAY">Jueves</option>
        <option value="FRIDAY">Viernes</option>
        <option value="SATURDAY">Sábado</option>
        <option value="SUNDAY">Domingo</option>
      </select>

      <label>Hora Inicio:</label>
      <input type="time" value={horaInicio} onChange={(e) => setHoraInicio(e.target.value)} />

      <label>Hora Fin:</label>
      <input type="time" value={horaFin} onChange={(e) => setHoraFin(e.target.value)} />

      <label>Intervalo (minutos):</label>
      <input 
        type="number" 
        value={intervalo} 
        min="1"
        onChange={(e) => setIntervalo(e.target.value)} 
      />

      <button className="dispo" onClick={crearDisponibilidad}>Agregar</button>
    </div>

       

    </div>
      

      <table>
        <thead>
          <tr>
            <th>Fecha y Hora</th>
            <th>Disponibilidad</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {horarios.map((h, i) => {
            {/*console.log(h);*/}
            return (
              <tr key={i}>
                <td>{new Date(h.fecha).toLocaleString()}</td>
                <td style={{ color: h.disponible ? "green" : "red" }}>
                  {h.disponible ? "Disponible" : "No disponible"}
                </td>
                <td>
                  <button
                    className="botonE"
                    onClick={() => cambiarEstado(h)}
                  >
                    Cambiar Estado
                  </button>

                  {h.disponible && (
                    <button
                      className="botonEliminar"
                      style={{ marginLeft: "8px", background: "red", color: "white" }}
                      onClick={() => eliminarHorario(h.idHorario)}
                    >
                      Eliminar
                    </button>
                  )}
                </td>

              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default HorarioAdmin;
