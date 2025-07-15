package ec.edu.ups.paw.DAO;

import java.util.List;

import ec.edu.ups.paw.demo66.Pais;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class PaisDAO {


	@PersistenceContext
	private EntityManager em;
	
	public void insert(Pais u) {
		em.persist(u);
	}

	public void update(Pais u) {
		em.merge(u);
			
		}
	
	public Pais read(int codigo ) {
		Pais u = em.find(Pais.class, codigo);
		 return u;	
		}
	
	public void delete(int cedula) {
		Pais u = em.find(Pais.class, cedula);
		 em.remove(u);	
		}
	
	public List<Pais>  getAll (){
		String sql =  "SELECT p FROM Pais p";
		Query q = em.createQuery(sql, Pais.class);
		return q.getResultList();
	}
}
