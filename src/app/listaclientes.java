package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Color;
import javax.swing.JButton;

public class listaclientes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					listaclientes frame = new listaclientes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public listaclientes() {
		setTitle("Lista de Clientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 400);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(147, 0, 0));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		String[] columnas = { "ID", "Usuario", "Nombre", "Correo", "Teléfono", "Dirección", "Ciudad", "Rol" };
		DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
		JTable tablaClientes = new JTable(modeloTabla);
		JScrollPane scrollPane = new JScrollPane(tablaClientes);
		scrollPane.setBounds(10, 10, 760, 300);
		contentPane.add(scrollPane);

		try {
			Connection conn = connect.getConnection();
			String sql = "SELECT u.id, u.usuario, u.nombre, u.correo, u.telefono, u.direccion, c.nombre AS ciudad, u.rol FROM usuarios u LEFT JOIN ciudades c ON u.ciudad_id = c.id";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Object[] fila = { rs.getInt("id"), rs.getString("usuario"), rs.getString("nombre"),
						rs.getString("correo"), rs.getString("telefono"), rs.getString("direccion"),
						rs.getString("ciudad"), rs.getString("rol") };
				modeloTabla.addRow(fila);
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + ex.getMessage());
		}

		JButton btnSalir = new JButton("Salir");
		btnSalir.setBounds(680, 320, 90, 30);
		contentPane.add(btnSalir);
		btnSalir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new adminMenu().setVisible(true);
				dispose();
			}
		});
	}

}
