package owenb.imc;

import org.junit.jupiter.api.Test;
import owenb.imc.dominio.*;

import static org.junit.jupiter.api.Assertions.*;

class ImcClienteDominioTest {

    @Test
    void debeValidarValoresDePesoYAltura() {
        assertThrows(DimensionIncorrectaException.class, () -> new Peso(0f));
        assertThrows(DimensionIncorrectaException.class, () -> new Peso(-10f));
        assertThrows(DimensionIncorrectaException.class, () -> new Altura(0f));
        assertThrows(DimensionIncorrectaException.class, () -> new Altura(-1.5f));

        assertDoesNotThrow(() -> new Peso(70f));
        assertDoesNotThrow(() -> new Altura(1.75f));
    }

    @Test
    void debeValidarDestinoServidor() {
        assertThrows(DestinoIncorrectoException.class, () -> new DestinoServidor("", 9876));
        assertThrows(DestinoIncorrectoException.class, () -> new DestinoServidor("127.0.0.1", 0));
        assertThrows(DestinoIncorrectoException.class, () -> new DestinoServidor("127.0.0.1", 70000));

        assertDoesNotThrow(() -> new DestinoServidor("127.0.0.1", 9876));
    }

    @Test
    void debeCrearDatosImcCorrectamente() {
        Peso peso = new Peso(65.0f);
        Altura altura = new Altura(1.70f);
        DestinoServidor destino = new DestinoServidor("127.0.0.1", 9876);

        DatosImc datos = new DatosImc(peso, altura, destino);

        assertEquals(65.0f, datos.peso().valor());
        assertEquals(1.70f, datos.altura().valor());
        assertEquals("127.0.0.1", datos.destino().host());
        assertEquals(9876, datos.destino().puerto());
    }
}
