package owenb.imc.aplicacion;

public interface GestionarServidorInputPort {
    void iniciar(int puerto);
    void detener();
    boolean isActivo();
    int getPuertoActual();
}
