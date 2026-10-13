package owenb.imc.dominio;

public record Destinatario(String ip, int puerto) {
    public Destinatario {
        if (ip == null || ip.isBlank()) {
            throw new DestinatarioIncorrectoException("La direccion IP de destino no puede estar vacia.");
        }
        if (puerto < 1 || puerto > 65535) {
            throw new DestinatarioIncorrectoException("Puerto de destino fuera de rango: " + puerto);
        }
    }
}
