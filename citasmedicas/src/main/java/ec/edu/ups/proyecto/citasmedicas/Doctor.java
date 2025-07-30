package ec.edu.ups.proyecto.citasmedicas;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Doctor extends Usuario {

	@ManyToOne
    @JoinColumn(name = "especialidad_id") 
    private Especialidad especialidad;
    


    public Doctor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }


}
