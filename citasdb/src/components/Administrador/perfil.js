import { useEffect, useState } from "react";
import { auth} from "../servicios/firebase.js";
import { onAuthStateChanged } from "firebase/auth";
import './Ajuste.css';
const PerfilD = () => {
  const [correo, setCorreo] = useState("");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [cedula, setCedula] = useState("");
  const [direccion, setDireccion] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [docId, setDocId] = useState("");



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

  return () => unsubscribe(); // limpiar listener al desmontar
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
      fechaNacimiento, // puedes enviar como string ISO: "2025-07-19"
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
    <>
     <div className="ajuste-container">
        
        <div>
        <h2>Datos Personales</h2>

       <div className="ajuste-form-group-row">
            <div className="ajuste-form-left">
                <label>Correo electrónico:</label>
                <input type="email" value={correo} onChange={(e) => setCorreo(e.target.value)} disabled/>
            </div>
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
            <label>Fecha de Nacimiento:</label>
            <input type="date" value={fechaNacimiento} onChange={(e) => setFechaNacimiento(e.target.value)} />
        </div>
        <div className="ajuste-form-right">
                <button className="ajuste-btn-guardar" onClick={handleGuardar}>
                Guardar Cambios
                </button>
        </div>
        </div>
    </div>
    </>
  );
};

export default PerfilD;
