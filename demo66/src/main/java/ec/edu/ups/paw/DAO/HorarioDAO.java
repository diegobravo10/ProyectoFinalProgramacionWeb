package ec.edu.ups.paw.DAO;

import java.util.List;

import ec.edu.ups.paw.demo66.Disponibilidad;
import ec.edu.ups.paw.demo66.Horario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class HorarioDAO {
	@PersistenceContext
	private EntityManager em;
	
	public void insert(Horario h) {
        // Verificamos si la disponibilidad viene con solo ID
        if (h.getDisponibilidad() != null && h.getDisponibilidad().getIdDisponibilidad() != 0) {
            Disponibilidad dispManaged = em.find(Disponibilidad.class, h.getDisponibilidad().getIdDisponibilidad());
            h.setDisponibilidad(dispManaged);
        }
        em.persist(h);
    }

    public void update(Horario h) {
        if (h.getDisponibilidad() != null && h.getDisponibilidad().getIdDisponibilidad() != 0) {
            Disponibilidad dispManaged = em.find(Disponibilidad.class, h.getDisponibilidad().getIdDisponibilidad());
            h.setDisponibilidad(dispManaged);
        }
        em.merge(h);
    }

	
	public Horario read(int idHorario ) {
		Horario u = em.find(Horario.class, idHorario);
		 return u;	
		}
	
	public void delete(int idHorario) {
		Horario u = em.find(Horario.class, idHorario);
		 em.remove(u);	
		}
	
	public List<Horario>  getAll (){
		String sql =  "SELECT p FROM Horario p";
		Query q = em.createQuery(sql, Horario.class);
		return q.getResultList();
	}
	
	public List<Horario> getHorarioDisp(int dispId) {
	    String jpql = "SELECT d FROM Horario d WHERE d.disponibilidad.id = :dispId";
	    return em.createQuery(jpql, Horario.class)
	             .setParameter("dispId", dispId)
	             .getResultList();
	}
	

}
