package ec.edu.ups.proyecto.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.*;
import jakarta.inject.Singleton;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

@Path("/stream-citas")
@Singleton
public class CitasStreamResource {

    private final List<SseEventSink> listeners = new CopyOnWriteArrayList<>();
    private Sse sse;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void stream(@Context SseEventSink sink) {
        listeners.add(sink);
        sink.send(sse.newEventBuilder().name("init").data("Conectado").build());
    }

    // Método para enviar notificaciones a todos
    public void notificarNuevaCita(String jsonCita) {
        for (SseEventSink sink : listeners) {
            if (!sink.isClosed()) {
                sink.send(sse.newEventBuilder()
                        .name("nueva-cita")
                        .data(jsonCita)
                        .build());
            }
        }
    }
}

