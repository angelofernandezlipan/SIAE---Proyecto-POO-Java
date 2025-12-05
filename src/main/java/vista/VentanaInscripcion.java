package vista;

import controlador.ControladorInscripcion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

/**
 * Vista encargada de la gestión de inscripción y anulación de asignaturas.
 * Implementa una interfaz gráfica basada en listas interactivas con renderizado personalizado.
 */
public class VentanaInscripcion extends JFrame {

    private final ControladorInscripcion controlador;

    // Componentes de lista y modelos de datos
    private final DefaultListModel<String> modeloDisponibles;
    private final JList<String> listaDisponibles;
    private final DefaultListModel<String> modeloInscritas;
    private final JList<String> listaInscritas;

    // Etiqueta para retroalimentación de operaciones
    private final JLabel mensajeResultado;

    // Constantes de color para la interfaz gráfica (UI)
    private final Color COLOR_FONDO = new Color(230, 240, 250);
    private final Color COLOR_PRIMARIO = new Color(70, 130, 180);
    private final Color COLOR_VERDE = new Color(40, 167, 69);
    private final Color COLOR_ROJO = new Color(220, 53, 69);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_HOVER = new Color(245, 247, 250);

    /**
     * Constructor que inicializa la ventana, sus componentes y los manejadores de eventos.
     * @param controlador Instancia del controlador de inscripción.
     */
    public VentanaInscripcion(ControladorInscripcion controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        // Configuración general del JFrame
        setTitle("SIAE - Gestión de Asignaturas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLOR_FONDO);

        // --- Configuración del Panel Superior (Header) ---
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

        // --- Configuración del Panel Central (Listas) ---
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Inicialización de modelos y listas
        modeloDisponibles = new DefaultListModel<>();
        listaDisponibles = new JList<>(modeloDisponibles);
        configurarListaVisual(listaDisponibles);

        modeloInscritas = new DefaultListModel<>();
        listaInscritas = new JList<>(modeloInscritas);
        configurarListaVisual(listaInscritas);

        // Adición de listas al panel central
        panelCentral.add(crearPanelColumna("Oferta Académica (Disponibles)", listaDisponibles));
        panelCentral.add(crearPanelColumna("Mi Carga Académica (Inscritas)", listaInscritas));

        add(panelCentral, BorderLayout.CENTER);

        // --- Configuración del Panel Inferior (Acciones) ---
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

        // --- Asignación de Listeners (Control de Eventos) ---

        // Evento Inscribir
        btnInscribir.addActionListener(e -> {
            String seleccion = listaDisponibles.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Debe seleccionar una asignatura de la oferta académica.");
                return;
            }
            controlador.intentarInscribir(extraerCodigo(seleccion));
        });

        // Evento Desinscribir
        btnDesinscribir.addActionListener(e -> {
            String seleccion = listaInscritas.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Debe seleccionar una asignatura inscrita para eliminarla.");
                return;
            }
            controlador.intentarDesinscribir(extraerCodigo(seleccion));
        });

        // Evento Cerrar
        btnVolver.addActionListener(e -> dispose());

