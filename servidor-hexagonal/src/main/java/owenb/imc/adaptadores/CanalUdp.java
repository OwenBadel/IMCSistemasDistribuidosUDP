package owenb.imc.adaptadores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import owenb.imc.aplicacion.ServidorRedException;

public class CanalUdp {
    private DatagramSocket socket;

    public synchronized void abrir(int puerto) throws ServidorRedException {
        try {
            cerrar();
            socket = new DatagramSocket(puerto);
        } catch (SocketException e) {
            throw new ServidorRedException("No se pudo abrir el socket UDP en el puerto " + puerto + ": " + e.getMessage(), e);
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

    public synchronized int getPuertoLocal() {
        return (socket != null && !socket.isClosed()) ? socket.getLocalPort() : 0;
    }

    public void enviar(DatagramPacket paquete) throws IOException {
        DatagramSocket s;
        synchronized (this) {
            s = socket;
        }
        if (s == null || s.isClosed()) {
            throw new IOException("Socket cerrado al intentar enviar");
        }
        s.send(paquete);
    }

    public void recibir(DatagramPacket paquete) throws IOException {
        DatagramSocket s;
        synchronized (this) {
            s = socket;
        }
        if (s == null || s.isClosed()) {
            throw new IOException("Socket cerrado al intentar recibir");
        }
        s.receive(paquete);
    }
}
