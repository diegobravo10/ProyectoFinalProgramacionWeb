package ec.edu.ups.paw.demo66.business;

import java.util.List;

import ec.edu.ups.paw.DAO.DoctorDAO;
import ec.edu.ups.paw.DAO.UsuarioDAO;
import ec.edu.ups.paw.demo66.Doctor;
import ec.edu.ups.paw.demo66.Usuario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class DoctorON {
	@Inject
	private DoctorDAO daoPersona;
	
	

	public void guardarDoctor(Doctor p) {
		// Buscar persona existente por cédula
		Usuario pe = daoPersona.read2(p.getCedula());
		
		if (pe == null) {
		    daoPersona.insert(p);
		} else {
		    daoPersona.update(p);
		}

		
	}
	
	public List<Doctor> getContactos(){
		return daoPersona.getAll();
	}
	
	public Doctor getPersonacedula(String cedula) throws Exception {
		
		if (cedula.length() != 10) 
			 throw new Exception("Ta' loco mi pana");
		
		return daoPersona.read2(cedula);
	
	}
	

}
