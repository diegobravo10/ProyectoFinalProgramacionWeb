import React from 'react';
import { signOut } from "firebase/auth";
import { useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { doc, getDoc} from "firebase/firestore";
import { onAuthStateChanged, getAuth } from "firebase/auth";
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
  const auth = getAuth();

  const unsubscribe = onAuthStateChanged(auth, async (user) => {
    if (user) {
      try {
        const token = await user.getIdToken();

        const res = await fetch("http://localhost:8080/citasmedicas/citasmedicas/usuarios/me", {
          headers: {
            Authorization: "Bearer " + token,
          },
        });

        if (!res.ok) {
          throw new Error("No se pudo obtener los datos del usuario");
        }

        const data = await res.json();

        setNombre(data.nombre || "");
        setApellido(data.apellido || "");
      } catch (error) {
        console.error("Error al obtener datos del usuario:", error);
      }
    }
  });

  return () => unsubscribe();
}, []);


  return (
    <nav className="navbar">
      <div className="navbar-logo">MedCitas</div>

      <div className="navbar-content">
    <ul className="navbar-links">
        <li><Link to="/paciente">Agendar</Link></li>
        <li><Link to="/paciente/perfil">Perfil</Link></li>
        <li><Link to="/paciente/notificacion">Notificaciones</Link></li>
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
