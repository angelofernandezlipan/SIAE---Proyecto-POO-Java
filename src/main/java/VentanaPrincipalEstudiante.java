import javax.swing.*;
import java.awt.*;

/**
 * Esta es la ventana principal (el menú) que ve el estudiante
 * DESPUÉS de iniciar sesión correctamente.
 */
public class VentanaPrincipalEstudiante {

    // Necesitamos guardar las referencias al sistema y al estudiante
    private SistemaInscripcion sistema;
    private Estudiante estudianteLogueado;

    // El constructor DEBE recibir los datos para saber QUIÉN está logueado
    public VentanaPrincipalEstudiante(SistemaInscripcion sistema, Estudiante estudianteLogueado) {
        this.sistema = sistema;
        this.estudianteLogueado = estudianteLogueado;

        // --- 1. Configuración de la Ventana ---
        JFrame menuEstudiantes = new JFrame("SIAE - Bienvenido " + this.estudianteLogueado.getNombre());
        menuEstudiantes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuEstudiantes.setBounds(500, 100, 600, 400); // (x, y, ancho, alto)
        menuEstudiantes.getContentPane().setBackground(Color.white);

        // ¡IMPORTANTE! Usar layout nulo para posicionar botones con setBounds
        menuEstudiantes.setLayout(null);

        // --- 2. Botón "Inscribir Asignaturas" ---
        JButton botonInscribir = new JButton("Inscribir Asignaturas");
        botonInscribir.setBounds(150, 80, 300, 50); // (x, y, ancho, alto)
        menuEstudiantes.add(botonInscribir);

        // Acción para el botón Inscribir
        botonInscribir.addActionListener(e -> {
            // Abre la OTRA ventana (VentanaInscripcion)
            // y le pasa los datos del sistema y del estudiante
            VentanaInscripcion vInscripcion = new VentanaInscripcion(this.sistema, this.estudianteLogueado);

            // Opcional: puedes cerrar esta ventana de menú si lo deseas
            // menuEstudiantes.dispose();
        });

        // --- 3. Botón "Desinscribir Asignaturas" ---
        JButton botonDesinscribir = new JButton("Desinscribir Asignaturas");
        botonDesinscribir.setBounds(150, 150, 300, 50); // Posición debajo del botón anterior
        menuEstudiantes.add(botonDesinscribir);

        // Acción para el botón Desinscribir
        botonDesinscribir.addActionListener(e -> {
            // Aquí deberías crear y abrir una 'VentanaDesinscribir'
            // (Asumiendo que crearás esa clase más adelante)

            // VentanaDesinscribir vDesinscribir = new VentanaDesinscribir(this.sistema, this.estudianteLogueado);

            // Por ahora, solo un mensaje:
            JOptionPane.showMessageDialog(menuEstudiantes, "Función 'Desinscribir' aún no implementada.");
        });

        // --- 4. Botón "Salir" (Cerrar Sesión) ---
        JButton botonSalir = new JButton("Salir (Cerrar Sesión)");
        botonSalir.setBounds(150, 220, 300, 50); // Posición debajo del anterior
        botonSalir.setForeground(Color.RED); // Color rojo para destacar
        menuEstudiantes.add(botonSalir);

        // Acción para el botón Salir
        botonSalir.addActionListener(e -> {
            // 1. Cierra esta ventana (el menú)
            menuEstudiantes.dispose();

            // 2. Vuelve a abrir la ventana de Login, pasando el sistema
            new VentanaLogin(this.sistema);
        });

        // --- 5. Mostrar la ventana ---
        // Esto siempre debe ir al final, después de añadir todos los componentes.
        menuEstudiantes.setVisible(true);
    }
}
