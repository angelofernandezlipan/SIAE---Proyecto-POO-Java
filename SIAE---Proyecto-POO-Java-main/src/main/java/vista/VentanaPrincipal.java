package vista;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final ControladorEstudiante contEstudiante;
    private final ControladorAdmin contAdmin;
    public VentanaPrincipal(ControladorEstudiante contEstudiante,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        this.contEstudiante = contEstudiante;
        this.contAdmin = null;

        this.contEstudiante.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);

        inicializarVentana("SIAE - Menú de Estudiante: " + contEstudiante.getEstudianteActual().getNombre());
        cargarPanelEstudiante();
    }


    public VentanaPrincipal(ControladorAdmin contAdmin,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        this.contEstudiante = null;
        this.contAdmin = contAdmin;

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

    private void cargarPanelAdmin() {
        PanelAdmin panelAdmin = new PanelAdmin(contAdmin);
        add(panelAdmin, BorderLayout.CENTER);
        validate();
    }

    private void cargarPanelEstudiante() {
        JTextArea area = new JTextArea("ERROR: Este menú ha sido reemplazado por VentanaPrincipalEstudiante.");
        area.setForeground(Color.RED);
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}