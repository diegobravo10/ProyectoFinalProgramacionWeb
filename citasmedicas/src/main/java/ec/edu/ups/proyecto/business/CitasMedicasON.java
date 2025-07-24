package ec.edu.ups.proyecto.business;

import java.util.List;

import ec.edu.ups.proyecto.DAO.CitasMedicasDAO;
import ec.edu.ups.proyecto.citasmedicas.CitasMedicas;
import ec.edu.ups.proyecto.citasmedicas.Horario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class CitasMedicasON {
	
	@Inject 
	private CitasMedicasDAO daoCM;
	
	@Inject 
	private HorarioON onHorario;
	
	public void guardarCitasMedicas(CitasMedicas p) {
		CitasMedicas pe = daoCM.read(p.getIdCita());	
		if (pe == null) {
			p.setRecordatorio24hEnviado(false);
			p.setRecordatorio2hEnviado(false);
			daoCM.insert(p);
		} else {
			p.setIdCita(pe.getIdCita());
			daoCM.update(p);
		}
	
	}
	
	public List<CitasMedicas> getCistasMedicas(){
		return daoCM.getAll();
	}
	
	public List<CitasMedicas> getPorDoctor(String cedula) {
		
	    return daoCM.getCitasPorCedulaDoctor(cedula);
	}
	
	public List<CitasMedicas> getPorPaciente(String cedula) {
		
	    return daoCM.getCitasPorCedulaPaciente(cedula);
	}
	public List<CitasMedicas> getPorDoctor(int id) {
			
		    return daoCM.findByDoctorId(id);
		}
	
	public void actualizarEstadoCita(int idCita, String nuevoEstado, int idHorario) {
	    CitasMedicas cita = daoCM.read(idCita);
	    Horario horario = onHorario.findById(idHorario);

	    if (cita != null && horario != null) {
	        cita.setEstado(nuevoEstado);
	        guardarCitasMedicas(cita);
	        

	        boolean disponible = nuevoEstado.equalsIgnoreCase("rechazado");
	        horario.setDisponible(disponible);
	        onHorario.guardarHorario(horario);
	    }
	}
	
	public CitasMedicas buscarPorId(int idCita) {
		
		return daoCM.read(idCita);
	}
	
	public List<CitasMedicas> obtenerCitasEnRango(int horasDesde, int horasHasta, boolean flag){
		
		return daoCM.obtenerCitasPendientesDeRecordatorio(horasDesde, horasHasta, flag);
	}

	

}
