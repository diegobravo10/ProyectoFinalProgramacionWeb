package ec.edu.ups.proyecto.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WhatsAppService {

    private static final String ACCOUNT_SID = "AC6a0d51335d92ebcd311b58070a506420";
    private static final String AUTH_TOKEN = "40e5cd625c5afac481800be874f9fae8";
    private static final String TWILIO_WHATSAPP = "whatsapp:+14155238886"; // sandbox Twilio

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println("Twilio inicializado para WhatsApp");
    }

    public void enviarMensaje(String numeroDestino, String mensaje) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + numeroDestino),
                    new PhoneNumber(TWILIO_WHATSAPP),
                    mensaje
            ).create();

            System.out.println("Mensaje de WhatsApp enviado con SID: " + message.getSid() + "...." + numeroDestino);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
