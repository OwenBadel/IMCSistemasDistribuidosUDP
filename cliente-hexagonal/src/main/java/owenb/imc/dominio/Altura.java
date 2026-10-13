package owenb.imc.dominio;

public record Altura(float valor) {
    public Altura {
        if (valor <= 0) {
            throw new DimensionIncorrectaException("La altura", valor);
        }
    }
}
