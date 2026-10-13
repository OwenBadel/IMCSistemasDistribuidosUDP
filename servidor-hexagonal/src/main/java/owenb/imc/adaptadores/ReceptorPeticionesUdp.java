package owenb.imc.adaptadores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;
import owenb.imc.aplicacion.ProcesarPeticionUdpCommand;
import owenb.imc.aplicacion.ProcesarPeticionUdpInputPort;
import owenb.imc.dominio.EventoServidor;
import owenb.imc.dominio.PuertoNotificacionEvento;
import owenb.imc.dominio.TipoRespuesta;

public class ReceptorPeticionesUdp implements Runnable {
    private final CanalUdp canal;
    private final ProcesarPeticionUdpInputPort procesador;
    private final PuertoNotificacionEvento notificador;
    private volatile boolean escuchando = false;
    private Thread hiloEscucha;

    public ReceptorPeticionesUdp(CanalUdp canal, ProcesarPeticionUdpInputPort procesador, PuertoNotificacionEvento notificador) {
        this.canal = canal;
        this.procesador = procesador;
        this.notificador = notificador;
    }

    public synchronized void iniciar() {
        if (!escuchando) {
            escuchando = true;
            hiloEscucha = new Thread(this, "Receptor-UDP-IMC");
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();
        }
    }

    public synchronized void detener() {
        escuchando = false;
        if (hiloEscucha != null) {
            hiloEscucha.interrupt();
            hiloEscucha = null;
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        while (escuchando && canal.isAbierto()) {
            try {
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                canal.recibir(paquete);
                byte[] datos = Arrays.copyOf(paquete.getData(), paquete.getLength());
                String ipRemota = paquete.getAddress().getHostAddress();
                int puertoRemoto = paquete.getPort();

                ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand(datos, ipRemota, puertoRemoto);
                procesador.procesar(cmd);
            } catch (IOException e) {
                if (escuchando) {
                    notificador.notificar(new EventoServidor("Aviso socket: " + e.getMessage(), TipoRespuesta.ERROR));
                }
            }
        }
    }
}
