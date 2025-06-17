package app;

import javax.swing.*;
import java.awt.*;

public class registro extends JFrame {
    public registro() {
        setTitle("Registrar Usuario");
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(147, 0, 0));
        panel.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(30, 30, 100, 25);
        panel.add(lblUsuario);
        JTextField txtUsuario = new JTextField();
        txtUsuario.setBounds(140, 30, 200, 25);
        panel.add(txtUsuario);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 70, 100, 25);
        panel.add(lblNombre);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(140, 70, 200, 25);
        panel.add(txtNombre);

        JLabel lblClave = new JLabel("Clave:");
        lblClave.setBounds(30, 110, 100, 25);
        panel.add(lblClave);
        JPasswordField txtClave = new JPasswordField();
        txtClave.setBounds(140, 110, 200, 25);
        panel.add(txtClave);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(30, 150, 100, 25);
        panel.add(lblRol);
        JComboBox<String> comboRol = new JComboBox<>();
        comboRol.addItem("admin");
        comboRol.addItem("panaderia");
        comboRol.addItem("cliente");
        comboRol.setBounds(140, 150, 200, 25);
        panel.add(comboRol);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(140, 200, 120, 30);
        panel.add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String nombre = txtNombre.getText();
            String clave = new String(txtClave.getPassword());
            String rol = (String) comboRol.getSelectedItem();
            try {
                java.sql.Connection conn = connect.getConnection();
                if (conn != null) {
                    String sql = "INSERT INTO usuarios (nombre_usuario, nombre, clave, rol_id) VALUES (?, ?, ?, (SELECT id FROM roles WHERE nombre = ?))";
                    java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, usuario);
                    ps.setString(2, nombre);
                    ps.setString(3, clave);
                    ps.setString(4, rol);
                    ps.executeUpdate();
                    ps.close();
                    conn.close();
                    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente", "Registro", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
