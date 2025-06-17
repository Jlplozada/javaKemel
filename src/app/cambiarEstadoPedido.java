package app;

import javax.swing.*;
import java.awt.*;

public class cambiarEstadoPedido extends JFrame {
    public cambiarEstadoPedido() {
        setTitle("Cambiar Estado de Pedido");
        setSize(500, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(147, 0, 0));
        panel.setLayout(null);

        JLabel lblPedido = new JLabel("ID Pedido:");
        lblPedido.setBounds(30, 30, 100, 25);
        panel.add(lblPedido);
        JTextField txtPedido = new JTextField();
        txtPedido.setBounds(140, 30, 200, 25);
        panel.add(txtPedido);

        JLabel lblEstado = new JLabel("Nuevo Estado:");
        lblEstado.setBounds(30, 70, 100, 25);
        panel.add(lblEstado);
        JComboBox<String> comboEstado = new JComboBox<>();
        comboEstado.addItem("preparado");
        comboEstado.addItem("entregado");
        comboEstado.setBounds(140, 70, 200, 25);
        panel.add(comboEstado);

        JButton btnCambiar = new JButton("Cambiar Estado");
        btnCambiar.setBounds(140, 120, 150, 30);
        panel.add(btnCambiar);

        btnCambiar.addActionListener(e -> {
            int pedidoId = Integer.parseInt(txtPedido.getText());
            String nuevoEstado = (String) comboEstado.getSelectedItem();
            try {
                java.sql.Connection conn = connect.getConnection();
                if (conn != null) {
                    // Obtener estado actual
                    String sqlEstado = "SELECT estado FROM pedidos WHERE id = ?";
                    java.sql.PreparedStatement psEstado = conn.prepareStatement(sqlEstado);
                    psEstado.setInt(1, pedidoId);
                    java.sql.ResultSet rsEstado = psEstado.executeQuery();
                    String estadoAnterior = null;
                    if (rsEstado.next()) {
                        estadoAnterior = rsEstado.getString("estado");
                    }
                    rsEstado.close();
                    psEstado.close();
                    // Cambiar estado
                    String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
                    java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nuevoEstado);
                    ps.setInt(2, pedidoId);
                    int rows = ps.executeUpdate();
                    ps.close();
                    // Insertar en historial
                    if (rows > 0 && estadoAnterior != null) {
                        String sqlHist = "INSERT INTO historial_pedido (pedido_id, estado_anterior, nuevo_estado, cambiado_por) VALUES (?, ?, ?, ?)";
                        java.sql.PreparedStatement psHist = conn.prepareStatement(sqlHist);
                        psHist.setInt(1, pedidoId);
                        psHist.setString(2, estadoAnterior);
                        psHist.setString(3, nuevoEstado);
                        psHist.setInt(4, 2); // ID del panadero (ajustar según login)
                        psHist.executeUpdate();
                        psHist.close();
                    }
                    conn.close();
                    JOptionPane.showMessageDialog(this, "Estado cambiado correctamente", "Estado", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cambiar estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
