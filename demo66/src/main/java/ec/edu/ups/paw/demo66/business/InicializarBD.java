package ec.edu.ups.paw.demo66.business;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import ec.edu.ups.paw.DAO.CitasMedicasDAO;
import ec.edu.ups.paw.DAO.DisponibilidadDAO;
import ec.edu.ups.paw.DAO.DoctorDAO;
import ec.edu.ups.paw.DAO.EspecialidadDAO;
import ec.edu.ups.paw.DAO.HorarioDAO;
import ec.edu.ups.paw.DAO.PaisDAO;
import ec.edu.ups.paw.DAO.PersonaDAO;
import ec.edu.ups.paw.DAO.UsuarioDAO;
import ec.edu.ups.paw.demo66.CitasMedicas;
import ec.edu.ups.paw.demo66.Disponibilidad;
import ec.edu.ups.paw.demo66.Doctor;
import ec.edu.ups.paw.demo66.Especialidad;
import ec.edu.ups.paw.demo66.Horario;
import ec.edu.ups.paw.demo66.Pais;
import ec.edu.ups.paw.demo66.Persona;
import ec.edu.ups.paw.demo66.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class InicializarBD {
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

	@Inject
	private PersonaDAO daoPe;
	
	@Inject
	private PaisDAO daopais;
	
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
		
		daoDoctor.insert(doc); 
		
		
		Usuario persona  = new Usuario();
		persona.setCedula("0151273012");
		persona.setNombre("Luis");
		persona.setApellido("Toledo");
		persona.setDireccion("jUBONES");
		persona.setCorreo("diego@gmail.com");
		
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
   
		List<Usuario> personas  = daoPersona.getAll();
		List<Horario> horarios  = daoHorario.getAll();
		List<CitasMedicas> citas  = daoCM.getAll();
		List<Especialidad> especialidades  = daoEsp.getAll();
		List<Doctor> doctores  = daoDoctor.getAll();
		List<Disponibilidad> disps  = daoDisp.getAll();
		
		
		
		
		
		for (Usuario p : personas) {
			System.out.println(p.toString());
		}
		
		
		for (Horario p : horarios) {
			System.out.println(p.toString());
		}
		
		
		for (CitasMedicas p : citas) {
			System.out.println(p.toString());
		}
		
		
		
		for (Especialidad p : especialidades) {
			System.out.println(p.toString());
		}
		
		
		for (Doctor p : doctores) {
			System.out.println(p.toString());
		}
		
		
		for (Disponibilidad p : disps) {
			System.out.println(p.toString());
		} 
		
        
        
        System.out.println("Persona y Pais");
        
        Pais pais = new Pais();
        pais.setCodigo(593);
        pais.setNombre("Ecuador");
        daopais.insert(pais);
        
        Persona per = new Persona();
        per.setCedula("0703533653");
        per.setNombre("Gladys");
        per.setDireccion("Jubones");
        per.setPais(pais);
        
        daoPe.insert(per);
        
        System.out.println(per.toString());
        
        
        
        
	}
	
	

}
