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

    public static boolean guardarOActualitzar(Article article) {
        String sql = "INSERT INTO articles (id, nom, id_tipus, talla_coll, amplada_pit, talla_cintura, llargada_camal, preu_base, iva, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nom=VALUES(nom), id_tipus=VALUES(id_tipus), talla_coll=VALUES(talla_coll), " +
                     "amplada_pit=VALUES(amplada_pit), talla_cintura=VALUES(talla_cintura), llargada_camal=VALUES(llargada_camal), " +
                     "preu_base=VALUES(preu_base), iva=VALUES(iva), stock=VALUES(stock)";
        try (Connection con = Connexio.connectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            omplirPreparedStatementArticle(ps, article);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR guardant article: " + e.getMessage());
            return false;
        }
    }

    public static boolean insertar(Article article) {
        if (existeix(article.getId())) {
            System.out.println("ERROR: Ja existeix un article amb aquest ID.");
            return false;
        }
        return guardarOActualitzar(article);
    }

    public static boolean modificar(Article article) {
        if (!existeix(article.getId())) {
            System.out.println("ERROR: No existeix cap article amb aquest ID.");
            return false;
        }
        return guardarOActualitzar(article);
    }

    private static void omplirPreparedStatementArticle(PreparedStatement ps, Article article) throws SQLException {
        ps.setInt(1, article.getId());
        ps.setString(2, article.getNom());
        ps.setInt(3, article.getIdTipus());

        if (article instanceof Camisa) {
            Camisa c = (Camisa) article;
            ps.setInt(4, c.getTallaColl());
            ps.setInt(5, c.getAmpladaPit());
            ps.setNull(6, Types.INTEGER);
            ps.setNull(7, Types.INTEGER);
        } else if (article instanceof Pantalo) {
            Pantalo p = (Pantalo) article;
            ps.setNull(4, Types.INTEGER);
            ps.setNull(5, Types.INTEGER);
            ps.setInt(6, p.getTallaCintura());
            ps.setInt(7, p.getLlargadaCamal());
        }

        ps.setDouble(8, article.getPreuBase());
        ps.setInt(9, article.getIva());
        ps.setInt(10, article.getStock());
    }

    public static ArrayList<Article> llistarTot() {
        ArrayList<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles ORDER BY id";
        try (Connection con = Connexio.connectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Article article = crearArticleDesDeResultSet(rs);
                if (article != null) articles.add(article);
            }
        } catch (SQLException e) {
            System.out.println("ERROR llistant articles: " + e.getMessage());
        }
        return articles;
    }

    public static Article buscarPerId(int id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (Connection con = Connexio.connectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return crearArticleDesDeResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("ERROR buscant article: " + e.getMessage());
        }
        return null;
    }

    private static Article crearArticleDesDeResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        int idTipus = rs.getInt("id_tipus");
        double preuBase = rs.getDouble("preu_base");
        int iva = rs.getInt("iva");
        int stock = rs.getInt("stock");

        if (idTipus == 1) {
            return new Camisa(id, nom, preuBase, iva, stock, rs.getInt("talla_coll"), rs.getInt("amplada_pit"));
        } else if (idTipus == 2) {
            return new Pantalo(id, nom, preuBase, iva, stock, rs.getInt("talla_cintura"), rs.getInt("llargada_camal"));
        }
        return null;
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection con = Connexio.connectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR eliminant article: " + e.getMessage());
            return false;
        }
    }

    public static boolean existeix(int id) {
        return buscarPerId(id) != null;
    }

    public static boolean actualitzarStock(int idArticle, int nouStock) {
        String sql = "UPDATE articles SET stock = ? WHERE id = ?";
        try (Connection con = Connexio.connectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nouStock);
            ps.setInt(2, idArticle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("ERROR actualitzant stock: " + e.getMessage());
            return false;
        }
    }
}
