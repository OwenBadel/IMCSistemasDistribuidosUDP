package owenb.imc.adaptadores;

import owenb.imc.aplicacion.ServidorRedException;
import owenb.imc.dominio.ControladorServidorRedPort;
import owenb.imc.dominio.PuertoRed;

public class AdaptadorControlServidorRed implements ControladorServidorRedPort {
    private final CanalUdp canal;
    private final Runnable alIniciar;
    private final Runnable alDetener;
    private PuertoRed puertoActual;

    public AdaptadorControlServidorRed(CanalUdp canal, Runnable alIniciar, Runnable alDetener) {
        this.canal = canal;
        this.alIniciar = alIniciar;
        this.alDetener = alDetener;
    }

    @Override
    public synchronized void iniciar(PuertoRed puerto) throws ServidorRedException {
        canal.abrir(puerto.valor());
        this.puertoActual = puerto;
        if (alIniciar != null) {
            alIniciar.run();
        }
    }

    @Override
    public synchronized void detener() {
        if (alDetener != null) {
            alDetener.run();
        }
        canal.cerrar();
        this.puertoActual = null;
    }

    @Override
    public synchronized boolean isActivo() {
        return canal.isAbierto();
    }

    @Override
    public synchronized PuertoRed getPuertoActual() {
        return puertoActual;
    }
}
