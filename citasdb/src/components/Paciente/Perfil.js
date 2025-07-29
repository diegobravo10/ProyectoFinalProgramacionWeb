import { useEffect, useState } from "react";
import { doc, getDoc, updateDoc } from "firebase/firestore";
import { auth, provider, db } from "../servicios/firebase.js";
import { Timestamp } from "firebase/firestore";
import './Ajuste.css';
import { onAuthStateChanged } from "firebase/auth";

const Perfil = () => {
  const [docId, setDocId] = useState("");
  const [correo, setCorreo] = useState("");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [cedula, setCedula] = useState("");
  const [direccion, setDireccion] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [rol, setRol] = useState("");



useEffect(() => {
  const unsubscribe = onAuthStateChanged(auth, async (user) => {
    if (user) {
      const token = await user.getIdToken();

      try {
        const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/usuarios/me", {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        if (!res.ok) {
          console.warn("No se pudo obtener el usuario");
          return;
        }

        const datos = await res.json();

        setCorreo(datos.email || "");
        setNombre(datos.nombre || "");
        setApellido(datos.apellido || "");
        setCedula(datos.cedula || "");
        setDireccion(datos.direccion || "");
        setTelefono(datos.telefono || "");
        setRol(datos.rol);

        if (datos.fechaNacimiento) {
          const fecha = new Date(datos.fechaNacimiento);
          const yyyy = fecha.getFullYear();
          const mm = String(fecha.getMonth() + 1).padStart(2, '0');
          const dd = String(fecha.getDate()).padStart(2, '0');
          setFechaNacimiento(`${yyyy}-${mm}-${dd}`);
        }
      } catch (error) {
        console.error("Error al obtener datos:", error);
      }
    } else {
      console.log("No hay usuario autenticado");
    }
  });

  return () => unsubscribe(); // limpiar listener
}, []);

//Funcion para guardar los datos modificados
  const handleGuardar = async () => {
    const user = auth.currentUser;
    const token = user && await user.getIdToken();

    if (!token) {
    alert("UID no disponible");
    return;
  }
  try {
    
    const payload = {
      correo,
      nombre,
      apellido,
      cedula,
      direccion,
      telefono,
      fechaNacimiento,
      rol,
      uid: localStorage.getItem("uid"),
    };

    const res = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload)
    });

    const result = await res.json();

    if (res.ok) {
      alert("Datos actualizados correctamente.");
    } else {
      console.error(result);
      alert("Error: " + result.message || "No se pudo actualizar.");
    }
  } catch (error) {
    console.error("Error al actualizar:", error);
    alert("Error al guardar los cambios.");
  }
};

  return (
    <div className="ajuste-container">
      <h2>Perfil del Paciente</h2>
      <div className="ajuste-form-group">
        <label>Correo electrónico:</label>
        <input type="email" value={correo} disabled />
      </div>
      <div className="ajuste-form-group">
        <label>Nombres:</label>
        <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} />
      </div>
      <div className="ajuste-form-group">
        <label>Apellidos:</label>
        <input type="text" value={apellido} onChange={(e) => setApellido(e.target.value)} />
      </div>
      <div className="ajuste-form-group">
        <label>Cédula:</label>
        <input type="text" value={cedula} onChange={(e) => setCedula(e.target.value)} />
      </div>
      <div className="ajuste-form-group">
        <label>Dirección:</label>
        <input type="text" value={direccion} onChange={(e) => setDireccion(e.target.value)} />
      </div>
      <div className="ajuste-form-group">
        <label>Teléfono:</label>
        <input type="text" value={telefono} onChange={(e) => setTelefono(e.target.value)} />
      </div>
      <div className="ajuste-form-group">
        <label>Fecha de nacimiento:</label>
        <input type="date" value={fechaNacimiento} onChange={(e) => setFechaNacimiento(e.target.value)} />
      </div>
      <button className="ajuste-btn-guardar" onClick={handleGuardar}>
        Guardar Cambios
      </button>
    </div>
  );
};

export default Perfil;
