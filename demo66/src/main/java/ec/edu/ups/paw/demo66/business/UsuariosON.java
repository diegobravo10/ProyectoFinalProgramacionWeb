package ec.edu.ups.paw.demo66.business;


import java.util.List;

import ec.edu.ups.paw.DAO.UsuarioDAO;
import ec.edu.ups.paw.demo66.Pais;
import ec.edu.ups.paw.demo66.Persona;
import ec.edu.ups.paw.demo66.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UsuariosON {
	
	@Inject
	private UsuarioDAO daoPersona;
	
	

	public void guardarPersonas(Usuario p) {
		// Buscar persona existente por cédula
		Usuario pe = daoPersona.read(p.getCedula());
		
		if (pe == null) {
		    daoPersona.insert(p);
		} else {
		    daoPersona.update(p);
		}

		
	}
	
	public List<Usuario> getContactos(){
		return daoPersona.getAll();
	}
	
	public Usuario getPersonacedula(String cedula) throws Exception {
		
		if (cedula.length() != 10) 
			 throw new Exception("Ta' loco mi pana");
		
		return daoPersona.read(cedula);
	
		
	}
	
	public List<Usuario> getUsuarioNombre(String nombre) {

		return daoPersona.getByNombreLike(nombre);
	
	}

}
