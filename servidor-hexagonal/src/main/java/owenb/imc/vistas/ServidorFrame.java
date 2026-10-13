package owenb.imc.vistas;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import owenb.imc.adaptadores.ObservadorServidor;
import owenb.imc.adaptadores.RedUtil;
import owenb.imc.aplicacion.GestionarServidorInputPort;
import owenb.imc.dominio.EventoServidor;
import owenb.imc.dominio.TipoRespuesta;

public class ServidorFrame extends JFrame implements ObservadorServidor {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final transient GestionarServidorInputPort gestionServidor;

    private JTextField txtIp;
    private JTextField txtPuerto;
    private JButton btnIniciar;
    private JLabel lblEstado;
    private JTextArea areaLogs;

    public ServidorFrame(GestionarServidorInputPort gestionServidor) {
        super("Servidor UDP - C\u00E1lculo de IMC (Arquitectura Hexagonal)");
        this.gestionServidor = Objects.requireNonNull(gestionServidor);
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBorder(new EmptyBorder(12, 16, 5, 16));

        JLabel lblTitulo = new JLabel("SERVIDOR C\u00C1LCULO DE IMC (UDP - ARQUITECTURA HEXAGONAL)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelConfig.setBorder(BorderFactory.createTitledBorder("Configuraci\u00F3n de Red"));

        panelConfig.add(new JLabel("IP Local:"));
        txtIp = new JTextField(RedUtil.obtenerIpLocal(), 11);
        txtIp.setEditable(false);
        txtIp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelConfig.add(txtIp);

        panelConfig.add(new JLabel("Puerto UDP:"));
        txtPuerto = new JTextField("9876", 6);
        txtPuerto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelConfig.add(txtPuerto);

        panelConfig.add(new JLabel("Estado:"));
        lblEstado = new JLabel("DETENIDO");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setForeground(new Color(198, 40, 40));
        panelConfig.add(lblEstado);

        btnIniciar = new JButton("INICIAR");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIniciar.setForeground(new Color(46, 125, 50));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciar.addActionListener(e -> alternarServidor());
        panelConfig.add(btnIniciar);

        panelNorte.add(panelConfig, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        // Panel Central: Log
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBorder(new EmptyBorder(0, 16, 12, 16));

        JPanel panelLogHeader = new JPanel(new BorderLayout());
        JLabel lblLog = new JLabel("Registro de Eventos y Peticiones:");
        lblLog.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelLogHeader.add(lblLog, BorderLayout.WEST);

        JButton btnLimpiar = new JButton("Limpiar Log");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> areaLogs.setText(""));
        panelLogHeader.add(btnLimpiar, BorderLayout.EAST);

        panelCentro.add(panelLogHeader, BorderLayout.NORTH);

        areaLogs = new JTextArea();
        areaLogs.setEditable(false);
        areaLogs.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLogs.setBackground(new Color(25, 28, 36));
        areaLogs.setForeground(new Color(230, 235, 245));
        areaLogs.setMargin(new Insets(8, 8, 8, 8));
        DefaultCaret caret = (DefaultCaret) areaLogs.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scroll = new JScrollPane(areaLogs);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        agregarLog("Servidor IMC listo. Ingrese el puerto y haga clic en INICIAR.");
    }

    private void alternarServidor() {
        if (gestionServidor.isActivo()) {
            gestionServidor.detener();
            btnIniciar.setText("INICIAR");
            btnIniciar.setForeground(new Color(46, 125, 50));
            lblEstado.setText("DETENIDO");
            lblEstado.setForeground(new Color(198, 40, 40));
            txtPuerto.setEditable(true);
        } else {
            try {
                int puerto = Integer.parseInt(txtPuerto.getText().trim());
                gestionServidor.iniciar(puerto);
                btnIniciar.setText("DETENER");
                btnIniciar.setForeground(new Color(198, 40, 40));
                lblEstado.setText("ONLINE");
                lblEstado.setForeground(new Color(46, 125, 50));
                txtPuerto.setEditable(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un n\u00FAmero de puerto v\u00E1lido.", "Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo iniciar: " + ex.getMessage(), "Error Servidor", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void onEvento(EventoServidor evento) {
        SwingUtilities.invokeLater(() -> {
            String hora = evento.fechaHora().format(FORMATO_HORA);
            String prefijo = evento.tipo() == TipoRespuesta.ERROR ? "[ERROR] " : "";
            agregarLog("[" + hora + "] " + prefijo + evento.mensaje());
        });
    }

    private void agregarLog(String mensaje) {
        areaLogs.append(mensaje + "\n");
    }
}
