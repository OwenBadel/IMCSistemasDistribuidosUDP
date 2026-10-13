package owenb.imc;

import org.junit.jupiter.api.Test;
import owenb.imc.dominio.*;

import static org.junit.jupiter.api.Assertions.*;

class ImcServidorDominioTest {

    @Test
    void debeCalcularImcCorrectamenteEnDiferentesRangos() {
        // Peso bajo: 45 kg, 1.70 m -> IMC ~ 15.57 (< 18.5)
        CalculoImc c1 = new CalculoImc(new Peso(45.0f), new Altura(1.70f));
        ResultadoImc r1 = c1.calcular();
        assertEquals(15.57f, r1.imc(), 0.05f);
        assertEquals("Debes consultar un Medico, tu peso es muy bajo", r1.mensaje());

        // Peso normal: 68 kg, 1.75 m -> IMC ~ 22.20 (18.5 - 24.9)
        CalculoImc c2 = new CalculoImc(new Peso(68.0f), new Altura(1.75f));
        ResultadoImc r2 = c2.calcular();
        assertEquals(22.20f, r2.imc(), 0.05f);
        assertEquals("Estas bien de peso", r2.mensaje());

        // Sobrepeso: 80 kg, 1.70 m -> IMC ~ 27.68 (24.9 - 29.9)
        CalculoImc c3 = new CalculoImc(new Peso(80.0f), new Altura(1.70f));
        ResultadoImc r3 = c3.calcular();
        assertEquals(27.68f, r3.imc(), 0.05f);
        assertEquals("Debes bajar un poco de peso", r3.mensaje());

        // Obesidad: 95 kg, 1.65 m -> IMC ~ 34.89 (> 29.9)
        CalculoImc c4 = new CalculoImc(new Peso(95.0f), new Altura(1.65f));
        ResultadoImc r4 = c4.calcular();
        assertEquals(34.89f, r4.imc(), 0.05f);
        assertEquals("Debes consultar un Medico, tu peso es muy alto", r4.mensaje());
    }

    @Test
    void debeLanzarExcepcionConDimensionesInvalidas() {
        assertThrows(DimensionIncorrectaException.class, () -> new Peso(0f));
        assertThrows(DimensionIncorrectaException.class, () -> new Peso(-5f));
        assertThrows(DimensionIncorrectaException.class, () -> new Altura(0f));
        assertThrows(DimensionIncorrectaException.class, () -> new Altura(-1.75f));
    }

    @Test
    void debeValidarPuertosYDestinatarios() {
        assertThrows(PuertoIncorrectoException.class, () -> new PuertoRed(0));
        assertThrows(PuertoIncorrectoException.class, () -> new PuertoRed(70000));
        assertDoesNotThrow(() -> new PuertoRed(9876));

        assertThrows(DestinatarioIncorrectoException.class, () -> new Destinatario("", 9876));
        assertThrows(DestinatarioIncorrectoException.class, () -> new Destinatario("127.0.0.1", -1));
        assertDoesNotThrow(() -> new Destinatario("127.0.0.1", 9876));
    }
}
