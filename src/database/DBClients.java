package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Client;

public class DBClients {
   public static void insertar(Client clientPerGuardar) {
        String consultaSql = "INSERT INTO clients (dni, nom, email, telefon) VALUES (?, ?, ?, ?)";

        try(Connection connexioBaseDades = Connexio.connectar();
            PreparedStatement sentenciaPreparada = connexioBaseDades.prepareStatement(consultaSql)){

                sentenciaPreparada.setString(1, clientPerGuardar.getDni());
                sentenciaPreparada.setString(2, clientPerGuardar.getNom());
                sentenciaPreparada.setString(3, clientPerGuardar.getEmail());
                sentenciaPreparada.setString(4, clientPerGuardar.getTelefon());

                sentenciaPreparada.executeUpdate();
                System.out.println("Client guardat a la base de dades.");
            }catch(SQLException errorSql){
                System.out.println("ERROR: No s'ha pogut guardar el client a la base de dades.");
            }
   } 
}
