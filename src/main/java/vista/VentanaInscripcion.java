package vista;

import controlador.ControladorInscripcion;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana que permite al estudiante inscribir y desinscribir asignaturas.
 */
public class VentanaInscripcion extends JFrame {

    private final ControladorInscripcion controlador;
    private final JTextArea areaDisponibles;
    private final JTextArea areaInscritas;
    private final JLabel mensajeResultado;
    private final JTextField campoCodigo;

    public VentanaInscripcion(ControladorInscripcion controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this); // Registrar la vista

        // CLAVE CORREGIDA: Usar el método público del controlador
        setTitle("Gestión de Asignaturas de " + controlador.getNombreEstudiante());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ... (El resto del código de la interfaz se mantiene) ...

        // --- Panel Central ---
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel de Asignaturas Disponibles
        areaDisponibles = new JTextArea();
        areaDisponibles.setEditable(false);
        panelCentral.add(crearPanelListado("Asignaturas Disponibles (Con Cupo)", areaDisponibles));

        // Panel de Asignaturas Inscritas
        areaInscritas = new JTextArea();
        areaInscritas.setEditable(false);
        panelCentral.add(crearPanelListado("Tus Asignaturas Inscritas", areaInscritas));

        add(panelCentral, BorderLayout.CENTER);

        // --- Panel Inferior (Acciones) ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        campoCodigo = new JTextField(10);
        JButton btnInscribir = new JButton("Inscribir Código");
        JButton btnDesinscribir = new JButton("Desinscribir Código");
        mensajeResultado = new JLabel("Listo para operar.");

        btnInscribir.addActionListener(e -> {
            controlador.intentarInscribir(campoCodigo.getText().trim().toUpperCase());
        });

        btnDesinscribir.addActionListener(e -> {
            controlador.intentarDesinscribir(campoCodigo.getText().trim().toUpperCase());
        });

        panelAcciones.add(new JLabel("Código:"));
        panelAcciones.add(campoCodigo);
        panelAcciones.add(btnInscribir);
        panelAcciones.add(btnDesinscribir);
        panelAcciones.add(mensajeResultado);

        add(panelAcciones, BorderLayout.SOUTH);

        actualizarListas(); // Cargar datos iniciales
        setVisible(true);
    }

    private JScrollPane crearPanelListado(String titulo, JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder(titulo));
        return scrollPane;
    }

    /**
     * Método público para recargar la información de ambas listas.
     */
    public void actualizarListas() {
        // Asignaturas Disponibles
        List<String> disponibles = controlador.obtenerAsignaturasDisponiblesInfo();
        areaDisponibles.setText(""); // Limpiar
        if (disponibles.isEmpty()) {
            areaDisponibles.setText("No hay asignaturas disponibles con cupos.");
        } else {
            disponibles.forEach(item -> areaDisponibles.append(item + "\n"));
        }

        // Asignaturas Inscritas
        List<String> inscritas = controlador.obtenerInscripcionesEstudiante();
        areaInscritas.setText(""); // Limpiar
        if (inscritas.isEmpty()) {
            areaInscritas.setText("Actualmente no estás inscrito en ninguna asignatura.");
        } else {
            inscritas.forEach(item -> areaInscritas.append(item + "\n"));
        }
    }

    /**
     * Muestra el resultado de la operación (éxito o error).
     */
    public void mostrarResultado(String mensaje) {
        mensajeResultado.setText(mensaje);
        mensajeResultado.setForeground(mensaje.startsWith("Error:") ? Color.RED : Color.BLUE);
    }
}
