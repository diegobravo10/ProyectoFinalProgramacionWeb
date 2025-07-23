package ec.edu.ups.proyecto.DAO;

import java.util.List;

import ec.edu.ups.proyecto.citasmedicas.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


@Stateless
public class UsuarioDAO {
	@PersistenceContext
	private EntityManager em;
	
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
	
	



}
