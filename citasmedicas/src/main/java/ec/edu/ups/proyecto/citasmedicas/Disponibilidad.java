package ec.edu.ups.proyecto.citasmedicas;

import java.io.Serializable;
import java.time.LocalTime;


import jakarta.persistence.*;

@Entity
public class Disponibilidad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_disponibilidad")
	 private int idDisponibilidad;
	   private String diaSemana;
	   private LocalTime horaInicio;
	   private LocalTime horaFin;
	   
	   @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "doctor_id") 
	    private Doctor doctor;

	    public int getIdDisponibilidad() {
	        return idDisponibilidad;
	    }

	    public void setIdDisponibilidad(int idDisponibilidad) {
	        this.idDisponibilidad = idDisponibilidad;
	    }

	    public String getDiaSemana() {
	        return diaSemana;
	    }

	    public void setDiaSemana(String diaSemana) {
	        this.diaSemana = diaSemana;
	    }

	    public LocalTime getHoraInicio() {
	        return horaInicio;
	    }

	    public void setHoraInicio(LocalTime horaInicio) {
	        this.horaInicio = horaInicio;
	    }

	    public LocalTime getHoraFin() {
	        return horaFin;
	    }

	    public void setHoraFin(LocalTime horaFin) {
	        this.horaFin = horaFin;
	    }

		public Doctor getDoctor() {
			return doctor;
		}

		public void setDoctor(Doctor doctor) {
			this.doctor = doctor;
		}

		@Override
		public String toString() {
			return "Disponibilidad [idDisponibilidad=" + idDisponibilidad + ", diaSemana=" + diaSemana + ", horaInicio="
					+ horaInicio + ", horaFin=" + horaFin + ", doctor=" + doctor + "]";
		}

}
