package ec.edu.ups.proyecto.services;

import java.util.List;

import ec.edu.ups.proyecto.business.DoctorON;
import ec.edu.ups.proyecto.citasmedicas.Doctor;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/doctor")
public class DoctorServices {
	

	@Inject
	private DoctorON onDoctor;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response addPersona(Doctor p) {
		try {
			onDoctor.guardarDoctor(p);
			MensajeJSON response = new MensajeJSON( "sucess",
		            "Doctor agregado exitosamente."
		        );
		        return Response.ok(response).build();
		}catch(Exception e) {
		        
		  return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MensajeJSON( "Error", e.getMessage())).build();
		}
		
	}	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersona(){
		List<Doctor> listado = onDoctor.getContactos();
		return Response.ok(listado).build();
	}
	
	@GET
    @Path("/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPersona(@PathParam("cedula") String cedula) {
        try {
            Doctor listado = onDoctor.getPersonacedula(cedula);
            if (listado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity(new MensajeJSON("Error", "Doctor no existe"))
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
    @Path("uid/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDoctor(@PathParam("uid") String uid) {
        try {
            Doctor listado = onDoctor.getDoctorUid(uid);
            if (listado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity(new MensajeJSON("Error", "Doctor no existe"))
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
