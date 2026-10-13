package owenb.imc.dominio;

public record Peso(float valor) {
    public Peso {
        if (valor <= 0) {
            throw new DimensionIncorrectaException("El peso", valor);
        }
    }
}
