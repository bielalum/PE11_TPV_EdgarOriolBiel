package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Article;
import model.Camisa;
import model.Pantalo;

public class GestorJSON {

    public static ArrayList<Article> llegirArticles(String rutaFitxer) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            String contingut = new String(Files.readAllBytes(Paths.get(rutaFitxer)), StandardCharsets.UTF_8);
            Pattern objectePattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
            Matcher matcher = objectePattern.matcher(contingut);
            while (matcher.find()) {
                String obj = matcher.group(1);
                int id = llegirInt(obj, "id");
                String nom = llegirString(obj, "nom");
                String familia = llegirString(obj, "familia").toLowerCase();
                double preuBase = llegirDouble(obj, "preu_base");
                int iva = llegirInt(obj, "iva");
                int stock = llegirInt(obj, "stock");

                if (familia.equals("camisa")) {
                    articles.add(new Camisa(id, nom, preuBase, iva, stock, llegirInt(obj, "talla_coll"), llegirInt(obj, "amplada_pit")));
                } else if (familia.equals("pantaló") || familia.equals("pantalo")) {
                    articles.add(new Pantalo(id, nom, preuBase, iva, stock, llegirInt(obj, "talla_cintura"), llegirInt(obj, "llargada_camal")));
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR llegint JSON: " + e.getMessage());
        }
        return articles;
    }

    private static String llegirString(String obj, String camp) {
        Pattern p = Pattern.compile("\\\"" + camp + "\\\"\\s*:\\s*\\\"(.*?)\\\"");
        Matcher m = p.matcher(obj);
        return m.find() ? m.group(1) : "";
    }

    private static int llegirInt(String obj, String camp) {
        return (int) Math.round(llegirDouble(obj, camp));
    }

    private static double llegirDouble(String obj, String camp) {
        Pattern p = Pattern.compile("\\\"" + camp + "\\\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher m = p.matcher(obj);
        return m.find() ? Double.parseDouble(m.group(1)) : 0;
    }
}
