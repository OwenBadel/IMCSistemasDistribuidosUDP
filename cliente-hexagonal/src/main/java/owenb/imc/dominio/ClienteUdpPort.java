package owenb.imc.dominio;

import owenb.imc.aplicacion.ClienteRedException;

public interface ClienteUdpPort {
    void conectar(DestinoServidor destino) throws ClienteRedException;
    void desconectar() throws ClienteRedException;
    ResultadoImc solicitarCalculo(DatosImc datos) throws ClienteRedException;
    boolean estaConectado();
    DestinoServidor getDestinoActual();
}
