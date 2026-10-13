package owenb.imc.dominio;

public class DestinatarioIncorrectoException extends DominioException {
    private static final long serialVersionUID = 1L;

    public DestinatarioIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
