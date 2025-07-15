package ec.edu.ups.paw.demo66;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Doctor extends Usuario {
    @ManyToOne
    @JoinColumn(name = "especialidad_id") 
    private Especialidad especialidad;
    
   /* @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitasMedicas> citas;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Disponibilidad> horarios;
 */
    

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

	/*public List<CitasMedicas> getCitas() {
		return citas;
	}

	public void setCitas(List<CitasMedicas> citas) {
		this.citas = citas;
	}

	public List<Disponibilidad> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Disponibilidad> horarios) {
		this.horarios = horarios;
	}

	@Override
	public String toString() {
		return "Doctor [especialidad=" + especialidad + ", citas=" + citas + ", horarios=" + horarios + ", getIdUser()="
				+ getIdUser() + ", getNombre()=" + getNombre() + ", getApellido()=" + getApellido() + ", getCedula()="
				+ getCedula() + ", getDireccion()=" + getDireccion() + ", getCorreo()=" + getCorreo()
				+ ", getFechaNacimiento()=" + getFechaNacimiento() + ", getRol()=" + getRol() + "]";
	}

	*/
	
	
    
    
    

}
