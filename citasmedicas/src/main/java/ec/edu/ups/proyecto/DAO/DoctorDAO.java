package ec.edu.ups.proyecto.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

import ec.edu.ups.proyecto.citasmedicas.Doctor;
import ec.edu.ups.proyecto.citasmedicas.Especialidad;
import jakarta.ejb.Stateless;

@Stateless
public class DoctorDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void insert(Doctor doctor) throws Exception {
		 try {
		        if (doctor.getEspecialidad() != null && doctor.getEspecialidad().getIdEspecialidad() > 0) {
		            Especialidad esp = em.find(Especialidad.class, doctor.getEspecialidad().getIdEspecialidad());
		            if (esp == null) {
		                throw new Exception("Especialidad no encontrada con ID: " + doctor.getEspecialidad().getIdEspecialidad());
		            }
		            doctor.setEspecialidad(esp);
		        } else {
		            throw new Exception("Especialidad inválida.");
		        }

		        em.persist(doctor);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new Exception("Error al guardar doctor: " + e.getMessage());
		    }
	}

	public void update(Doctor doc) {
		em.merge(doc);
			
		}
	
	public Doctor read( String cedula) {
		Doctor doc = em.find(Doctor.class, cedula);
		 return doc;	
		}
	
	public void delete (String cedula) {
		Doctor doc = em.find(Doctor.class, cedula);
		 em.remove(doc);	
		}
	
	public List<Doctor>  getAll (){
		String sql =  "SELECT p FROM Doctor p";
		Query q = em.createQuery(sql, Doctor.class);
		return q.getResultList();
	}
	
	public Doctor read2(String cedula) {
	    String jpql = "SELECT u FROM Doctor u WHERE u.cedula = :cedula";
	    TypedQuery<Doctor> query = em.createQuery(jpql, Doctor.class);
	    query.setParameter("cedula", cedula);
	    List<Doctor> resultado = query.getResultList();
	    return resultado.isEmpty() ? null : resultado.get(0);
	}
	
	public Doctor buscarUID(String uid) {
	    String jpql = "SELECT u FROM Doctor u WHERE u.uid = :uid";
	    TypedQuery<Doctor> query = em.createQuery(jpql, Doctor.class);
	    query.setParameter("uid", uid);
	    List<Doctor> resultado = query.getResultList();
	    return resultado.isEmpty() ? null : resultado.get(0);
	}
}
