package ec.edu.ups.paw.demo66.services;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest")
public class PathWS extends Application { 
	
	 /*  @Override
	    public Set<Class<?>> getClasses() {
	        Set<Class<?>> resources = new HashSet<>();

	        // Registrar manualmente el filtro CORS
	        resources.add(CorsFilter.class);

	        // Aquí puedes agregar otros servicios REST si quieres también
	        // resources.add(PersonaService.class);

	        return resources;
	    }*/

}
