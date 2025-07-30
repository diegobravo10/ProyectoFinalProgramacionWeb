package ec.edu.ups.proyecto.services;

import ec.edu.ups.proyecto.business.CitasMedicasON;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Singleton
@Startup  
public class NotificacionScheduler {

    @Inject
    private CitasMedicasON onCitas;

    @Inject
    private EmailService emailService;

    @Inject
    private WhatsAppService whatsappService;

    // Ejecuta cada hora en punto
    @Schedule(hour = "*", minute = "*", second = "56", persistent = false)
    public void enviarRecordatorios() {
        System.out.println("=== INICIANDO PROCESO DE RECORDATORIOS ===");
        System.out.println("Hora actual: " + LocalDateTime.now());

        try {
            enviarRecordatorios24h();
            enviarRecordatorios2h();
        } catch (Exception e) {
            System.err.println("Error en el proceso de recordatorios: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN PROCESO DE RECORDATORIOS ===");
    }

    private void enviarRecordatorios24h() {
        System.out.println("--- Procesando recordatorios 24h ---");

        var citas24h = onCitas.obtenerCitasEnRango(23, 25, true);
        System.out.println("Citas encontradas para 24h: " + citas24h.size());

        for (var cita : citas24h) {
            try {
                String correo = cita.getPaciente().getCorreo();
                String numeroWhats = formatearNumeroEcuador(cita.getPaciente().getTelefono());

                Date fechaCita = cita.getHorario().getFecha();
                LocalDateTime fechaCitaLocal = fechaCita.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                String fechaFormateada = fechaCitaLocal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                String asunto = "Recordatorio: Cita médica mañana";
                String cuerpoCorreo = String.format(
                        "Estimado/a %s,\n\nLe recordamos que tiene una cita médica programada para mañana:\n" +
                                "Fecha y hora: %s\n\nPor favor, llegue 15 minutos antes de su cita.\n\nSaludos cordiales.",
                        cita.getPaciente().getNombre(), fechaFormateada
                );

                String mensajeWhats = String.format(
                        "Recordatorio: Tiene cita médica mañana %s. Llegue 15 min antes. Gracias!",
                        fechaFormateada
                );

                System.out.println("Enviando recordatorio 24h a: " + correo);

                emailService.enviarCorreo(correo, asunto, cuerpoCorreo);
                whatsappService.enviarMensaje(numeroWhats, mensajeWhats);

                // Marcar como enviado
                cita.setRecordatorio24hEnviado(true);
                onCitas.guardarCitasMedicas(cita);

            } catch (Exception e) {
                System.err.println("Error enviando recordatorio 24h para cita ID " + cita.getIdCita() + ": " + e.getMessage());
            }
        }
    }

    private void enviarRecordatorios2h() {
        System.out.println("--- Procesando recordatorios 2h ---");

        var citas2h = onCitas.obtenerCitasEnRango(1, 3, false);
        System.out.println("Citas encontradas para 2h: " + citas2h.size());

        for (var cita : citas2h) {
            try {
                String correo = cita.getPaciente().getCorreo();
                String numeroWhats = formatearNumeroEcuador(cita.getPaciente().getTelefono());

                Date fechaCita = cita.getHorario().getFecha();
                LocalDateTime fechaCitaLocal = fechaCita.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                String fechaFormateada = fechaCitaLocal.format(DateTimeFormatter.ofPattern("HH:mm"));

                String asunto = "¡Su cita es en 2 horas!";
                String cuerpoCorreo = String.format(
                        "Estimado/a %s,\n\nSu cita médica es en aproximadamente 2 horas:\n" +
                                "Hora: %s\n\nRecuerde llegar 15 minutos antes.\n\nSaludos cordiales.",
                        cita.getPaciente().getNombre(), fechaFormateada
                );

                String mensajeWhats = String.format(
                        "Su cita médica es en 2 horas (%s). No olvide llegar 15 min antes!",
                        fechaFormateada
                );

                System.out.println("Enviando recordatorio 2h a: " + correo);

                emailService.enviarCorreo(correo, asunto, cuerpoCorreo);
                whatsappService.enviarMensaje(numeroWhats, mensajeWhats);

                // Marcar como enviado
                cita.setRecordatorio2hEnviado(true);
                onCitas.guardarCitasMedicas(cita);

            } catch (Exception e) {
                System.err.println("Error enviando recordatorio 2h para cita ID " + cita.getIdCita() + ": " + e.getMessage());
            }
        }
    }

    private String formatearNumeroEcuador(String numero) {
        if (numero == null || numero.isBlank()) {
            return null;
        }
        numero = numero.replaceAll("\\D", "");
        if (numero.startsWith("0")) {
            numero = "+593" + numero.substring(1);
        } else if (!numero.startsWith("+593")) {
            numero = "+593" + numero;
        }
        return numero;
    }
}
