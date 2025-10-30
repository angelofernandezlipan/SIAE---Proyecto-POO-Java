import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana que permite a un estudiante ver las asignaturas disponibles
 * e inscribir una de ellas.
 */
public class VentanaInscripcion {

    // Referencias al sistema y al usuario logueado
    private SistemaInscripcion sistema;
    private Estudiante estudianteLogueado;

    // Componentes de Swing
    private JFrame ventana;
    private JTable tablaAsignaturas;
    private DefaultTableModel tableModel;

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
        // Configuración de la ventana
        ventana = new JFrame("Inscripción de Asignaturas");
        ventana.setBounds(550, 150, 700, 500); // (x, y, ancho, alto)
        ventana.getContentPane().setBackground(Color.WHITE);

        // Importante: DISPOSE_ON_CLOSE solo cierra esta ventana, no la aplicación
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLayout(null);

        // Título
        JLabel lblTitulo = new JLabel("Asignaturas Disponibles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(50, 20, 400, 30);
        ventana.add(lblTitulo);

        // --- Configuración de la Tabla ---
        String[] columnas = {"Código", "Nombre", "Sección", "Cupos Disp."};

        // Creamos un DefaultTableModel para que no se puedan editar las celdas
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Todas las celdas no son editables
                return false;
            }
        };

        tablaAsignaturas = new JTable(tableModel);
        tablaAsignaturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo seleccionar una fila

        // Añadimos la tabla a un JScrollPane para poder verla con barra de scroll
        JScrollPane scrollPane = new JScrollPane(tablaAsignaturas);
        scrollPane.setBounds(50, 60, 580, 250);
        ventana.add(scrollPane);

        // --- Botón Inscribir ---
        JButton botonInscribir = new JButton("Inscribir Asignatura Seleccionada");
        botonInscribir.setBounds(200, 330, 250, 40);
        ventana.add(botonInscribir);

        // --- Botón Volver ---
        JButton botonVolver = new JButton("Volver al Menú");
        botonVolver.setBounds(50, 400, 150, 30);
        ventana.add(botonVolver);

        // --- Lógica y Action Listeners ---

        // 1. Llenar la tabla con datos al abrir la ventana
        cargarAsignaturasDisponibles();

        // 2. Acción del botón Inscribir
        botonInscribir.addActionListener(e -> {
            // Llamamos al método de lógica de inscripción
            inscribirAsignaturaSeleccionada();
        });

        // 3. Acción del botón Volver
        botonVolver.addActionListener(e -> {
            ventana.dispose(); // Cierra solo esta ventana
        });

        // Mostrar la ventana
        ventana.setVisible(true);
    }

    /**
     * MÉTODO CLAVE (Usa SistemaInscripcion):
     * Obtiene la lista de asignaturas del sistema, filtra las disponibles
     * y las añade al modelo de la tabla.
     */
    private void cargarAsignaturasDisponibles() {
        // Limpiar la tabla por si se está actualizando
        tableModel.setRowCount(0);

        // Obtenemos la lista COMPLETA de asignaturas
        List<Asignatura> asignaturas = sistema.getAsignaturas();

        // Filtramos y añadimos solo las que tienen cupos
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
     * MÉTODO CLAVE (Usa SistemaInscripcion):
     * Se ejecuta al presionar el botón "Inscribir".
     * Obtiene la fila seleccionada, extrae el código y llama
     * al método 'inscribirAsignatura' del sistema.
     */
    private void inscribirAsignaturaSeleccionada() {
        int selectedRow = tablaAsignaturas.getSelectedRow();

        // 1. Validar que se haya seleccionado una fila
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(ventana,
                    "Por favor, seleccione una asignatura de la lista.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Obtener el código de la asignatura (está en la columna 0)
        String codigoAsignatura = (String) tableModel.getValueAt(selectedRow, 0);

        // 3. LLAMAR AL MÉTODO DEL SISTEMA
        String resultado = sistema.inscribirAsignatura(estudianteLogueado, codigoAsignatura);

        // 4. Mostrar el resultado (éxito o error)
        if (resultado.startsWith("¡Inscripción exitosa")) {
            JOptionPane.showMessageDialog(ventana,
                    resultado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // 5. Actualizar la tabla para reflejar el nuevo cupo
            cargarAsignaturasDisponibles();

        } else {
            // Mostrar el mensaje de error exacto que envió el sistema
            JOptionPane.showMessageDialog(ventana,
                    resultado,
                    "Error de Inscripción", JOptionPane.ERROR_MESSAGE);
        }
    }
}