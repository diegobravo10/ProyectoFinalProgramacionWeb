package ec.edu.ups.proyecto.business;

import java.util.List;

import ec.edu.ups.proyecto.DAO.DisponibilidadDAO;
import ec.edu.ups.proyecto.citasmedicas.Disponibilidad;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;


@Stateless
public class DisponibilidadON {

	@Inject 
	private DisponibilidadDAO daoDisp;
	
	public void guardarDisponibilidad(Disponibilidad p) {
		Disponibilidad pe = daoDisp.read(p.getIdDisponibilidad());	
		if (pe == null) {
			daoDisp.insert(p);
		} else {
			daoDisp.update(p);
		}
	
	}
	
	public List<Disponibilidad> getDispoibilidades(){
		return daoDisp.getAll();
	}
	
	public List<Disponibilidad> getDispDoctor(int id) {
		
	    return daoDisp.getDisponibilidadPorDoctor(id);
	}

}
