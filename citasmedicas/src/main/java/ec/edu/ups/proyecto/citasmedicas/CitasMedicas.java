package ec.edu.ups.proyecto.citasmedicas;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
public class CitasMedicas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cita")
	private int idCita;
	    private String estado;
	    private String descripcion;
	    
	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "paciente_id")
	    private Usuario paciente;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "doctor_id")
	    private Doctor doctor;
	    
	    @OneToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "id_horario")
	    private Horario horario;

	    public int getIdCita() {
	        return idCita;
	    }

	    public void setIdCita(int idCita) {
	        this.idCita = idCita;
	    }


	    public String getEstado() {
	        return estado;
	    }

	    public void setEstado(String estado) {
	        this.estado = estado;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }
	    

		public Horario getHorario() {
			return horario;
		}

		public void setHorario(Horario horario) {
			this.horario = horario;
		}

		public Usuario getPaciente() {
			return paciente;
		}

		public void setPaciente(Usuario paciente) {
			this.paciente = paciente;
		}

		public Doctor getDoctor() {
			return doctor;
		}

		public void setDoctor(Doctor doctor) {
			this.doctor = doctor;
		}

		@Override
		public String toString() {
			return "CitasMedicas [idCita=" + idCita + ", estado=" + estado + ", descripcion=" + descripcion
					+ ", paciente=" + paciente + ", doctor=" + doctor + ", horario=" + horario + "]";
		}

}
