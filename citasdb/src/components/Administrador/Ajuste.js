import { useEffect, useState } from "react";
import { doc, getDoc, updateDoc, query, collection, where, getDocs, addDoc,onSnapshot  } from "firebase/firestore";
import { auth, provider, db } from "../servicios/firebase.js";
import { FaSearch } from "react-icons/fa";
import { FaPlus } from "react-icons/fa";
import { Timestamp } from "firebase/firestore";

import './Ajuste.css';
const Ajuste = () => {
  const [correo, setCorreo] = useState("");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [cedula, setCedula] = useState("");
  const [direccion, setDireccion] = useState("");
  const [telefono, setTelefono] = useState("");
  const [fechaNacimiento, setFechaNacimiento] = useState("");
  const [espid, setEspid] = useState("");
  const [nuevaEspecialidad, setNuevaEspecialidad] = useState("");
  const [busquedaCedula, setBusquedaCedula] = useState("");
  const [resultadoBusqueda, setResultadoBusqueda] = useState(null);
  const [especialidades, setEspecialidades] = useState([]);
const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("");
const [especialidadFiltrada, setEspecialidadFiltrada] = useState("");
const [doctoresPorEspecialidad, setDoctoresPorEspecialidad] = useState([]);
const [doctorSeleccionado, setDoctorSeleccionado] = useState(null);
const [rolSeleccionado, setRolSeleccionado] = useState('');
const [doctores, setDoctores] = useState([]);
const [idDoctor, setIdDoctor] = useState('');
const [uidDoctor, setUIdDoctor] = useState('');
const [espDoctor, setEspDoctor] = useState('');


//buscar todas las especialidades 
  useEffect(() => {
  const cargarEspecialidades = async () => {
    try {
      const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/especialidades");
      
      if (!response.ok) {
        throw new Error("Error al obtener especialidades");
      }

      const data = await response.json();
      setEspecialidades(data);  // El backend ya devuelve la lista en formato esperado
    } catch (error) {
      console.error("Error al cargar especialidades:", error);
    }
  };

  cargarEspecialidades();
}, []);

//cargar doctores por su especialidad
useEffect(() => {
  const cargarDoctores = async () => {
    try {
      const user = auth.currentUser;
      const token = user && await user.getIdToken();

      const res = await fetch("http://localhost:8080/citasmedicas/citasmedicas/doctor", {
        headers: {
          'Authorization': 'Bearer ' + token // solo si tu backend lo requiere
        }
      });

      if (!res.ok) throw new Error("Error al obtener doctores");

      const doctoresData = await res.json();

      const formatted = doctoresData.map(doc => ({
        ...doc,
        id: doc.idUser, // Para el select
        idUser: doc.idUser // Para el backend
      }));

      setDoctores(formatted);

    } catch (error) {
      console.error("Error cargando doctores:", error);
    }
  };

  cargarDoctores();
}, []);

// Filtrar doctores por especialidad seleccionada
const doctoresFiltrados = especialidadSeleccionada 
  ? doctores.filter(d => d.especialidad?.nombre === especialidadSeleccionada)
  : doctores;


//obtener los datos del doctor 
 useEffect(() => {
  if (!doctorSeleccionado) return;

  const doc = doctores.find(d => d.id === parseInt(doctorSeleccionado));
  if (doc) {
    setCorreo(doc.correo || "");
    setNombre(doc.nombre || "");
    setApellido(doc.apellido || "");
    setCedula(doc.cedula || "");
    setDireccion(doc.direccion || "");
    setTelefono(doc.telefono || "");
    setEspid(doc.especialidad?.nombre || "");
    setIdDoctor(doc.idUser || "");
    setUIdDoctor(doc.uid || "");
    setEspDoctor(doc.especialidad || "");
        if (doc.fechaNacimiento) {
          const fecha = new Date(doc.fechaNacimiento);
          const yyyy = fecha.getFullYear();
          const mm = String(fecha.getMonth() + 1).padStart(2, '0');
          const dd = String(fecha.getDate()).padStart(2, '0');
          setFechaNacimiento(`${yyyy}-${mm}-${dd}`);
        }

  }
}, [doctorSeleccionado, doctores]);

//guardar cambios en el doctor guardarCambiosDoctor
const guardarCambiosDoctor = async () => {
  const user = auth.currentUser;
    const token = user && await user.getIdToken();
    
  try {
    
    const data = {
      especialidad: espDoctor,
      idUser: idDoctor,
      correo,
      nombre,
      apellido,
      direccion,
      cedula,
      telefono,
      rol: 'doctor', 
      uid: uidDoctor, 
      fechaNacimiento
    };
    console.log("Datos enviados:", data);
    const res = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/doctor`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        //"Authorization": "Bearer " + token
      },
      body: JSON.stringify(data)
    });

    if (!res.ok) throw new Error("Error al actualizar usuario");

    alert("Cambios guardados correctamente.");
  } catch (error) {
    console.error("Error al guardar:", error);
    alert("Error al guardar los cambios.");
  }
};




//buscar por la cedula

const buscarPorCedula = async () => {
  try {
    const response = await fetch(`http://localhost:8080/citasmedicas/citasmedicas/usuarios/cedula/${busquedaCedula}`);

    if (!response.ok) {
      if (response.status === 404) {
        alert("No se encontró ningún usuario con esa cédula.");
        setResultadoBusqueda(null);
      } else {
        throw new Error("Error al buscar el usuario");
      }
      return;
    }

    const usuario = await response.json();
    if (Array.isArray(usuario.fechaNacimiento)) {
  const [year, month, day] = usuario.fechaNacimiento;
  usuario.fechaNacimiento = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
}

setResultadoBusqueda(usuario);

  } catch (error) {
    console.error("Error al buscar:", error);
    alert("Ocurrió un error al buscar el usuario.");
  }
};


  const guardarCambiosAdmin = async () => {
  if (!resultadoBusqueda || !rolSeleccionado) {
    alert("Seleccione un rol válido.");
    return;
  }

  if (rolSeleccionado === "doctor" && !especialidadSeleccionada) {
    alert("Seleccione una especialidad válida para el doctor.");
    return;
  }

  try {
    // Construir objeto Usuario con los datos existentes + cambios
    const usuarioActualizado = {
      ...resultadoBusqueda,
      rol: rolSeleccionado,
      especialidad: rolSeleccionado === "doctor"
        ? especialidadSeleccionada
        : null
    };

    console.log("Datos enviados:", usuarioActualizado);
    const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/usuarios/cambiar-rol", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(usuarioActualizado),
      });


    if (!response.ok) {
      throw new Error("Error al guardar el usuario");
    }

    alert("Rol y especialidad actualizados correctamente.");
    setResultadoBusqueda(null);
    setBusquedaCedula("");
    setEspecialidadSeleccionada("");
    setRolSeleccionado("");
  } catch (error) {
    console.error("Error al guardar cambios administrativos:", error);
    alert("Ocurrió un error al guardar los cambios.");
  }
};

