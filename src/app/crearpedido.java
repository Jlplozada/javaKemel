package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;

public class crearpedido extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					crearpedido frame = new crearpedido();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public crearpedido() {
		setTitle("Crear Pedido");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 326);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(147, 0, 0));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblCliente = new JLabel("Cliente:");
		lblCliente.setBounds(30, 30, 100, 25);
		contentPane.add(lblCliente);
		JComboBox<String> comboClientes = new JComboBox<>();
		comboClientes.setBounds(150, 30, 250, 25);
		contentPane.add(comboClientes);

		try {
			Connection conn = connect.getConnection();
			String sqlClientes = "SELECT nombre FROM usuarios WHERE rol = 'cliente'";
			PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
			ResultSet rsClientes = psClientes.executeQuery();
			while (rsClientes.next()) {
				comboClientes.addItem(rsClientes.getString("nombre"));
			}
			rsClientes.close();
			psClientes.close();
			conn.close();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + ex.getMessage());
		}

		JLabel lblProducto = new JLabel("Producto:");
		lblProducto.setBounds(30, 70, 100, 25);
		contentPane.add(lblProducto);
		JComboBox<String> comboProductos = new JComboBox<>();
		comboProductos.setBounds(150, 70, 250, 25);
		contentPane.add(comboProductos);

		JLabel lblCantidad = new JLabel("Cantidad:");
		lblCantidad.setBounds(30, 110, 100, 25);
		contentPane.add(lblCantidad);
		javax.swing.JTextField txtCantidad = new javax.swing.JTextField();
		txtCantidad.setBounds(150, 110, 250, 25);
		contentPane.add(txtCantidad);

		JLabel lblPrecio = new JLabel("Precio Unitario:");
		lblPrecio.setBounds(30, 150, 100, 25);
		contentPane.add(lblPrecio);
		javax.swing.JTextField txtPrecio = new javax.swing.JTextField();
		txtPrecio.setBounds(150, 150, 250, 25);
		contentPane.add(txtPrecio);

		JLabel lblSubtotal = new JLabel("Subtotal:");
		lblSubtotal.setBounds(30, 190, 100, 25);
		contentPane.add(lblSubtotal);
		javax.swing.JTextField txtSubtotal = new javax.swing.JTextField();
		txtSubtotal.setBounds(150, 190, 250, 25);
		txtSubtotal.setEditable(false);
		contentPane.add(txtSubtotal);

		try {
			Connection conn = connect.getConnection();
			String sqlProductos = "SELECT nombre FROM productos";
			PreparedStatement psProductos = conn.prepareStatement(sqlProductos);
			ResultSet rsProductos = psProductos.executeQuery();
			while (rsProductos.next()) {
				comboProductos.addItem(rsProductos.getString("nombre"));
			}
			rsProductos.close();
			psProductos.close();
			conn.close();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar productos: " + ex.getMessage());
		}

		javax.swing.event.DocumentListener docListener = new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				calcularSubtotal();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				calcularSubtotal();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				calcularSubtotal();
			}

			private void calcularSubtotal() {
				try {
					int cantidad = Integer.parseInt(txtCantidad.getText());
					double precio = Double.parseDouble(txtPrecio.getText());
					txtSubtotal.setText(String.format("%.2f", cantidad * precio));
				} catch (Exception ex) {
					txtSubtotal.setText("");
				}
			}
		};
		txtCantidad.getDocument().addDocumentListener(docListener);
		txtPrecio.getDocument().addDocumentListener(docListener);

		javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar Pedido");
		btnGuardar.setBounds(180, 230, 140, 30);
		contentPane.add(btnGuardar);

		btnGuardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					Connection conn = connect.getConnection();
					String clienteNombre = (String) comboClientes.getSelectedItem();
					int clienteId = 0;
					PreparedStatement psCliente = conn.prepareStatement("SELECT id FROM usuarios WHERE nombre = ?");
					psCliente.setString(1, clienteNombre);
					ResultSet rsCliente = psCliente.executeQuery();
					if (rsCliente.next()) {
						clienteId = rsCliente.getInt("id");
					}
					rsCliente.close();
					psCliente.close();
					PreparedStatement psPedido = conn.prepareStatement("INSERT INTO pedidos (usuario_id) VALUES (?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
					psPedido.setInt(1, clienteId);
					psPedido.executeUpdate();
					ResultSet rsPedido = psPedido.getGeneratedKeys();
					int pedidoId = 0;
					if (rsPedido.next()) {
						pedidoId = rsPedido.getInt(1);
					}
					rsPedido.close();
					psPedido.close();
					String productoNombre = (String) comboProductos.getSelectedItem();
					int productoId = 0;
					PreparedStatement psProducto = conn.prepareStatement("SELECT id FROM productos WHERE nombre = ?");
					psProducto.setString(1, productoNombre);
					ResultSet rsProducto = psProducto.executeQuery();
					if (rsProducto.next()) {
						productoId = rsProducto.getInt("id");
					}
					rsProducto.close();
					psProducto.close();
					int cantidad = Integer.parseInt(txtCantidad.getText());
					double precio = Double.parseDouble(txtPrecio.getText());
					double subtotal = Double.parseDouble(txtSubtotal.getText());
					PreparedStatement psDetalle = conn.prepareStatement(
							"INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)");
					psDetalle.setInt(1, pedidoId);
					psDetalle.setInt(2, productoId);
					psDetalle.setInt(3, cantidad);
					psDetalle.setDouble(4, precio);
					psDetalle.setDouble(5, subtotal);
					psDetalle.executeUpdate();
					psDetalle.close();
					conn.close();
					javax.swing.JOptionPane.showMessageDialog(null, "Pedido creado exitosamente.");
					new adminMenu().setVisible(true);
					dispose();
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(null, "Error al crear pedido: " + ex.getMessage());
				}
			}
		});
	}
}
