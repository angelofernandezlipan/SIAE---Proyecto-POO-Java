package vista;

import controlador.ControladorInscripcion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Vista encargada de la gestión de inscripción y anulación de asignaturas.
 * Actualización: Protección al cerrar ventana si faltan asignaturas.
 */
public class VentanaInscripcion extends JFrame {

    private final ControladorInscripcion controlador;

    // Componentes de lista
    private final DefaultListModel<String> modeloDisponibles;
    private final JList<String> listaDisponibles;
    private final DefaultListModel<String> modeloInscritas;
    private final JList<String> listaInscritas;

    // Etiqueta para feedback
    private final JLabel mensajeResultado;

    // Paleta de Colores
    private final Color COLOR_FONDO = new Color(230, 240, 250);
    private final Color COLOR_PRIMARIO = new Color(70, 130, 180);
    private final Color COLOR_VERDE = new Color(40, 167, 69);
    private final Color COLOR_ROJO = new Color(220, 53, 69);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_HOVER = new Color(245, 247, 250);

    public VentanaInscripcion(ControladorInscripcion controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        // Configuración general
        setTitle("SIAE - Gestión de Asignaturas");

        // CAMBIO: Evitar cierre automático para validar requisitos primero
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLOR_FONDO);

        // Listener para la "X" de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.manejarSalida();
            }
        });

        // --- 1. HEADER ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_PRIMARIO);
        panelSuperior.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Inscripción Académica");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_BLANCO);

        JLabel lblSubtitulo = new JLabel("Estudiante: " + controlador.getNombreEstudiante());
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(220, 220, 220));

        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(lblSubtitulo, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Listas) ---
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(20, 20, 10, 20));

        modeloDisponibles = new DefaultListModel<>();
        listaDisponibles = new JList<>(modeloDisponibles);
        configurarListaVisual(listaDisponibles);

        modeloInscritas = new DefaultListModel<>();
        listaInscritas = new JList<>(modeloInscritas);
        configurarListaVisual(listaInscritas);

        panelCentral.add(crearPanelColumna("Oferta Académica (Disponibles)", listaDisponibles));
        panelCentral.add(crearPanelColumna("Mi Carga Académica (Inscritas)", listaInscritas));

        add(panelCentral, BorderLayout.CENTER);

        // --- 3. PANEL INFERIOR (Acciones) ---
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(COLOR_FONDO);

        JButton btnInscribir = crearBotonEstilizado("Inscribir Asignatura", COLOR_VERDE);
        JButton btnDesinscribir = crearBotonEstilizado("Desinscribir Asignatura", COLOR_ROJO);
        JButton btnVolver = crearBotonEstilizado("Volver al Menú", Color.GRAY);

        panelBotones.add(btnInscribir);
        panelBotones.add(btnDesinscribir);
        panelBotones.add(btnVolver);

        mensajeResultado = new JLabel("Seleccione una asignatura para realizar una acción.", SwingConstants.CENTER);
        mensajeResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mensajeResultado.setForeground(Color.DARK_GRAY);
        mensajeResultado.setBorder(new EmptyBorder(0, 0, 15, 0));

        panelInferior.add(mensajeResultado, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // --- Listeners ---
        btnInscribir.addActionListener(e -> {
            String seleccion = listaDisponibles.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Debe seleccionar una asignatura de la oferta académica.");
                return;
            }
            controlador.intentarInscribir(extraerCodigo(seleccion));
        });

        btnDesinscribir.addActionListener(e -> {
            String seleccion = listaInscritas.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Debe seleccionar una asignatura inscrita para eliminarla.");
                return;
            }
            controlador.intentarDesinscribir(extraerCodigo(seleccion));
        });

        // CAMBIO: Botón Volver ahora pasa por la validación
        btnVolver.addActionListener(e -> controlador.manejarSalida());

        // Carga inicial
        actualizarListas();
        setVisible(true);
    }

    // --- MÉTODOS VISUALES Y LÓGICA UI ---

    private void configurarListaVisual(JList<String> lista) {
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setBackground(COLOR_FONDO);

        TarjetaAsignaturaRenderer renderer = new TarjetaAsignaturaRenderer();
        lista.setCellRenderer(renderer);

        lista.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = lista.locationToIndex(e.getPoint());
                if (index != -1 && lista.getCellBounds(index, index).contains(e.getPoint())) {
                    if (renderer.getHoveredIndex() != index) {
                        renderer.setHoveredIndex(index);
                        lista.repaint();
                    }
                } else {
                    if (renderer.getHoveredIndex() != -1) {
                        renderer.setHoveredIndex(-1);
                        lista.repaint();
                    }
                }
            }
        });

        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                renderer.setHoveredIndex(-1);
                lista.repaint();
            }
        });
    }

    private JPanel crearPanelColumna(String titulo, JList<String> lista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelTitulo.setForeground(new Color(60, 60, 60));
        labelTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(labelTitulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200)));
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBotonEstilizado(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorBase.darker()); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(colorBase); }
        });
        return btn;
    }

    // --- LÓGICA AUXILIAR Y VALIDACIÓN ---

    private String extraerCodigo(String textoLinea) {
        if (textoLinea.startsWith("Código:")) {
            int finCodigo = textoLinea.indexOf(",");
            if (finCodigo != -1) return textoLinea.substring(8, finCodigo).trim();
        }
        return textoLinea.split(" ")[0].trim();
    }

    public void actualizarListas() {
        List<String> disponibles = controlador.obtenerAsignaturasDisponiblesInfo();
        modeloDisponibles.clear();
        if (disponibles.isEmpty()) {
            modeloDisponibles.addElement("Sin asignaturas disponibles.");
            listaDisponibles.setEnabled(false);
        } else {
            listaDisponibles.setEnabled(true);
            disponibles.forEach(modeloDisponibles::addElement);
        }

        List<String> inscritas = controlador.obtenerInscripcionesEstudiante();
        modeloInscritas.clear();
        if (inscritas.isEmpty()) {
            modeloInscritas.addElement("Sin inscripciones activas.");
            listaInscritas.setEnabled(false);
        } else {
            listaInscritas.setEnabled(true);
            inscritas.forEach(modeloInscritas::addElement);
        }
    }

    public void mostrarResultado(String mensaje) {
        mensajeResultado.setText(mensaje);
        boolean esError = mensaje.startsWith("Error") || mensaje.startsWith("⚠️");
        mensajeResultado.setForeground(esError ? COLOR_ROJO : new Color(40, 167, 69));
    }

    /**
     * Muestra la advertencia de salida personalizada.
     * @return true si el usuario confirma salir, false si decide quedarse.
     */
    public boolean mostrarAdvertenciaSalida(String mensaje) {
        Object[] opciones = {"Sí, salir igual", "No, seguir inscribiendo"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                mensaje + "\n\n¿Deseas cerrar esta ventana de todos modos?",
                "Requisito Académico Pendiente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[1] // Selecciona "No" por defecto
        );

        return respuesta == 0; // 0 = Primera opción ("Sí")
    }

    // --- RENDERIZADOR ---
    class TarjetaAsignaturaRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel lbIcono = new JLabel();
        private final JLabel lbTexto = new JLabel();
        private int hoveredIndex = -1;

        public TarjetaAsignaturaRenderer() {
            setLayout(new BorderLayout(15, 0));
            setBorder(new EmptyBorder(8, 10, 8, 10));
            setOpaque(true);
            lbIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            lbTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            add(lbIcono, BorderLayout.WEST);
            add(lbTexto, BorderLayout.CENTER);
        }

        public void setHoveredIndex(int index) { this.hoveredIndex = index; }
        public int getHoveredIndex() { return hoveredIndex; }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            lbTexto.setText("<html>" + value.replace("\n", "<br>") + "</html>");
            if (value.contains("Sin asignaturas") || value.contains("Sin inscripciones")) {
                lbIcono.setText("ℹ️");
            } else {
                lbIcono.setText("📘");
            }

            if (isSelected) {
                setBackground(new Color(220, 235, 255));
                setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 5, 2, 5), BorderFactory.createLineBorder(COLOR_PRIMARIO, 1)));
                lbTexto.setForeground(COLOR_PRIMARIO);
            } else if (index == hoveredIndex) {
                setBackground(COLOR_HOVER);
                setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 5, 2, 5), BorderFactory.createLineBorder(new Color(180, 180, 180), 1)));
                lbTexto.setForeground(Color.BLACK);
            } else {
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 5, 2, 5), BorderFactory.createLineBorder(new Color(230, 230, 230), 1)));
                lbTexto.setForeground(Color.DARK_GRAY);
            }
            setPreferredSize(new Dimension(0, 60));
            return this;
        }
    }
}