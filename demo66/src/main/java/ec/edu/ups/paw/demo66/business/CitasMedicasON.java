package ec.edu.ups.paw.demo66.business;

import java.util.List;

import ec.edu.ups.paw.DAO.CitasMedicasDAO;
import ec.edu.ups.paw.DAO.DisponibilidadDAO;
import ec.edu.ups.paw.demo66.CitasMedicas;
import ec.edu.ups.paw.demo66.Disponibilidad;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class CitasMedicasON {
	
	@Inject 
	private CitasMedicasDAO daoCM;
	
	public void guardarDisponibilidad(CitasMedicas p) {
		CitasMedicas pe = daoCM.read(p.getIdCita());	
		if (pe == null) {
			daoCM.insert(p);
		} else {
			daoCM.update(p);
		}
	
	}
	
	public List<CitasMedicas> getDispoibilidades(){
		return daoCM.getAll();
	}
	
	public List<CitasMedicas> getPorDoctor(String cedula) {
		
	    return daoCM.getCitasPorCedulaDoctor(cedula);
	}
	
	public List<CitasMedicas> getPorPaciente(String cedula) {
		
	    return daoCM.getCitasPorCedulaPaciente(cedula);
	}
	

}
