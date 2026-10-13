package owenb.imc.aplicacion;

import owenb.imc.dominio.*;

public class ProcesarPeticionUdpService implements ProcesarPeticionUdpInputPort {
    private final PuertoSalidaRed salidaRed;
    private final PuertoNotificacionEvento notificador;
    private final PeticionMapper peticionMapper;
    private final CalculoMapper calculoMapper;

    public ProcesarPeticionUdpService(PuertoSalidaRed salidaRed, PuertoNotificacionEvento notificador,
                                      PeticionMapper peticionMapper, CalculoMapper calculoMapper) {
        this.salidaRed = salidaRed;
        this.notificador = notificador;
        this.peticionMapper = peticionMapper;
        this.calculoMapper = calculoMapper;
    }

    @Override
    public void procesar(ProcesarPeticionUdpCommand command) {
        Destinatario destinatario = null;
        try {
            PeticionMapper.PeticionParseada parseada = peticionMapper.parsear(command);
            destinatario = parseada.destinatario();

            notificador.notificar(new EventoServidor(
                "[PETICION] De " + destinatario.ip() + ":" + destinatario.puerto() +
                " -> Peso: " + parseada.comando().peso() + " kg, Altura: " + parseada.comando().altura() + " m",
                TipoRespuesta.EXITO
            ));

            CalculoImc calculo = calculoMapper.toDomain(parseada.comando());
            ResultadoImc resultado = calculo.calcular();

            RespuestaCliente respuesta = RespuestaCliente.exitosa(resultado, destinatario);
            salidaRed.enviarRespuesta(respuesta);

            notificador.notificar(new EventoServidor(
                "[RESPUESTA] A " + destinatario.ip() + ":" + destinatario.puerto() +
                " -> IMC: " + resultado.imc() + " (" + resultado.mensaje() + ")",
                TipoRespuesta.EXITO
            ));
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            notificador.notificar(new EventoServidor("[ERROR] " + errorMsg, TipoRespuesta.ERROR));
            if (destinatario != null) {
                try {
                    salidaRed.enviarRespuesta(RespuestaCliente.fallida(errorMsg, destinatario));
                } catch (Exception ignored) {}
            }
        }
    }
}
