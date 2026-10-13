package owenb.imc.dominio;

public record PuertoRed(int valor) {
    public PuertoRed {
        if (valor < 1 || valor > 65535) {
            throw new PuertoIncorrectoException(valor);
        }
    }
}
