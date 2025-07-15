package ec.edu.ups.proyecto.business;

import java.util.List;

import ec.edu.ups.proyecto.DAO.EspecialidadDAO;
import ec.edu.ups.proyecto.citasmedicas.Especialidad;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class EspecialidadON {
	
	@Inject
	private EspecialidadDAO daoEspecialidad;
	
	

	public void guardarPersonas(Especialidad p) {
		// Buscar persona existente por cédula
		Especialidad pe = daoEspecialidad.read(p.getIdEspecialidad());
		
		if (pe == null) {
			daoEspecialidad.insert(p);
		} else {
			daoEspecialidad.update(p);
		}

		
	}
	
	public List<Especialidad> getContactos(){
		return daoEspecialidad.getAll();
	}
	
	public Especialidad getEspecialidad(String nombre) {
	    if (nombre == null || nombre.trim().isEmpty()) {
	        throw new IllegalArgumentException("El nombre no puede ser vacío");
	    }
	    return daoEspecialidad.read2(nombre);
	}

	



}
