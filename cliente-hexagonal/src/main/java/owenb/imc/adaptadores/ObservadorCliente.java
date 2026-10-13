package owenb.imc.adaptadores;

import owenb.imc.dominio.EstadoConexion;
import owenb.imc.dominio.EventoCliente;
import owenb.imc.dominio.ResultadoImc;

public interface ObservadorCliente {
    void onEvento(EventoCliente evento);
    void onCambioEstado(EstadoConexion estado);
    void onResultado(ResultadoImc resultado);
}
