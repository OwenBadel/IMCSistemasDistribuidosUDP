package owenb.imc.aplicacion;

public class ComandoInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ComandoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
