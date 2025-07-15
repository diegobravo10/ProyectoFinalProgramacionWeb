package ec.edu.ups.paw.demo66.business;

import java.util.List;

import ec.edu.ups.paw.DAO.DisponibilidadDAO;
import ec.edu.ups.paw.DAO.HorarioDAO;
import ec.edu.ups.paw.demo66.Disponibilidad;
import ec.edu.ups.paw.demo66.Horario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class HorarioON {
	@Inject 
	private HorarioDAO daoHorario;
	
	public void guardarHorario(Horario p) {
		Horario pe = daoHorario.read(p.getIdHorario());	
		if (pe == null) {
			daoHorario.insert(p);
		} else {
			daoHorario.update(p);
		}
	
	}
	
	public List<Horario> getHorarios(){
		return daoHorario.getAll();
	}
	
	public List<Horario> getHorariosDisp(int id) {
		
	    return daoHorario.getHorarioDisp(id);
	}

	

}
