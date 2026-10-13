package owenb.imc.adaptadores;

import java.nio.charset.StandardCharsets;
import owenb.imc.dominio.RespuestaCliente;
import owenb.imc.dominio.TipoRespuesta;

public class UdpNetworkMapper {
    public UdpResponse toUdpResponse(RespuestaCliente respuesta) {
        StringBuilder sb = new StringBuilder();
        if (respuesta.tipo() == TipoRespuesta.EXITO) {
            sb.append("ESTADO:EXITO;IMC:").append(respuesta.resultado().imc())
              .append(";MENSAJE:").append(respuesta.resultado().mensaje());
        } else {
            sb.append("ESTADO:ERROR;MENSAJE:").append(respuesta.error());
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new UdpResponse(bytes, respuesta.destinatario().ip(), respuesta.destinatario().puerto());
    }
}
