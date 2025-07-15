package ec.edu.ups.proyecto.DAO;

import java.time.LocalTime;
import java.util.Date;

import ec.edu.ups.proyecto.citasmedicas.CitasMedicas;
import ec.edu.ups.proyecto.citasmedicas.Disponibilidad;
import ec.edu.ups.proyecto.citasmedicas.Doctor;
import ec.edu.ups.proyecto.citasmedicas.Especialidad;
import ec.edu.ups.proyecto.citasmedicas.Horario;
import ec.edu.ups.proyecto.citasmedicas.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class InciarBD {
	
	@Inject
	private UsuarioDAO daoPersona;

	@Inject
	private CitasMedicasDAO daoCM;

	@Inject
	private DoctorDAO daoDoctor;

	@Inject
	private EspecialidadDAO daoEsp;

	@Inject
	private DisponibilidadDAO daoDisp;

	@Inject
	private HorarioDAO daoHorario;

	
	@PostConstruct
	public void init() {
		System.out.println("Inicializando BD");
		
		Especialidad esp = new Especialidad();
		esp.setNombre("General");
		
		daoEsp.insert(esp);
		
		
		Doctor doc =  new Doctor();
		doc.setCedula("0702981770");
		doc.setNombre("Diego");
		doc.setApellido("Bravo");
		doc.setCorreo("brzoale2510@gmail.com");
		doc.setDireccion("Jubones");
		doc.setEspecialidad(esp);
		doc.setRol("doctor");
		
		daoDoctor.insert(doc); 
		
		
		Usuario persona  = new Usuario();
		persona.setCedula("0151273012");
		persona.setNombre("Luis");
		persona.setApellido("Toledo");
		persona.setDireccion("jUBONES");
		persona.setCorreo("diego@gmail.com");
		persona.setRol("admin");
		
		daoPersona.insert(persona);
		
		Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setDiaSemana("Lunes");
        disponibilidad.setHoraInicio(LocalTime.of(9, 0)); // 09:00
        disponibilidad.setHoraFin(LocalTime.of(12, 0));   // 12:00
        disponibilidad.setDoctor(doc);
		
        daoDisp.insert(disponibilidad);
        
        Horario horario = new Horario();
        horario.setFecha(new Date()); // Fecha actual
        horario.setDisponible(true);
        horario.setDisponibilidad(disponibilidad);
		
        daoHorario.insert(horario);
		
		CitasMedicas cita = new CitasMedicas();
        cita.setEstado("Confirmada");
        cita.setDescripcion("Chequeo general");
        cita.setDoctor(doc);
        cita.setPaciente(persona);
        cita.setHorario(horario);
		
        daoCM.insert(cita);
   

	}
}
