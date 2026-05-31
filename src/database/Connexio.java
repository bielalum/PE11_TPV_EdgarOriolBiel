package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexio {
    private static final String URL = "jdbc:mysql://localhost:3306/tpv_botiga";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en la connexió: " + e.getMessage());
            return null;
        }
    }
}
