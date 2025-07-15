package ec.edu.ups.paw.demo66.services;


import java.util.List;

import ec.edu.ups.paw.demo66.Persona;
import ec.edu.ups.paw.demo66.business.ContactosON;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/personas")
public class PersonaServices {
	
	@Inject
	private ContactosON onContactos;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Persona p) {
		try {
			onContactos.guardarPersonas(p);
			MensajeJSON response = new MensajeJSON( "sucess",
		            "Persona agregada exitosamente."
		        );
		        return Response.ok(response).build();
		}catch(Exception e) {
		        
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeJSON( "Error", e.getMessage())).build();
		}
		
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersona(){
		List<Persona> listado = onContactos.getContactos();
		return Response.ok(listado).build();
	}
	
	@GET
	@Path("/{cedula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerPersona(@PathParam("cedula") String cedula) {
	    try {
	        Persona p = onContactos.getPersonacedula(cedula);
	        
	        if (p == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity(new MensajeJSON("Error", "Persona no existe"))
	                           .build();
	        }
	        
	        return Response.ok(p).build();
	        
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity(new MensajeJSON("Error", e.getMessage()))
	                       .build();
	    }
	}
	
	@DELETE
	@Path("/{cedula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminarPersona(@PathParam("cedula") String cedula) {
	    try {
	        Persona p = onContactos.getPersonacedula(cedula);
	        
	        if (p == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity(new MensajeJSON("Error", "Persona no existe"))
	                           .build();
	        }

	        onContactos.eliminarPersona(cedula);

	        return Response.ok(new MensajeJSON("OK", "Persona eliminada")).build();
	        
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity(new MensajeJSON("Error", e.getMessage()))
	                       .build();
	    }
	}


	
	
	

}
