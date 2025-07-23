package ec.edu.ups.proyecto.citasmedicas;

public class CitaDetalle {

	 	private int id;
	    private Usuario paciente;
	    private Horario horario;
	    private String descripcion;
	    private String estado;

	    // Getters y setters
	    public int getId() { return id; }
	    public void setId(int id) { this.id = id; }

	    public Usuario getPaciente() { return paciente; }
	    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

	    public Horario getHorario() { return horario; }
	    public void setHorario(Horario horario) { this.horario = horario; }
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
	    
	    
}
