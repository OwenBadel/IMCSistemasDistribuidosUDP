package owenb.imc.dominio;

public class PuertoIncorrectoException extends DominioException {
    private static final long serialVersionUID = 1L;

    public PuertoIncorrectoException(int puerto) {
        super("Puerto fuera de rango valido (1 - 65535): " + puerto);
    }
}
