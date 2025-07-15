package ec.edu.ups.paw.DAO;
import java.util.List;

import ec.edu.ups.paw.demo66.Persona;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Stateless
public class PersonaDAO  {
	
	@PersistenceContext
	private EntityManager em;
	
	public void insert(Persona u) {
		em.persist(u);
	}

	public void update(Persona u) {
		em.merge(u);
			
		}
	
	public Persona read(String cedula ) {
		Persona u = em.find(Persona.class, cedula);
		 return u;	
		}
	
	public void delete(String cedula) {
		Persona u = em.find(Persona.class, cedula);
		 em.remove(u);	
		}
	
	public List<Persona>  getAll (){
		String sql =  "SELECT p FROM Persona p";
		Query q = em.createQuery(sql, Persona.class);
		return q.getResultList();
	}

}
