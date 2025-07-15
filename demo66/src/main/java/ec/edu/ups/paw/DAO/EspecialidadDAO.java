package ec.edu.ups.paw.DAO;

import java.util.List;

import ec.edu.ups.paw.demo66.Especialidad;
import ec.edu.ups.paw.demo66.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ejb.Stateless;

@Stateless
public class EspecialidadDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void insert(Especialidad e) {
		em.persist(e);
	}

	public void update(Especialidad u) {
		em.merge(u);
			
		}
	
	public Especialidad read( int idEspecialidad ) {
		Especialidad u = em.find(Especialidad.class, idEspecialidad);
		 return u;	
		}
	
	public void delete (int idEspecialidad) {
		Especialidad u = em.find(Especialidad.class, idEspecialidad);
		 em.remove(u);	
		}
	
	public List<Especialidad>  getAll (){
		String sql =  "SELECT p FROM Especialidad p";
		Query q = em.createQuery(sql, Especialidad.class);
		return q.getResultList();
	}
	public Especialidad read2(String nombre) {
		String jpql = "SELECT u FROM Especialidad u WHERE LOWER(u.nombre) = LOWER(:nombre)";
	    TypedQuery<Especialidad> query = em.createQuery(jpql, Especialidad.class);
	    query.setParameter("nombre", nombre);
	    List<Especialidad> resultado = query.getResultList();
	    return resultado.isEmpty() ? null : resultado.get(0);
	}

	
}
