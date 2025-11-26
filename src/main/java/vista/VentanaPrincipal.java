package vista;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la ventana principal de la aplicación.
 * Actúa como contenedor principal y decide qué panel de rol cargar.
 * Mantiene la inyección de las referencias globales para el manejo de Logout en Admin.
 */
public class VentanaPrincipal extends JFrame {

    // --- Atributos de Rol ---
    private final ControladorEstudiante contEstudiante;
    private final ControladorAdmin contAdmin;

    // 1. Constructor para Estudiante (SIMPLIFICADO: Ya no necesita cargar la UI, el ControladorLogin lo hace)
    // Este constructor ya no debería ser llamado por la lógica corregida.
    public VentanaPrincipal(ControladorEstudiante contEstudiante,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        // En un diseño limpio, si se usa VentanaPrincipalEstudiante, esta clase se enfoca en Admin.
        // Mantenemos el código para evitar errores de compilación en el ControladorLogin,
        // pero la lógica del Estudiante debe ser removida o marcada como obsoleto.
        this.contEstudiante = contEstudiante;
        this.contAdmin = null;

        // Aunque no se use, se mantienen los setters para la consistencia
        this.contEstudiante.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);

        inicializarVentana("SIAE - Menú de Estudiante: " + contEstudiante.getEstudianteActual().getNombre());
        // NO CARGA NADA ESPECÍFICO AQUÍ, pues VentanaPrincipalEstudiante es el JFrame real.
        cargarPanelEstudiante();
    }


    // 2. Constructor para Administrador (CORRECTO)
    public VentanaPrincipal(ControladorAdmin contAdmin,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        this.contEstudiante = null;
        this.contAdmin = contAdmin;

        // Inyección de globales y de la propia Vista
        this.contAdmin.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);
        this.contAdmin.setVistaPrincipal(this);

        inicializarVentana("SIAE - Menú de Administrador");
        cargarPanelAdmin();
    }

    private void inicializarVentana(String titulo) {
        setTitle(titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);
    }

    // Método para cargar el panel de administrador
    private void cargarPanelAdmin() {
        // Nota: PanelAdmin es el JPanel contenido en este JFrame.
        PanelAdmin panelAdmin = new PanelAdmin(contAdmin);
        add(panelAdmin, BorderLayout.CENTER);
        validate();
    }

    // Método placeholder para cargar el panel de estudiante (AHORA ES UN PLACEHOLDER DE ERROR)
    private void cargarPanelEstudiante() {
        // Si llegamos aquí es un error en el flujo, ya que VentanaPrincipalEstudiante debe usarse directamente.
        JTextArea area = new JTextArea("ERROR: Este menú ha sido reemplazado por VentanaPrincipalEstudiante.");
        area.setForeground(Color.RED);
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}