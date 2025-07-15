package ec.edu.ups.paw.demo66.business;

import java.util.List;

import ec.edu.ups.paw.DAO.PaisDAO;
import ec.edu.ups.paw.DAO.PersonaDAO;
import ec.edu.ups.paw.demo66.Pais;
import ec.edu.ups.paw.demo66.Persona;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ContactosON {
	
	@Inject
	private PersonaDAO daoPersona;
	
	@Inject
	private PaisDAO daoPais;
	
	public void guardarPersonas(Persona p) {
		// Buscar persona existente por cédula
		Persona pe = daoPersona.read(p.getCedula());

		if (pe == null) {
		    daoPersona.insert(p);
		} else {
		    daoPersona.update(p);
		}

		
	}
	
	public List<Persona> getContactos(){
		return daoPersona.getAll();
	}
	
	public Persona getPersonacedula(String cedula) throws Exception {
		
		if (cedula.length() != 10) 
			 throw new Exception("Ta' loco mi pana");
		
		return daoPersona.read(cedula);
	
		
	}
	
	public void eliminarPersona(String cedula) throws Exception {
		if (cedula.length() != 10) 
			 throw new Exception("Ta' loco mi pana");
		daoPersona.delete(cedula);
	}
}
