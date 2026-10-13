package owenb.imc.dominio;

import java.time.LocalDateTime;

public record EventoServidor(String mensaje, TipoRespuesta tipo, LocalDateTime fechaHora) {
    public EventoServidor(String mensaje, TipoRespuesta tipo) {
        this(mensaje, tipo, LocalDateTime.now());
    }
}
