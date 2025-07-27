package ec.edu.ups.proyecto.services;

import java.util.List;

import ec.edu.ups.proyecto.business.HorarioON;
import ec.edu.ups.proyecto.citasmedicas.Horario;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
	
	
	@GET
	@Path("/doctor/{doctorId}/horarios")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Horario> obtenerHorariosPorDoctor(@PathParam("doctorId") int doctorId) {
        return onDisp.getHorariosDispDoc(doctorId);
    }
	 
	@POST
	@Path("/{idHorario}/disponibilidad")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response actualizarDisponibilidad(
	    @PathParam("idHorario") int idHorario,
	    DisponibilidadDTO dto
	) {
		
		System.out.println("PARA SABER:" + dto.getDisponible());
	    try {
	        Horario h = onDisp.findById(idHorario);
	        if (h == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("Horario no encontrado con id " + idHorario)
	                           .build();
	        }
	        h.setDisponible(dto.getDisponible());
	        onDisp.guardarHorario(h);

	        return Response.ok().entity(h).build(); // opcional: retorna el horario actualizado
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                       .entity("Error al actualizar disponibilidad")
	                       .build();
	    }
	}
        
        public static class DisponibilidadDTO {
            private Boolean disponible;

            public Boolean getDisponible() {
                return disponible;
            }

            public void setDisponible(Boolean disponible) {
                this.disponible = disponible;
            }
        }
        
        @DELETE
        @Path("/{id}")
        public Response eliminarHorario(@PathParam("id") int idHorario) {
            onDisp.eliminarHorario(idHorario);
            return Response.noContent().build(); // 204 sin contenido
        }

    


}
