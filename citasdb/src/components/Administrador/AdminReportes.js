import React, { useState } from "react";
import axios from "axios";

const AdminReportes = () => {
  const [fechaInicio, setFechaInicio] = useState("");
  const [fechaFin, setFechaFin] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [especialidad, setEspecialidad] = useState("");

  const descargarReporte = async (tipo) => {
    if (!fechaInicio || !fechaFin) {
      alert("Por favor seleccione la fecha de inicio y fecha fin");
      return;
    }

    let endpoint = `http://localhost:8080/citasmedicas/citasmedicas/reportes/${tipo}`;
    let params = { fechaInicio, fechaFin };

    if (tipo === "doctor") {
      if (!doctorId) {
        alert("Debe ingresar el ID del doctor");
        return;
      }
      params.doctorId = doctorId;
    }

    if (tipo === "especialidad") {
      if (!especialidad) {
        alert("Debe ingresar la especialidad");
        return;
      }
    params.especialidad = especialidad.trim() === "" ? null : especialidad.trim();


    }

    try {
      const response = await axios.get(endpoint, { params, responseType: "blob" });
      const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `reporte-${tipo}.pdf`);
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      console.error("Error al generar el reporte", error);
      alert("No se pudo generar el reporte");
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: "40px auto", padding: 20, border: "1px solid #ccc", borderRadius: 10 }}>
      <h2 style={{ textAlign: "center", marginBottom: 20 }}>Generación de Reportes</h2>

      {/* Selección de fechas */}
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 20 }}>
        <div>
          <label>Fecha Inicio: </label>
          <input type="date" value={fechaInicio} onChange={(e) => setFechaInicio(e.target.value)} />
        </div>
        <div>
          <label>Fecha Fin: </label>
          <input type="date" value={fechaFin} onChange={(e) => setFechaFin(e.target.value)} />
        </div>
      </div>

      {/* Reporte Doctor */}
      <div style={{ marginBottom: 20 }}>
        <label>ID Doctor: </label>
        <input type="text" placeholder="Ej: 1" value={doctorId} onChange={(e) => setDoctorId(e.target.value)} />
        <button style={botonEstilo} onClick={() => descargarReporte("doctor")}>
          📥 Generar Reporte Doctor
        </button>
      </div>

      {/* Reporte Especialidad */}
      <div style={{ marginBottom: 20 }}>
        <label>Especialidad: </label>
        <input
          type="text"
          placeholder="Ej: Cardiología"
          value={especialidad}
          onChange={(e) => setEspecialidad(e.target.value)}
        />
        <button style={botonEstilo} onClick={() => descargarReporte("especialidad")}>
          📥 Generar Reporte Especialidad
        </button>
      </div>

      {/* Reporte General */}
      <div style={{ textAlign: "center" }}>
        <button style={{ ...botonEstilo, width: "100%" }} onClick={() => descargarReporte("general")}>
          📥 Generar Reporte General
        </button>
      </div>
    </div>
  );
};

const botonEstilo = {
  marginLeft: 10,
  cursor: "pointer",
  padding: "6px 12px",
  backgroundColor: "#2F8EF5",
  color: "#fff",
  border: "none",
  borderRadius: 5,
};

export default AdminReportes;
