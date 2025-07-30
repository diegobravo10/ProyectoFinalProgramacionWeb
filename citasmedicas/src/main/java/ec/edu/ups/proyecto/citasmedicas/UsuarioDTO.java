package ec.edu.ups.proyecto.citasmedicas;

import java.time.LocalDate;

public class UsuarioDTO {
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
    private EspecialidadDTO especialidad; 

    // Getters y setters

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
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public EspecialidadDTO getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(EspecialidadDTO especialidad) {
        this.especialidad = especialidad;
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
    
}


