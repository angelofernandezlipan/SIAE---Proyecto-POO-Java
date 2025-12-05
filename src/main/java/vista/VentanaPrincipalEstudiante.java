package vista;

import controlador.ControladorPrincipalEstudiante;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista principal del módulo de Estudiante (Dashboard).
 * Diseño corregido: Eliminación de iconos incompatibles (emojis) por texto limpio.
 */
public class VentanaPrincipalEstudiante {

    private final ControladorPrincipalEstudiante controlador;
    private final JFrame menuEstudiantes;

    // Constantes de color (Paleta unificada SIAE)
    private final Color COLOR_FONDO = new Color(230, 240, 250);
    private final Color COLOR_PRIMARIO = new Color(70, 130, 180);
    private final Color COLOR_PRIMARIO_HOVER = new Color(60, 110, 160);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_ROJO = new Color(220, 53, 69);
    private final Color COLOR_TEXTO_GRIS = new Color(100, 100, 100);

    public VentanaPrincipalEstudiante(ControladorPrincipalEstudiante controlador, String nombreEstudiante) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        // Configuración de la ventana principal
        menuEstudiantes = new JFrame("SIAE - Portal del Estudiante");
        menuEstudiantes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuEstudiantes.setSize(1000, 600);
        menuEstudiantes.setLocationRelativeTo(null);
        menuEstudiantes.setLayout(new BorderLayout());
        menuEstudiantes.getContentPane().setBackground(COLOR_FONDO);

        // --- 1. ENCABEZADO (Header Azul) ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_PRIMARIO);
        panelSuperior.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("SIAE | Portal Estudiante");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_BLANCO);

        JLabel lblUsuario = new JLabel("Usuario: " + nombreEstudiante);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(230, 230, 230)); // Blanco suave

        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(lblUsuario, BorderLayout.EAST);
        menuEstudiantes.add(panelSuperior, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Tarjeta de Menú) ---
        JPanel panelCentral = new JPanel(new GridBagLayout()); // Centrado absoluto
        panelCentral.setBackground(COLOR_FONDO);

        // Tarjeta blanca contenedora
        JPanel tarjetaMenu = new JPanel();
        tarjetaMenu.setLayout(new BoxLayout(tarjetaMenu, BoxLayout.Y_AXIS));
        tarjetaMenu.setBackground(COLOR_BLANCO);
        // Borde sutil gris en lugar de sombra compleja
        tarjetaMenu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(40, 50, 40, 50)
        ));

        // Título de la tarjeta
        JLabel lblInstruccion = new JLabel("Menú Principal");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInstruccion.setForeground(new Color(60, 60, 60));
        lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Seleccione una opción para continuar");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(COLOR_TEXTO_GRIS);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjetaMenu.add(lblInstruccion);
        tarjetaMenu.add(Box.createVerticalStrut(5));
        tarjetaMenu.add(lblSub);
        tarjetaMenu.add(Box.createVerticalStrut(30)); // Separador

        // BOTÓN PRINCIPAL: Gestión de Asignaturas
        JButton btnInscribir = crearBotonMenu("Gestión de Asignaturas (Inscribir / Ver)");
        btnInscribir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInscribir.addActionListener(e -> controlador.manejarAperturaInscripcion());

        tarjetaMenu.add(btnInscribir);

        // Agregar la tarjeta al panel de fondo
        panelCentral.add(tarjetaMenu);
        menuEstudiantes.add(panelCentral, BorderLayout.CENTER);

        // --- 3. PANEL INFERIOR (Botón Salir) ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panelInferior.setBackground(COLOR_FONDO);

        JButton btnLogout = crearBotonLogout("Cerrar Sesión");
        btnLogout.addActionListener(e -> controlador.manejarLogout());

        panelInferior.add(btnLogout);
        menuEstudiantes.add(panelInferior, BorderLayout.SOUTH);

        menuEstudiantes.setVisible(true);
    }

    // --- MÉTODOS DE ESTILO (UI) ---

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(COLOR_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        // Dimensiones fijas para que se vea robusto
        btn.setPreferredSize(new Dimension(350, 55));
        btn.setMaximumSize(new Dimension(350, 55));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto Hover simple
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARIO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });
        return btn;
    }

    private JButton crearBotonLogout(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(COLOR_ROJO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_ROJO.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_ROJO);
            }
        });
        return btn;
    }

    public void cerrar() {
        menuEstudiantes.dispose();
    }
}