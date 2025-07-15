package ec.edu.ups.paw.demo66;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;


@Entity
public class Usuario  implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private int idUser;
    private String nombre;
    private String apellido;
    private String cedula;
    private String direccion;
    private String correo;
    private Date fechaNacimiento;
    private String rol;
    
   
   // @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<CitasMedicas> citasComoPaciente;
    
    
    
    
    public int getIdUser() {
        return idUser;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
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

	@Override
	public String toString() {
		return "Usuario [idUser=" + idUser + ", nombre=" + nombre + ", apellido=" + apellido + ", cedula=" + cedula
				+ ", direccion=" + direccion + ", correo=" + correo + ", fechaNacimiento=" + fechaNacimiento + ", rol="
				+ rol + ", citasComoPaciente=" +  "]";
	}
    
    

}
