package owenb.imc.adaptadores;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import owenb.imc.dominio.EstadoConexion;
import owenb.imc.dominio.EventoCliente;
import owenb.imc.dominio.PuertoNotificacionCliente;
import owenb.imc.dominio.ResultadoImc;

public class AdaptadorNotificacionCliente implements PuertoNotificacionCliente {
    private final List<ObservadorCliente> observadores = new CopyOnWriteArrayList<>();

    public void registrar(ObservadorCliente observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void notificar(EventoCliente evento) {
        for (ObservadorCliente obs : observadores) {
            obs.onEvento(evento);
        }
    }

    @Override
    public void notificarEstadoConexion(EstadoConexion estado) {
        for (ObservadorCliente obs : observadores) {
            obs.onCambioEstado(estado);
        }
    }

    @Override
    public void notificarResultado(ResultadoImc resultado) {
        for (ObservadorCliente obs : observadores) {
            obs.onResultado(resultado);
        }
    }
}
