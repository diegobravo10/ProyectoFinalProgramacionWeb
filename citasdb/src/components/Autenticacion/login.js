import { auth, provider, db } from "../servicios/firebase.js";
import { signInWithPopup } from "firebase/auth";
import { collection, query, where, getDocs} from "firebase/firestore";
import { useNavigate } from "react-router-dom";
import "./login.css";
const Login = () => {
     const navigate = useNavigate();

  const handleLogin = async () => {
  try {
    const result = await signInWithPopup(auth, provider);
    const user = result.user;

    const idToken = await user.getIdToken();

    // Llamar a backend con token para validar sesión
    const response = await fetch('http://localhost:8080/citasmedicas/citasmedicas/usuarios/me', {
      headers: {
        'Authorization': 'Bearer ' + idToken,
      }
    });

    if (response.ok) {
      const userData = await response.json();

      // Aquí puedes manejar roles si tu backend retorna el rol
      // Ejemplo:
      if(userData.rol === 'doctor') {
        navigate('/doctor');
      } else if(userData.rol === 'paciente') {
        navigate('/paciente');
      } else if(userData.rol === 'admin') {
        navigate('/admin');
      } else {
        window.location.href = '/no-autorizado';
      }

      // Guardar info en localStorage si quieres
      localStorage.setItem('uid', user.uid);
      localStorage.setItem('email', user.email);

    } else {
      alert('Token inválido o sesión no autorizada');
      navigate("/registro");
    }

  } catch (error) {
    console.error("Error de login:", error);
    alert("Hubo un problema al iniciar sesión.");
  }
};


  return (
    <div className="login-container">
      <div className="left-side">
        <img src="/assest/inicio.jpg" alt="Imagen" className="login-image" />
      </div>
      <div className="right-side">
        <h1>Citas Medicas</h1>
          <p className="parrafo">
          ¡Agenda tu consulta médica en línea hoy mismo!
          Facilitamos la gestión de tus citas con un sistema rápido, seguro y disponible las 24 horas.
          </p>
        <button className="principal" onClick={handleLogin}>Ingresar</button>
      </div>
    </div>
  );
};

export default Login;
