package owenb.imc.dominio;

public class RespuestaInvalidaException extends DominioException {
    private static final long serialVersionUID = 1L;

    public RespuestaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
