package ec.edu.ups.proyecto.DAO;

import java.util.List;



import ec.edu.ups.proyecto.citasmedicas.Doctor;
import ec.edu.ups.proyecto.citasmedicas.Especialidad;
import ec.edu.ups.proyecto.citasmedicas.Usuario;
import ec.edu.ups.proyecto.citasmedicas.UsuarioDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;


@Stateless
public class UsuarioDAO {
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private DoctorDAO daoDoc;
	
	public void insert(Usuario u) {
		em.persist(u);
	}

	public void update(Usuario u) {
		em.merge(u);
			
		}
	
	public Usuario findById(int idUser) {
		Usuario u = em.find(Usuario.class, idUser);
		 return u;	

	}
	
	public Usuario read(String cedula) {
	    String jpql = "SELECT u FROM Usuario u WHERE u.cedula = :cedula";
	    TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
	    query.setParameter("cedula", cedula);
	    List<Usuario> resultado = query.getResultList();
	    return resultado.isEmpty() ? null : resultado.get(0);
	}

	
	public void delete(String cedula) {
		 Usuario u = em.find(Usuario.class, cedula);
		 em.remove(u);	
		}
	
	public List<Usuario>  getAll (){
		String sql =  "SELECT p FROM Usuario p";
		Query q = em.createQuery(sql, Usuario.class);
		return q.getResultList();
	}
	
	public List<Usuario> getByNombreLike(String nombre) {
	    String sql = "SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(:nombre)";
	    TypedQuery<Usuario> q = em.createQuery(sql, Usuario.class);
	    q.setParameter("nombre", "%" + nombre + "%");
	    return q.getResultList();
	}
	
	public Usuario readPorEmail(String correo) {
	    String jpql = "SELECT u FROM Usuario u WHERE u.correo = :correo";
	    TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
	    query.setParameter("correo", correo);
	    List<Usuario> resultado = query.getResultList();
	    return resultado.isEmpty() ? null : resultado.get(0);
	}
	

	public void cambiarRolUsuario(UsuarioDTO dto) throws Exception {
	    Usuario existente = em.find(Usuario.class, dto.getIdUser());
	    if (existente == null) {
	        throw new Exception("Usuario no encontrado");
	    }

	    if ("doctor".equalsIgnoreCase(dto.getRol())) {
	        if (dto.getEspecialidad() == null) {
	            throw new Exception("Especialidad requerida para doctor");
	        }

	        Especialidad esp = em.find(Especialidad.class, dto.getEspecialidad().getIdEspecialidad());
	        if (esp == null) {
	            throw new Exception("Especialidad no encontrada");
	        }

	        // Actualizar directamente el discriminador dtype a 'Doctor'
	        String sql = "UPDATE usuario SET dtype = 'Doctor' WHERE id_user = :id";
	        em.createNativeQuery(sql)
	          .setParameter("id", dto.getIdUser())
	          .executeUpdate();

	        // Limpiar y sincronizar el contexto de persistencia
	        em.flush();
	        em.clear();

	        // Crear instancia Doctor con los datos existentes y la especialidad
	        Doctor doctor = new Doctor();
	        doctor.setIdUser(existente.getIdUser());
	        doctor.setUid(existente.getUid());
	        doctor.setNombre(existente.getNombre());
	        doctor.setApellido(existente.getApellido());
	        doctor.setCorreo(existente.getCorreo());
	        doctor.setCedula(existente.getCedula());
	        doctor.setDireccion(existente.getDireccion());
	        doctor.setTelefono(existente.getTelefono());
	        doctor.setFechaNacimiento(existente.getFechaNacimiento());
	        doctor.setRol("doctor");
	        doctor.setEspecialidad(esp);

	        // Merge del doctor (actualiza la entidad con dtype = Doctor)
	        daoDoc.update(doctor);

	    } else {
	        // Para otros roles solo actualizar el campo rol
	        existente.setRol(dto.getRol());
	        existente.setUid(dto.getUid());
	        update(existente);
	    }
	}



	
	



}
