package owenb.imc.dominio;

import java.util.Locale;

public record CalculoImc(Peso peso, Altura altura) {
    public CalculoImc {
        if (peso == null || altura == null) {
            throw new CalculoInvalidoException("Peso y altura son obligatorios para calcular el IMC.");
        }
    }

    public ResultadoImc calcular() {
        float resultado = peso.valor() / (altura.valor() * altura.valor());
        resultado = Math.round(resultado * 100.0f) / 100.0f;

        String mensaje;
        if (resultado < 18.5f) {
            mensaje = "Debes consultar un Medico, tu peso es muy bajo";
        } else if (resultado >= 18.5f && resultado <= 24.9f) {
            mensaje = "Estas bien de peso";
        } else if (resultado > 24.9f && resultado <= 29.9f) {
            mensaje = "Debes bajar un poco de peso";
        } else {
            mensaje = "Debes consultar un Medico, tu peso es muy alto";
        }

        return new ResultadoImc(resultado, mensaje);
    }
}
