package owenb.imc.dominio;

public record ResultadoImc(float imc, String mensaje) {
    public ResultadoImc {
        if (imc < 0) {
            throw new CalculoInvalidoException("El IMC calculado no puede ser negativo.");
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new CalculoInvalidoException("El mensaje de resultado no puede estar vacio.");
        }
    }
}
