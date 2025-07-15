package ec.edu.ups.proyecto.services;

import java.util.List;

import ec.edu.ups.proyecto.business.DisponibilidadON;
import ec.edu.ups.proyecto.citasmedicas.Disponibilidad;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/disponibilidad")
public class DisponibilidadServices {


	@Inject
	private DisponibilidadON onDisp;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Disponibilidad p) {
		try {
			onDisp.guardarDisponibilidad(p);
			MensajeJSON response = new MensajeJSON( "sucess",
		            "Disponibilidad del doctor agregada exitosamente."
		        );
		        return Response.ok(response).build();
		}catch(Exception e) {
		        
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeJSON( "Error", e.getMessage())).build();
		}
		
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersona(){
		List<Disponibilidad> listado = onDisp.getDispoibilidades();
		return Response.ok(listado).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerDispDoctor(@PathParam("id") int id) {
	    try {
	        List<Disponibilidad> especialidad = onDisp.getDispDoctor(id);

	        if (especialidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity(new MensajeJSON("Error", "Disponibilidad no existe"))
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
