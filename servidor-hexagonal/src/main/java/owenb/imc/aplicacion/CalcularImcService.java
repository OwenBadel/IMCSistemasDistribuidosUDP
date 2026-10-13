package owenb.imc.aplicacion;

import owenb.imc.dominio.CalculoImc;
import owenb.imc.dominio.ResultadoImc;

public class CalcularImcService implements CalcularImcInputPort {
    private final CalculoMapper mapper;

    public CalcularImcService(CalculoMapper mapper) {
        this.mapper = mapper != null ? mapper : new CalculoMapper();
    }

    @Override
    public ResultadoImcDto calcular(CalcularImcCommand command) {
        CalculoImc calculo = mapper.toDomain(command);
        ResultadoImc resultado = calculo.calcular();
        return mapper.toDto(resultado);
    }
}
