package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class adminMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaPedidos;
	private JScrollPane scrollPedidos;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					adminMenu frame = new adminMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public adminMenu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(adminMenu.class.getResource("/img/logov.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1106, 660);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(147, 0, 0));
		contentPane.setForeground(new Color(147, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(816, 0, 226, 622);
		contentPane.add(panel);
		panel.setLayout(null);
		
				JButton btnSalir = new JButton("Salir");
				btnSalir.setBounds(10, 581, 206, 30);
				panel.add(btnSalir);
				
				JLabel lblNewLabel = new JLabel("");
				lblNewLabel.setBounds(10, 11, 206, 119);
				panel.add(lblNewLabel);
				ImageIcon icon = new ImageIcon(adminMenu.class.getResource("/img/logov.png"));
				java.awt.Image img = icon.getImage();
				java.awt.Image imgScaled = img.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
				lblNewLabel.setIcon(new ImageIcon(imgScaled));
				btnSalir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						new login().setVisible(true);
					}
				});
				
				tablaPedidos = new JTable();
				scrollPedidos = new JScrollPane(tablaPedidos);
				scrollPedidos.setBounds(10, 11, 798, 599);
				contentPane.add(scrollPedidos);
				cargarTablaPedidos();

				JButton btnActualizar = new JButton("Actualizar");
				btnActualizar.setBounds(10, 540, 206, 30);
				panel.add(btnActualizar);
				btnActualizar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cargarTablaPedidos();
					}
				});
				
				JButton btnAprobado = new JButton("Aprobado");
				btnAprobado.setBounds(10, 200, 206, 30);
				panel.add(btnAprobado);
				btnAprobado.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        cambiarEstadoPedido("aprobado");
				    }
				});
				
				JButton btnPreparado = new JButton("Preparado");
				btnPreparado.setBounds(10, 240, 206, 30);
				panel.add(btnPreparado);
				btnPreparado.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        cambiarEstadoPedido("preparado");
				    }
				});
				
				JButton btnEntregado = new JButton("Entregado");
				btnEntregado.setBounds(10, 281, 206, 30);
				panel.add(btnEntregado);
				btnEntregado.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        cambiarEstadoPedido("entregado");
				    }
				});
				
				JButton btnPendiente = new JButton("Pendiente");
				btnPendiente.setBounds(10, 321, 206, 30);
				panel.add(btnPendiente);
				btnPendiente.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        cambiarEstadoPedido("pendiente");
				    }
				});
				
				JButton btnCrearCliente = new JButton("Crear Cliente");
				btnCrearCliente.setBounds(10, 500, 206, 30);
				panel.add(btnCrearCliente);
				btnCrearCliente.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        new crearcliente().setVisible(true);
				        dispose();
				    }
				});
				
				JButton btnClientes = new JButton("Clientes");
				btnClientes.setBounds(10, 460, 206, 30);
				panel.add(btnClientes);
				btnClientes.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        new listaclientes().setVisible(true);
				        dispose();
				    }
				});
				
				JButton btnCrearPedido = new JButton("Crear Pedido");
				btnCrearPedido.setBounds(10, 420, 206, 30);
				panel.add(btnCrearPedido);
				btnCrearPedido.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				        new crearpedido().setVisible(true);
				        dispose();
				    }
				});
	}
	
	private void cargarTablaPedidos() {
    String[] columnas = {"ID Detalle", "ID Usuario", "Nombre Usuario", "Nombre Pedido", "Teléfono", "Dirección", "Estado"};
    DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    try {
        Connection conn = connect.getConnection();
        String sql = "SELECT pd.id AS id_detalle, u.id AS usuario_id, u.usuario AS nombre_usuario, p.nombre AS nombre_pedido, u.telefono, u.direccion, pd.estado " +
            "FROM pedido_detalles pd " +
            "JOIN pedidos pe ON pd.pedido_id = pe.id " +
            "JOIN usuarios u ON pe.usuario_id = u.id " +
            "JOIN productos p ON pd.producto_id = p.id";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("id_detalle"),
                rs.getInt("usuario_id"),
                rs.getString("nombre_usuario"),
                rs.getString("nombre_pedido"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                rs.getString("estado")
            };
            modeloTabla.addRow(fila);
        }
        rs.close();
        ps.close();
        conn.close();
    } catch (Exception ex) {
        modeloTabla.addRow(new Object[]{"Error", "-", "-", "-", "-", "-", ex.getMessage()});
    }
    tablaPedidos.setModel(modeloTabla);
    // Ocultar la columna ID Detalle visualmente
    if (tablaPedidos.getColumnCount() > 0) {
        tablaPedidos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaPedidos.getColumnModel().getColumn(0).setWidth(0);
    }
}
	
	private void cambiarEstadoPedido(String nuevoEstado) {
    int filaSeleccionada = tablaPedidos.getSelectedRow();
    if (filaSeleccionada != -1) {
        int idDetalle = (int) tablaPedidos.getValueAt(filaSeleccionada, 0);
        try {
            Connection conn = connect.getConnection();
            String sql = "UPDATE pedido_detalles SET estado = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idDetalle);
            ps.executeUpdate();
            ps.close();
            conn.close();
            javax.swing.JOptionPane.showMessageDialog(null, "Estado actualizado a '" + nuevoEstado + "'.");
            cargarTablaPedidos();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(null, "Selecciona un pedido de la tabla.");
    }
}
}
