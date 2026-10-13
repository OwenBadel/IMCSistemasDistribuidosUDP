package owenb.imc.adaptadores;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import owenb.imc.aplicacion.ClienteRedException;
import owenb.imc.dominio.ClienteUdpPort;
import owenb.imc.dominio.DatosImc;
import owenb.imc.dominio.DestinoServidor;
import owenb.imc.dominio.ResultadoImc;

public class AdaptadorClienteUdp implements ClienteUdpPort {
    private final CanalUdp canal;
    private final ProtocoloUdpMapper mapper;
    private DestinoServidor destinoActual;

    public AdaptadorClienteUdp(CanalUdp canal, ProtocoloUdpMapper mapper) {
        this.canal = canal;
        this.mapper = mapper;
    }

    @Override
    public synchronized void conectar(DestinoServidor destino) throws ClienteRedException {
        canal.abrir();
        this.destinoActual = destino;
    }

    @Override
    public synchronized void desconectar() throws ClienteRedException {
        canal.cerrar();
        this.destinoActual = null;
    }

    @Override
    public synchronized boolean estaConectado() {
        return canal.isAbierto() && destinoActual != null;
    }

    @Override
    public synchronized DestinoServidor getDestinoActual() {
        return destinoActual;
    }

    @Override
    public ResultadoImc solicitarCalculo(DatosImc datos) throws ClienteRedException {
        if (!estaConectado()) {
            throw new ClienteRedException("El cliente no esta conectado.");
        }

        try {
            byte[] payload = mapper.toPayload(datos);
            InetAddress ip = InetAddress.getByName(datos.destino().host());
            DatagramPacket paqueteEnvio = new DatagramPacket(payload, payload.length, ip, datos.destino().puerto());
            canal.enviar(paqueteEnvio);

            byte[] buffer = new byte[1024];
            DatagramPacket paqueteRespuesta = new DatagramPacket(buffer, buffer.length);
            canal.recibir(paqueteRespuesta);

            return mapper.toResultado(paqueteRespuesta.getData(), paqueteRespuesta.getLength());
        } catch (SocketTimeoutException e) {
            throw new ClienteRedException("Tiempo de espera agotado. El servidor no respondio.", e);
        } catch (Exception e) {
            throw new ClienteRedException("Error en comunicacion UDP: " + e.getMessage(), e);
        }
    }
}
