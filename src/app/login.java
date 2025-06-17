package app;

import java.awt.EventQueue;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;

public class login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField textClave;
	private JLabel lblNewLabel_1;
	private JComboBox<String> comboBoxRoles;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                login frame = new login();
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 762, 341);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(147, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel();
		int logoWidth = 200; 
		int logoHeight = 120;
		lblNewLabel.setBounds(70, 35, 337, 232);
		ImageIcon icon = new ImageIcon(login.class.getResource("/img/logov.png"));
		java.awt.Image img = icon.getImage();
		java.awt.Image imgScaled = img.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), java.awt.Image.SCALE_SMOOTH);
		lblNewLabel.setIcon(new ImageIcon(imgScaled));
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(476, 0, 229, 304);
		contentPane.add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(10, 47, 203, 30);
		panel.add(textField);
		textField.setColumns(10);

		textClave = new JPasswordField();
		textClave.setBounds(10, 115, 203, 35);
		panel.add(textClave);
		textClave.setColumns(10);

		JPanel sesionBtn = new JPanel();
		sesionBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String usuario = textField.getText();
				String clave = new String(textClave.getPassword());
				String rolSeleccionado = (String) comboBoxRoles.getSelectedItem();
				try {
					java.sql.Connection conn = connect.getConnection();
					if (conn != null) {
						String sql = "SELECT r.nombre AS rol FROM usuarios u JOIN roles r ON u.rol_id = r.id WHERE LOWER(u.nombre_usuario) = LOWER(?) AND u.clave = ? AND r.nombre = ?";
						java.sql.PreparedStatement ps = conn.prepareStatement(sql);
						ps.setString(1, usuario);
						ps.setString(2, clave);
						ps.setString(3, rolSeleccionado);
						java.sql.ResultSet rs = ps.executeQuery();
						if (rs.next()) {
							String rol = rs.getString("rol");
							javax.swing.JOptionPane.showMessageDialog(null, "¡Login exitoso!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
							new menu(rol).setVisible(true);
							login.this.dispose();
						} else {
						    textField.setBackground(java.awt.Color.RED);
						    textClave.setBackground(java.awt.Color.RED);
						    javax.swing.JOptionPane.showMessageDialog(null, "Usuario, clave o rol incorrectos\nUsuario: " + usuario + "\nClave: " + clave, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
						}
						rs.close();
						ps.close();
						conn.close();
					} else {
						javax.swing.JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos", "kemel", javax.swing.JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(null, "Usuario no valido", "kemel", javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sesionBtn.setBackground(new Color(147, 0, 0));
				lblNewLabel_1.setForeground(Color.WHITE);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sesionBtn.setBackground(Color.LIGHT_GRAY);
				lblNewLabel_1.setForeground(Color.BLACK);
			}
		});
		sesionBtn.setBackground(new Color(147, 0, 0));
		sesionBtn.setBounds(10, 211, 203, 58);
		panel.add(sesionBtn);
		sesionBtn.setLayout(null);

		lblNewLabel_1 = new JLabel("Inisiar sesion");
		lblNewLabel_1.setBounds(58, 32, 133, 13);
		sesionBtn.add(lblNewLabel_1);

		JLabel Usuario = new JLabel("Usuario");
		Usuario.setBounds(10, 24, 45, 13);
		panel.add(Usuario);

		JLabel Clave = new JLabel("Clave");
		Clave.setBounds(10, 87, 45, 13);
		panel.add(Clave);

		comboBoxRoles = new JComboBox<>();
		comboBoxRoles.addItem("admin");
		comboBoxRoles.addItem("panaderia"); // Cambiado para coincidir con la base de datos
		comboBoxRoles.setBounds(10, 160, 203, 21);
		panel.add(comboBoxRoles);

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textField.setBackground(java.awt.Color.WHITE);
			}
		});
		textClave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				textClave.setBackground(java.awt.Color.WHITE);
			}
		});
	}
}
