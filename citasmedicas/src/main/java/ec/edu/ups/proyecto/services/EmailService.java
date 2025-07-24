package ec.edu.ups.proyecto.services;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

@ApplicationScoped
public class EmailService {

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        String remitente = "ups5126@gmail.com";
        String clave = "vcbs afzj lvcf viqh";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);
            System.out.println("Correo enviado a " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

