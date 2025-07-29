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
	 
	 @Inject
	 private EmailService emailService;
	 
	 @Inject
	 private WhatsAppService whatsappService;

	 

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
	            
	            if ("confirmado".equalsIgnoreCase(estadoRequest.getNuevoEstado())) {
	                var cita = onCitas.buscarPorId(idCita);
	                if (cita != null && cita.getPaciente() != null && cita.getDoctor() != null) {
	                    String destinatario = cita.getPaciente().getCorreo();
	                    String asunto = "Confirmación de su cita médica";

	                    // Mensaje con doctor y fecha formateada
	                    String cuerpo = "Estimado/a " + cita.getPaciente().getNombre().split(" ")[0] +' ' + cita.getPaciente().getApellido().split(" ")[0]+ ",\n\n" +
	                            "Su cita médica ha sido confirmada.\n\n" +
	                            "Detalles de la cita:\n" +
	                            "Doctor: Dr. " + cita.getDoctor().getNombre().split(" ")[0] + " " + cita.getDoctor().getApellido().split(" ")[0] + "\n" +
	                            "Fecha y hora: " + cita.getHorario().getFecha() + "\n\n" +
	                            "Por favor, llegue con 15 minutos de anticipación.\n\n" +
	                            "Gracias por confiar en nuestro servicio.\n" +
	                            "Atentamente,\nCitas Medicas =)";

	                    emailService.enviarCorreo(destinatario, asunto, cuerpo);
	                }
	            }
	            
	            if ("confirmado".equalsIgnoreCase(estadoRequest.getNuevoEstado())) {
	                var cita = onCitas.buscarPorId(idCita);
	                if (cita != null && cita.getPaciente() != null) {
	                    String telefono = cita.getPaciente().getTelefono();
	                    
	                    // Asegurar formato internacional con +593
	                    if (!telefono.startsWith("+")) {
	                        telefono = "+593" + telefono.substring(1); // asumiendo que guarda 09xxxxxxxx
	                    }

	                    String mensaje = "Hola " + cita.getPaciente().getNombre().split(" ")[0] +' ' + cita.getPaciente().getApellido().split(" ")[0]+
	                                     ", su cita con el Dr. " + cita.getDoctor().getNombre().split(" ")[0] + ' ' +cita.getDoctor().getApellido().split(" ")[0] +
	                                     " ha sido confirmada para el " + cita.getHorario().getFecha() + ".";
	                    whatsappService.enviarMensaje(telefono, mensaje);
	                }
	            }

	            
	            
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
	    
	    
	    @POST
	    @Path("/{idCita}/editar")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response editarCita(
	        @PathParam("idCita") int idCita,
	        CitaEdicionDTO dto
	    ) {
	        try {
	            // 1. Buscar la cita
	            CitasMedicas cita = onCitas.buscarPorId(idCita);
	            if (cita == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                    .entity("Cita no encontrada con id " + idCita).build();
	            }

	            // 2. Marcar horario anterior como disponible (si existe)
	            if (cita.getHorario() != null) {
	                Horario horarioAnterior = onHorario.findById(cita.getHorario().getIdHorario());
	                horarioAnterior.setDisponible(true);
	                onHorario.guardarHorario(horarioAnterior);
	            }

	            // 3. Buscar el nuevo horario
	            Horario nuevoHorario = onHorario.findById(dto.getIdHorarioNuevo());
	            if (nuevoHorario == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                    .entity("Horario no encontrado con id " + dto.getIdHorarioNuevo()).build();
	            }

	            // 4. Marcar nuevo horario como NO disponible
	            nuevoHorario.setDisponible(false);
	            onHorario.guardarHorario(nuevoHorario);

	            // 5. Actualizar cita con nuevo horario y descripción
	            cita.setHorario(nuevoHorario);
	            cita.setDescripcion(dto.getDescripcion());
	            onCitas.guardarCitasMedicas(cita);

	            return Response.ok(cita).build();

	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Error al editar la cita").build();
	        }
	    }
	    
	    public static class CitaEdicionDTO {
	        private int idHorarioNuevo;
	        private String descripcion;

	        public int getIdHorarioNuevo() { return idHorarioNuevo; }
	        public void setIdHorarioNuevo(int idHorarioNuevo) { this.idHorarioNuevo = idHorarioNuevo; }

	        public String getDescripcion() { return descripcion; }
	        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	    }
	    
	    
	    @GET
	    @Path("/estado/{estado}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitasPorEstado(@PathParam("estado") String estado) {
	        try {
	            List<CitasMedicas> citas = onCitas.listarPorEstado(estado);

	            if (citas == null || citas.isEmpty()) {
	                return Response.status(Response.Status.NOT_FOUND)
	                               .entity("No hay citas con el estado: " + estado)
	                               .build();
	            }

	            return Response.ok(citas).build();

	        } catch (Exception e) {
	            e.printStackTrace(); // O usa un logger
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity("Error al obtener las citas: " + e.getMessage())
	                           .build();
	        }
	    }



	    
	    

}
