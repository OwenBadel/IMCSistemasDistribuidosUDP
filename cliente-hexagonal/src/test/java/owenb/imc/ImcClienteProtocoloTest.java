package owenb.imc;

import org.junit.jupiter.api.Test;
import owenb.imc.adaptadores.ProtocoloUdpMapper;
import owenb.imc.dominio.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ImcClienteProtocoloTest {

    @Test
    void debeGenerarPayloadCorrectamente() {
        ProtocoloUdpMapper mapper = new ProtocoloUdpMapper();
        DatosImc datos = new DatosImc(new Peso(70.0f), new Altura(1.75f), new DestinoServidor("127.0.0.1", 9876));

        byte[] payload = mapper.toPayload(datos);
        String texto = new String(payload, StandardCharsets.UTF_8);

        assertEquals("PESO:70.0;ALTURA:1.75", texto);
    }

    @Test
    void debeParsearRespuestaExitosa() {
        ProtocoloUdpMapper mapper = new ProtocoloUdpMapper();
        String respuesta = "ESTADO:EXITO;IMC:22.86;MENSAJE:Estas bien de peso";
        byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);

        ResultadoImc resultado = mapper.toResultado(bytes, bytes.length);

        assertTrue(resultado.exitoso());
        assertEquals(22.86f, resultado.imc(), 0.01f);
        assertEquals("Estas bien de peso", resultado.mensaje());
    }

    @Test
    void debeManejarRespuestaError() {
        ProtocoloUdpMapper mapper = new ProtocoloUdpMapper();
        String respuesta = "ESTADO:ERROR;MENSAJE:El peso debe ser mayor a 0";
        byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);

        ResultadoImc resultado = mapper.toResultado(bytes, bytes.length);

        assertFalse(resultado.exitoso());
        assertEquals("El peso debe ser mayor a 0", resultado.mensaje());
    }
}
