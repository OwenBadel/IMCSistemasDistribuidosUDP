package owenb.imc.dominio;

import java.time.LocalDateTime;

public record EventoCliente(LocalDateTime fechaHora, String mensaje, boolean esError) {
    public EventoCliente(String mensaje, boolean esError) {
        this(LocalDateTime.now(), mensaje, esError);
    }
}
