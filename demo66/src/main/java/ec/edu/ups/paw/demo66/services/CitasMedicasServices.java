package ec.edu.ups.paw.demo66.services;

import java.util.List;

import ec.edu.ups.paw.demo66.CitasMedicas;
import ec.edu.ups.paw.demo66.business.CitasMedicasON;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/citasmedicas")
public class CitasMedicasServices {
	
	 @Inject
	    private CitasMedicasON onCitas;

	    // Obtener todas las citas
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response getCitas() {
	        List<CitasMedicas> listado = onCitas.getDispoibilidades(); // O renómbralo a getCitas()
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
	            onCitas.guardarDisponibilidad(cita); // Considera cambiar el nombre del método a guardarCita()
	            MensajeJSON response = new MensajeJSON("success", "Cita médica guardada exitosamente.");
	            return Response.ok(response).build();
	        } catch (Exception e) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity(new MensajeJSON("Error", e.getMessage()))
	                           .build();
	        }
	    }

}
