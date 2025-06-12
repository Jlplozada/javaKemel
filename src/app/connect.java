package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class connect {

    private static final String URL = "jdbc:mysql://kemel.online/kemlOnline";
    private static final String USER = "kemelOnlineJava";
    private static final String PASSWORD = "Kemel2025@";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: No se pudo conectar a la base de datos.");
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
        	//el joptionpane permite visualizar el mensaje de error de conexion 
            JOptionPane.showMessageDialog(null, "Perdón, no se puede conectar con la base de datos", "kemel Online", JOptionPane.ERROR_MESSAGE);
        }
    }
}
