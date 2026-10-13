package owenb.imc.dominio;

public class DimensionIncorrectaException extends DominioException {
    private static final long serialVersionUID = 1L;

    public DimensionIncorrectaException(String dimension, float valor) {
        super(dimension + " debe ser mayor a 0. Valor recibido: " + valor);
    }
}
