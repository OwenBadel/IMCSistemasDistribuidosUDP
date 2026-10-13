package owenb.imc.dominio;

import owenb.imc.aplicacion.ServidorRedException;

public interface ControladorServidorRedPort {
    void iniciar(PuertoRed puerto) throws ServidorRedException;
    void detener();
    boolean isActivo();
    PuertoRed getPuertoActual();
}
