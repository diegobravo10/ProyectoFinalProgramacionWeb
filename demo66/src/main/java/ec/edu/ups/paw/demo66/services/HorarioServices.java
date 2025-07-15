package ec.edu.ups.paw.demo66.services;

import java.util.List;

import ec.edu.ups.paw.demo66.Disponibilidad;
import ec.edu.ups.paw.demo66.Horario;
import ec.edu.ups.paw.demo66.business.HorarioON;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/horario")
public class HorarioServices {
	
	@Inject
	private HorarioON onDisp;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Horario p) {
		try {
			onDisp.guardarHorario(p);
			MensajeJSON response = new MensajeJSON( "sucess",
		            "Horario agregado exitosamente."
		        );
		        return Response.ok(response).build();
		}catch(Exception e) {
		        
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeJSON( "Error", e.getMessage())).build();
		}
		
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersona(){
		List<Horario> listado = onDisp.getHorarios();
		return Response.ok(listado).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerDispDoctor(@PathParam("id") int id) {
	    try {
	        List<Horario> especialidad = onDisp.getHorariosDisp(id);

	        if (especialidad == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity(new MensajeJSON("Error", "Horario no existe"))
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
