package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class connect {

    private static final String URL = "jdbc:mysql://kemel.online/kemelOnline";
    private static final String USER = "kemelOnlineJava";
    private static final String PASSWORD = "Kemel2025@";

    public static Connection getConnection() {
        System.out.println("Intentando cargar el driver JDBC...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC cargado correctamente.");
            System.out.println("Intentando conectar a la base de datos...");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
            System.out.println("Detalles: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                new login().setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null, "Perdón, no se puede conectar con la base de datos", "kemel Online", JOptionPane.ERROR_MESSAGE);
        }
    }
}
