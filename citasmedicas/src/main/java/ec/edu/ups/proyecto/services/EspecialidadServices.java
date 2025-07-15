package ec.edu.ups.proyecto.services;

import java.util.List;

import ec.edu.ups.proyecto.business.EspecialidadON;
import ec.edu.ups.proyecto.citasmedicas.Especialidad;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/especialidades")
public class EspecialidadServices {

	
	@Inject
	private EspecialidadON onContactos;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Especialidad p) {
		try {
			onContactos.guardarPersonas(p);
			MensajeJSON response = new MensajeJSON( "sucess",
		            "Especialidad agregada exitosamente."
		        );
		        return Response.ok(response).build();
		}catch(Exception e) {
		        
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeJSON( "Error", e.getMessage())).build();
		}
		
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersona(){
		List<Especialidad> listado = onContactos.getContactos();
		return Response.ok(listado).build();
	}
	
	@GET
	@Path("/{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerEspecialidad(@PathParam("nombre") String nombre) {
	    try {
	        if (nombre == null || nombre.trim().isEmpty()) {
	            return Response.status(Response.Status.BAD_REQUEST)
	                           .entity(new MensajeJSON("Error", "El nombre es obligatorio"))
	                           .build();
	        }

	        Especialidad especialidad = onContactos.getEspecialidad(nombre);

	        if (especialidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity(new MensajeJSON("Error", "Especialidad no existe"))
	                           .build();
	        }
	        return Response.ok(especialidad).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity(new MensajeJSON("Error", e.getMessage()))
	                       .build();
	    }
	}


	
}
