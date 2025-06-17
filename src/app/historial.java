package app;

import javax.swing.*;
import java.awt.*;

public class historial extends JFrame {
    public historial() {
        setTitle("Historial de Pedidos");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(147, 0, 0));
        panel.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Historial de Pedidos", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTable tabla = new JTable();
        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);

        try {
            java.sql.Connection conn = app.connect.getConnection();
            if (conn != null) {
                String sql = "SELECT p.id, u.nombre_usuario, p.fecha_pedido, p.estado, p.total FROM pedidos p JOIN usuarios u ON p.usuario_id = u.id ORDER BY p.fecha_pedido DESC";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                java.sql.ResultSet rs = ps.executeQuery();
                java.util.List<Object[]> data = new java.util.ArrayList<>();
                while (rs.next()) {
                    data.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre_usuario"),
                        rs.getString("fecha_pedido"),
                        rs.getString("estado"),
                        rs.getDouble("total")
                    });
                }
                String[] cols = {"ID", "Usuario", "Fecha", "Estado", "Total"};
                Object[][] rows = data.toArray(new Object[0][]);
                tabla.setModel(new javax.swing.table.DefaultTableModel(rows, cols));
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(panel);
    }
}
