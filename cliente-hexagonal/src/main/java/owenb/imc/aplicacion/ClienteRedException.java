package owenb.imc.aplicacion;

public class ClienteRedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClienteRedException(String mensaje) {
        super(mensaje);
    }

    public ClienteRedException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
