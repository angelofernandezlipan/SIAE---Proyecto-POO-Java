package vista;

import controlador.ControladorAdmin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel central de administración (Dashboard).
 * Ajustes: Botones de Reportes más pequeños y centrados en su columna.
 */
public class PanelAdmin extends JPanel {

    private final ControladorAdmin contAdmin;

    // Componentes visuales
    private final JTextPane resultadosArea;
    private final JTextField campoRutaCarga;

    // Paleta de Colores
    private final Color COLOR_FONDO = new Color(230, 240, 250);
    private final Color COLOR_PRIMARIO = new Color(70, 130, 180);
    private final Color COLOR_VERDE = new Color(40, 167, 69);
    private final Color COLOR_ROJO = new Color(220, 53, 69);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    public PanelAdmin(ControladorAdmin contAdmin) {
        this.contAdmin = contAdmin;

        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        // --- 1. HEADER ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARIO);
        panelHeader.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel lblTitulo = new JLabel("SIAE | Administración");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_BLANCO);

        JButton btnLogout = crearBotonAccion("Cerrar Sesión", COLOR_ROJO);
        btnLogout.setPreferredSize(new Dimension(140, 35));
        btnLogout.addActionListener(e -> contAdmin.manejarLogout());

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnLogout, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- 2. CONTENIDO CENTRAL ---
        JPanel panelContenido = new JPanel(new GridLayout(1, 2, 40, 0));
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(new EmptyBorder(40, 80, 40, 80));

        // COLUMNA IZQUIERDA: Controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setOpaque(false);

        panelControles.add(crearSeccionCargaMasiva());
        panelControles.add(Box.createVerticalStrut(25));
        panelControles.add(crearSeccionReportes());
        panelControles.add(Box.createVerticalStrut(25));
        panelControles.add(crearSeccionMantenimiento());

        // COLUMNA DERECHA: Registro de Actividad
        resultadosArea = new JTextPane();
        resultadosArea.setEditable(false);
        resultadosArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultadosArea.setBackground(COLOR_BLANCO);
        resultadosArea.setMargin(new Insets(15, 15, 15, 15));

        JPanel panelConsola = new JPanel(new BorderLayout());
        panelConsola.setOpaque(false);

        JLabel lblConsola = new JLabel("Historial de Operaciones");
        lblConsola.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblConsola.setForeground(COLOR_TEXTO);
        lblConsola.setBorder(new EmptyBorder(0, 0, 10, 0));

        JScrollPane scrollConsola = new JScrollPane(resultadosArea);
        scrollConsola.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        scrollConsola.getViewport().setBackground(COLOR_BLANCO);

        panelConsola.add(lblConsola, BorderLayout.NORTH);
        panelConsola.add(scrollConsola, BorderLayout.CENTER);

        panelContenido.add(panelControles);
        panelContenido.add(panelConsola);

        add(panelContenido, BorderLayout.CENTER);

        // Referencia auxiliar
        campoRutaCarga = new JTextField("estudiantes.csv");

