package app;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.Color;

public class crearcliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JTextField txtNombre;
	private JPasswordField txtClave;
	private JTextField txtCorreo;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JComboBox<String> comboCiudad;
	private JComboBox<String> comboRol;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					crearcliente frame = new crearcliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public crearcliente() {
		setTitle("Crear Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 450);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(147, 0, 0));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(30, 30, 100, 25);
		contentPane.add(lblUsuario);
		txtUsuario = new JTextField();
		txtUsuario.setBounds(150, 30, 250, 25);
		contentPane.add(txtUsuario);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(30, 70, 100, 25);
		contentPane.add(lblNombre);
		txtNombre = new JTextField();
		txtNombre.setBounds(150, 70, 250, 25);
		contentPane.add(txtNombre);

		JLabel lblClave = new JLabel("Clave:");
		lblClave.setBounds(30, 110, 100, 25);
		contentPane.add(lblClave);
		txtClave = new JPasswordField();
		txtClave.setBounds(150, 110, 250, 25);
		contentPane.add(txtClave);

		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setBounds(30, 150, 100, 25);
		contentPane.add(lblCorreo);
		txtCorreo = new JTextField();
		txtCorreo.setBounds(150, 150, 250, 25);
		contentPane.add(txtCorreo);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(30, 190, 100, 25);
		contentPane.add(lblTelefono);
		txtTelefono = new JTextField();
		txtTelefono.setBounds(150, 190, 250, 25);
		contentPane.add(txtTelefono);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setBounds(30, 230, 100, 25);
		contentPane.add(lblDireccion);
		txtDireccion = new JTextField();
		txtDireccion.setBounds(150, 230, 250, 25);
		contentPane.add(txtDireccion);

		JLabel lblCiudad = new JLabel("Ciudad:");
		lblCiudad.setBounds(30, 270, 100, 25);
		contentPane.add(lblCiudad);
		comboCiudad = new JComboBox<>();
		comboCiudad.setBounds(150, 270, 250, 25);
		contentPane.add(comboCiudad);

		JLabel lblRol = new JLabel("Rol:");
		lblRol.setBounds(30, 310, 100, 25);
		contentPane.add(lblRol);
		comboRol = new JComboBox<>();
		comboRol.setBounds(150, 310, 250, 25);
		contentPane.add(comboRol);

		try {
			Connection conn = connect.getConnection();
			String sqlCiudades = "SELECT nombre FROM ciudades";
			PreparedStatement psCiudades = conn.prepareStatement(sqlCiudades);
			ResultSet rsCiudades = psCiudades.executeQuery();
			while (rsCiudades.next()) {
				comboCiudad.addItem(rsCiudades.getString("nombre"));
			}
			rsCiudades.close();
			psCiudades.close();
			conn.close();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar ciudades: " + ex.getMessage());
		}

		try {
			Connection conn = connect.getConnection();
			String sqlRoles = "SELECT nombre FROM roles";
			PreparedStatement psRoles = conn.prepareStatement(sqlRoles);
			ResultSet rsRoles = psRoles.executeQuery();
			while (rsRoles.next()) {
				comboRol.addItem(rsRoles.getString("nombre"));
			}
			rsRoles.close();
			psRoles.close();
			conn.close();
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar roles: " + ex.getMessage());
		}

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(200, 360, 100, 30);
		contentPane.add(btnEnviar);

		btnEnviar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					Connection conn = connect.getConnection();
					String ciudadNombre = (String) comboCiudad.getSelectedItem();
					int ciudadId = 0;
					PreparedStatement psCiudad = conn.prepareStatement("SELECT id FROM ciudades WHERE nombre = ?");
					psCiudad.setString(1, ciudadNombre);
					ResultSet rsCiudad = psCiudad.executeQuery();
					if (rsCiudad.next()) {
						ciudadId = rsCiudad.getInt("id");
					}
					rsCiudad.close();
					psCiudad.close();
					String rolNombre = (String) comboRol.getSelectedItem();
					int rolId = 0;
					PreparedStatement psRol = conn.prepareStatement("SELECT id FROM roles WHERE nombre = ?");
					psRol.setString(1, rolNombre);
					ResultSet rsRol = psRol.executeQuery();
					if (rsRol.next()) {
						rolId = rsRol.getInt("id");
					}
					rsRol.close();
					psRol.close();

					String sql = "INSERT INTO usuarios (usuario, nombre, clave, correo, telefono, direccion, ciudad_id, rol_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, txtUsuario.getText());
					ps.setString(2, txtNombre.getText());
					ps.setString(3, new String(txtClave.getPassword()));
					ps.setString(4, txtCorreo.getText());
					ps.setString(5, txtTelefono.getText());
					ps.setString(6, txtDireccion.getText());
					ps.setInt(7, ciudadId);
					ps.setInt(8, rolId);
					ps.executeUpdate();
					ps.close();
					conn.close();
					javax.swing.JOptionPane.showMessageDialog(null, "Cliente creado exitosamente.");
					new adminMenu().setVisible(true);
					dispose();
				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(null, "Error al crear cliente: " + ex.getMessage());
				}
			}
		});

	}

}
