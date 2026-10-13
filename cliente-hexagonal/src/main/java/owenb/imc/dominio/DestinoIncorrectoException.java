package owenb.imc.dominio;

public class DestinoIncorrectoException extends DominioException {
    private static final long serialVersionUID = 1L;

    public DestinoIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
