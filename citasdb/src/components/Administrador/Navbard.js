import React, { useEffect, useState } from "react";
import { doc, getDoc } from "firebase/firestore";
import { signOut } from "firebase/auth";
import { useNavigate, Link } from "react-router-dom";
import { onAuthStateChanged, getAuth } from "firebase/auth";
import "../Paciente/Navbar.css";

const NavbarA = () => {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [docId, setDocId] = useState("");

  const auth = getAuth();
  const navigate = useNavigate();

  // Función para cerrar sesión
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
        <div className="navbar-left">
          <ul className="navbar-links">
            <li><Link to="/admin">Citas</Link></li>
            <li><Link to="/admin/horarios">Horarios</Link></li>
            <li><Link to="/admin/ajustes">Ajustes</Link></li>
            <li><Link to="/admin/perfil">Perfil</Link></li>
            <li><Link to="/admin/reportes">Reportes</Link></li>
            <li>
              <button onClick={handleLogout} className="logout-button">
                Salir
              </button>
            </li>
          </ul>
          <div className="navbar-divider" />
        </div>

        <div className="navbar-user">
          👤 <span>{nombre.split(" ")[0]} {apellido.split(" ")[0]}</span>
        </div>
      </div>
    </nav>
  );
};

export default NavbarA;
