package ec.edu.ups.paw.demo66.services;

import java.util.List;

import ec.edu.ups.paw.demo66.Persona;
import ec.edu.ups.paw.demo66.Usuario;
import ec.edu.ups.paw.demo66.business.ContactosON;
import ec.edu.ups.paw.demo66.business.UsuariosON;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuarios")
public class UsuarioServices {
	
	
	@Inject
	private UsuariosON onContactos;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Usuario p) {
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
		List<Usuario> listado = onContactos.getContactos();
		return Response.ok(listado).build();
	}
	
	@GET
    @Path("/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPersona(@PathParam("nombre") String nombre) {
        try {
            List<Usuario> listado = onContactos.getUsuarioNombre(nombre);
            if (listado == null || listado.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity(new MensajeJSON("Error", "Persona no existe"))
                               .build();
            }
            return Response.ok(listado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(new MensajeJSON("Error", e.getMessage()))
                           .build();
        }
    }

}
