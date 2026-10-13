package owenb.imc.aplicacion;

import java.nio.charset.StandardCharsets;
import owenb.imc.dominio.Destinatario;

public class PeticionMapper {
    public record PeticionParseada(CalcularImcCommand comando, Destinatario destinatario) {}

    public PeticionParseada parsear(ProcesarPeticionUdpCommand peticion) {
        if (peticion == null || peticion.datos() == null) {
            throw new ComandoInvalidoException("Peticion o datos UDP nulos.");
        }
        String texto = new String(peticion.datos(), StandardCharsets.UTF_8).trim();
        Destinatario destinatario = new Destinatario(peticion.ipRemota(), peticion.puertoRemoto());

        float peso = 0;
        float altura = 0;
        boolean pesoEncontrado = false;
        boolean alturaEncontrado = false;

        String[] partes = texto.split(";");
        for (String p : partes) {
            String[] kv = p.split(":");
            if (kv.length == 2) {
                String clave = kv[0].trim().toUpperCase();
                String valor = kv[1].trim();
                try {
                    if (clave.equals("PESO")) {
                        peso = Float.parseFloat(valor);
                        pesoEncontrado = true;
                    } else if (clave.equals("ALTURA")) {
                        altura = Float.parseFloat(valor);
                        alturaEncontrado = true;
                    }
                } catch (NumberFormatException e) {
                    throw new ComandoInvalidoException("Valor numerico invalido para " + clave + ": " + valor);
                }
            }
        }

        if (!pesoEncontrado || !alturaEncontrado) {
            throw new ComandoInvalidoException("Formato de peticion invalido. Se esperaba 'PESO:val;ALTURA:val'. Recibido: " + texto);
        }

        return new PeticionParseada(new CalcularImcCommand(peso, altura), destinatario);
    }
}
