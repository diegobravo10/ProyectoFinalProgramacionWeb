package ec.edu.ups.proyecto.citasmedicas;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
public class Horario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario")
   private int idHorario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Guayaquil")
    @Column(name = "fecha")
    private Date fecha;
   private boolean disponible;

   
   @ManyToOne
   @JoinColumn(name = "id_disponibilidad")
   private Disponibilidad disponibilidad;
   

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

	public Disponibilidad getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(Disponibilidad disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	@Override
	public String toString() {
		return "Horario [idHorario=" + idHorario + ", fecha=" + fecha + ", disponible=" + disponible
				+ ", disponibilidad=" + disponibilidad + "]";
	}
}