        // Carga inicial de datos
        actualizarListas();
        setVisible(true);
    }

    // --- Métodos de Configuración Visual ---

    /**
     * Aplica el renderizador personalizado y los listeners de movimiento del mouse
     * para efectos visuales (hover) en la lista.
     * @param lista Componente JList a configurar.
     */
    private void configurarListaVisual(JList<String> lista) {
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setBackground(COLOR_FONDO);

        // Instancia del renderizador para controlar el estado hover
        TarjetaAsignaturaRenderer renderer = new TarjetaAsignaturaRenderer();
        lista.setCellRenderer(renderer);

        // Listener para detectar movimiento del mouse sobre los elementos
        lista.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = lista.locationToIndex(e.getPoint());

                // Valida si el cursor está dentro de los límites de la celda
                if (index != -1 && lista.getCellBounds(index, index).contains(e.getPoint())) {
                    if (renderer.getHoveredIndex() != index) {
                        renderer.setHoveredIndex(index);
                        lista.repaint(); // Fuerza repintado para actualizar color
                    }
                } else {
                    if (renderer.getHoveredIndex() != -1) {
                        renderer.setHoveredIndex(-1);
                        lista.repaint();
                    }
                }
            }
        });

        // Listener para limpiar estado hover al salir del componente
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                renderer.setHoveredIndex(-1);
                lista.repaint();
            }
        });
    }

    /**
     * Crea un panel contenedor con ScrollPane para las listas.
     */
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

    /**
     * Genera un botón con estilos predefinidos y efecto de oscurecimiento al pasar el mouse.
     */
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
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    // --- Métodos de Lógica Auxiliar ---

    /**
     * Analiza una cadena de texto para extraer el código de asignatura.
     * Soporta formato detallado ("Código: XXX, ...") o formato simple (primera palabra).
     */
    private String extraerCodigo(String textoLinea) {
        if (textoLinea.startsWith("Código:")) {
            int finCodigo = textoLinea.indexOf(",");
            if (finCodigo != -1) return textoLinea.substring(8, finCodigo).trim();
        }
        return textoLinea.split(" ")[0].trim();
    }

    /**
     * Solicita al controlador la información actual y refresca los modelos de datos de las listas.
     */
    public void actualizarListas() {
        // Actualización de lista de disponibles
        List<String> disponibles = controlador.obtenerAsignaturasDisponiblesInfo();
        modeloDisponibles.clear();
        if (disponibles.isEmpty()) {
            modeloDisponibles.addElement("Sin asignaturas disponibles.");
            listaDisponibles.setEnabled(false);
        } else {
            listaDisponibles.setEnabled(true);
            disponibles.forEach(modeloDisponibles::addElement);
        }

        // Actualización de lista de inscritas
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

    /**
     * Actualiza la etiqueta de mensaje en la interfaz.
     * Cambia el color del texto según si el mensaje indica error o éxito.
     */
    public void mostrarResultado(String mensaje) {
        mensajeResultado.setText(mensaje);
        boolean esError = mensaje.startsWith("Error") || mensaje.startsWith("⚠️");
        mensajeResultado.setForeground(esError ? COLOR_ROJO : new Color(40, 167, 69));
    }

    // --- Clase Interna: Renderizador Personalizado ---

    /**
     * Renderizador de celdas para JList. Dibuja cada elemento como una tarjeta (JPanel)
     * y gestiona cambios de color para selección y hover.
     */
    class TarjetaAsignaturaRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel lbIcono = new JLabel();
        private final JLabel lbTexto = new JLabel();

        // Variable para almacenar el índice sobre el que se encuentra el mouse
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

        public void setHoveredIndex(int index) {
            this.hoveredIndex = index;
        }

        public int getHoveredIndex() {
            return hoveredIndex;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {

            // Configuración del contenido de texto (soporte básico HTML para salto de línea)
            lbTexto.setText("<html>" + value.replace("\n", "<br>") + "</html>");

            // Asignación de icono según contenido
            if (value.contains("Sin asignaturas") || value.contains("Sin inscripciones")) {
                lbIcono.setText("ℹ️");
            } else {
                lbIcono.setText("📘");
            }

            // --- Gestión de Estados Visuales ---

            if (isSelected) {
                // Estado: Seleccionado
                setBackground(new Color(220, 235, 255));
                setBorder(BorderFactory.createCompoundBorder(
                        new EmptyBorder(2, 5, 2, 5),
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 1)
                ));
                lbTexto.setForeground(COLOR_PRIMARIO);

            } else if (index == hoveredIndex) {
                // Estado: Hover (Mouse encima)
                setBackground(COLOR_HOVER);
                setBorder(BorderFactory.createCompoundBorder(
                        new EmptyBorder(2, 5, 2, 5),
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 1)
                ));
                lbTexto.setForeground(Color.BLACK);

            } else {
                // Estado: Normal
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        new EmptyBorder(2, 5, 2, 5),
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
                ));
                lbTexto.setForeground(Color.DARK_GRAY);
            }

            setPreferredSize(new Dimension(0, 60));
            return this;
        }
    }
}