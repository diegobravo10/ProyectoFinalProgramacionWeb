package ec.edu.ups.proyecto.citasmedicas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;


@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private int idUser;
    private String nombre;
    private String apellido;
    private String cedula;
    private String direccion;
    private String correo;
    private LocalDate fechaNacimiento;
    private String rol;
    private String telefono;
    private String uid;
    
   
    
    public int getIdUser() {
        return idUser;
    }

    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
	/*public List<CitasMedicas> getCitasComoPaciente() {
		return citasComoPaciente;
	}

	public void setCitasComoPaciente(List<CitasMedicas> citasComoPaciente) {
		this.citasComoPaciente = citasComoPaciente;
	}*/

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
		return "Usuario [idUser=" + idUser + ", nombre=" + nombre + ", apellido=" + apellido + ", cedula=" + cedula
				+ ", direccion=" + direccion + ", correo=" + correo + ", fechaNacimiento=" + fechaNacimiento + ", rol="
				+ rol + ", citasComoPaciente=" +  "]";
	}
    

}
