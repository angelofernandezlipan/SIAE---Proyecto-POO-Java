package vista;

import controlador.ControladorInscripcion;
import modelo.Asignatura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class VentanaInscripcion {

    // --- Atributos ---
    private final ControladorInscripcion controlador;
    private final JFrame ventana;

    // Tabla 1: Asignaturas Disponibles
    private final JTable tablaAsignaturas;
    private final DefaultTableModel tableModel;

    // Tabla 2: Asignaturas Inscritas
    private final JTable tablaInscritas;
    private final DefaultTableModel tableModelInscritas;


    public VentanaInscripcion(ControladorInscripcion controlador) {
        this.controlador = controlador;

        // Llama al méthodo que crea los componentes visuales
        ventana = crearVentana();

        // Llama al méthodo que define las tablas
        String[] columnasDisponibles = {"Código", "Nombre", "Sección", "Cupos Disp."};
        tableModel = crearModeloTabla(columnasDisponibles);
        tablaAsignaturas = new JTable(tableModel);

        String[] columnasInscritas = {"Código", "Nombre", "Sección"};
        tableModelInscritas = crearModeloTabla(columnasInscritas);
        tablaInscritas = new JTable(tableModelInscritas);

        // Llama al méthodo que ensambla
        ensamblarVentana();

        this.controlador.setVista(this);
    }


    private JFrame crearVentana() {
        JFrame frame = new JFrame("Gestión de Asignaturas");
        frame.setBounds(550, 150, 700, 750);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        return frame;
    }


    private DefaultTableModel crearModeloTabla(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }


    private void ensamblarVentana() {
        // --- SECCIÓN 1: ASIGNATURAS DISPONIBLES ---
        JLabel lblTitulo = new JLabel("Asignaturas Disponibles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(50, 20, 400, 30);
        ventana.add(lblTitulo);

        tablaAsignaturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaAsignaturas);
        scrollPane.setBounds(50, 60, 580, 250);
        ventana.add(scrollPane);

        JButton botonInscribir = new JButton("Inscribir Asignatura Seleccionada");
        botonInscribir.setBounds(200, 320, 250, 40);
        ventana.add(botonInscribir);

        // --- SECCIÓN 2: MIS ASIGNATURAS INSCRITAS ---
        JLabel lblTituloInscritas = new JLabel("Mis Asignaturas Inscritas");
        lblTituloInscritas.setFont(new Font("Arial", Font.BOLD, 18));
        lblTituloInscritas.setBounds(50, 380, 400, 30);
        ventana.add(lblTituloInscritas);

        tablaInscritas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPaneInscritas = new JScrollPane(tablaInscritas);
        scrollPaneInscritas.setBounds(50, 420, 580, 150);
        ventana.add(scrollPaneInscritas);

        JButton botonDesinscribir = new JButton("Desinscribir Asignatura Seleccionada");
        botonDesinscribir.setBounds(200, 580, 280, 40);
        botonDesinscribir.setForeground(Color.RED);
        ventana.add(botonDesinscribir);

        // --- BOTÓN VOLVER ---
        JButton botonVolver = new JButton("Volver al Menú");
        botonVolver.setBounds(50, 650, 150, 30);
        ventana.add(botonVolver);

        // --- Action Listeners ---

        // 1. Acción del botón Inscribir
        botonInscribir.addActionListener(e -> {
            inscribirAsignaturaSeleccionada();
        });

        // 2. Acción del botón Desinscribir
        botonDesinscribir.addActionListener(e -> {
            desinscribirAsignaturaSeleccionada();
        });

        // 3. Acción del botón Volver
        botonVolver.addActionListener(e -> {
            ventana.dispose(); // Cierra solo esta ventana
        });

        // 4. Hacer visible
        ventana.setVisible(true);
    }


    private void inscribirAsignaturaSeleccionada() {
        int selectedRow = tablaAsignaturas.getSelectedRow();
        if (selectedRow == -1) {
            // Le pasa 'null' al controlador, él sabrá que es un error
            controlador.manejarInscripcion(null);
            return;
        }
        String codigoAsignatura = (String) tableModel.getValueAt(selectedRow, 0);
        controlador.manejarInscripcion(codigoAsignatura);
    }


    private void desinscribirAsignaturaSeleccionada() {
        int selectedRow = tablaInscritas.getSelectedRow();
        if (selectedRow == -1) {
            controlador.manejarDesinscripcion(null, null);
            return;
        }
        String codigoAsignatura = (String) tableModelInscritas.getValueAt(selectedRow, 0);
        String nombreAsignatura = (String) tableModelInscritas.getValueAt(selectedRow, 1);

        controlador.manejarDesinscripcion(codigoAsignatura, nombreAsignatura);
    }


    // --- MÉTODOS PÚBLICOS (Para que el Controlador los llame) ---

    public void actualizarTablaDisponibles(List<Asignatura> asignaturas) {
        tableModel.setRowCount(0); // Limpia la tabla
        for (Asignatura asig : asignaturas) {
            Object[] rowData = {
                    asig.getCodigo(),
                    asig.getNombre(),
                    asig.getSeccion(),
                    asig.getCuposDisponibles()
            };
            tableModel.addRow(rowData);
        }
    }


    public void actualizarTablaInscritas(List<Asignatura> asignaturas) {
        tableModelInscritas.setRowCount(0); // Limpia la tabla
        for (Asignatura asig : asignaturas) {
            Object[] rowData = {
                    asig.getCodigo(),
                    asig.getNombre(),
                    asig.getSeccion()
            };
            tableModelInscritas.addRow(rowData);
        }
    }


    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }


    public boolean mostrarConfirmacion(String mensaje) {
        int confirm = JOptionPane.showConfirmDialog(ventana,
                mensaje,
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }
}