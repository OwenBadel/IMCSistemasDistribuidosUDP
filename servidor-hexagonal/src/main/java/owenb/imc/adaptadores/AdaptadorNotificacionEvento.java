package owenb.imc.adaptadores;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import owenb.imc.dominio.EventoServidor;
import owenb.imc.dominio.PuertoNotificacionEvento;

public class AdaptadorNotificacionEvento implements PuertoNotificacionEvento {
    private final List<ObservadorServidor> observadores = new CopyOnWriteArrayList<>();

    public void registrarObservador(ObservadorServidor observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(ObservadorServidor observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(EventoServidor evento) {
        for (ObservadorServidor obs : observadores) {
            obs.onEvento(evento);
        }
    }
}
