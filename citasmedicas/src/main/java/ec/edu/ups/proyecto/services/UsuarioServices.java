package ec.edu.ups.proyecto.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseToken;

import ec.edu.ups.proyecto.business.UsuariosON;
import ec.edu.ups.proyecto.citasmedicas.Usuario;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
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
	
	 private FirebaseTokenVerifier verifier;

	    public UsuarioServices() {
	        try {
	            verifier = new FirebaseTokenVerifier();
	        } catch (IOException e) {
	            throw new RuntimeException("No se pudo inicializar Firebase", e);
	        }
	    }
	
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
	
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuarioLogueado(@HeaderParam("Authorization") String authHeader) {
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return Response.status(Response.Status.BAD_REQUEST)
	                           .entity("Falta el token o formato incorrecto").build();
	        }

	        String token = authHeader.replace("Bearer ", "").trim();
	        FirebaseToken decodedToken = verifier.verificarToken(token);
	        String email = decodedToken.getEmail();

	        // Buscar usuario en la BD por email
	        Usuario usuario = onContactos.getPorCorreo(email);

	        if (usuario == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("Usuario no encontrado").build();
	        }

	        // Retornar datos del usuario
	        Map<String, Object> response = new HashMap<>();
	        response.put("email", usuario.getCorreo());
	        response.put("nombre", usuario.getNombre());
	        response.put("rol", usuario.getRol());
	        response.put("cedula", usuario.getCedula());
	        response.put("apellido", usuario.getApellido());
	        response.put("direccion", usuario.getDireccion());
	        response.put("telefono", usuario.getTelefono());
	        response.put("fechaNacimiento", usuario.getFechaNacimiento());
	        



	        return Response.ok(response).build();

	    } catch (Exception e) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("Token inválido o error interno").build();
	    }
	}


}
