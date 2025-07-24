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
	    private boolean recordatorio24hEnviado;
	    private boolean recordatorio2hEnviado;

	    
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
		

		public boolean isRecordatorio24hEnviado() {
			return recordatorio24hEnviado;
		}

		public void setRecordatorio24hEnviado(boolean recordatorio24hEnviado) {
			this.recordatorio24hEnviado = recordatorio24hEnviado;
		}

		public boolean isRecordatorio2hEnviado() {
			return recordatorio2hEnviado;
		}

		public void setRecordatorio2hEnviado(boolean recordatorio2hEnviado) {
			this.recordatorio2hEnviado = recordatorio2hEnviado;
		}

		@Override
		public String toString() {
			return "CitasMedicas [idCita=" + idCita + ", estado=" + estado + ", descripcion=" + descripcion
					+ ", paciente=" + paciente + ", doctor=" + doctor + ", horario=" + horario + "]";
		}

}
