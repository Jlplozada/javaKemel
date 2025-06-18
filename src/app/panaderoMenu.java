package app;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class panaderoMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList listaPedidos;
	private JTable tablaPedidos;
	private JScrollPane scrollPedidos;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					panaderoMenu frame = new panaderoMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public panaderoMenu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(panaderoMenu.class.getResource("/img/logov.png")));
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
		panel.setBounds(828, 0, 226, 622);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 5, 206, 119);
		panel.add(lblNewLabel);
		ImageIcon icon = new ImageIcon(panaderoMenu.class.getResource("/img/logov.png"));
		java.awt.Image img = icon.getImage();
		java.awt.Image imgScaled = img.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(imgScaled));
		
		JButton btnPedidoListo = new JButton("Pedido Listo");
		btnPedidoListo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaPedidos.getSelectedRow();
				if (filaSeleccionada != -1) {
					int idDetalle = (int) tablaPedidos.getValueAt(filaSeleccionada, 0); // Columna ID
					try {
						Connection conn = connect.getConnection();
						String sql = "UPDATE pedido_detalles SET estado = 'preparado' WHERE id = ?";
						PreparedStatement ps = conn.prepareStatement(sql);
						ps.setInt(1, idDetalle);
						ps.executeUpdate();
						ps.close();
						conn.close();
						javax.swing.JOptionPane.showMessageDialog(null, "Pedido actualizado a 'preparado'.");
						cargarTablaPedidos();
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
					}
				} else {
					javax.swing.JOptionPane.showMessageDialog(null, "Selecciona un pedido de la tabla.");
				}
			}
		});
		btnPedidoListo.setBounds(29, 204, 149, 23);
		panel.add(btnPedidoListo);

		JButton btnSalir = new JButton("Salir");
		btnSalir.setBounds(67, 581, 90, 30);
		panel.add(btnSalir);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(29, 238, 149, 23);
		panel.add(btnActualizar);
		
		tablaPedidos = new JTable();
		scrollPedidos = new JScrollPane(tablaPedidos);
		scrollPedidos.setBounds(10, 11, 798, 599);
		contentPane.add(scrollPedidos);

		cargarTablaPedidos();
		
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarTablaPedidos();
			}
		});
	}

	private void cargarTablaPedidos() {
		String[] columnas = {"ID", "Pedido", "Producto", "Estado"};
		DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
		try {
			Connection conn = connect.getConnection();
			String sql = "SELECT pd.id, pd.pedido_id, p.nombre AS producto, pd.estado " +
				"FROM pedido_detalles pd " +
				"JOIN productos p ON pd.producto_id = p.id " +
				"WHERE pd.estado IN ('aprobado', 'pendiente')";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Object[] fila = {
					rs.getInt("id"),
					rs.getInt("pedido_id"),
					rs.getString("producto"),
					rs.getString("estado")
				};
				modeloTabla.addRow(fila);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception ex) {
			modeloTabla.addRow(new Object[]{"Error", "-", "-", ex.getMessage()});
		}
		tablaPedidos.setModel(modeloTabla);
	}
}
