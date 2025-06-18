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

public class adminMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public adminMenu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(adminMenu.class.getResource("/img/logov.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1106, 660);
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
		
				// Botón Salir
				JButton btnSalir = new JButton("Salir");
				btnSalir.setBounds(73, 581, 90, 30);
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
	}
}
