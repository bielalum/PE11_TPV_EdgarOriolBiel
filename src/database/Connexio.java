package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexio {
    private static final String URL = "jdbc:mysql://localhost:3306/tpv_botiga";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection connectar() {
        Connection connexio = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connexio = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexió amb la BD establerta!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en la connexió: " + e.getMessage());
        }
        return connexio;
    }
}