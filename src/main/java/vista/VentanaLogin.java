package vista;

import controlador.ControladorLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin {

    // --- Atributos ---
    private final ControladorLogin controlador;

    // Componentes de la Vista
    private final JFrame login;
    private final JTextField campoUsuario;
    private final JPasswordField campoContraseña;
    private final JComboBox<String> campoRol; // <--- ¡NUEVO! Selector de rol
    private final JButton botonIniciarSesion;
    private final JLabel mensaje;

    /**
     * El constructor ahora recibe el Controlador.
     * @param controlador El controlador que gestionará la lógica de esta vista.
     */
    public VentanaLogin(ControladorLogin controlador) {
        // 1. Guarda la referencia al controlador
        this.controlador = controlador;
        this.controlador.setVista(this); // La Vista se "registra" con el controlador

        // --- Configuración de la Ventana ---
        login = new JFrame("SIAE - Inicio de Sesión");
        login.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.setBounds(500, 100, 600, 400);
        login.getContentPane().setBackground(Color.WHITE);
        login.setLayout(null);

        int y_pos = 70; // Posición inicial Y

        // 1. Selector de Rol (Nuevo)
        JLabel labelRol = new JLabel("Rol:");
        labelRol.setBounds(100, y_pos, 100, 30);
        labelRol.setForeground(Color.BLACK);
        login.add(labelRol);

        String[] roles = {"Estudiante", "Administrador"};
        campoRol = new JComboBox<>(roles);
        campoRol.setBounds(200, y_pos, 250, 30);
        login.add(campoRol);

        y_pos += 50; // Siguiente posición

        // 2. Campo Usuario
        JLabel labelUsuario = new JLabel("Usuario (RUT/Admin):");
        labelUsuario.setBounds(100, y_pos, 150, 30);
        labelUsuario.setForeground(Color.BLACK);
        login.add(labelUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(250, y_pos, 200, 30);
        login.add(campoUsuario);

        y_pos += 50; // Siguiente posición

        // 3. Campo Contraseña
        JLabel labelContraseña = new JLabel("Contraseña:");
        labelContraseña.setBounds(100, y_pos, 100, 30);
        labelContraseña.setForeground(Color.BLACK);
        login.add(labelContraseña);

        campoContraseña = new JPasswordField();
        campoContraseña.setBounds(250, y_pos, 200, 30);
        login.add(campoContraseña);

        y_pos += 30; // Ajuste para el mensaje

        // 4. Mensaje de Error
        mensaje = new JLabel();
        mensaje.setBounds(100, y_pos, 350, 25);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setVisible(false);
        login.add(mensaje);

        y_pos += 30; // Posición del botón

        // 5. Botón
        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonIniciarSesion.setBounds(200, y_pos, 150, 40);
        login.add(botonIniciarSesion);

        // --- Action Listeners ---
        botonIniciarSesion.addActionListener(e -> intentarLogin());
        campoContraseña.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarLogin();
                }
            }
        });

        // 6. Hacer visible la ventana
        login.setVisible(true);
    }

    /**
     * Méthodo privado que recolecta los datos y los pasa al controlador.
     */
    private void intentarLogin() {
        String rolSeleccionado = (String) campoRol.getSelectedItem(); // Obtiene el rol
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        // La Vista DELEGA la decisión y la lógica en el Controlador
        controlador.intentarLogin(rolSeleccionado, usuario, contraseña);
    }

    // --- Métodos Públicos para el Controlador (sin cambios) ---
    public void mostrarError(String textoError) {
        mensaje.setText(textoError);
        mensaje.setForeground(Color.RED);
        mensaje.setVisible(true);
    }

    public void cerrar() {
        login.dispose();
    }
}