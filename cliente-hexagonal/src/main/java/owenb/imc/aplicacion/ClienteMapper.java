package owenb.imc.aplicacion;

import owenb.imc.dominio.Altura;
import owenb.imc.dominio.DatosImc;
import owenb.imc.dominio.DestinoServidor;
import owenb.imc.dominio.Peso;

public class ClienteMapper {
    public DestinoServidor toDestino(ConectarCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando de conexion es nulo");
        }
        return new DestinoServidor(command.host(), command.puerto());
    }

    public DatosImc toDatos(CalcularImcCommand command, DestinoServidor destino) {
        if (command == null || destino == null) {
            throw new IllegalArgumentException("Comando o destino nulo");
        }
        return new DatosImc(new Peso(command.peso()), new Altura(command.altura()), destino);
    }
}
