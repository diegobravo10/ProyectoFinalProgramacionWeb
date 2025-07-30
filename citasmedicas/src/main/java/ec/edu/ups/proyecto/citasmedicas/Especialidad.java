package ec.edu.ups.proyecto.citasmedicas;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Especialidad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_especialidad")
	private int idEspecialidad;
    private String nombre;
    


    public int getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


	

}
