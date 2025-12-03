package vista;

import controlador.ControladorInscripcion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VentanaInscripcion extends JFrame {

    private final ControladorInscripcion controlador;

    // Modelos y componentes de lista para asignaturas disponibles e inscritas
    private final DefaultListModel<String> modeloDisponibles;
    private final JList<String> listaDisponibles;

    private final DefaultListModel<String> modeloInscritas;
    private final JList<String> listaInscritas;

    // Etiqueta para feedback al usuario
    private final JLabel mensajeResultado;

    public VentanaInscripcion(ControladorInscripcion controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        // Configuración inicial de la ventana principal
        setTitle("Gestión de Asignaturas de " + controlador.getNombreEstudiante());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 245));

        // Configuración del panel superior (Título)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180));
        JLabel titulo = new JLabel("Inscripción de Asignaturas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelSuperior.add(titulo);
        add(panelSuperior, BorderLayout.NORTH);

        // Configuración del panel central (Listas de asignaturas)
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setBackground(new Color(240, 240, 245));
        panelCentral.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Inicialización de la lista de disponibles con renderizado personalizado
        modeloDisponibles = new DefaultListModel<>();
        listaDisponibles = new JList<>(modeloDisponibles);
        configurarListaVisual(listaDisponibles);

        // Inicialización de la lista de inscritas con renderizado personalizado
        modeloInscritas = new DefaultListModel<>();
        listaInscritas = new JList<>(modeloInscritas);
        configurarListaVisual(listaInscritas);

        // Adición de las listas al panel central dentro de paneles con desplazamiento
        panelCentral.add(crearPanelListado("Asignaturas Disponibles", listaDisponibles));
        panelCentral.add(crearPanelListado("Tus Asignaturas Inscritas", listaInscritas));

        add(panelCentral, BorderLayout.CENTER);

        // Configuración del panel inferior (Botones y Mensajes)
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(new Color(240, 240, 245));
        panelInferior.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);

        // Creación de botones de acción
        JButton btnInscribir = crearBotonEstilizado("Inscribir Asignatura", new Color(34, 139, 34));
        JButton btnDesinscribir = crearBotonEstilizado("Desinscribir Asignatura", new Color(178, 34, 34));
        JButton btnVolver = crearBotonEstilizado("Volver", Color.GRAY);

        panelBotones.add(btnInscribir);
        panelBotones.add(btnDesinscribir);
        panelBotones.add(btnVolver);

        mensajeResultado = new JLabel("Seleccione una asignatura para comenzar.", SwingConstants.CENTER);
        mensajeResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mensajeResultado.setForeground(Color.DARK_GRAY);
        mensajeResultado.setBorder(new EmptyBorder(10, 0, 10, 0));

        panelInferior.add(mensajeResultado, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Listener para el botón de inscripción
        btnInscribir.addActionListener(e -> {
            String seleccion = listaDisponibles.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Seleccione una asignatura de la izquierda primero.");
                return;
            }
            controlador.intentarInscribir(extraerCodigo(seleccion));
        });

        // Listener para el botón de desinscripción
        btnDesinscribir.addActionListener(e -> {
            String seleccion = listaInscritas.getSelectedValue();
            if (seleccion == null) {
                mostrarResultado("⚠️ Seleccione una asignatura de la derecha primero.");
                return;
            }
            controlador.intentarDesinscribir(extraerCodigo(seleccion));
        });

        // Listener para cerrar la ventana
        btnVolver.addActionListener(e -> dispose());

        // Carga inicial de datos en las listas
        actualizarListas();
        setVisible(true);
    }

    // Métodos de configuración visual

    /**
     * Aplica la configuración visual y el renderizador personalizado a la lista proporcionada.
     */
    private void configurarListaVisual(JList<String> lista) {
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setBackground(new Color(240, 240, 245));
        lista.setCellRenderer(new TarjetaAsignaturaRenderer());
    }

    /**
     * Crea un JScrollPane con título y borde para contener la lista.
     */
    private JScrollPane crearPanelListado(String titulo, JList<String> lista) {
        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), titulo));
        scrollPane.getViewport().setBackground(new Color(240, 240, 245));
        return scrollPane;
    }

    /**
     * Genera un botón con estilo estandarizado (fuente, color, tamaño).
     */
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    // Lógica de negocio auxiliar

    /**
     * Extrae el código de la asignatura desde la cadena de texto formateada.
     * Identifica patrones específicos como "Código: XXX" o toma la primera palabra.
     */
    private String extraerCodigo(String textoLinea) {
        if (textoLinea.startsWith("Código:")) {
            int finCodigo = textoLinea.indexOf(",");
            if (finCodigo != -1) return textoLinea.substring(8, finCodigo).trim();
        }
        return textoLinea.split(" ")[0].trim();
    }

    /**
     * Solicita al controlador la información actualizada y refresca los modelos de lista.
     */
    public void actualizarListas() {
        // Actualización de lista de disponibles
        List<String> disponibles = controlador.obtenerAsignaturasDisponiblesInfo();
        modeloDisponibles.clear();
        if (disponibles.isEmpty()) {
            modeloDisponibles.addElement("No hay asignaturas disponibles.");
            listaDisponibles.setEnabled(false);
        } else {
            listaDisponibles.setEnabled(true);
            disponibles.forEach(modeloDisponibles::addElement);
        }

        // Actualización de lista de inscritas
        List<String> inscritas = controlador.obtenerInscripcionesEstudiante();
        modeloInscritas.clear();
        if (inscritas.isEmpty()) {
            modeloInscritas.addElement("No tienes inscripciones.");
            listaInscritas.setEnabled(false);
        } else {
            listaInscritas.setEnabled(true);
            inscritas.forEach(modeloInscritas::addElement);
        }
    }

    /**
     * Actualiza la etiqueta de estado con el mensaje proporcionado.
     * Cambia el color del texto según si el mensaje indica error o éxito.
     */
    public void mostrarResultado(String mensaje) {
        mensajeResultado.setText(mensaje);
        mensajeResultado.setForeground((mensaje.startsWith("Error") || mensaje.startsWith("⚠️")) ? Color.RED : new Color(0, 100, 0));
    }

    // Clase interna para renderizado personalizado de celdas (Diseño de Tarjeta)
    static class TarjetaAsignaturaRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel lbTexto = new JLabel();
        private final JLabel lbIcono = new JLabel("📚");

        public TarjetaAsignaturaRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setOpaque(false);

            // Configuración del panel interno que simula la tarjeta
            JPanel tarjeta = new JPanel(new BorderLayout(10, 5));
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(10, 10, 10, 10)
            ));

            lbTexto.setFont(new Font("Monospaced", Font.BOLD, 12));
            lbIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

            tarjeta.add(lbIcono, BorderLayout.WEST);
            tarjeta.add(lbTexto, BorderLayout.CENTER);

            add(tarjeta, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            lbTexto.setText(value);

            // Obtención del componente tarjeta (hijo 0 del panel contenedor)
            JPanel tarjeta = (JPanel) getComponent(0);

            // Modificación visual basada en el estado de selección
            if (isSelected) {
                tarjeta.setBackground(new Color(220, 235, 255));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                        new EmptyBorder(9, 9, 9, 9)
                ));
            } else {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        new EmptyBorder(10, 10, 10, 10)
                ));
            }

            return this;
        }
    }
}
