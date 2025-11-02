package gui;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipalEstudiante {

    // Referencia al "cerebro" del sistema
    private SistemaInscripcion sistema;
    // Referencia al usuario que inició sesión
    private Estudiante estudianteLogueado;

    public VentanaPrincipalEstudiante(SistemaInscripcion sistema, Estudiante estudianteLogueado) {
        this.sistema = sistema;
        this.estudianteLogueado = estudianteLogueado;

        // --- Configuración de la Ventana ---
        JFrame menuEstudiantes = new JFrame("SIAE - Bienvenido " + this.estudianteLogueado.getNombre());
        menuEstudiantes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuEstudiantes.setBounds(500, 100, 600, 400);
        menuEstudiantes.getContentPane().setBackground(Color.white);
        menuEstudiantes.setLayout(null); // Permite posicionar botones manualmente

        // --- Botón para Gestionar Asignaturas ---
        JButton botonGestionar = new JButton("Inscribir / Desinscribir Asignaturas");
        botonGestionar.setBounds(150, 100, 300, 50);
        menuEstudiantes.add(botonGestionar);

        // Acción: Al hacer clic, abre la VentanaInscripcion
        botonGestionar.addActionListener(e -> {
            // Pasa el sistema y el estudiante a la nueva ventana
            new VentanaInscripcion(this.sistema, this.estudianteLogueado);
        });

        // --- Botón de Salir ---
        JButton botonSalir = new JButton("Cerrar Sesión");
        botonSalir.setBounds(150, 180, 300, 50);
        botonSalir.setForeground(Color.RED);
        menuEstudiantes.add(botonSalir);

        // Acción: Al hacer clic, cierra esta ventana y abre la de Login
        botonSalir.addActionListener(e -> {
            menuEstudiantes.dispose(); // Cierra la ventana actual
            new VentanaLogin(this.sistema); // Vuelve a la pantalla de inicio
        });

        // Muestra la ventana
        menuEstudiantes.setVisible(true);
    }
}
