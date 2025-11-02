package gui;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.SistemaInscripcion;
import modelo.acciones.DesinscribirAsignatura; // Importa la clase de acción

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaInscripcion {

    // Referencias al "cerebro" y al usuario
    private SistemaInscripcion sistema;
    private Estudiante estudianteLogueado;

    // Componentes principales de la ventana
    private JFrame ventana;

    // Componentes de la Tabla 1 (Disponibles)
    private JTable tablaAsignaturas;
    private DefaultTableModel tableModel;

    // Componentes de la Tabla 2 (Inscritas)
    private JTable tablaInscritas;
    private DefaultTableModel tableModelInscritas;

    public VentanaInscripcion(SistemaInscripcion sistema, Estudiante estudianteLogueado) {
        this.sistema = sistema;
        this.estudianteLogueado = estudianteLogueado;
        crearVentana();
    }

    private void crearVentana() {
        // --- Configuración General de la Ventana ---
        ventana = new JFrame("Gestión de Asignaturas - " + estudianteLogueado.getNombre());
        ventana.setBounds(550, 150, 700, 750);
        ventana.getContentPane().setBackground(Color.WHITE);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        ventana.setLayout(null);

        // --- Sección 1: Tabla de Asignaturas Disponibles ---
        JLabel lblTitulo = new JLabel("Asignaturas Disponibles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(50, 20, 400, 30);
        ventana.add(lblTitulo);

        String[] columnasDisponibles = {"Código", "Nombre", "Sección", "Cupos Disp."};
        // Modelo de tabla no editable
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

        // --- Sección 2: Tabla de Asignaturas Inscritas ---
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

        // --- Sección 3: Botones de Acción ---
        JButton botonVolver = new JButton("Volver al Menú");
        botonVolver.setBounds(50, 650, 150, 30);
        ventana.add(botonVolver);

        // Carga los datos iniciales en ambas tablas
        actualizarAmbasTablas();

        // Asigna la acción al botón de inscribir
        botonInscribir.addActionListener(e -> {
            inscribirAsignaturaSeleccionada();
        });

        // Asigna la acción al botón de desinscribir
        botonDesinscribir.addActionListener(e -> {
            desinscribirAsignaturaSeleccionada();
        });

        // Asigna la acción al botón de volver
        botonVolver.addActionListener(e -> {
            ventana.dispose();
        });

        // Muestra la ventana
        ventana.setVisible(true);
    }

    private void actualizarAmbasTablas() {
        cargarAsignaturasDisponibles();
        cargarAsignaturasInscritas();
    }

    private void cargarAsignaturasDisponibles() {
        tableModel.setRowCount(0); // Limpia la tabla
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

    private void cargarAsignaturasInscritas() {
        tableModelInscritas.setRowCount(0); // Limpia la tabla
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

        // Muestra el resultado y actualiza las tablas
        if (resultado.startsWith("¡Inscripción exitosa")) {
            JOptionPane.showMessageDialog(ventana, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarAmbasTablas();
        } else {
            JOptionPane.showMessageDialog(ventana, resultado, "Error de Inscripción", JOptionPane.ERROR_MESSAGE);
        }
    }

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

        // Pide confirmación antes de desinscribir
        int confirm = JOptionPane.showConfirmDialog(ventana,
                "¿Estás seguro de que deseas desinscribir '" + nombreAsignatura + "'?",
                "Confirmar Desinscripción",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Llama a la clase de acción estática para ejecutar la lógica
            String resultado = DesinscribirAsignatura.ejecutar(
                    this.sistema,
                    this.estudianteLogueado,
                    codigoAsignatura
            );

            // Muestra el resultado y actualiza las tablas
            if (resultado.startsWith("¡Desinscripción exitosa")) {
                JOptionPane.showMessageDialog(ventana, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarAmbasTablas();
            } else {
                JOptionPane.showMessageDialog(ventana, resultado, "Error de Desinscripción", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}