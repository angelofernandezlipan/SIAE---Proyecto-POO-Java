package vista;

import controlador.ControladorLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin {

    private final ControladorLogin controlador;

    private final JFrame login;
    private final JTextField campoUsuario;
    private final JPasswordField campoContraseña;
    private final JComboBox<String> campoRol;
    private final JButton botonIniciarSesion;
    private final JLabel mensaje;


    public VentanaLogin(ControladorLogin controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        login = new JFrame("SIAE - Inicio de Sesión");
        login.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.setBounds(500, 100, 600, 400);
        login.getContentPane().setBackground(Color.WHITE);
        login.setLayout(null);

        int y_pos = 70;

        JLabel labelRol = new JLabel("Rol:");
        labelRol.setBounds(100, y_pos, 100, 30);
        labelRol.setForeground(Color.BLACK);
        login.add(labelRol);

        String[] roles = {"Estudiante", "Administrador"};
        campoRol = new JComboBox<>(roles);
        campoRol.setBounds(200, y_pos, 250, 30);
        login.add(campoRol);

        y_pos += 50;

        JLabel labelUsuario = new JLabel("Usuario (RUT/Admin):");
        labelUsuario.setBounds(100, y_pos, 150, 30);
        labelUsuario.setForeground(Color.BLACK);
        login.add(labelUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(250, y_pos, 200, 30);
        login.add(campoUsuario);

        y_pos += 50;

        JLabel labelContraseña = new JLabel("Contraseña:");
        labelContraseña.setBounds(100, y_pos, 100, 30);
        labelContraseña.setForeground(Color.BLACK);
        login.add(labelContraseña);

        campoContraseña = new JPasswordField();
        campoContraseña.setBounds(250, y_pos, 200, 30);
        login.add(campoContraseña);

        y_pos += 30;

        mensaje = new JLabel();
        mensaje.setBounds(100, y_pos, 350, 25);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setVisible(false);
        login.add(mensaje);

        y_pos += 30;

        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonIniciarSesion.setBounds(200, y_pos, 150, 40);
        login.add(botonIniciarSesion);

        botonIniciarSesion.addActionListener(e -> intentarLogin());
        campoContraseña.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarLogin();
                }
            }
        });

        login.setVisible(true);
    }

    private void intentarLogin() {
        String rolSeleccionado = (String) campoRol.getSelectedItem();
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        controlador.intentarLogin(rolSeleccionado, usuario, contraseña);
    }

    public void mostrarError(String textoError) {
        mensaje.setText(textoError);
        mensaje.setForeground(Color.RED);
        mensaje.setVisible(true);
    }

    public void cerrar() {
        login.dispose();
    }
}