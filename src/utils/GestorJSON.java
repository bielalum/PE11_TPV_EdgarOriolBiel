package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Article;
import model.Camisa;
import model.Pantalo;

public class GestorJSON {

    public static ArrayList<Article> llegirArticles(String rutaFitxer) {
        ArrayList<Article> articles = new ArrayList<>();

        try {
            Path ruta = buscarRuta(rutaFitxer);
            if (ruta == null) {
                System.out.println("ERROR: No s'ha trobat el fitxer JSON: " + rutaFitxer);
                return articles;
            } 

            String contingut = new String(Files.readAllBytes(ruta), StandardCharsets.UTF_8).trim();
            
            int iniciArray = contingut.indexOf("[");
            int fiArray = contingut.lastIndexOf("]");
            
            if (iniciArray == -1 || fiArray == -1) {
                return articles;
            } 
            
            String interiorArray = contingut.substring(iniciArray + 1, fiArray).trim();
            if (interiorArray.isEmpty()) {
                return articles;
            } 

            int indexCerca = 0;
            boolean processant = true;
            
            while (processant) {
                int iniciObjecte = interiorArray.indexOf("{", indexCerca);
                if (iniciObjecte == -1) {
                    processant = false;
                } else {
                    int fiObjecte = interiorArray.indexOf("}", iniciObjecte);
                    if (fiObjecte == -1) {
                        processant = false;
                    } else {
                        String jsonObjecte = interiorArray.substring(iniciObjecte + 1, fiObjecte);
                        Article article = crearArticleDesDeJson(jsonObjecte);
                        
                        if (article != null) {
                            articles.add(article);
                        } 
                        
                        indexCerca = fiObjecte + 1;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR llegint JSON: " + e.getMessage());
        }

        return articles;
    }

    private static Article crearArticleDesDeJson(String jsonObjecte) {
        int id = llegirInt(jsonObjecte, "id");
        String nom = llegirString(jsonObjecte, "nom");
        String familia = llegirString(jsonObjecte, "familia").trim().toLowerCase();
        double preuBase = llegirDouble(jsonObjecte, "preu_base");
        int iva = llegirInt(jsonObjecte, "iva");
        int stock = llegirInt(jsonObjecte, "stock");

        if (id <= 0 || nom.isEmpty() || preuBase < 0 || iva < 4 || iva > 21 || stock < 0) {
            return null;
        } 

        if ("camisa".equals(familia)) {
            int tallaColl = llegirInt(jsonObjecte, "talla_coll");
            int ampladaPit = llegirInt(jsonObjecte, "amplada_pit");
            
            if (tallaColl < 36 || tallaColl > 52 || ampladaPit < 10 || ampladaPit > 15) {
                return null;
            }
            return new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);

        } else if ("pantaló".equals(familia) || "pantalo".equals(familia)) {
            int tallaCintura = llegirInt(jsonObjecte, "talla_cintura");
            int llargadaCamal = llegirInt(jsonObjecte, "llargada_camal");
            
            if (tallaCintura < 24 || tallaCintura > 56 || llargadaCamal < 32 || llargadaCamal > 46) {
                return null;
            }
            return new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);
        }

        return null;
    }

    public static boolean escriureRecompra(String rutaFitxer, ArrayList<Article> articles, int stockObjectiu) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            int unitatsRecompra = stockObjectiu - article.getStock();
            if (unitatsRecompra < 0) {
                unitatsRecompra = 0;
            } 

            String familia = (article.getIdTipus() == 1) ? "camisa" : "pantaló";

            json.append("  {\n");
            json.append("    \"id\": ").append(article.getId()).append(",\n");
            json.append("    \"nom\": \"").append(escaparJson(article.getNom())).append("\",\n");
            json.append("    \"familia\": \"").append(familia).append("\",\n");
            json.append("    \"stock_actual\": ").append(article.getStock()).append(",\n");
            json.append("    \"unitats_recompra\": ").append(unitatsRecompra).append(",\n");
            json.append("    \"stock_final\": ").append(stockObjectiu).append("\n");
            json.append("  }");
            
            if (i < articles.size() - 1) {
                json.append(",");
            } 
            json.append("\n");
        }

        json.append("]\n");

        try {
            Path rutaDesti = Paths.get(rutaFitxer);
            Path carpetaDesti = rutaDesti.getParent();
            
            if (carpetaDesti != null && !Files.exists(carpetaDesti)) {
                Files.createDirectories(carpetaDesti);
            } 
            
            Files.write(rutaDesti, json.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            System.out.println("ERROR escrivint JSON de recompra: " + e.getMessage());
            return false;
        }
    }

    private static Path buscarRuta(String rutaFitxer) {
        Path rutaDirecta = Paths.get(rutaFitxer);
        if (Files.exists(rutaDirecta)) {
            return rutaDirecta;
        } 

        Path rutaAbsoluta = Paths.get("").toAbsolutePath().resolve(rutaFitxer);
        if (Files.exists(rutaAbsoluta)) {
            return rutaAbsoluta;
        } 

        Path rutaParent = Paths.get("").toAbsolutePath().resolve("..").resolve(rutaFitxer).normalize();
        if (Files.exists(rutaParent)) {
            return rutaParent;
        } 

        return null;
    }

    private static String llegirString(String jsonObjecte, String camp) {
        String clau = "\"" + camp + "\"";
        int posicioClau = jsonObjecte.indexOf(clau);
        if (posicioClau == -1) {
            return "";
        }

        int posicioDosPunts = jsonObjecte.indexOf(":", posicioClau);
        if (posicioDosPunts == -1) {
            return "";
        }

        int primerCometa = jsonObjecte.indexOf("\"", posicioDosPunts);
        if (primerCometa == -1) {
            return "";
        }

        int segonCometa = jsonObjecte.indexOf("\"", primerCometa + 1);
        if (segonCometa == -1) {
            return "";
        }

        return jsonObjecte.substring(primerCometa + 1, segonCometa);
    }

    private static int llegirInt(String jsonObjecte, String camp) {
        return (int) Math.round(llegirDouble(jsonObjecte, camp));
    }

    private static double llegirDouble(String jsonObjecte, String camp) {
        String clau = "\"" + camp + "\"";
        int posicioClau = jsonObjecte.indexOf(clau);
        if (posicioClau == -1) {
            return 0.0;
        }

        int posicioDosPunts = jsonObjecte.indexOf(":", posicioClau);
        if (posicioDosPunts == -1) {
            return 0.0;
        }

        int fiCamp = jsonObjecte.indexOf(",", posicioDosPunts);
        if (fiCamp == -1) {
            fiCamp = jsonObjecte.length();
        } 

        String valorBrut = jsonObjecte.substring(posicioDosPunts + 1, fiCamp).trim();
        valorBrut = valorBrut.replace("\"", "").trim();

        try {
            return Double.parseDouble(valorBrut);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static String escaparJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}