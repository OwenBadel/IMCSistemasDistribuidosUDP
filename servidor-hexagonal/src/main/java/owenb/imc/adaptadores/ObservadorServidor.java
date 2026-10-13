package owenb.imc.adaptadores;

import owenb.imc.dominio.EventoServidor;

public interface ObservadorServidor {
    void onEvento(EventoServidor evento);
}
