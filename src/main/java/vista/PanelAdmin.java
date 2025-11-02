package vista;

import controlador.ControladorAdmin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelAdmin extends JPanel {

    private final ControladorAdmin contAdmin;

    // Componentes
    private final JTextArea resultadosArea;
    private final JTextField campoRutaCarga;

    public PanelAdmin(ControladorAdmin contAdmin) {
        this.contAdmin = contAdmin;

        // Inicialización de resultadosArea y campoRutaCarga (Necesario en el constructor)
        resultadosArea = new JTextArea("Mensajes del sistema aparecerán aquí...");
        campoRutaCarga = new JTextField(30);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior (Logout, Carga Masiva) ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        JButton btnLogout = new JButton("⬅️ Cerrar Sesión y Volver al Login");
        btnLogout.addActionListener(e -> contAdmin.manejarLogout());

        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLogout.add(btnLogout);
        panelSuperior.add(panelLogout, BorderLayout.NORTH);

        JPanel panelCarga = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCarga.setBorder(BorderFactory.createTitledBorder("Carga Masiva de Estudiantes (.txt/.csv)"));

        JLabel labelRuta = new JLabel("Ruta del archivo:");
        JButton botonCargar = new JButton("Cargar Estudiantes");

        botonCargar.addActionListener(e -> intentarCargaMasiva());

        panelCarga.add(labelRuta);
        panelCarga.add(campoRutaCarga);
        panelCarga.add(botonCargar);

        panelSuperior.add(panelCarga, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // --- Panel Central: Reportes ---
        JPanel panelReportes = new JPanel(new GridLayout(2, 1, 10, 10));

        // 1. Opciones de Exportación
        JPanel panelOpcionesReporte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpcionesReporte.setBorder(BorderFactory.createTitledBorder("Exportar Reportes Administrativos"));

        JButton btnReporteCupos = new JButton("1. Reporte de Cupos");
        JButton btnReporteInscripciones = new JButton("2. Reporte de Inscripciones");
        JButton btnVerCredenciales = new JButton("3. Ver Credenciales");
        JButton btnLimpiarDatos = new JButton("⚠️ Limpiar TODOS los Datos (JSON)");

        // CLAVE: Conexión al nuevo método para mostrar el contenido
        btnReporteCupos.addActionListener(e -> mostrarReporte(1, "Reporte de Cupos"));
        btnReporteInscripciones.addActionListener(e -> mostrarReporte(2, "Reporte de Inscripciones"));

        btnVerCredenciales.addActionListener(e -> verReporteCredenciales());
        btnLimpiarDatos.addActionListener(e -> confirmarLimpiezaDatos());

        panelOpcionesReporte.add(btnReporteCupos);
        panelOpcionesReporte.add(btnReporteInscripciones);
        panelOpcionesReporte.add(btnVerCredenciales);
        panelOpcionesReporte.add(Box.createRigidArea(new Dimension(50, 0)));
        panelOpcionesReporte.add(btnLimpiarDatos);

        panelReportes.add(panelOpcionesReporte);

        // 2. Área de resultados de operaciones
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Operaciones"));

        panelReportes.add(scrollPane);

        add(panelReportes, BorderLayout.CENTER);
    }

    // ====================================================================
    // NUEVO MÉTODO AUXILIAR PARA MOSTRAR REPORTES
    // ====================================================================

    private void mostrarReporte(int tipoReporte, String nombreReporte) {
        // 1. Obtener el contenido del reporte (llama al método modificado en GestionAdministrativa)
        List<String> reporteContenido = contAdmin.exportarReportes(tipoReporte);

        resultadosArea.setText(""); // Limpia el área

        // 2. Muestra la cabecera
        mostrarMensaje("✅ Reporte generado y exportado: " + nombreReporte, false);
        mostrarMensaje("------------------------------------------------------", false);

        // 3. Muestra el contenido línea por línea
        reporteContenido.forEach(linea -> {
            resultadosArea.append(linea + "\n");
        });

        // Asegura el scroll hasta el inicio del reporte
        resultadosArea.setCaretPosition(0);
    }

    // --- MÉTODOS AUXILIARES FALTANTES (Copiar desde tu versión anterior) ---
    private void intentarCargaMasiva() {
        String filePath = campoRutaCarga.getText().trim();
        if (filePath.isEmpty()) {
            mostrarMensaje("Error: Debe ingresar la ruta del archivo.", true);
            return;
        }

        List<String> mensajes = contAdmin.cargarEstudiantes(filePath);

        resultadosArea.setText("");

        boolean exitoTotal = mensajes.stream().anyMatch(msg -> msg.startsWith("Carga masiva exitosa"));

        if (exitoTotal) {
            mostrarMensaje("✅ Carga masiva completada.", false);
            mostrarMensaje("📌 **REPORTE DE CREDENCIALES GENERADO**:\n   Ahora puede usar el botón '3. Ver Credenciales'.", false);
        } else {
            mostrarMensaje("❌ Carga fallida o con advertencias. Detalles:", true);
        }

        mensajes.forEach(msg -> mostrarMensaje(msg, msg.startsWith("Error:") || msg.startsWith("Advertencia:")));

        mostrarMensaje("\n------------------------------------------------------", false);
    }

    private void verReporteCredenciales() {
        String resultado = contAdmin.abrirReporteCredenciales();
        boolean esError = resultado.startsWith("Error");
        mostrarMensaje(resultado, esError);
    }

    private void confirmarLimpiezaDatos() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "ADVERTENCIA: ¿Está seguro de que desea ELIMINAR TODOS los datos (Estudiantes y Asignaturas)?\nEsta acción es irreversible.",
                "Confirmar Limpieza de Datos",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            contAdmin.limpiarDatosSistema();
            mostrarMensaje("✅ Datos del sistema eliminados exitosamente.", false);
        } else {
            mostrarMensaje("❌ Limpieza de datos cancelada.", true);
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        Color color = esError ? Color.RED : Color.BLACK;

        // Simulación de color
        resultadosArea.setForeground(color);
        resultadosArea.append("\n" + mensaje);
        resultadosArea.setCaretPosition(resultadosArea.getDocument().getLength());
    }
}