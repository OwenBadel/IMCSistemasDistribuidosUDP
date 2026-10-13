package owenb.imc.vistas;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import owenb.imc.adaptadores.ObservadorCliente;
import owenb.imc.adaptadores.RedUtil;
import owenb.imc.aplicacion.CalcularImcCommand;
import owenb.imc.aplicacion.CalcularImcInputPort;
import owenb.imc.aplicacion.ConectarCommand;
import owenb.imc.aplicacion.GestionarConexionInputPort;
import owenb.imc.dominio.DimensionIncorrectaException;
import owenb.imc.dominio.EstadoConexion;
import owenb.imc.dominio.EventoCliente;
import owenb.imc.dominio.ResultadoImc;

public class ClienteFrame extends JFrame implements ObservadorCliente {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final transient GestionarConexionInputPort conexionPort;
    private final transient CalcularImcInputPort calculoPort;

    private JTextField txtHost;
    private JTextField txtPuerto;
    private JButton btnConectar;
    private JLabel lblEstado;

    private JTextField txtPeso;
    private JTextField txtAltura;
    private JButton btnCalcular;
    private JLabel lblResultadoImc;
    private JLabel lblMensajeServidor;

    private JTextArea areaLogs;

    public ClienteFrame(GestionarConexionInputPort conexionPort, CalcularImcInputPort calculoPort) {
        super("Cliente UDP - C\u00E1lculo de IMC (Arquitectura Hexagonal)");
        this.conexionPort = Objects.requireNonNull(conexionPort);
        this.calculoPort = Objects.requireNonNull(calculoPort);
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior: Titulo y Configuracion
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBorder(new EmptyBorder(12, 16, 5, 16));

        JLabel lblTitulo = new JLabel("CLIENTE C\u00C1LCULO DE IMC (UDP - ARQUITECTURA HEXAGONAL)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentroNorte = new JPanel(new GridLayout(2, 1, 8, 8));

        // Panel Conexion
        JPanel panelConexion = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        panelConexion.setBorder(BorderFactory.createTitledBorder("Conexi\u00F3n con el Servidor (IP local: " + RedUtil.obtenerIpLocal() + ")"));

        panelConexion.add(new JLabel("Host Servidor:"));
        txtHost = new JTextField("127.0.0.1", 10);
        txtHost.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelConexion.add(txtHost);

        panelConexion.add(new JLabel("Puerto:"));
        txtPuerto = new JTextField("9876", 5);
        txtPuerto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelConexion.add(txtPuerto);

        panelConexion.add(new JLabel("Estado:"));
        lblEstado = new JLabel("DESCONECTADO");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstado.setForeground(new Color(198, 40, 40));
        panelConexion.add(lblEstado);

        btnConectar = new JButton("Conectar");
        btnConectar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConectar.setForeground(new Color(46, 125, 50));
        btnConectar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConectar.addActionListener(e -> alternarConexion());
        panelConexion.add(btnConectar);

        panelCentroNorte.add(panelConexion);

        // Panel Datos Corporales (Peso y Altura)
        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos Corporales"));

        panelDatos.add(new JLabel("Peso (kg):"));
        txtPeso = new JTextField(7);
        txtPeso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelDatos.add(txtPeso);

        panelDatos.add(new JLabel("Altura (m):"));
        txtAltura = new JTextField(7);
        txtAltura.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelDatos.add(txtAltura);

        btnCalcular = new JButton("CALCULAR IMC");
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCalcular.setForeground(new Color(46, 125, 50));
        btnCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalcular.setEnabled(false);
        btnCalcular.addActionListener(e -> solicitarCalculo());
        panelDatos.add(btnCalcular);

        panelDatos.add(new JLabel("IMC:"));
        lblResultadoImc = new JLabel("0.0");
        lblResultadoImc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResultadoImc.setForeground(new Color(46, 125, 50));
        panelDatos.add(lblResultadoImc);

        lblMensajeServidor = new JLabel("Sin c\u00E1lculos");
        lblMensajeServidor.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMensajeServidor.setForeground(new Color(80, 80, 80));
        panelDatos.add(lblMensajeServidor);

        panelCentroNorte.add(panelDatos);

        panelNorte.add(panelCentroNorte, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        // Panel Central: Logs
        JPanel panelLogs = new JPanel(new BorderLayout(5, 5));
        panelLogs.setBorder(new EmptyBorder(0, 16, 12, 16));

        JPanel panelLogHeader = new JPanel(new BorderLayout());
        JLabel lblLog = new JLabel("Historial de Comunicaci\u00F3n y Logs:");
        lblLog.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelLogHeader.add(lblLog, BorderLayout.WEST);

        JButton btnLimpiar = new JButton("Limpiar Log");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> areaLogs.setText(""));
        panelLogHeader.add(btnLimpiar, BorderLayout.EAST);

        panelLogs.add(panelLogHeader, BorderLayout.NORTH);

        areaLogs = new JTextArea();
        areaLogs.setEditable(false);
        areaLogs.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLogs.setBackground(new Color(25, 28, 36));
        areaLogs.setForeground(new Color(230, 235, 245));
        areaLogs.setMargin(new Insets(8, 8, 8, 8));
        DefaultCaret caret = (DefaultCaret) areaLogs.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(areaLogs);
        panelLogs.add(scroll, BorderLayout.CENTER);

        add(panelLogs, BorderLayout.CENTER);

        agregarLog("Cliente iniciado. Con\u00E9ctese al servidor para habilitar el c\u00E1lculo.");
    }

    private void alternarConexion() {
        if (conexionPort.estaConectado()) {
            conexionPort.desconectar();
        } else {
            try {
                String host = txtHost.getText().trim();
                int puerto = Integer.parseInt(txtPuerto.getText().trim());
                conexionPort.conectar(new ConectarCommand(host, puerto));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un n\u00FAmero de puerto v\u00E1lido.", "Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Falla al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void solicitarCalculo() {
        String strPeso = txtPeso.getText().trim();
        String strAltura = txtAltura.getText().trim();

        if (strPeso.isEmpty() || strAltura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete ambos campos (Peso y Altura).", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            float peso = Float.parseFloat(strPeso);
            float altura = Float.parseFloat(strAltura);

            if (peso <= 0 || altura <= 0) {
                throw new DimensionIncorrectaException("Peso y altura", 0);
            }

            calculoPort.calcular(new CalcularImcCommand(peso, altura));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese n\u00FAmeros v\u00E1lidos en Peso y Altura.", "Formato Inv\u00E1lido", JOptionPane.WARNING_MESSAGE);
        } catch (DimensionIncorrectaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dimensiones Inv\u00E1lidas", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void onEvento(EventoCliente evento) {
        SwingUtilities.invokeLater(() -> {
            String hora = evento.fechaHora().format(FORMATO_HORA);
            String prefijo = evento.esError() ? "[ERROR] " : "";
            agregarLog("[" + hora + "] " + prefijo + evento.mensaje());
        });
    }

    @Override
    public void onCambioEstado(EstadoConexion estado) {
        SwingUtilities.invokeLater(() -> {
            boolean conectado = estado == EstadoConexion.CONECTADO;
            lblEstado.setText(conectado ? "CONECTADO" : "DESCONECTADO");
            lblEstado.setForeground(conectado ? new Color(46, 125, 50) : new Color(198, 40, 40));
            btnConectar.setText(conectado ? "Desconectar" : "Conectar");
            btnConectar.setForeground(conectado ? new Color(198, 40, 40) : new Color(46, 125, 50));
            btnCalcular.setEnabled(conectado);
            txtHost.setEditable(!conectado);
            txtPuerto.setEditable(!conectado);
        });
    }

    @Override
    public void onResultado(ResultadoImc resultado) {
        SwingUtilities.invokeLater(() -> {
            lblResultadoImc.setText(String.valueOf(resultado.imc()));
            lblMensajeServidor.setText(resultado.mensaje());
        });
    }

    private void agregarLog(String mensaje) {
        areaLogs.append(mensaje + "\n");
    }
}
