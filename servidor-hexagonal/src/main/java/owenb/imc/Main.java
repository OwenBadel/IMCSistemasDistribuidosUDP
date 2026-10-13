package owenb.imc;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import owenb.imc.adaptadores.*;
import owenb.imc.aplicacion.*;
import owenb.imc.dominio.*;
import owenb.imc.vistas.ServidorFrame;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        CanalUdp canal = new CanalUdp();
        AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();
        UdpNetworkMapper networkMapper = new UdpNetworkMapper();
        PuertoSalidaRed salidaRed = new AdaptadorSalidaUdp(canal, networkMapper, notificador);

        PeticionMapper peticionMapper = new PeticionMapper();
        CalculoMapper calculoMapper = new CalculoMapper();
        ProcesarPeticionUdpInputPort procesarPort = new ProcesarPeticionUdpService(salidaRed, notificador, peticionMapper, calculoMapper);

        ReceptorPeticionesUdp receptor = new ReceptorPeticionesUdp(canal, procesarPort, notificador);
        ControladorServidorRedPort controladorRed = new AdaptadorControlServidorRed(canal, receptor::iniciar, receptor::detener);
        GestionarServidorInputPort gestionPort = new GestionarServidorService(controladorRed, notificador);

        SwingUtilities.invokeLater(() -> {
            ServidorFrame frame = new ServidorFrame(gestionPort);
            notificador.registrarObservador(frame);
            frame.setVisible(true);
        });
    }
}
