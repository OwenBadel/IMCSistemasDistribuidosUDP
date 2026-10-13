package owenb.imc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import owenb.imc.adaptadores.*;
import owenb.imc.aplicacion.*;
import owenb.imc.dominio.*;

import static org.junit.jupiter.api.Assertions.*;

class ImcIntegracionUdpTest {
    private CanalUdp canal;
    private AdaptadorControlServidorRed controlador;
    private final int puertoTest = 19877;

    @BeforeEach
    void setUp() {
        canal = new CanalUdp();
        AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();
        UdpNetworkMapper networkMapper = new UdpNetworkMapper();
        PuertoSalidaRed salidaRed = new AdaptadorSalidaUdp(canal, networkMapper, notificador);

        PeticionMapper peticionMapper = new PeticionMapper();
        CalculoMapper calculoMapper = new CalculoMapper();
        ProcesarPeticionUdpInputPort procesarPort = new ProcesarPeticionUdpService(salidaRed, notificador, peticionMapper, calculoMapper);

        ReceptorPeticionesUdp receptor = new ReceptorPeticionesUdp(canal, procesarPort, notificador);
        controlador = new AdaptadorControlServidorRed(canal, receptor::iniciar, receptor::detener);

        controlador.iniciar(new PuertoRed(puertoTest));
    }

    @AfterEach
    void tearDown() {
        controlador.detener();
    }

    @Test
    void debeRecibirPeticionUdpYResponderImcCorrecto() throws Exception {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            clientSocket.setSoTimeout(3000);

            // Enviar peticion: Peso: 70.0 kg, Altura: 1.75 m -> IMC = 22.86
            String payload = "PESO:70.0;ALTURA:1.75";
            byte[] sendData = payload.getBytes(StandardCharsets.UTF_8);
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, puertoTest);
            clientSocket.send(sendPacket);

            // Recibir respuesta
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String respuesta = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

            assertTrue(respuesta.contains("ESTADO:EXITO"));
            assertTrue(respuesta.contains("IMC:22.86"));
            assertTrue(respuesta.contains("Estas bien de peso"));
        }
    }

    @Test
    void debeResponderConErrorCuandoFormatoEsInvalido() throws Exception {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            clientSocket.setSoTimeout(3000);

            String payload = "PESO:invalido;ALTURA:1.75";
            byte[] sendData = payload.getBytes(StandardCharsets.UTF_8);
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, puertoTest);
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String respuesta = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

            assertTrue(respuesta.contains("ESTADO:ERROR"));
        }
    }
}
