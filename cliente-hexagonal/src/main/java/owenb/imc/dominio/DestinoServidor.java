package owenb.imc.dominio;

public record DestinoServidor(String host, int puerto) {
    public DestinoServidor {
        if (host == null || host.isBlank()) {
            throw new DestinoIncorrectoException("El host del servidor no puede estar vacio.");
        }
        if (puerto < 1 || puerto > 65535) {
            throw new DestinoIncorrectoException("El puerto debe estar entre 1 y 65535. Valor: " + puerto);
        }
    }
}
