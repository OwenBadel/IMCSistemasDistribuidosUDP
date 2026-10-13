package owenb.imc.aplicacion;

import owenb.imc.dominio.Altura;
import owenb.imc.dominio.CalculoImc;
import owenb.imc.dominio.Peso;
import owenb.imc.dominio.ResultadoImc;

public class CalculoMapper {
    public CalculoImc toDomain(CalcularImcCommand command) {
        if (command == null) {
            throw new ComandoInvalidoException("El comando no puede ser nulo.");
        }
        return new CalculoImc(new Peso(command.peso()), new Altura(command.altura()));
    }

    public ResultadoImcDto toDto(ResultadoImc resultado) {
        if (resultado == null) {
            return new ResultadoImcDto(0f, "Sin resultado", false);
        }
        return new ResultadoImcDto(resultado.imc(), resultado.mensaje(), true);
    }
}
