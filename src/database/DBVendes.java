package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.LiniaFactura;
import model.Tiquet;

public class DBVendes {

    public static boolean guardarTiquet(Tiquet tiquet) {
        String sqlTiquet = "INSERT INTO tiquets (data_compra, dni_client, total_base, total_iva, total_final) VALUES (?, ?, ?, ?, ?)";
        String sqlLinia = "INSERT INTO linies_factura (id_tiquet, id_article, quantitat, preu_base, iva, preu_final) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;

        try {
            con = Connexio.connectar();
            if (con == null) return false;
            con.setAutoCommit(false);

            try (PreparedStatement psTiquet = con.prepareStatement(sqlTiquet, Statement.RETURN_GENERATED_KEYS)) {
                psTiquet.setDate(1, Date.valueOf(tiquet.getDataCompra()));
                psTiquet.setString(2, tiquet.getClient().getDni());
                psTiquet.setDouble(3, tiquet.getTotalBase());
                psTiquet.setDouble(4, tiquet.getTotalIva());
                psTiquet.setDouble(5, tiquet.getTotalFinal());
                psTiquet.executeUpdate();

                try (ResultSet generatedKeys = psTiquet.getGeneratedKeys()) {
                    if (generatedKeys.next()) tiquet.setId(generatedKeys.getInt(1));
                    else throw new SQLException("No s'ha pogut recuperar l'ID del tiquet.");
                }
            }

            try (PreparedStatement psLinia = con.prepareStatement(sqlLinia)) {
                for (LiniaFactura linia : tiquet.getLinies()) {
                    psLinia.setInt(1, tiquet.getId());
                    psLinia.setInt(2, linia.getArticle().getId());
                    psLinia.setInt(3, linia.getQuantitat());
                    psLinia.setDouble(4, linia.getPreuBase());
                    psLinia.setInt(5, linia.getIva());
                    psLinia.setDouble(6, linia.getPreuFinal());
                    psLinia.addBatch();
                }
                psLinia.executeBatch();
            }

            for (LiniaFactura linia : tiquet.getLinies()) {
                int nouStock = linia.getArticle().getStock() - linia.getQuantitat();
                try (PreparedStatement psStock = con.prepareStatement("UPDATE articles SET stock=? WHERE id=?")) {
                    psStock.setInt(1, nouStock);
                    psStock.setInt(2, linia.getArticle().getId());
                    psStock.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR guardant la venda: " + e.getMessage());
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.out.println("ERROR fent rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("ERROR tancant connexió: " + e.getMessage());
            }
        }
    }

    public static void consultaVendesPerClient(String dni) {
        String sql = "SELECT c.dni, c.nom, COUNT(t.id) AS num_tiquets, COALESCE(SUM(t.total_final),0) AS total_despesa " +
                     "FROM clients c LEFT JOIN tiquets t ON c.dni = t.dni_client WHERE c.dni = ? GROUP BY c.dni, c.nom";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dni);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n--- VENDES PER CLIENT ---");
                        System.out.println("DNI: " + rs.getString("dni"));
                        System.out.println("Nom: " + rs.getString("nom"));
                        System.out.println("Nombre de tiquets: " + rs.getInt("num_tiquets"));
                        System.out.printf("Despesa total: %.2f €\n", rs.getDouble("total_despesa"));
                    } else {
                        System.out.println("ERROR: No existeix cap client amb aquest DNI.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR consultant vendes per client: " + e.getMessage());
        }
    }

    public static void consultaVendesPerArticle(int idArticle) {
        String sql = "SELECT a.id, a.nom, COALESCE(SUM(l.quantitat),0) AS quantitat_venuda " +
                     "FROM articles a LEFT JOIN linies_factura l ON a.id = l.id_article WHERE a.id = ? GROUP BY a.id, a.nom";
        try (Connection con = Connexio.connectar()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idArticle);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n--- VENDES PER ARTICLE ---");
                        System.out.println("Codi: " + rs.getInt("id"));
                        System.out.println("Nom: " + rs.getString("nom"));
                        System.out.println("Quantitat total venuda: " + rs.getInt("quantitat_venuda"));
                    } else {
                        System.out.println("ERROR: No existeix cap article amb aquest codi.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR consultant vendes per article: " + e.getMessage());
        }
    }

    public static void mostrarInformeBeneficis(double percentCostCamisa, double percentCostPantalo, boolean ascendent) {
        String ordre = ascendent ? "ASC" : "DESC";
        // Consulta corregida substituint el JOIN per un CASE WHEN
        String sql = "SELECT a.id, a.nom, a.id_tipus, " +
                     "CASE WHEN a.id_tipus = 1 THEN 'Camisa' ELSE 'Pantaló' END AS tipus, a.preu_base, " +
                     "COALESCE(SUM(l.quantitat),0) AS unitats_venudes, " +
                     "COALESCE(SUM(l.preu_base),0) AS vendes_base " +
                     "FROM articles a " +
                     "LEFT JOIN linies_factura l ON a.id = l.id_article " +
                     "GROUP BY a.id, a.nom, a.id_tipus, a.preu_base " +
                     "ORDER BY (COALESCE(SUM(l.preu_base),0) - " +
                     "(COALESCE(SUM(l.quantitat),0) * a.preu_base * " +
                     "CASE WHEN a.id_tipus = 1 THEN ? ELSE ? END / 100)) " + ordre;

        double totalVendes = 0;
        double totalCost = 0;
        double totalBenefici = 0;

        try (Connection con = Connexio.connectar()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, percentCostCamisa);
                ps.setDouble(2, percentCostPantalo);

                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("\n--- INFORME DE COSTOS I BENEFICIS ---");
                    System.out.printf("%-5s %-25s %-10s %10s %12s %12s %12s\n", "ID", "Article", "Tipus", "Unitats", "Vendes", "Cost", "Benefici");
                    System.out.println("--------------------------------------------------------------------------------------");

                    while (rs.next()) {
                        int idTipus = rs.getInt("id_tipus");
                        int unitats = rs.getInt("unitats_venudes");
                        double preuBase = rs.getDouble("preu_base");
                        double vendes = rs.getDouble("vendes_base");
                        double percentCost = idTipus == 1 ? percentCostCamisa : percentCostPantalo;
                        double cost = unitats * preuBase * percentCost / 100.0;
                        double benefici = vendes - cost;

                        totalVendes += vendes;
                        totalCost += cost;
                        totalBenefici += benefici;

                        System.out.printf("%-5d %-25s %-10s %10d %11.2f€ %11.2f€ %11.2f€\n",
                                rs.getInt("id"), retallar(rs.getString("nom"), 25), rs.getString("tipus"), unitats, vendes, cost, benefici);
                    }
                }

                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("TOTAL VENDES: %.2f €\n", totalVendes);
                System.out.printf("TOTAL COST: %.2f €\n", totalCost);
                System.out.printf("BENEFICI TOTAL: %.2f €\n", totalBenefici);
            }
        } catch (SQLException e) {
            System.out.println("ERROR calculant beneficis: " + e.getMessage());
        }
    }

    private static String retallar(String text, int max) {
        if (text == null) return "";
        if (text.length() <= max) return text;
        return text.substring(0, max - 3) + "...";
    }
}