package owenb.imc.adaptadores;

import java.nio.charset.StandardCharsets;
import owenb.imc.dominio.DatosImc;
import owenb.imc.dominio.RespuestaServidorException;
import owenb.imc.dominio.ResultadoImc;

public class ProtocoloUdpMapper {
    public byte[] toPayload(DatosImc datos) {
        String texto = "PESO:" + datos.peso().valor() + ";ALTURA:" + datos.altura().valor();
        return texto.getBytes(StandardCharsets.UTF_8);
    }

    public ResultadoImc toResultado(byte[] datos, int longitud) {
        String texto = new String(datos, 0, longitud, StandardCharsets.UTF_8).trim();
        float imc = 0f;
        String mensaje = "";
        boolean exitoso = false;

        String[] partes = texto.split(";");
        for (String p : partes) {
            int idx = p.indexOf(":");
            if (idx > 0) {
                String clave = p.substring(0, idx).trim().toUpperCase();
                String valor = p.substring(idx + 1).trim();
                if (clave.equals("ESTADO")) {
                    exitoso = valor.equalsIgnoreCase("EXITO");
                } else if (clave.equals("IMC")) {
                    try {
                        imc = Float.parseFloat(valor);
                    } catch (NumberFormatException ignored) {}
                } else if (clave.equals("MENSAJE")) {
                    mensaje = valor;
                }
            }
        }

        if (!exitoso && mensaje.isBlank()) {
            throw new RespuestaServidorException("Respuesta de error desconocida del servidor: " + texto);
        }

        return new ResultadoImc(imc, mensaje, exitoso);
    }
}
