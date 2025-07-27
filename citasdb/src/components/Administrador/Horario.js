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
const [citas, setCitas] = useState([]);

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
// Agregar un horario al doctor
const agregarHorario = async () => {
  if (!nuevaFechaHora || !doctorSeleccionado) {
    alert("Por favor complete todos los campos");
    return;
  }

  try {
    // Convertir fecha a formato compatible con @JsonFormat del backend
    const fechaFormateada = nuevaFechaHora.replace("T", " ") + ":00"; // yyyy-MM-dd HH:mm:ss

    const horarioData = {
      fecha: fechaFormateada,
      disponible: true,
      disponibilidad: {
        doctor: {
          idUser: parseInt(doctorSeleccionado)
        }
      }
    };

    const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/horario", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(horarioData)
    });

    if (!response.ok) {
      const err = await response.text();
      throw new Error("Error al agregar horario: " + err);
    }

    setNuevaFechaHora("");
    cargarHorarios(doctorSeleccionado);

  } catch (error) {
    console.error("Error al agregar horario:", error);
    alert("Error al agregar horario: " + error.message);
  }
};



//cambiar el estado
  const cambiarEstado = async (horario) => {
  try {
    setCargando(true);

    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/${horario.idHorario}/disponibilidad`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        disponible: !horario.disponibilidad
      })
    });

    if (!response.ok) {
      const err = await response.text();
      throw new Error("Error al actualizar disponibilidad: " + err);
    }

    await cargarHorarios(doctorSeleccionado);
  } catch (error) {
    console.error("Error al cambiar estado:", error);
    alert("Error al cambiar estado: " + error.message);
  } finally {
    setCargando(false);
  }
};


//eliminar un horario
  const eliminarHorario = async (horario) => {
    if (!window.confirm("¿Está seguro de eliminar este horario?")) return;
    try {
      setCargando(true);
      const ref = doc(db, "horarios", horario.id);
      await deleteDoc(ref);
      await cargarHorarios(doctorSeleccionado);
    } catch (error) {
      console.error("Error al eliminar horario:", error);
      alert("Error al eliminar horario: " + error.message);
    } finally {
      setCargando(false);
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

        <input
          type="datetime-local"
          value={nuevaFechaHora}
          onChange={(e) => setNuevaFechaHora(e.target.value)}
          disabled={cargando}
        />

       <button 
          onClick={agregarHorario} 
          disabled={!nuevaFechaHora || !doctorSeleccionado || cargando}
        >
          {cargando ? "Agregando..." : "Agregar"}
        </button>

      </div>

      {cargando && <p>Cargando...</p>}

      

      <table>
        <thead>
          <tr>
            <th>Fecha y Hora</th>
            <th>Disponibilidad</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {horarios.length === 0 && !cargando ? (
            <tr>
              <td colSpan="3" style={{ textAlign: 'center' }}>
                No hay horarios disponibles
              </td>
            </tr>
          ) : (
            horarios.map((h) => (
              
              <tr key={h.id}>
                <td>{h.fecha?.toLocaleString("es-ES") || "Fecha no válida"}</td>
                <td style={{ color: h.disponible ? "green" : "red" }}>
                  {h.disponible ? "Disponible" : "No disponible"}
                </td>
                <td>
                  <button onClick={() => cambiarEstado(h)} disabled={cargando}>
                    Cambiar Estado
                  </button>
                  <button onClick={() => eliminarHorario(h)} disabled={cargando}>
                    Eliminar
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default HorarioAdmin;
