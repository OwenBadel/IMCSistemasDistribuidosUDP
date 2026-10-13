package owenb.imc.aplicacion;

public record ProcesarPeticionUdpCommand(byte[] datos, String ipRemota, int puertoRemoto) {
}
