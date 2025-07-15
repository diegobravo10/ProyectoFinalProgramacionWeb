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
    
    //@OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<Doctor> doctores;
    

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

	/*public List<Doctor> getDoctores() {
		return doctores;
	}

	public void setDoctores(List<Doctor> doctores) {
		this.doctores = doctores;
	}

	@Override
	public String toString() {
		return "Especialidad [idEspecialidad=" + idEspecialidad + ", nombre=" + nombre + ", doctores=" + doctores + "]";
	}*/
	

}
