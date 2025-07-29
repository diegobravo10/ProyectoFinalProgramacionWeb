import { useEffect, useState } from "react";
import { doc, getDoc, updateDoc, query, collection, where, getDocs, addDoc } from "firebase/firestore";
import { db } from "../servicios/firebase";
import axios from 'axios';

import { Timestamp } from "firebase/firestore";



import './Ajuste.css';
const Perfil = () => {
  const [correo, setCorreo] = useState("");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [cedula, setCedula] = useState("");
  const [direccion, setDireccion] = useState("");
  const [telefono, setTelefono] = useState("");
  const [rol, setRol] = useState("")
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [espid, setEspid] = useState("");
  const [docId, setDocId] = useState("");
  const [idEsp , setIdEsp] = useState("")
  const [especialidades, setEspecialidades] = useState([]);


 useEffect(() => {
  const storedUid = localStorage.getItem("uid");
  if (storedUid) {
    const cargarDatos = async () => {
      try {
        const response = await fetch(`https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/doctor/uid/${storedUid}`);
        if (!response.ok) {
          throw new Error("No se encontró el doctor");
        }

        const datos = await response.json();

        setDocId(datos.uid || ""); // Guardamos el uid si es útil más adelante
        setCorreo(datos.correo || "");
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

        if (datos.especialidad && datos.especialidad.nombre) {
          setEspid(datos.especialidad.nombre);
          setIdEsp(datos.especialidad.idEspecialidad);
        }

      } catch (error) {
        console.error("Error al obtener los datos:", error);
      }
    };

    cargarDatos();
  }
}, []);



useEffect(() => {
  const obtenerEspecialidades = async () => {
    try {
      const querySnapshot = await getDocs(collection(db, "especialidad"));
      const lista = querySnapshot.docs.map(doc => ({
        id: doc.id,
        nombre: doc.data().nombre
      }));
      setEspecialidades(lista);
    } catch (error) {
      console.error("Error al obtener especialidades:", error);
    }
  };

  obtenerEspecialidades();
}, []);


const handleGuardar = async () => {
  const uid = localStorage.getItem("uid");
  if (!uid) {
    alert("UID no disponible");
    return;
  }

  try {
    // Construir el objeto Doctor que se enviará al backend
    const doctor = {
      uid,
      nombre,
      apellido,
      cedula,
      direccion,
      telefono,
      correo,
      rol,
      fechaNacimiento: fechaNacimiento, // yyyy-mm-dd formato ISO
      especialidad: {
        idEspecialidad: parseInt(idEsp) // MUY IMPORTANTE: debe ser int
      }
    };

    const response = await fetch("https://citasmedicas.ngrok.app/citasmedicas/citasmedicas/doctor", {
      method: "POST", // Usa "PUT" si tienes implementado update
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();

    if (response.ok) {
      alert("Datos actualizados correctamente");
    } else {
      alert("Error: " + data.mensaje);
    }
  } catch (error) {
    console.error("Error al guardar:", error);
    alert("Ocurrió un error al guardar los cambios.");
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
                <input type="email" value={correo} disabled/>
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
            <label>Especialidad:</label>
            <input type="text" value={espid} disabled />
        </div>
        <div className="ajuste-form-group">
            <label>Teléfono:</label>
            <input type="text" value={telefono} onChange={(e) => setTelefono(e.target.value)} />
        </div>

        <div className="ajuste-form-group">
            <label>Fecha de Nacimiento:</label>
            <input type="date" value={fechaNacimiento} onChange={(e) => setFechaNacimiento(e.target.value)} />
        </div>
        <button className="ajuste-btn-guardar" onClick={handleGuardar}>
        Guardar Cambios
      </button>
        </div>
    </div>
    </>
  );
};

export default Perfil;
