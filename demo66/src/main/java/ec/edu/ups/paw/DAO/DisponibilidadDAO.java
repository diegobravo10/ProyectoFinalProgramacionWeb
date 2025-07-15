package ec.edu.ups.paw.DAO;

import java.util.List;

import ec.edu.ups.paw.demo66.Disponibilidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ejb.Stateless;

@Stateless
public class DisponibilidadDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	public void insert(Disponibilidad d) {
		em.persist(d);
	}

	public void update(Disponibilidad d) {
		em.merge(d);
			
		}
	
	public Disponibilidad read( int idDisponibilidad ) {
		Disponibilidad u = em.find(Disponibilidad.class, idDisponibilidad);
		 return u;	
		}
	
	public void delete (int idDisponibilidad) {
		Disponibilidad u = em.find(Disponibilidad.class, idDisponibilidad);
		 em.remove(u);	
		}
	
	public List<Disponibilidad>  getAll (){
		String sql =  "SELECT p FROM Disponibilidad p";
		Query q = em.createQuery(sql, Disponibilidad.class);
		return q.getResultList();
	}
	
	public List<Disponibilidad> getDisponibilidadPorDoctor(int doctorId) {
	    String jpql = "SELECT d FROM Disponibilidad d WHERE d.doctor.id = :doctorId";
	    return em.createQuery(jpql, Disponibilidad.class)
	             .setParameter("doctorId", doctorId)
	             .getResultList();
	}


}
