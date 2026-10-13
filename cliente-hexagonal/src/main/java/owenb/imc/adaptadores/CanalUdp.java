package owenb.imc.adaptadores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import owenb.imc.aplicacion.ClienteRedException;

public class CanalUdp {
    private DatagramSocket socket;
    private final int timeoutMs;

    public CanalUdp(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public synchronized void abrir() throws ClienteRedException {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(timeoutMs);
            } catch (SocketException e) {
                throw new ClienteRedException("Error al abrir socket UDP cliente: " + e.getMessage(), e);
            }
        }
    }

    public synchronized void cerrar() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
    }

    public synchronized boolean isAbierto() {
        return socket != null && !socket.isClosed();
    }

    public void enviar(DatagramPacket paquete) throws IOException {
        DatagramSocket s;
        synchronized (this) {
            s = socket;
        }
        if (s == null || s.isClosed()) {
            throw new IOException("Socket cliente no esta disponible.");
        }
        s.send(paquete);
    }

    public void recibir(DatagramPacket paquete) throws IOException {
        DatagramSocket s;
        synchronized (this) {
            s = socket;
        }
        if (s == null || s.isClosed()) {
            throw new IOException("Socket cliente no esta disponible.");
        }
        s.receive(paquete);
    }
}
