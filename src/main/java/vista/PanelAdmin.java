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

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Superior Contenedor (para la Carga Masiva y el Botón de Logout) ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        // Botón de Logout (Nuevo)
        JButton btnLogout = new JButton("⬅️ Cerrar Sesión y Volver al Login");
        btnLogout.addActionListener(e -> contAdmin.manejarLogout()); // Delegación al controlador

        // Panel para el botón de logout (centrado a la derecha)
        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLogout.add(btnLogout);
        panelSuperior.add(panelLogout, BorderLayout.NORTH);

        // Panel de Carga Masiva
        JPanel panelCarga = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCarga.setBorder(BorderFactory.createTitledBorder("Carga Masiva de Estudiantes (.txt/.csv)"));

        JLabel labelRuta = new JLabel("Ruta del archivo:");
        campoRutaCarga = new JTextField(30);
        JButton botonCargar = new JButton("Cargar Estudiantes");

        botonCargar.addActionListener(e -> intentarCargaMasiva()); // <--- Método conectado

        panelCarga.add(labelRuta);
        panelCarga.add(campoRutaCarga);
        panelCarga.add(botonCargar);

        panelSuperior.add(panelCarga, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH); // Agrega el panel superior completo

        // --- Panel Central: Reportes ---
        JPanel panelReportes = new JPanel(new GridLayout(2, 1, 10, 10));

        // 1. Opciones de Exportación
        JPanel panelOpcionesReporte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpcionesReporte.setBorder(BorderFactory.createTitledBorder("Exportar Reportes Administrativos"));

        JButton btnReporteCupos = new JButton("1. Reporte de Cupos");
        JButton btnReporteInscripciones = new JButton("2. Reporte de Inscripciones");
        JButton btnVerCredenciales = new JButton("3. Ver Credenciales");
        JButton btnLimpiarDatos = new JButton("⚠️ Limpiar TODOS los Datos (JSON)");

        btnReporteCupos.addActionListener(e -> contAdmin.exportarReportes(1));
        btnReporteInscripciones.addActionListener(e -> contAdmin.exportarReportes(2));
        btnVerCredenciales.addActionListener(e -> verReporteCredenciales()); // <--- Método conectado
        btnLimpiarDatos.addActionListener(e -> confirmarLimpiezaDatos()); // <--- Método conectado

        panelOpcionesReporte.add(btnReporteCupos);
        panelOpcionesReporte.add(btnReporteInscripciones);
        panelOpcionesReporte.add(btnVerCredenciales);
        panelOpcionesReporte.add(Box.createRigidArea(new Dimension(50, 0)));
        panelOpcionesReporte.add(btnLimpiarDatos);

        panelReportes.add(panelOpcionesReporte);

        // 2. Área de resultados de operaciones
        resultadosArea = new JTextArea("Mensajes del sistema aparecerán aquí...");
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Operaciones"));

        panelReportes.add(scrollPane);

        add(panelReportes, BorderLayout.CENTER); // Agrega el panel de reportes al centro
    }

    // ====================================================================
    // MÉTODOS AUXILIARES FALTANTES
    // ====================================================================

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
        // Llama al controlador para abrir el archivo y muestra el resultado.
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