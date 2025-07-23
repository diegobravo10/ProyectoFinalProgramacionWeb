package ec.edu.ups.proyecto.business;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.edu.ups.proyecto.DAO.DisponibilidadDAO;
import ec.edu.ups.proyecto.citasmedicas.Disponibilidad;
import ec.edu.ups.proyecto.citasmedicas.Doctor;
import ec.edu.ups.proyecto.citasmedicas.Horario;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;


@Stateless
public class DisponibilidadON {

	@Inject 
	private DisponibilidadDAO daoDisp;
	
	@Inject 
	private HorarioON onHorario;
	
	@Inject 
	private DoctorON onDoctor;
	
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
	
	public void crearDisponibilidadYHorarios(String diaSemana, LocalTime horaInicio, LocalTime horaFin, String uid) throws Exception {
	    // 1. Buscar el doctor
	    Doctor doctor = onDoctor.getDoctorUid(uid);
	    if (doctor == null) {
	        throw new IllegalArgumentException("Doctor no encontrado");
	    }

	    // 2. Crear la Disponibilidad
	    Disponibilidad disponibilidad = new Disponibilidad();
	    disponibilidad.setDiaSemana(diaSemana);
	    disponibilidad.setHoraInicio(horaInicio);
	    disponibilidad.setHoraFin(horaFin);
	    disponibilidad.setDoctor(doctor);

	    guardarDisponibilidad(disponibilidad);

	    // 3. Calcular la próxima fecha del día de la semana
	    DayOfWeek dia = DayOfWeek.valueOf(diaSemana.toUpperCase());
	    LocalDate fechaBase = LocalDate.now().with(TemporalAdjusters.nextOrSame(dia));

	    // 4. Generar horarios cada 30 minutos
	    List<Horario> horarios = new ArrayList<>();
	    LocalTime actual = horaInicio;

	    while (!actual.isAfter(horaFin.minusMinutes(30))) {
	        Horario horario = new Horario();

	        // Combinar fecha + hora
	        LocalDateTime fechaHora = LocalDateTime.of(fechaBase, actual);
	        Date fechaFinal = Date.from(fechaHora.atZone(ZoneId.of("America/Guayaquil")).toInstant());

	        horario.setFecha(fechaFinal);
	        horario.setDisponible(true);
	        horario.setDisponibilidad(disponibilidad);

	        onHorario.guardarHorario(horario);
	        horarios.add(horario);

	        // Siguiente intervalo
	        actual = actual.plusMinutes(30);
	    }
	}


}
