package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Client;

public class DBClients {

    public static boolean insertar(Client client) {
        if (existeix(client.getDni())) {
            System.out.println("ERROR: Ja existeix un client amb aquest DNI.");
            return false;
        }

        String sql = "INSERT INTO clients (dni, nom, email, telefon) VALUES (?, ?, ?, ?)";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, client.getDni());
                ps.setString(2, client.getNom());
                ps.setString(3, client.getEmail());
                ps.setString(4, client.getTelefon());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("ERROR guardant client: " + e.getMessage());
            return false;
        }
    }

    public static boolean modificar(Client client) {
        String sql = "UPDATE clients SET nom=?, email=?, telefon=? WHERE dni=?";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, client.getNom());
                ps.setString(2, client.getEmail());
                ps.setString(3, client.getTelefon());
                ps.setString(4, client.getDni());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("ERROR modificant client: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminar(String dni) {
        if ("000".equals(dni)) {
            System.out.println("ERROR: El client genèric 000 no es pot eliminar.");
            return false;
        }

        String sql = "DELETE FROM clients WHERE dni=?";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dni);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("ERROR eliminant client: " + e.getMessage());
            return false;
        }
    }

    public static Client buscarPerDni(String dni) {
        String sql = "SELECT * FROM clients WHERE dni=?";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dni);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return new Client(rs.getString("dni"), rs.getString("nom"), rs.getString("email"), rs.getString("telefon"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR buscant client: " + e.getMessage());
        }
        return null;
    }

    public static boolean existeix(String dni) {
        return buscarPerDni(dni) != null;
    }

    public static ArrayList<Client> llistarTot() {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY dni";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return clients;
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) clients.add(new Client(rs.getString("dni"), rs.getString("nom"), rs.getString("email"), rs.getString("telefon")));
            }
        } catch (SQLException e) {
            System.out.println("ERROR llistant clients: " + e.getMessage());
        }
        return clients;
    }
}