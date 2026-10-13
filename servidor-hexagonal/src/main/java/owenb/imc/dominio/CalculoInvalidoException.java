package owenb.imc.dominio;

public class CalculoInvalidoException extends DominioException {
    private static final long serialVersionUID = 1L;

    public CalculoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