        agregarLog("Sistema iniciado correctamente.", "INFO");
    }

    // --- MÉTODOS DE INTERFAZ (UI) ---

    private JPanel crearSeccionCargaMasiva() {
        JPanel panel = crearPanelTarjeta("Carga Masiva de Estudiantes");

        JLabel lblInstruccion = new JLabel("Ruta del archivo (CSV/TXT):");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInstruccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelInput = new JPanel(new BorderLayout(5, 0));
        panelInput.setOpaque(false);
        panelInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panelInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtRuta = new JTextField("estudiantes.csv");
        txtRuta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRuta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(4, 5, 4, 5)
        ));

        txtRuta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoRutaCarga.setText(txtRuta.getText());
            }
        });

        JButton btnCargar = crearBotonAccion("Cargar", COLOR_PRIMARIO);
        btnCargar.setPreferredSize(new Dimension(80, 30));
        btnCargar.addActionListener(e -> realizarCarga(txtRuta.getText()));

        panelInput.add(txtRuta, BorderLayout.CENTER);
        panelInput.add(btnCargar, BorderLayout.EAST);

        JButton btnVerCredenciales = crearBotonAccion("Ver Reporte de Credenciales", COLOR_PRIMARIO);
        btnVerCredenciales.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnVerCredenciales.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVerCredenciales.addActionListener(e -> verReporteCredenciales());

        panel.add(lblInstruccion);
        panel.add(Box.createVerticalStrut(8));
        panel.add(panelInput);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnVerCredenciales);

        return panel;
    }

    private JPanel crearSeccionReportes() {
        JPanel panel = crearPanelTarjeta("Reportes del Sistema");

        // CAMBIO: Usamos FlowLayout CENTER para que los botones queden al medio
        // del panel de la columna izquierda.
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT); // El panel contenedor se alinea a la izq

        JButton btnCupos = crearBotonAccion("Rep. Cupos", COLOR_PRIMARIO);
        // CAMBIO: Tamaño reducido (110 ancho x 30 alto)
        btnCupos.setPreferredSize(new Dimension(110, 30));
        btnCupos.addActionListener(e -> generarReporte(1));

        JButton btnInscripciones = crearBotonAccion("Rep. Inscritos", COLOR_PRIMARIO);
        // CAMBIO: Tamaño reducido
        btnInscripciones.setPreferredSize(new Dimension(110, 30));
        btnInscripciones.addActionListener(e -> generarReporte(2));

        panelBotones.add(btnCupos);
        panelBotones.add(btnInscripciones);

        panel.add(Box.createVerticalStrut(5));
        panel.add(panelBotones);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    private JPanel crearSeccionMantenimiento() {
        JPanel panel = crearPanelTarjeta("Zona de Peligro");

        JLabel lblInfo = new JLabel("Acciones irreversibles:");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLimpiar = crearBotonAccion("ELIMINAR TODOS LOS DATOS", COLOR_ROJO);
        btnLimpiar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.addActionListener(e -> confirmarLimpiezaDatos());

        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(btnLimpiar);
        return panel;
    }

    // --- MÉTODOS AUXILIARES UI ---

    private JPanel crearPanelTarjeta(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                BorderFactory.createTitledBorder(
                        new LineBorder(new Color(220, 220, 220)),
                        titulo,
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 13),
                        COLOR_PRIMARIO
                )
        ));
        JPanel contentPadding = new JPanel();
        contentPadding.setLayout(new BoxLayout(contentPadding, BoxLayout.Y_AXIS));
        contentPadding.setBackground(COLOR_BLANCO);
        contentPadding.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.add(contentPadding);
        return contentPadding;
    }

    private JButton crearBotonAccion(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorFondo.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(colorFondo); }
        });
        return btn;
    }

    // --- LÓGICA DE REGISTRO (LOGS) ---

    private void agregarLog(String mensaje, String tipo) {
        StyledDocument doc = resultadosArea.getStyledDocument();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String hora = "[" + sdf.format(new Date()) + "] ";

        SimpleAttributeSet estiloBase = new SimpleAttributeSet();
        StyleConstants.setFontFamily(estiloBase, "Segoe UI");
        StyleConstants.setFontSize(estiloBase, 12);

        SimpleAttributeSet estiloTipo = new SimpleAttributeSet();

        Color colorTexto;
        String prefijo;
        if (tipo.equals("ERROR")) {
            colorTexto = new Color(220, 53, 69);
            prefijo = "[ERROR] ";
        } else if (tipo.equals("SUCCESS")) {
            colorTexto = new Color(40, 167, 69);
            prefijo = "[OK] ";
        } else {
            colorTexto = Color.DARK_GRAY;
            prefijo = ">> ";
        }
        StyleConstants.setForeground(estiloTipo, colorTexto);
        StyleConstants.setBold(estiloTipo, true);

        try {
            StyleConstants.setForeground(estiloBase, Color.GRAY);
            doc.insertString(doc.getLength(), hora, estiloBase);
            doc.insertString(doc.getLength(), prefijo + mensaje + "\n", estiloTipo);
            resultadosArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // --- LÓGICA DE NEGOCIO ---

    private void realizarCarga(String ruta) {
        if (ruta.isEmpty()) {
            agregarLog("Debe ingresar una ruta de archivo.", "ERROR");
            return;
        }
        agregarLog("Cargando: " + ruta + "...", "INFO");
        java.util.List<String> errores = contAdmin.cargarEstudiantes(ruta);

        if (errores.isEmpty()) {
            agregarLog("Carga completada exitosamente.", "SUCCESS");
            agregarLog("Genere reporte de credenciales.", "INFO");
        } else {
            agregarLog("Problemas detectados:", "ERROR");
            errores.forEach(err -> agregarLog(err, "ERROR"));
        }
    }

    private void generarReporte(int tipo) {
        java.util.List<String> mensajes = contAdmin.exportarReportes(tipo);
        agregarLog("Generando reporte...", "INFO");
        mensajes.forEach(msg -> {
            boolean esError = msg.startsWith("Error");
            agregarLog(msg, esError ? "ERROR" : "SUCCESS");
        });
    }

    private void verReporteCredenciales() {
        String resultado = contAdmin.abrirReporteCredenciales();
        agregarLog(resultado, resultado.startsWith("Error") ? "ERROR" : "INFO");
    }

    private void confirmarLimpiezaDatos() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿ELIMINAR TODOS los datos?\nEsta acción es irreversible.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            contAdmin.limpiarDatosSistema();
            agregarLog("Base de datos reiniciada.", "SUCCESS");
        }
    }
}