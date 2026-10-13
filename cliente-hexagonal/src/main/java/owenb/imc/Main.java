package owenb.imc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import owenb.imc.adaptadores.*;
import owenb.imc.aplicacion.*;
import owenb.imc.vistas.ClienteFrame;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        CanalUdp canal = new CanalUdp(4000); // 4 segundos de timeout
        ProtocoloUdpMapper mapper = new ProtocoloUdpMapper();
        AdaptadorClienteUdp clienteUdp = new AdaptadorClienteUdp(canal, mapper);
        AdaptadorNotificacionCliente notificador = new AdaptadorNotificacionCliente();
        ClienteMapper clienteMapper = new ClienteMapper();

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "ClienteImc-Worker");
            t.setDaemon(true);
            return t;
        });

        ClienteImcService servicio = new ClienteImcService(clienteUdp, notificador, clienteMapper, executor);

        SwingUtilities.invokeLater(() -> {
            ClienteFrame frame = new ClienteFrame(servicio, servicio);
            notificador.registrar(frame);
            frame.setVisible(true);
        });
    }
}
