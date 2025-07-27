import { useEffect, useState } from "react";
import { collection, query, where, getDocs, updateDoc, doc, deleteDoc, addDoc, orderBy, limit, onSnapshot  } from "firebase/firestore";
import { db } from "../servicios/firebase";
import './horariod.css'

const Horario = () => {
  const [horarios, setHorarios] = useState([]);
  const [nuevaFechaHora, setNuevaFechaHora] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [diaSemana, setDiaSemana] = useState("MONDAY");
  const [horaInicio, setHoraInicio] = useState("");
  const [horaFin, setHoraFin] = useState("");
  const [intervalo, setIntervalo] = useState(30);



  useEffect(() => {
    const storedId = localStorage.getItem("uid");
    const idUser = localStorage.getItem("idUser");
    if (!storedId) return;

    setDoctorId(storedId);
    cargarHorarios(idUser);
  }, []);




const cargarHorarios = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/doctor/${id}/horarios`);
    if (!response.ok) throw new Error("Error al cargar horarios");

    const data = await response.json();

    // Convierte la fecha si es necesario
    const lista = data.map(h => ({
      ...h,
      fecha: new Date(h.fecha)
    }));

    setHorarios(lista);
  } catch (error) {
    console.error("Error al cargar horarios:", error);
  }
};




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
  } catch (error) {
    console.error("Error al cambiar estado:", error);
  }
};



  const crearDisponibilidad = async () => {
  if (!horaInicio || !horaFin || !doctorId) return;

  const body = {
    diaSemana,
    horaInicio,
    horaFin,
    doctorId: doctorId,
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

const eliminarHorario = async (idHorario) => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/horario/${idHorario}`, {
      method: "DELETE"
    });

    if (!response.ok) throw new Error("Error al eliminar el horario");
    alert("Horario eliminado correctamente");
    cargarHorarios(localStorage.getItem("idUser"));
  } catch (error) {
    console.error("Error al eliminar horario:", error);
    alert("No se pudo eliminar el horario");
  }
};



  return (
    <div className="contenedorhorarios">
      <h2>Horarios del Doctor</h2>

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

      <button  onClick={crearDisponibilidad}>Crear Disponibilidad</button>
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

export default Horario;
