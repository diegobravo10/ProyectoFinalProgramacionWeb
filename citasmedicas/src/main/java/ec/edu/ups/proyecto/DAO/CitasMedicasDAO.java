package ec.edu.ups.proyecto.DAO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import ec.edu.ups.proyecto.citasmedicas.CitasMedicas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ejb.Stateless;

@Stateless
public class CitasMedicasDAO {
	@PersistenceContext
	private EntityManager em;
	
	public void insert(CitasMedicas c) {
		em.persist(c);
	}

	public void update(CitasMedicas c) {
		em.merge(c);
			
		}
	
	public CitasMedicas read( int idCita ) {
		CitasMedicas c = em.find(CitasMedicas.class, idCita);
		 return c;	
		}
	
	public void delete (int idEspecialidad) {
		CitasMedicas c = em.find(CitasMedicas.class, idEspecialidad);
		 em.remove(c);	
		}
	
	public List<CitasMedicas>  getAll (){
		String sql =  "SELECT p FROM CitasMedicas p";
		Query q = em.createQuery(sql, CitasMedicas.class);
		return q.getResultList();
	}
	public List<CitasMedicas> getCitasPorCedulaPaciente(String cedulaPaciente) {
	    String jpql = "SELECT c FROM CitasMedicas c WHERE c.paciente.cedula = :cedula";
	    return em.createQuery(jpql, CitasMedicas.class)
	             .setParameter("cedula", cedulaPaciente)
	             .getResultList();
	}

	public List<CitasMedicas> getCitasPorCedulaDoctor(String cedulaDoctor) {
	    String jpql = "SELECT c FROM CitasMedicas c WHERE c.doctor.cedula = :cedula";
	    return em.createQuery(jpql, CitasMedicas.class)
	             .setParameter("cedula", cedulaDoctor)
	             .getResultList();
	}
	
	public List<CitasMedicas> findByDoctorId(int idUser) {
	    String jpql = "SELECT c FROM CitasMedicas c WHERE c.doctor.idUser = :idUser";
	    return em.createQuery(jpql, CitasMedicas.class)
	             .setParameter("idUser", idUser)
	             .getResultList();
	}
	
	public List<CitasMedicas> obtenerCitasPendientesDeRecordatorio(int horasDesde, int horasHasta, boolean es24h) {
	    String flag = es24h ? "recordatorio24hEnviado" : "recordatorio2hEnviado";

	    String jpql = "SELECT c FROM CitasMedicas c " +
	              "WHERE c.horario.fecha BETWEEN :desde AND :hasta " +
	              "AND c." + flag + " = false " +
	              "AND c.estado = 'confirmado'";


	    ZoneId zoneId = ZoneId.of("America/Guayaquil");
	    ZonedDateTime ahora = ZonedDateTime.now(zoneId);
	    ZonedDateTime desdeZdt = ahora.plusHours(horasDesde).minusMinutes(1);
	    ZonedDateTime hastaZdt = ahora.plusHours(horasHasta).plusMinutes(1);

	    Date desde = Date.from(desdeZdt.toInstant());
	    Date hasta = Date.from(hastaZdt.toInstant());

	    return em.createQuery(jpql, CitasMedicas.class)
	             .setParameter("desde", desde)
	             .setParameter("hasta", hasta)
	             .getResultList();
	}



}
