package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import model.Article;
import model.Camisa;
import model.Pantalo;

public class DBArticles {

    //MÈTODE PER GUARDAR UN ARTICLE A LA BASE DE DADES
    public static void insertar(Article articlePerGuardar) {

        String consultaSql = "INSERT INTO articles (nom, id_tipus, talla_coll, llargada_camal, talla_cintura, amplada_pit, preu_base, iva, stock) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection connexioBaseDades = Connexio.connectar(); 
             PreparedStatement sentenciaPreparada = connexioBaseDades.prepareStatement(consultaSql)) {

            sentenciaPreparada.setString(1, articlePerGuardar.getNom());
            sentenciaPreparada.setDouble(7, articlePerGuardar.getPreuBase());
            sentenciaPreparada.setInt(8, articlePerGuardar.getIva());
            sentenciaPreparada.setInt(9, articlePerGuardar.getStock());

            if (articlePerGuardar instanceof Camisa) {
                Camisa camisaAuxiliar = (Camisa) articlePerGuardar;
                sentenciaPreparada.setInt(2, 1); //Aquí fem una modificació, ara 1 representa "Camisa"
                sentenciaPreparada.setInt(3, camisaAuxiliar.getTallaColl());
                sentenciaPreparada.setNull(4, Types.INTEGER); //Aquí aniria la llargada_camal, en aquest cas la deixem com a null
                sentenciaPreparada.setNull(5, Types.INTEGER); //Aquí aniria la talla_cintura, en aquest cas la deixem com a null
                sentenciaPreparada.setInt(6, camisaAuxiliar.getAmpladaPit());
            } 
            else if (articlePerGuardar instanceof Pantalo) {
                Pantalo pantaloAuxiliar = (Pantalo) articlePerGuardar;
                sentenciaPreparada.setInt(2, 2); //Aquí fem una modificació, ara 1 representa "Camisa"
                sentenciaPreparada.setNull(3, Types.INTEGER); //Aquí aniria la talla_coll en aquest cas la deixem com a null
                sentenciaPreparada.setInt(4, pantaloAuxiliar.getLlargadaCamal());
                sentenciaPreparada.setInt(5, pantaloAuxiliar.getTallaCintura());
                sentenciaPreparada.setNull(6, Types.INTEGER); //Aquí aniria l'amplada_pit en aquest cas la deixem com a null
            }

            sentenciaPreparada.executeUpdate();
            System.out.println("Article guardat a la base de dades.");

        } catch (SQLException errorSql) {
            System.out.println("ERROR: No s'ha pogut guardar l'article");
        }
    }

    // MÈTODE PER LLISTAR TOTS ELS ARTICLES
    public static ArrayList<Article> llistarTot() {
        ArrayList<Article> llistaArticles = new ArrayList<>();
        String consultaSql = "SELECT * FROM articles";

        try (Connection connexioBaseDades = Connexio.connectar(); 
             PreparedStatement sentenciaPreparada = connexioBaseDades.prepareStatement(consultaSql); 
             ResultSet resultatConsulta = sentenciaPreparada.executeQuery()) {

            while (resultatConsulta.next()) {
                int idTipus = resultatConsulta.getInt("id_tipus"); //Canviem familia per id_tipus de (String a Int)
                
                int id = resultatConsulta.getInt("id");
                String nom = resultatConsulta.getString("nom");
                double preuBase = resultatConsulta.getDouble("preu_base");
                int iva = resultatConsulta.getInt("iva");
                int stock = resultatConsulta.getInt("stock");

                if (idTipus == 1) { //Apliquem els canvis aquí també
                    int tallaColl = resultatConsulta.getInt("talla_coll");
                    int ampladaPit = resultatConsulta.getInt("amplada_pit");
                    llistaArticles.add(new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit));
                } 
                else if (idTipus == 2) { //Apliquem els canvis aquí també
                    int tallaCintura = resultatConsulta.getInt("talla_cintura");
                    int llargadaCamal = resultatConsulta.getInt("llargada_camal");
                    llistaArticles.add(new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal));
                }
            }

        } catch (SQLException errorSql) {
            System.out.println("ERROR: No es pot mostrar la llista");
        }
        return llistaArticles;
    }
}