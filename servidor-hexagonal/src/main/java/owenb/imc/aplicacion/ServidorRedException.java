package owenb.imc.aplicacion;

public class ServidorRedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServidorRedException(String mensaje) {
        super(mensaje);
    }

    public ServidorRedException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
