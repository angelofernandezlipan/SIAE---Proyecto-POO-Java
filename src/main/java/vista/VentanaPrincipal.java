package vista;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;

import javax.swing.*;
import java.awt.*;

/**
 * Contenedor principal de la interfaz de Administrador.
 * Configura el JFrame y carga el PanelAdmin con la nueva estética.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorEstudiante contEstudiante;
    private final ControladorAdmin contAdmin;

    // Constante de color de fondo base
    private final Color COLOR_FONDO = new Color(230, 240, 250);

    /**
     * Inicializa la ventana principal del administrador.
     * Mantiene la inyección de dependencias para la navegación entre ventanas.
     */
    public VentanaPrincipal(ControladorAdmin contAdmin,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {

        this.contAdmin = contAdmin;
        this.contEstudiante = contEstudianteGlobal;

        // Configuración de referencias globales en el controlador
        this.contAdmin.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);
        this.contAdmin.setVistaPrincipal(this);

        // Configuración visual del Frame
        setTitle("SIAE - Panel de Administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700); // Ventana amplia para el dashboard
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Establecer color de fondo del content pane
        getContentPane().setBackground(COLOR_FONDO);

        cargarPanelAdmin();
        setVisible(true);
    }

    /**
     * Instancia y agrega el panel de administración al centro de la ventana.
     */
    private void cargarPanelAdmin() {
        PanelAdmin panelAdmin = new PanelAdmin(contAdmin);
        add(panelAdmin, BorderLayout.CENTER);
        validate(); // Refresca el layout
    }
}