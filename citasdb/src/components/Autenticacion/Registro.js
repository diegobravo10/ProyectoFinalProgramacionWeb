import { useState, useEffect } from "react";
import { db, auth } from "../servicios/firebase.js";
import { doc, setDoc } from "firebase/firestore";
import './registro.css'
import { Timestamp } from "firebase/firestore";
const Registro = () => {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [cedula, setCedula] = useState("");
  const [correo, setCorreo] = useState("");
  const [direccion, setDireccion] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [uid, setUid] = useState("");
  const [rol, setRol] = useState("paciente");  // Por defecto paciente

  useEffect(() => {
    const storedUid = localStorage.getItem("uid");
    const storedEmail = localStorage.getItem("email");

    if (storedUid && storedEmail) {
      setUid(storedUid);
      setCorreo(storedEmail);
    } else if (auth.currentUser) {
      setUid(auth.currentUser.uid);
      setCorreo(auth.currentUser.email);
    } else {
      alert("No hay usuario autenticado.");
      window.location.href = "/";
    }
  }, []);

  const handleSubmit = async (e) => {
  e.preventDefault();

  if (!nombre || !apellido || !cedula || !direccion || !telefono || !fechaNacimiento) {
    alert("Por favor, completa todos los campos.");
    return;
  }

  const fechaComoTimestamp = fechaNacimiento
    ? Timestamp.fromDate(new Date(fechaNacimiento + "T12:00:00"))
    : null;

  try {
    // Guardar en Firestore
    await setDoc(doc(db, "users", uid), {
      uid,
      correo,
      nombre,
      apellido,
      cedula,
      direccion,
      telefono,
      fechaNacimiento: fechaComoTimestamp,
      rol,
    });

    // Enviar al backend (asegúrate de que la URL sea correcta)
    const response = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/usuarios", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        uid,
        correo,
        nombre,
        apellido,
        cedula,
        direccion,
        telefono,
        fechaNacimiento, // se envía como string yyyy-mm-dd
        rol,
      }),
    });

    const result = await response.json();
    if (response.ok) {
      alert("Registro exitoso!");
      window.location.href = rol === "doctor" ? "/doctor" : "/paciente";
    } else {
      console.error(result);
      alert("Error al guardar en el backend: " + result.mensaje);
    }
  } catch (error) {
    console.error("Error general al registrar:", error);
    alert("Error al guardar los datos.");
  }
};


  return (
    <div className="registro-container">
      <h2>Registro</h2>
      <p>Estimado paciente, por favor complete el siguiente formulario con sus datos personales.</p>
      <form onSubmit={handleSubmit} className="registro-form">
        <div>
          <label>Correo electrónico:</label>
          <input type="email" value={correo} disabled />
        </div>
        <div>
          <label>Nombres:</label>
          <input type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} />
        </div>
        <div>
          <label>Apellidos:</label>
          <input type="text" value={apellido} onChange={(e) => setApellido(e.target.value)} />
        </div>
        <div>
          <label>Cédula:</label>
          <input
            type="text"
            value={cedula}
            onChange={(e) => setCedula(e.target.value)}
            maxLength={10}
            pattern="\d{10}"
            title="Ingrese solo números (10 dígitos)"
          />
        </div>
        <div>
          <label>Dirección:</label>
          <input type="text" value={direccion} onChange={(e) => setDireccion(e.target.value)} />
        </div>
        <div>
          <label>Teléfono:</label>
          <input type="text" value={telefono} onChange={(e) => setTelefono(e.target.value)} />
        </div>
        <div>
          <label>Fecha de Nacimiento:</label>
          <input type="date" value={fechaNacimiento} onChange={(e) => setFechaNacimiento(e.target.value)} />
        </div>
        <button type="submit">Registrar</button>
      </form>
    </div>

  );
};

export default Registro;
