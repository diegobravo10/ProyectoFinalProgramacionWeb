import React from 'react';
import { getAuth, signOut } from "firebase/auth";
import { useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { doc, getDoc} from "firebase/firestore";
import { db } from "../servicios/firebase";
import './Navbar.css';


const Navbar = () => {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [docId, setDocId] = useState("");
    const auth = getAuth();
  const navigate = useNavigate();
//Para cerrar sesión 
  const handleLogout = () => {
    localStorage.removeItem("uid");
    signOut(auth)
      .then(() => {
        navigate("/", { replace: true });
      })
      .catch((error) => {
        console.error("Error al cerrar sesión:", error);
      });
  };

 useEffect(() => {
  //Extraemos el id del paciente del localStorage
    const storedId = localStorage.getItem("uid");
    if (storedId) {
      setDocId(storedId);
      //Extraer los datos del paciente, a trave del id obtenido
      const cargarDatos = async () => {
        try {
          const userRef = doc(db, "users", storedId);
          const docSnap = await getDoc(userRef);
         
          if (docSnap.exists()) {
            const datos = docSnap.data();
            setNombre(datos.nombre || "");
            setApellido(datos.apellido || "");


          } else {
            console.warn("No se encontró el usuario");
          }
        } catch (error) {
          console.error("Error al obtener datos:", error);
        }
      };

      cargarDatos();
    }
  }, []);


  return (
    <nav className="navbar">
      <div className="navbar-logo">MedCitas</div>

      <div className="navbar-content">
    <ul className="navbar-links">
        <li><Link to="/paciente">Agendar</Link></li>
        <li><Link to="/paciente/perfil">Perfil</Link></li>
        <li>
              <button onClick={handleLogout} className="logout-button">
                Salir
              </button>
            </li>
    </ul>

    <div className="navbar-divider" />
    {/*Usamos el nombre y apellido para mostrar en barra superior */}
    <div className="navbar-user">
      👤 <span> {nombre.split(" ")[0]} {apellido.split(" ")[0]} </span>
    </div>
    </div>

    </nav>
  );
};

export default Navbar;
