package vista;

// Importa el nuevo controlador
import controlador.ControladorPrincipalEstudiante;

// ¡YA NO SE IMPORTAN!
// import modelo.SistemaInscripcion;
// import modelo.Estudiante;

import javax.swing.*;
import java.awt.*;

/**
 * Esta es la ventana principal (el menú) que ve el estudiante.
 * VERSIÓN MVC: Esta vista es "tonta" y solo habla con su controlador.
 */
public class VentanaPrincipalEstudiante {

    // --- Atributos ---
    // ¡Cambio clave! Ahora guarda la referencia al Controlador
    private final ControladorPrincipalEstudiante controlador;
    private final JFrame menuEstudiantes;

    /**
     * El constructor AHORA RECIBE el Controlador y el nombre del estudiante
     * (el nombre es solo para mostrarlo en el título).
     *
     * @param controlador El controlador que gestionará esta vista.
     * @param nombreEstudiante El nombre del estudiante logueado (para el título).
     */
    public VentanaPrincipalEstudiante(ControladorPrincipalEstudiante controlador, String nombreEstudiante) {
        this.controlador = controlador;

        // La Vista se "registra" con su controlador
        this.controlador.setVista(this);

        // --- 1. Configuración de la Ventana ---
        menuEstudiantes = new JFrame("SIAE - Bienvenido " + nombreEstudiante);
        menuEstudiantes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuEstudiantes.setBounds(500, 100, 600, 400); // (x, y, ancho, alto)
        menuEstudiantes.getContentPane().setBackground(Color.white);
        menuEstudiantes.setLayout(null);

        // --- 2. Botón "Inscribir Asignaturas" ---
        JButton botonInscribir = new JButton("Inscribir Asignaturas");
        botonInscribir.setBounds(150, 80, 300, 50); // (x, y, ancho, alto)
        menuEstudiantes.add(botonInscribir);

        // Acción para el botón Inscribir
        botonInscribir.addActionListener(e -> {
            // La Vista solo le avisa al Controlador
            controlador.manejarAperturaInscripcion();
        });



        // --- 4. Botón "Salir" (Cerrar Sesión) ---
        JButton botonSalir = new JButton("Salir (Cerrar Sesión)");
        botonSalir.setBounds(150, 220, 300, 50); // Posición debajo del anterior
        botonSalir.setForeground(Color.RED); // Color rojo para destacar
        menuEstudiantes.add(botonSalir);

        // Acción para el botón Salir
        botonSalir.addActionListener(e -> {
            // La Vista le avisa al Controlador que quiere "salir"
            controlador.manejarLogout();
        });

        // --- 5. Mostrar la ventana ---
        menuEstudiantes.setVisible(true);
    }

    /**
     * Méthodo público para que el controlador pueda cerrar esta ventana.
     */
    public void cerrar() {
        menuEstudiantes.dispose();
    }
}
