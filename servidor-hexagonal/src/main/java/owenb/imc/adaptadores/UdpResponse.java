package owenb.imc.adaptadores;

public record UdpResponse(byte[] datos, String ipDestino, int puertoDestino) {
}
