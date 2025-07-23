package ec.edu.ups.proyecto.services;

import java.time.LocalTime;
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
	
	@POST
	@Path("/disponibilidades")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crearDisponibilidad(DisponibilidadRequest request) throws Exception {
	    LocalTime inicio = LocalTime.parse(request.getHoraInicio());
	    LocalTime fin = LocalTime.parse(request.getHoraFin());

	    onDisp.crearDisponibilidadYHorarios(
	        request.getDiaSemana(), inicio, fin, request.getDoctorId()
	    );

	    return Response.status(Response.Status.CREATED).build();
	}

	// Clase interna o externa según necesites
	public static class DisponibilidadRequest {
	    private String diaSemana;
	    private String horaInicio;
	    private String horaFin;
	    private String doctorId;

	    public String getDiaSemana() {
	        return diaSemana;
	    }

	    public void setDiaSemana(String diaSemana) {
	        this.diaSemana = diaSemana;
	    }

	    public String getHoraInicio() {
	        return horaInicio;
	    }

	    public void setHoraInicio(String horaInicio) {
	        this.horaInicio = horaInicio;
	    }

	    public String getHoraFin() {
	        return horaFin;
	    }

	    public void setHoraFin(String horaFin) {
	        this.horaFin = horaFin;
	    }

	    public String getDoctorId() {
	        return doctorId;
	    }

	    public void setDoctorId(String doctorId) {
	        this.doctorId = doctorId;
	    }
	}


}
