package owenb.imc.aplicacion;

import owenb.imc.dominio.ControladorServidorRedPort;
import owenb.imc.dominio.EventoServidor;
import owenb.imc.dominio.PuertoNotificacionEvento;
import owenb.imc.dominio.PuertoRed;
import owenb.imc.dominio.TipoRespuesta;

public class GestionarServidorService implements GestionarServidorInputPort {
    private final ControladorServidorRedPort controladorRed;
    private final PuertoNotificacionEvento notificador;

    public GestionarServidorService(ControladorServidorRedPort controladorRed, PuertoNotificacionEvento notificador) {
        this.controladorRed = controladorRed;
        this.notificador = notificador;
    }

    @Override
    public void iniciar(int puerto) {
        try {
            PuertoRed p = new PuertoRed(puerto);
            controladorRed.iniciar(p);
            notificador.notificar(new EventoServidor("Servidor UDP iniciado en el puerto " + puerto, TipoRespuesta.EXITO));
        } catch (Exception e) {
            notificador.notificar(new EventoServidor("Error al iniciar servidor: " + e.getMessage(), TipoRespuesta.ERROR));
            throw new ServidorRedException(e.getMessage(), e);
        }
    }

    @Override
    public void detener() {
        controladorRed.detener();
        notificador.notificar(new EventoServidor("Servidor UDP detenido", TipoRespuesta.EXITO));
    }

    @Override
    public boolean isActivo() {
        return controladorRed.isActivo();
    }

    @Override
    public int getPuertoActual() {
        return controladorRed.getPuertoActual() != null ? controladorRed.getPuertoActual().valor() : 0;
    }
}
