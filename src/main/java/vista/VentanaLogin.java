package vista;

import controlador.ControladorLogin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaLogin {

    private final ControladorLogin controlador;

    private final JFrame frame;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JComboBox<String> campoRol;
    private JLabel mensajeError;

    // Paleta de colores
    private final Color COLOR_FONDO = new Color(230, 240, 250); // Azul muy pálido
    private final Color COLOR_PRIMARIO = new Color(70, 130, 180); // Azul Acero
    private final Color COLOR_PRIMARIO_HOVER = new Color(60, 110, 160);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(80, 80, 80);

    public VentanaLogin(ControladorLogin controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);

        frame = new JFrame("SIAE - Acceso al Sistema");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout()); // Centra la tarjeta
        frame.getContentPane().setBackground(COLOR_FONDO);

        JPanel panelTarjeta = crearPanelTarjeta();
        frame.add(panelTarjeta);

        frame.setVisible(true);
    }

    private JPanel crearPanelTarjeta() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setPreferredSize(new Dimension(400, 520));
        tarjeta.setBackground(COLOR_BLANCO);
        tarjeta.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // --- CONTENIDO DE LA TARJETA ---
        int ancho = 320;
        int x_center = (400 - ancho) / 2;

        // 1. TÍTULO DEL PROYECTO (SIAE)
        JLabel lblTitulo = new JLabel("SIAE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Letra Grande
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBounds(0, 30, 400, 50);
        tarjeta.add(lblTitulo);

        // 2. Subtítulo descriptivo
        JLabel lblSubtitulo = new JLabel("Sistema de Inscripción Académica", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(0, 80, 400, 20);
        tarjeta.add(lblSubtitulo);

        // Separador visual pequeño (Línea)
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(230,230,230));
        separador.setBounds(x_center, 115, ancho, 2);
        tarjeta.add(separador);

        // 3. Campo ROL
        JLabel lblRol = crearEtiqueta("Seleccione su Rol:");
        lblRol.setBounds(x_center, 130, ancho, 20);
        tarjeta.add(lblRol);

        campoRol = new JComboBox<>(new String[]{"Estudiante", "Administrador"});
        campoRol.setBounds(x_center, 155, ancho, 40);
        campoRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoRol.setBackground(Color.WHITE);
        ((JLabel)campoRol.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(campoRol);

        // 4. Campo USUARIO
        JLabel lblUser = crearEtiqueta("Usuario (RUT):");
        lblUser.setBounds(x_center, 210, ancho, 20);
        tarjeta.add(lblUser);

        campoUsuario = new JTextField();
        estilizarCampoTexto(campoUsuario);
        campoUsuario.setBounds(x_center, 235, ancho, 40);
        tarjeta.add(campoUsuario);

        // 5. Campo PASSWORD
        JLabel lblPass = crearEtiqueta("Contraseña:");
        lblPass.setBounds(x_center, 290, ancho, 20);
        tarjeta.add(lblPass);

        campoContraseña = new JPasswordField();
        estilizarCampoTexto(campoContraseña);
        campoContraseña.setBounds(x_center, 315, ancho, 40);

        campoContraseña.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) intentarLogin();
            }
        });
        tarjeta.add(campoContraseña);

        // 6. Mensaje de Error
        mensajeError = new JLabel("", SwingConstants.CENTER);
        mensajeError.setForeground(new Color(220, 53, 69));
        mensajeError.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mensajeError.setBounds(20, 365, 360, 20);
        tarjeta.add(mensajeError);

        // 7. Botón Login
        JButton btnLogin = new JButton("INICIAR SESIÓN");
        estilizarBoton(btnLogin);
        btnLogin.setBounds(x_center, 400, ancho, 45);
        btnLogin.addActionListener(e -> intentarLogin());
        tarjeta.add(btnLogin);

        return tarjeta;
    }

    // --- MÉTODOS DE ESTILO ---

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void estilizarBoton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(COLOR_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARIO_HOVER);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });
    }

    // --- LÓGICA  ---

    private void intentarLogin() {
        String rolSeleccionado = (String) campoRol.getSelectedItem();
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());
        controlador.intentarLogin(rolSeleccionado, usuario, contraseña);
    }

    public void mostrarError(String textoError) {
        mensajeError.setText(textoError);
        campoContraseña.setText("");
        campoContraseña.requestFocus();
    }

    public void cerrar() {
        frame.dispose();
    }
}