//funcion para agregar una especialidad
const agregarEspecialidad = async () => {
  if (!nuevaEspecialidad.trim()) {
    alert("Ingrese un nombre válido para la especialidad.");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/citasmedicas/citasmedicas/especialidades", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        nombre: nuevaEspecialidad.trim()
      })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.mensaje || "Error desconocido");
    }

    alert("Especialidad agregada correctamente.");
    setNuevaEspecialidad("");
  } catch (error) {
    console.error("Error al agregar especialidad:", error);
    alert("Ocurrió un error al agregar la especialidad: " + error.message);
  }
};







  return (
    <>
     <div className="ajuste-container">
        
        <div>

         <div className="select-container">
            <select
  value={especialidadSeleccionada}
  onChange={(e) => {
    setEspecialidadSeleccionada(e.target.value);
    setDoctorSeleccionado(""); // Limpiar doctor al cambiar especialidad
  }}
>
  <option value="">Seleccione una especialidad</option>
      {especialidades.map((esp) => (
        <option key={esp.id} value={esp.id}>
          {esp.nombre}
        </option>
      ))}
    </select>

    {doctoresFiltrados.length > 0 && (
      <select
        value={doctorSeleccionado}
        onChange={(e) => setDoctorSeleccionado(e.target.value)}
      >
        <option value="">Seleccione un doctor</option>
        {doctoresFiltrados.map((doc) => (
          <option key={doc.id} value={doc.id}>
            {doc.nombre} {doc.apellido}
          </option>
        ))}
      </select>
    )}
          </div>


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
           <button
            className="ajuste-btn-guardar"
            onClick={() => {
              if (!doctorSeleccionado) {
                alert("Seleccione un doctor primero.");
                return;
              }
              guardarCambiosDoctor({
                id: doctorSeleccionado.id,
                nombre,
                apellido,
                cedula,
                telefono,
              });
            }}
          >
            Guardar Cambios
          </button>
        </div>
    </div>


       <div className="ajuste-container">
          <h2 className="admin-title">Cambios Administrativos</h2>
          <p className="instrucciones-admin">
              Ingrese la <strong>cédula</strong> del usuario y seleccione el <strong>rol</strong> para asignarlo como <strong>admin, paciente o doctor</strong>.
          </p>
          <div className="search-section">
              <input
                  className="cedula-input"
                  placeholder="Cédula"
                  value={busquedaCedula}
                  onChange={(e) => setBusquedaCedula(e.target.value)}
              />
              <button className="search-button" onClick={buscarPorCedula}>
                  <FaSearch />
                  Buscar
              </button>
          </div>

          {resultadoBusqueda && (
              <div className="result-card">
                  <h3 className="result-title">Información del Usuario</h3>
                  <div className="user-details">
                      <p><strong>Nombre:</strong> {resultadoBusqueda.nombre}</p>
                      <p><strong>Apellido:</strong> {resultadoBusqueda.apellido}</p>
                      <p><strong>Correo:</strong> {resultadoBusqueda.correo}</p>
                      <p><strong>Rol:</strong> {resultadoBusqueda.rol}</p>
                  </div>

                  <div className="action-section">
                      <label htmlFor="rol" className="select-label">Rol:</label>
                      <select
                          id="rol"
                          className="change-type-select"
                          value={rolSeleccionado}
                          onChange={(e) => setRolSeleccionado(e.target.value)}
                      >
                          <option value="">Seleccione un rol</option>
                          <option value="admin">admin</option>
                          <option value="paciente">paciente</option>
                          <option value="doctor">doctor</option>
                      </select>

                      {rolSeleccionado === 'doctor' && (
                      <>
                        <label htmlFor="especialidad" className="select-label">Especialidad:</label>
                        <select
                          id="especialidad"
                          className="change-type-select"
                          value={especialidadSeleccionada?.idEspecialidad || ""}
                          onChange={(e) => {
                            const id = Number(e.target.value); // usar Number en lugar de parseInt por claridad

                            // Validación
                            if (!id || isNaN(id)) {
                              setEspecialidadSeleccionada(null);
                              return;
                            }

                            // Buscar objeto especialidad completo
                            const especialidadCompleta = especialidades.find(
                              (esp) => esp.idEspecialidad === id
                            );

                            setEspecialidadSeleccionada(especialidadCompleta);
                          }}
                        >
                          <option value="">Seleccione la especialidad</option>
                          {especialidades.map((esp) => (
                            <option key={esp.idEspecialidad} value={esp.idEspecialidad}>
                              {esp.nombre}
                            </option>
                          ))}
                        </select>
                      </>
                    )}



                      <button className="save-button" onClick={guardarCambiosAdmin}>
                          Guardar Cambios
                      </button>
                  </div>
              </div>
          )}
      </div>

        <div className="ajuste-container">

        <h2> <FaPlus style={{ marginLeft: "8px", color: "#28a745" }} /> Agregar Especialidades </h2>
        <div className="agregar-especialidad">
            <input
                placeholder="Especialidad"
                value={nuevaEspecialidad}
                onChange={(e) => setNuevaEspecialidad(e.target.value)}
            />
            <button onClick={agregarEspecialidad}>Agregar</button>
        </div>


        </div>
    </>
  );
};

export default Ajuste;
