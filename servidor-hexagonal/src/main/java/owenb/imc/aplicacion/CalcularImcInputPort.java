package owenb.imc.aplicacion;

public interface CalcularImcInputPort {
    ResultadoImcDto calcular(CalcularImcCommand command);
}
