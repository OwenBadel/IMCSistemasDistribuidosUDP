package owenb.imc.dominio;

public interface PuertoNotificacionCliente {
    void notificar(EventoCliente evento);
    void notificarEstadoConexion(EstadoConexion estado);
    void notificarResultado(ResultadoImc resultado);
}
