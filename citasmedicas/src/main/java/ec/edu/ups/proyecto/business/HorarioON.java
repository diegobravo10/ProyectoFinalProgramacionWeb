package ec.edu.ups.proyecto.business;

import java.util.List;

import ec.edu.ups.proyecto.DAO.HorarioDAO;
import ec.edu.ups.proyecto.citasmedicas.Horario;
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
	
	public Horario findById(int id) {
		
		return daoHorario.read(id);
	}
	
	public List<Horario> getHorariosDispDoc(int doctorid) {
			
		    return daoHorario.obtenerHorariosPorDoctor(doctorid);
		   
		}
	
	public void actualizarEstado(int idHorario, boolean disponible) {
		
		daoHorario.actualizarEstadoHorario(idHorario, disponible);
	}
	
	public void eliminarHorario(int idHorario) {
		daoHorario.delete(idHorario);
	}


	

}
