package ec.edu.ups.paw.DAO;

import java.util.List;

import ec.edu.ups.paw.demo66.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ejb.Stateless;

@Stateless
public class DoctorDAO {
	@PersistenceContext
	private EntityManager em;
	
	public void insert(Doctor doc) {
		em.persist(doc);
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

}
