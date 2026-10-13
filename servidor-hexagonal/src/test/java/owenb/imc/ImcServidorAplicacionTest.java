package owenb.imc;

import org.junit.jupiter.api.Test;
import owenb.imc.aplicacion.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ImcServidorAplicacionTest {

    @Test
    void debeCalcularImcDesdeServicioAplicacion() {
        CalcularImcService service = new CalcularImcService(new CalculoMapper());
        CalcularImcCommand cmd = new CalcularImcCommand(70.0f, 1.75f);

        ResultadoImcDto dto = service.calcular(cmd);

        assertTrue(dto.exitoso());
        assertEquals(22.86f, dto.imc(), 0.05f);
        assertEquals("Estas bien de peso", dto.mensaje());
    }

    @Test
    void debeParsearPeticionUdpCorrectamente() {
        PeticionMapper mapper = new PeticionMapper();
        String payload = "PESO:75.5;ALTURA:1.80";
        ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand(payload.getBytes(StandardCharsets.UTF_8), "192.168.1.10", 5000);

        PeticionMapper.PeticionParseada parseada = mapper.parsear(cmd);

        assertEquals(75.5f, parseada.comando().peso(), 0.001f);
        assertEquals(1.80f, parseada.comando().altura(), 0.001f);
        assertEquals("192.168.1.10", parseada.destinatario().ip());
        assertEquals(5000, parseada.destinatario().puerto());
    }

    @Test
    void debeFallarConPeticionInvalida() {
        PeticionMapper mapper = new PeticionMapper();
        String payload = "DATO:INCORRECTO";
        ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand(payload.getBytes(StandardCharsets.UTF_8), "127.0.0.1", 5000);

        assertThrows(ComandoInvalidoException.class, () -> mapper.parsear(cmd));
    }
}
