package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin {
    private SistemaInscripcion sistema;

    public VentanaLogin(SistemaInscripcion sistema) {
        this.sistema = sistema;

        JFrame login = new JFrame("SIAE - Inicio de Sesión");
        login.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.setBounds(500, 100, 600, 400);
        login.getContentPane().setBackground(Color.WHITE);
        login.setLayout(null);

        JLabel labelUsuario = new JLabel("Usuario (RUT):");
        labelUsuario.setBounds(100, 100, 100, 30);
        labelUsuario.setForeground(Color.BLACK); // Cambiado a negro
        login.add(labelUsuario);

        JTextField campoUsuario = new JTextField();
        campoUsuario.setBounds(200, 100, 250, 30);
        login.add(campoUsuario);

        JLabel labelContraseña = new JLabel("Contraseña:");
        labelContraseña.setBounds(100, 150, 100, 30);
        labelContraseña.setForeground(Color.BLACK); // Cambiado a negro
        login.add(labelContraseña);

        JPasswordField campoContraseña = new JPasswordField();
        campoContraseña.setBounds(200, 150, 250, 30);
        login.add(campoContraseña);

        JLabel mensaje = new JLabel();
        mensaje.setBounds(100, 180, 350, 25);
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setVisible(false);
        login.add(mensaje);

        JButton botonIniciarSesion = new JButton("Iniciar Sesión");
        botonIniciarSesion.setBounds(200, 200, 150, 40);

        botonIniciarSesion.addActionListener(e -> {
            String rutIngresado = campoUsuario.getText();
            String contraseñaIngresada = new String(campoContraseña.getPassword());

            Estudiante estudianteValidado = this.sistema.validarCredenciales(rutIngresado, contraseñaIngresada);

            // El método devuelve null si falla, o un Estudiante si tiene éxito
            if (estudianteValidado != null) {
                mensaje.setText("Inicio de sesión exitoso. ¡Bienvenido!"); // Puedes usar estudianteValidado.getNombre()
                mensaje.setForeground(new Color(0, 128, 0)); // Verde oscuro

                login.dispose();

            } else {
                mensaje.setText("Usuario (RUT) o contraseña incorrectos");
                mensaje.setForeground(Color.RED);
            }
            mensaje.setVisible(true);
        });


        campoContraseña.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botonIniciarSesion.doClick();
                }
            }
        });


        login.add(botonIniciarSesion);
        login.setVisible(true);
    }

    }
