package ec.edu.ups.proyecto.services;

import java.util.ArrayList;
import java.util.List;

import ec.edu.ups.proyecto.business.CitasMedicasON;
import ec.edu.ups.proyecto.business.DoctorON;
import ec.edu.ups.proyecto.business.HorarioON;
import ec.edu.ups.proyecto.business.UsuariosON;
import ec.edu.ups.proyecto.citasmedicas.CitaDetalle;
import ec.edu.ups.proyecto.citasmedicas.CitasMedicas;
import ec.edu.ups.proyecto.citasmedicas.Doctor;
import ec.edu.ups.proyecto.citasmedicas.Horario;
import ec.edu.ups.proyecto.citasmedicas.Usuario;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/citas")
public class CitasMedicasServices {
	
	 @Inject
	    private CitasMedicasON onCitas;
	 
	 @Inject
	    private UsuariosON onUsuario;
	 
	 @Inject
	    private HorarioON onHorario;
	 
	 @Inject
	    private DoctorON onDoctor;
	 

	    // Obtener todas las citas
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitas() {
	        List<CitasMedicas> listado = onCitas.getCistasMedicas(); // O renómbralo a getCitas()
	        return Response.ok(listado).build();
	    }

	    // Obtener citas por cédula del doctor
	    @GET
	    @Path("/doctor/{cedula}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitasPorDoctor(@PathParam("cedula") String cedula) {
	        try {
	            List<CitasMedicas> citas = onCitas.getPorDoctor(cedula);
	            if (citas == null || citas.isEmpty()) {
	                return Response.status(Response.Status.NOT_FOUND)
	                               .entity(new MensajeJSON("Error", "No hay citas para este doctor"))
	                               .build();
	            }
	            return Response.ok(citas).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity(new MensajeJSON("Error", e.getMessage()))
	                           .build();
	        }
	    }

	    // Obtener citas por cédula del paciente
	    @GET
	    @Path("/paciente/{cedula}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitasPorPaciente(@PathParam("cedula") String cedula) {
	        try {
	            List<CitasMedicas> citas = onCitas.getPorPaciente(cedula);
	            if (citas == null || citas.isEmpty()) {
	                return Response.status(Response.Status.NOT_FOUND)
	                               .entity(new MensajeJSON("Error", "No hay citas para este paciente"))
	                               .build();
	            }
	            return Response.ok(citas).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity(new MensajeJSON("Error", e.getMessage()))
	                           .build();
	        }
	    }

	    // Crear o actualizar una cita médica
	    @POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON) 
	    public Response guardarCita(CitasMedicas cita) {
	        try {
	            onCitas.guardarCitasMedicas(cita); // Considera cambiar el nombre del método a guardarCita()
	            MensajeJSON response = new MensajeJSON("success", "Cita médica guardada exitosamente.");
	            return Response.ok(response).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity(new MensajeJSON("Error", e.getMessage()))
	                           .build();
	        }
	    }
	    
	    
	    @GET
	    @Path("/doctor/{uid}/conDetalles")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitasConDetallesPorDoctorUid(@PathParam("uid") String uid) throws Exception {
	        // Buscar el doctor por UID
	        Doctor doctor = onDoctor.getDoctorUid(uid);
	        if (doctor == null) {
	            return Response.status(Response.Status.NOT_FOUND).entity("Doctor no encontrado").build();
	        }

	        List<CitasMedicas> citas = onCitas.getPorDoctor(doctor.getIdUser());
	        List<CitaDetalle> detalles = new ArrayList<>();

	        for (CitasMedicas cita : citas) {
	        	CitaDetalle dto = new CitaDetalle();
	            dto.setId(cita.getIdCita());

	            // Obtener paciente
	            Usuario paciente = onUsuario.findById(cita.getPaciente().getIdUser());
	            dto.setPaciente(paciente);

	            // Obtener horario
	            Horario horario = onHorario.findById(cita.getHorario().getIdHorario());
	            dto.setHorario(horario);
	            
	            dto.setDescripcion(cita.getDescripcion());
	            
	            dto.setEstado(cita.getEstado());

	            detalles.add(dto);
	        }

	        return Response.ok(detalles).build();
	    }
	    
	    
	    @POST
	    @Path("/{idCita}/estado")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response actualizarEstadoCita(
	            @PathParam("idCita") int idCita,
	            EstadoRequest estadoRequest) {

	        if (estadoRequest == null || estadoRequest.getNuevoEstado() == null || estadoRequest.getIdHorario() == 0) {
	            return Response.status(Response.Status.BAD_REQUEST)
	                           .entity(new MensajeJSON("Error", "Datos incompletos"))
	                           .build();
	        }

	        try {
	            onCitas.actualizarEstadoCita(idCita, estadoRequest.getNuevoEstado(), estadoRequest.getIdHorario());
	            return Response.ok(new MensajeJSON("success", "Estado de la cita actualizado")).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity(new MensajeJSON("Error", e.getMessage()))
	                           .build();
	        }
	    }

	    // Clase interna para mapear el cuerpo JSON
	    public static class EstadoRequest {
	        private String nuevoEstado;
	        private int idHorario;

	        public String getNuevoEstado() {
	            return nuevoEstado;
	        }

	        public void setNuevoEstado(String nuevoEstado) {
	            this.nuevoEstado = nuevoEstado;
	        }

	        public int getIdHorario() {
	            return idHorario;
	        }

	        public void setIdHorario(int idHorario) {
	            this.idHorario = idHorario;
	        }
	    }
	    
	    

}
