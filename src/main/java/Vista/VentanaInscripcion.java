package Vista;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.SistemaInscripcion;
import modelo.acciones.DesinscribirAsignatura; // <-- Importa la nueva clase

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana que permite a un estudiante INSCRIBIR y DESINSCRIBIR asignaturas.
 * Esta versión utiliza la clase de acción 'DesinscribirAsignatura'.
 */
public class VentanaInscripcion {

    // Referencias al sistema y al usuario logueado
    private SistemaInscripcion sistema;
    private Estudiante estudianteLogueado;

    // Componentes de Swing
    private JFrame ventana;

    // Tabla 1: Asignaturas Disponibles
    private JTable tablaAsignaturas;
    private DefaultTableModel tableModel;

    // Tabla 2: Asignaturas Inscritas
    private JTable tablaInscritas;
    private DefaultTableModel tableModelInscritas;

    /**
     * Constructor que recibe los datos necesarios para operar.
     * @param sistema Instancia del sistema de inscripción.
     * @param estudianteLogueado El objeto Estudiante que ha iniciado sesión.
     */
    public VentanaInscripcion(SistemaInscripcion sistema, Estudiante estudianteLogueado) {
        this.sistema = sistema;
        this.estudianteLogueado = estudianteLogueado;

        crearVentana();
    }

    private void crearVentana() {
        // Configuración de la ventana (alta para 2 tablas)
        ventana = new JFrame("Gestión de Asignaturas - " + estudianteLogueado.getNombre());
        ventana.setBounds(550, 150, 700, 750); // (x, y, ancho, alto)
        ventana.getContentPane().setBackground(Color.WHITE);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLayout(null);

        // --- SECCIÓN 1: ASIGNATURAS DISPONIBLES ---

        JLabel lblTitulo = new JLabel("Asignaturas Disponibles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(50, 20, 400, 30);
        ventana.add(lblTitulo);

        String[] columnasDisponibles = {"Código", "Nombre", "Sección", "Cupos Disp."};
        tableModel = new DefaultTableModel(columnasDisponibles, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAsignaturas = new JTable(tableModel);
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

        String[] columnasInscritas = {"Código", "Nombre", "Sección"};
        tableModelInscritas = new DefaultTableModel(columnasInscritas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaInscritas = new JTable(tableModelInscritas);
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

        // --- Lógica y Action Listeners ---

        // 1. Cargar AMBAS tablas al abrir la ventana
        actualizarAmbasTablas();

        // 2. Acción del botón Inscribir
        botonInscribir.addActionListener(e -> {
            inscribirAsignaturaSeleccionada();
        });

        // 3. Acción del botón Desinscribir
        botonDesinscribir.addActionListener(e -> {
            // *** ¡AQUÍ ESTÁ EL CAMBIO! ***
            desinscribirAsignaturaSeleccionada();
        });

        // 4. Acción del botón Volver
        botonVolver.addActionListener(e -> {
            ventana.dispose(); // Cierra solo esta ventana
        });

        ventana.setVisible(true);
    }

    /**
     * Método de ayuda para actualizar los datos de ambas tablas.
     */
    private void actualizarAmbasTablas() {
        cargarAsignaturasDisponibles();
        cargarAsignaturasInscritas();
    }

    /**
     * Carga la tabla de asignaturas DISPONIBLES.
     */
    private void cargarAsignaturasDisponibles() {
        tableModel.setRowCount(0);
        List<Asignatura> asignaturas = sistema.getAsignaturas();
        for (Asignatura asig : asignaturas) {
            if (asig.getCuposDisponibles() > 0) {
                Object[] rowData = {
                        asig.getCodigo(),
                        asig.getNombre(),
                        asig.getSeccion(),
                        asig.getCuposDisponibles()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Carga la tabla de asignaturas INSCRITAS por el estudiante.
     */
    private void cargarAsignaturasInscritas() {
        tableModelInscritas.setRowCount(0);

        List<String> codigosInscritos = estudianteLogueado.getAsignaturasInscritas();

        for (String codigo : codigosInscritos) {
            Asignatura asig = sistema.buscarAsignaturaPorCodigo(codigo);
            if (asig != null) {
                Object[] rowData = {
                        asig.getCodigo(),
                        asig.getNombre(),
                        asig.getSeccion()
                };
                tableModelInscritas.addRow(rowData);
            }
        }
    }

    /**
     * Lógica para el botón INSCRIBIR.
     */
    private void inscribirAsignaturaSeleccionada() {
        int selectedRow = tablaAsignaturas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(ventana,
                    "Por favor, seleccione una asignatura de la lista DISPONIBLES.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigoAsignatura = (String) tableModel.getValueAt(selectedRow, 0);

        // Llama a la lógica de negocio en SistemaInscripcion
        String resultado = sistema.inscribirAsignatura(estudianteLogueado, codigoAsignatura);

        if (resultado.startsWith("¡Inscripción exitosa")) {
            JOptionPane.showMessageDialog(ventana, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarAmbasTablas(); // Actualiza ambas tablas
        } else {
            JOptionPane.showMessageDialog(ventana, resultado, "Error de Inscripción", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lógica para el botón DESINSCRIBIR.
     * Esta versión llama a la clase de acción 'DesinscribirAsignatura'.
     */
    private void desinscribirAsignaturaSeleccionada() {
        int selectedRow = tablaInscritas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(ventana,
                    "Por favor, seleccione una asignatura de la lista de SUS ASIGNATURAS.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String codigoAsignatura = (String) tableModelInscritas.getValueAt(selectedRow, 0);
        String nombreAsignatura = (String) tableModelInscritas.getValueAt(selectedRow, 1);

        // Preguntar por confirmación
        int confirm = JOptionPane.showConfirmDialog(ventana,
                "¿Estás seguro de que deseas desinscribir '" + nombreAsignatura + "'?",
                "Confirmar Desinscripción",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            // *** ¡AQUÍ ESTÁ LA LLAMADA A LA NUEVA CLASE! ***
            String resultado = DesinscribirAsignatura.ejecutar(
                    this.sistema,
                    this.estudianteLogueado,
                    codigoAsignatura
            );

            if (resultado.startsWith("¡Desinscripción exitosa")) {
                JOptionPane.showMessageDialog(ventana, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarAmbasTablas(); // Actualiza ambas tablas
            } else {
                JOptionPane.showMessageDialog(ventana, resultado, "Error de Desinscripción", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}