package vista;

import controlador.ControladorPrincipalEstudiante;

// ¡YA NO SE IMPORTAN!
// import modelo.SistemaInscripcion;
// import modelo.Estudiante;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipalEstudiante {

    private final ControladorPrincipalEstudiante controlador;
    private final JFrame menuEstudiantes;

    public VentanaPrincipalEstudiante(ControladorPrincipalEstudiante controlador, String nombreEstudiante) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        menuEstudiantes = new JFrame("SIAE - Bienvenido " + nombreEstudiante);
        menuEstudiantes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuEstudiantes.setBounds(500, 100, 600, 400);
        menuEstudiantes.getContentPane().setBackground(Color.white);
        menuEstudiantes.setLayout(null);

        JButton botonInscribir = new JButton("Inscribir Asignaturas");
        botonInscribir.setBounds(150, 80, 300, 50);
        menuEstudiantes.add(botonInscribir);
        botonInscribir.addActionListener(e -> {
            controlador.manejarAperturaInscripcion();
        });

        JButton botonSalir = new JButton("Salir (Cerrar Sesión)");
        botonSalir.setBounds(150, 220, 300, 50);
        botonSalir.setForeground(Color.RED);
        menuEstudiantes.add(botonSalir);

        botonSalir.addActionListener(e -> {
            controlador.manejarLogout();
        });

        menuEstudiantes.setVisible(true);
    }
    public void cerrar() {
        menuEstudiantes.dispose();
    }
}