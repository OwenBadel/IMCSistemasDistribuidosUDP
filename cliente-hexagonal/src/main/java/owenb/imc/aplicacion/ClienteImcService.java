package owenb.imc.aplicacion;

import java.util.concurrent.ExecutorService;
import owenb.imc.dominio.*;

public class ClienteImcService implements GestionarConexionInputPort, CalcularImcInputPort {
    private final ClienteUdpPort clienteUdp;
    private final PuertoNotificacionCliente notificador;
    private final ClienteMapper mapper;
    private final ExecutorService executor;

    public ClienteImcService(ClienteUdpPort clienteUdp, PuertoNotificacionCliente notificador,
                             ClienteMapper mapper, ExecutorService executor) {
        this.clienteUdp = clienteUdp;
        this.notificador = notificador;
        this.mapper = mapper;
        this.executor = executor;
    }

    @Override
    public void conectar(ConectarCommand command) {
        try {
            DestinoServidor destino = mapper.toDestino(command);
            clienteUdp.conectar(destino);
            notificador.notificarEstadoConexion(EstadoConexion.CONECTADO);
            notificador.notificar(new EventoCliente("Conectado con servidor en " + destino.host() + ":" + destino.puerto(), false));
        } catch (Exception e) {
            notificador.notificar(new EventoCliente("Falla al conectar: " + e.getMessage(), true));
            throw new ClienteRedException(e.getMessage(), e);
        }
    }

    @Override
    public void desconectar() {
        try {
            clienteUdp.desconectar();
            notificador.notificarEstadoConexion(EstadoConexion.DESCONECTADO);
            notificador.notificar(new EventoCliente("Desconectado del servidor", false));
        } catch (Exception e) {
            notificador.notificar(new EventoCliente("Error al desconectar: " + e.getMessage(), true));
        }
    }

    @Override
    public boolean estaConectado() {
        return clienteUdp.estaConectado();
    }

    @Override
    public void calcular(CalcularImcCommand command) {
        if (!estaConectado()) {
            notificador.notificar(new EventoCliente("No hay conexion activa con el servidor.", true));
            return;
        }

        executor.submit(() -> {
            try {
                DatosImc datos = mapper.toDatos(command, clienteUdp.getDestinoActual());
                notificador.notificar(new EventoCliente("[ENVIO] Peso: " + command.peso() + " kg | Altura: " + command.altura() + " m", false));

                ResultadoImc resultado = clienteUdp.solicitarCalculo(datos);
                notificador.notificarResultado(resultado);
                notificador.notificar(new EventoCliente("[RESPUESTA] IMC: " + resultado.imc() + " (" + resultado.mensaje() + ")", false));
            } catch (Exception ex) {
                notificador.notificar(new EventoCliente("[ERROR] " + ex.getMessage(), true));
            }
        });
    }
}
