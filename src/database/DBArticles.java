package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import model.Article;
import model.Camisa;
import model.Pantalo;

public class DBArticles {

    //MÈTODE PER GUARDAR
    public static void insertar(Article articlePerGuardar) {
        String consultaSql = "INSERT INTO articles (nom, preu, stock, tipus, tallaColl, ampladaPit) VALUES (?,?,?,?,?,?)";

        try(Connection connexioBaseDades = Connexio.connectar(); PreparedStatement sentenciaPreparada = connexioBaseDades.prepareStatement(consultaSql)){
            
                sentenciaPreparada.setString(1, articlePerGuardar.getNom());
                sentenciaPreparada.setDouble(2, articlePerGuardar.getPreu());
                sentenciaPreparada.setInt(3, articlePerGuardar.getStock());

                if (articlePerGuardar instanceof Camisa) {
                    sentenciaPreparada.setString(4, "Camisa");
                    sentenciaPreparada.setInt(5, ((Camisa) articlePerGuardar).getTallaColl());
                    sentenciaPreparada.setObject(6, null);
                }
                else if (articlePerGuardar instanceof Pantalo){
                    sentenciaPreparada.setString(4, "Pantalo");
                    sentenciaPreparada.setObject(5, null);
                    sentenciaPreparada.setInt(6, ((Pantalo) articlePerGuardar).getAmpladaPit());
                }

                sentenciaPreparada.executeUpdate();
                System.out.println("S'ha guardat l'artícle a la base de dades.");
            
            }catch(SQLException errorSql){
                System.out.println("ERROR: No s'ha pogut guardar l'artícle a la base de dades");
            }
    }

    //MÈTODE PER LLISTAR
    public static ArrayList<Article> llistarTot(){
        ArrayList<Article> llistaArticles = new ArrayList<>();
        String consultaSql = "SELECT * FROM articles";

        try(Connection connexioBaseDades = Connexio.connectar(); PreparedStatement sentenciaPreparada = connexioBaseDades.prepareStatement(consultaSql); ResultSet resultatConsulta = sentenciaPreparada.executeQuery()){
            
            while (resultatConsulta.next()) {
                String tipusArticle = resultatConsulta.getString("tipus");
                if ("Camisa".equalsIgnoreCase(tipusArticle)) {
                    llistaArticles.add(new Camisa(resultatConsulta.getInt("id"),resultatConsulta.getString("nom"),resultatConsulta.getDouble("preu"),resultatConsulta.getInt("stock"),resultatConsulta.getInt("tallaColl")));
                }
                else{
                    llistaArticles.add(new Pantalo(resultatConsulta.getInt("id"),resultatConsulta.getString("nom"),resultatConsulta.getDouble("preu"),resultatConsulta.getInt("stock"),resultatConsulta.getInt("ampladaPit")));
                }
            }
        
        }catch(SQLException errorSql) {
            System.out.println("ERROR: No s'ha pogut mostrar la llista d'articles");
        }
        return llistaArticles;
    }
}