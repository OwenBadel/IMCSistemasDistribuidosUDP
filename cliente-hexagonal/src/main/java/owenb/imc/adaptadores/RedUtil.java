package owenb.imc.adaptadores;

import java.net.InetAddress;

public final class RedUtil {
    private RedUtil() {}

    public static String obtenerIpLocal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}
