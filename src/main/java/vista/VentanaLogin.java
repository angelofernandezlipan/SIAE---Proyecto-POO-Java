package vista;

import controlador.ControladorLogin; // <- ¡NUEVO! Importa el controlador

// ¡YA NO SE IMPORTAN!
// import modelo.SistemaInscripcion;
// import modelo.Estudiante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin {

    // --- Atributos ---
    // ¡El cambio clave! Ya no guarda el Modelo (SistemaInscripcion)
    private final ControladorLogin controlador;

    // Componentes de la Vista
    private final JFrame login;
    private final JTextField campoUsuario;
    private final JPasswordField campoContraseña;
    private final JButton botonIniciarSesion;
    private final JLabel mensaje;

    /**
     * El constructor ahora recibe el Controlador.
     * @param controlador El controlador que gestionará la lógica de esta vista.
     */
    public VentanaLogin(ControladorLogin controlador) {
        // 1. Guarda la referencia al controlador
        this.controlador = controlador;

        // 2. La Vista se "registra" con el controlador
        this.controlador.setVista(this);

        // --- Configuración de la Ventana ---
        login = new JFrame("SIAE - Inicio de Sesión");
        login.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.setBounds(500, 100, 600, 400);
        login.getContentPane().setBackground(Color.WHITE);
        login.setLayout(null);

        // --- Creación de Componentes ---
        JLabel labelUsuario = new JLabel("Usuario (RUT):");
        labelUsuario.setBounds(100, 100, 100, 30);
        labelUsuario.setForeground(Color.BLACK);
        login.add(labelUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(200, 100, 250, 30);
        login.add(campoUsuario);

        JLabel labelContraseña = new JLabel("Contraseña:");
        labelContraseña.setBounds(100, 150, 100, 30);
        labelContraseña.setForeground(Color.BLACK);
        login.add(labelContraseña);

        campoContraseña = new JPasswordField();
        campoContraseña.setBounds(200, 150, 250, 30);
        login.add(campoContraseña);

        mensaje = new JLabel();
        mensaje.setBounds(100, 180, 350, 25);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setVisible(false);
        login.add(mensaje);

        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonIniciarSesion.setBounds(200, 200, 150, 40);
        login.add(botonIniciarSesion);

        // --- Action Listeners (El gran cambio) ---

        // 3. El ActionListener AHORA SOLO DELEGA en el controlador
        botonIniciarSesion.addActionListener(e -> {
            intentarLogin();
        });

        // 4. El KeyListener también se simplifica
        campoContraseña.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Llama al mismo méthodo que el botón
                    intentarLogin();
                }
            }
        });

        // 5. Hacer visible la ventana
        login.setVisible(true);
    }

    /**
     * Méthodo privado que recolecta los datos y los pasa al controlador.
     */
    private void intentarLogin() {
        String rutIngresado = campoUsuario.getText();
        String contraseñaIngresada = new String(campoContraseña.getPassword());

        // La Vista le pasa los datos al Controlador, no sabe qué hacer con ellos
        controlador.intentarLogin(rutIngresado, contraseñaIngresada);
    }

    // --- Métodos Públicos para el Controlador ---
    // El Controlador usará estos métodos para manipular la Vista

    /**
     * Muestra un mensaje de error en la ventana.
     * @param textoError El mensaje a mostrar.
     */
    public void mostrarError(String textoError) {
        mensaje.setText(textoError);
        mensaje.setForeground(Color.RED);
        mensaje.setVisible(true);
    }

    /**
     * Cierra la ventana de login.
     */
    public void cerrar() {
        login.dispose();
    }
}