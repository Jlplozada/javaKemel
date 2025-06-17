package app;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class menu extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                try {
                    menu frame = new menu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public menu() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }

    public menu(String rol) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(147, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        if (rol.equals("admin")) {
            JButton btnRegistrar = new JButton("Registrar Usuario");
            btnRegistrar.setBounds(50, 30, 150, 40);
            btnRegistrar.addActionListener(e -> new registro().setVisible(true));
            contentPane.add(btnRegistrar);

            JButton btnHistorial = new JButton("Historial Pedidos");
            btnHistorial.setBounds(220, 30, 150, 40);
            btnHistorial.addActionListener(e -> new historial().setVisible(true));
            contentPane.add(btnHistorial);

            JButton btnCrearPedido = new JButton("Crear Pedido");
            btnCrearPedido.setBounds(50, 90, 150, 40);
            btnCrearPedido.addActionListener(e -> new crearpedido().setVisible(true));
            contentPane.add(btnCrearPedido);
        } else if (rol.equals("panaderia")) {
            JButton btnPedidos = new JButton("Pedidos");
            btnPedidos.setBounds(100, 60, 120, 40);
            btnPedidos.addActionListener(e -> new cambiarEstadoPedido().setVisible(true));
            contentPane.add(btnPedidos);
        }
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(200, 200, 120, 40);
        btnSalir.addActionListener(e -> {
            this.dispose();
            new login().setVisible(true);
        });
        contentPane.add(btnSalir);
    }
}
