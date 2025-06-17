package app;

import javax.swing.*;
import java.awt.*;

public class crearpedido extends JFrame {
    public crearpedido() {
        setTitle("Crear Pedido");
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(147, 0, 0));
        panel.setLayout(null);

        // Reemplazar los JTextField por JComboBox para usuario y producto
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(30, 30, 100, 25);
        panel.add(lblUsuario);
        JComboBox<String> comboUsuario = new JComboBox<>();
        comboUsuario.setBounds(140, 30, 200, 25);
        try {
            java.sql.Connection conn = connect.getConnection();
            if (conn != null) {
                java.sql.PreparedStatement ps = conn.prepareStatement("SELECT nombre_usuario FROM usuarios");
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    comboUsuario.addItem(rs.getString("nombre_usuario"));
                }
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (Exception ex) { /* ignorar para demo */ }
        panel.add(comboUsuario);

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setBounds(30, 70, 100, 25);
        panel.add(lblProducto);
        JComboBox<String> comboProducto = new JComboBox<>();
        comboProducto.setBounds(140, 70, 200, 25);
        try {
            java.sql.Connection conn = connect.getConnection();
            if (conn != null) {
                java.sql.PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM productos");
                java.sql.ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    comboProducto.addItem(rs.getString("nombre"));
                }
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (Exception ex) { /* ignorar para demo */ }
        panel.add(comboProducto);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(30, 110, 100, 25);
        panel.add(lblCantidad);
        JTextField txtCantidad = new JTextField();
        txtCantidad.setBounds(140, 110, 200, 25);
        panel.add(txtCantidad);

        JButton btnCrear = new JButton("Crear Pedido");
        btnCrear.setBounds(140, 160, 150, 30);
        panel.add(btnCrear);

        btnCrear.addActionListener(e -> {
            String usuario = (String) comboUsuario.getSelectedItem();
            String producto = (String) comboProducto.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            try {
                java.sql.Connection conn = connect.getConnection();
                if (conn != null) {
                    // Buscar id del usuario
                    String sqlUser = "SELECT id FROM usuarios WHERE LOWER(nombre_usuario) = LOWER(?)";
                    java.sql.PreparedStatement psUser = conn.prepareStatement(sqlUser);
                    psUser.setString(1, usuario);
                    java.sql.ResultSet rsUser = psUser.executeQuery();
                    if (rsUser.next()) {
                        int usuarioId = rsUser.getInt("id");
                        // Buscar id del producto
                        String sqlProd = "SELECT id, precio FROM productos WHERE nombre = ?";
                        java.sql.PreparedStatement psProd = conn.prepareStatement(sqlProd);
                        psProd.setString(1, producto);
                        java.sql.ResultSet rsProd = psProd.executeQuery();
                        if (rsProd.next()) {
                            int productoId = rsProd.getInt("id");
                            double precio = rsProd.getDouble("precio");
                            double subtotal = precio * cantidad;
                            // Insertar pedido
                            String sqlPedido = "INSERT INTO pedidos (usuario_id, total) VALUES (?, ?)";
                            java.sql.PreparedStatement psPedido = conn.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS);
                            psPedido.setInt(1, usuarioId);
                            psPedido.setDouble(2, subtotal);
                            psPedido.executeUpdate();
                            java.sql.ResultSet rsPedido = psPedido.getGeneratedKeys();
                            if (rsPedido.next()) {
                                int pedidoId = rsPedido.getInt(1);
                                // Insertar detalle
                                String sqlDet = "INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
                                java.sql.PreparedStatement psDet = conn.prepareStatement(sqlDet);
                                psDet.setInt(1, pedidoId);
                                psDet.setInt(2, productoId);
                                psDet.setInt(3, cantidad);
                                psDet.setDouble(4, precio);
                                psDet.setDouble(5, subtotal);
                                psDet.executeUpdate();
                                psDet.close();
                            }
                            rsPedido.close();
                            psPedido.close();
                        } else {
                            JOptionPane.showMessageDialog(this, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        rsProd.close();
                        psProd.close();
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    rsUser.close();
                    psUser.close();
                    conn.close();
                    JOptionPane.showMessageDialog(this, "Pedido creado correctamente", "Pedido", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
