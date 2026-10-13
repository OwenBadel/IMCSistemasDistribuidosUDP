package owenb.imc.dominio;

public abstract class DominioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected DominioException(String mensaje) {
        super(mensaje);
    }
}
