package owenb.imc.aplicacion;

public interface GestionarConexionInputPort {
    void conectar(ConectarCommand command);
    void desconectar();
    boolean estaConectado();
}
