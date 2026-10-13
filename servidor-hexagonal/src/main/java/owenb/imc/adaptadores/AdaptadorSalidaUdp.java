package owenb.imc.adaptadores;

import java.net.DatagramPacket;
import java.net.InetAddress;
import owenb.imc.dominio.PuertoNotificacionEvento;
import owenb.imc.dominio.PuertoSalidaRed;
import owenb.imc.dominio.RespuestaCliente;

public class AdaptadorSalidaUdp implements PuertoSalidaRed {
    private final CanalUdp canal;
    private final UdpNetworkMapper mapper;
    private final PuertoNotificacionEvento notificador;

    public AdaptadorSalidaUdp(CanalUdp canal, UdpNetworkMapper mapper, PuertoNotificacionEvento notificador) {
        this.canal = canal;
        this.mapper = mapper;
        this.notificador = notificador;
    }

    @Override
    public void enviarRespuesta(RespuestaCliente respuesta) throws Exception {
        UdpResponse res = mapper.toUdpResponse(respuesta);
        InetAddress ip = InetAddress.getByName(res.ipDestino());
        DatagramPacket packet = new DatagramPacket(res.datos(), res.datos().length, ip, res.puertoDestino());
        canal.enviar(packet);
    }
}
