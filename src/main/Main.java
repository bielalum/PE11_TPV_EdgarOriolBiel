package main;

import database.DBArticles;
import database.DBClients;
import database.DBVendes;
import java.util.ArrayList;
import java.util.Scanner;
import model.Article;
import model.Camisa;
import model.Client;
import model.LiniaFactura;
import model.Pantalo;
import model.Tiquet;
import utils.GestorJSON;

public class Main {
    public static void main(String[] args) {
        Scanner esc = new Scanner(System.in);
        int opcio;

        do{
            System.out.println("\n----- MENU PRINCIPAL TPV -----");
            System.out.println("1. Importar articles");
            System.out.println("2. Gestionar articles");
            System.out.println("3. Gestionar clients");
            System.out.println("4. TPV");
            System.out.println("5. Consultar vendes per client");
            System.out.println("6. Consultar vendes per article");
            System.out.println("7. Calcular beneficis totals");
            System.out.println("8. Recompra automàtica d'articles");
            System.out.println("9. Sortir");
            opcio = llegirEnter(esc, "\nSelecciona una opció: ");

            switch (opcio) {
                case 1: importarArticles(esc); break;
                case 2: menuArticles(esc); break;
                case 3: menuClients(esc); break;
                case 4: ferVendaTPV(esc); break;
                case 5: consultarVendesClient(esc); break;
                case 6: consultarVendesArticle(esc); break;
                case 7: System.out.println("Funcionalitat prevista per al sprint final."); break;
                case 8: System.out.println("Funcionalitat prevista per al sprint final."); break;
                case 9: System.out.println("\nSortint del programa..."); break;
                default: System.out.println("\nERROR: Opció no vàlida. Tria una entre 1-9");
            }
        } while (opcio != 9);

        esc.close();
    }

    private static void importarArticles(Scanner esc) {
        String ruta = "resources/PE11_articles.json";
        System.out.print("Ruta JSON [ENTER per resources/PE11_articles.json]: ");
        String entrada = esc.nextLine();
        if (!entrada.trim().isEmpty()) ruta = entrada.trim();

        ArrayList<Article> articles = GestorJSON.llegirArticles(ruta);
        int camises = 0;
        int pantalons = 0;
        for (Article a : articles) {
            if (a instanceof Camisa) camises++;
            if (a instanceof Pantalo) pantalons++;
        }
        System.out.println("Articles llegits del JSON: " + articles.size());
        System.out.println("Camises: " + camises + " | Pantalons: " + pantalons);
        System.out.print("Vols volcar-los a la base de dades? (s/n): ");
        String resposta = esc.nextLine();
        if (!resposta.equalsIgnoreCase("s")) return;

        int afegits = 0;
        int actualitzats = 0;
        for (Article a : articles) {
            boolean existia = DBArticles.existeix(a.getId());
            if (DBArticles.guardarOActualitzar(a)) {
                if (existia) actualitzats++; else afegits++;
            }
        }
        System.out.println("Importació finalitzada. Afegits: " + afegits + " | Actualitzats: " + actualitzats);
    }

    public static void menuArticles(Scanner esc){
        int opcio;
        do{
            System.out.println("\nGESTIÓ ARTICLES");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar article");
            System.out.println("4. Consultar articles");
            System.out.println("5. Tornar al menú principal");
            opcio = llegirEnter(esc, "\nSelecciona una opció: ");

            switch (opcio) {
                case 1: altaArticle(esc); break;
                case 2: baixaArticle(esc); break;
                case 3: modificarArticle(esc); break;
                case 4: consultarArticles(); break;
                case 5: System.out.println("\nTornant al menú principal..."); break;
                default: System.out.println("ERROR: Opció no vàlida. Tria una entre 1-5");
            }
        } while (opcio !=5);
    }

    private static void altaArticle(Scanner esc) {
        Article article = demanarArticle(esc);
        if (article != null && DBArticles.insertar(article)) System.out.println("Article creat correctament.");
    }

    private static void modificarArticle(Scanner esc) {
        Article article = demanarArticle(esc);
        if (article != null && DBArticles.modificar(article)) System.out.println("Article modificat correctament.");
    }

    private static Article demanarArticle(Scanner esc) {
        int id = llegirEnter(esc, "ID article: ");
        System.out.print("Nom: ");
        String nom = esc.nextLine();
        int tipus = llegirEnter(esc, "Tipus (1 Camisa / 2 Pantaló): ");
        double preu = llegirDouble(esc, "Preu base: ");
        int iva = llegirEnter(esc, "IVA (4-21): ");
        int stock = llegirEnter(esc, "Stock: ");

        if (iva < 4 || iva > 21 || stock < 0) {
            System.out.println("ERROR: IVA o stock no vàlids.");
            return null;
        }

        if (tipus == 1) {
            int tallaColl = llegirEnter(esc, "Talla coll (36-52): ");
            int ampladaPit = llegirEnter(esc, "Amplada pit (10-15): ");
            if (tallaColl < 36 || tallaColl > 52 || ampladaPit < 10 || ampladaPit > 15) {
                System.out.println("ERROR: Mesures de camisa no vàlides.");
                return null;
            }
            return new Camisa(id, nom, preu, iva, stock, tallaColl, ampladaPit);
        } else if (tipus == 2) {
            int tallaCintura = llegirEnter(esc, "Talla cintura (24-56): ");
            int llargadaCamal = llegirEnter(esc, "Llargada camal (32-46): ");
            if (tallaCintura < 24 || tallaCintura > 56 || llargadaCamal < 32 || llargadaCamal > 46) {
                System.out.println("ERROR: Mesures de pantaló no vàlides.");
                return null;
            }
            return new Pantalo(id, nom, preu, iva, stock, tallaCintura, llargadaCamal);
        }

        System.out.println("ERROR: Tipus d'article no vàlid.");
        return null;
    }

    private static void baixaArticle(Scanner esc) {
        int id = llegirEnter(esc, "ID article a eliminar: ");
        if (DBArticles.eliminar(id)) System.out.println("Article eliminat correctament.");
        else System.out.println("No s'ha pogut eliminar l'article.");
    }

    private static void consultarArticles() {
        ArrayList<Article> articles = DBArticles.llistarTot();
        if (articles.isEmpty()) System.out.println("No hi ha articles.");
        for (Article a : articles) System.out.println(a);
    }

    public static void menuClients(Scanner esc){
        int opcio;
        do{
            System.out.println("\nGESTIÓ CLIENTS");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar client");
            System.out.println("4. Consultar clients");
            System.out.println("5. Tornar al menú principal");
            opcio = llegirEnter(esc, "\nSelecciona una opció: ");

            switch (opcio) {
                case 1: altaClient(esc); break;
                case 2: baixaClient(esc); break;
                case 3: modificarClient(esc); break;
                case 4: consultarClients(); break;
                case 5: System.out.println("\nTornant al menú principal..."); break;
                default: System.out.println("ERROR: Opció no vàlida. Tria una entre 1-5");
            }
        } while (opcio !=5);
    }

    private static void altaClient(Scanner esc) {
        Client c = demanarClient(esc);
        if (c != null && DBClients.insertar(c)) System.out.println("Client creat correctament.");
    }

    private static void modificarClient(Scanner esc) {
        Client c = demanarClient(esc);
        if (c != null && DBClients.modificar(c)) System.out.println("Client modificat correctament.");
    }

    private static Client demanarClient(Scanner esc) {
        System.out.print("DNI: ");
        String dni = esc.nextLine().trim().toUpperCase();
        if (!dni.equals("000") && !validarDniBasic(dni)) {
            System.out.println("ERROR: DNI no vàlid.");
            return null;
        }
        System.out.print("Nom: ");
        String nom = esc.nextLine();
        System.out.print("Email: ");
        String email = esc.nextLine();
        System.out.print("Telèfon: ");
        String telefon = esc.nextLine();
        return new Client(dni, nom, email, telefon);
    }

    private static boolean validarDniBasic(String dni) {
        return dni.matches("^[0-9]{8}[A-Z]$");
    }

    private static void baixaClient(Scanner esc) {
        System.out.print("DNI client a eliminar: ");
        String dni = esc.nextLine().trim().toUpperCase();
        if (DBClients.eliminar(dni)) System.out.println("Client eliminat correctament.");
        else System.out.println("No s'ha pogut eliminar el client.");
    }

    private static void consultarClients() {
        ArrayList<Client> clients = DBClients.llistarTot();
        if (clients.isEmpty()) System.out.println("No hi ha clients.");
        for (Client c : clients) System.out.println(c);
    }

    private static void ferVendaTPV(Scanner esc) {
        System.out.print("Codi client/DNI (si no existeix usa 000): ");
        String dni = esc.nextLine().trim().toUpperCase();
        Client client = DBClients.buscarPerDni(dni);
        if (client == null) {
            System.out.println("Client no trobat. S'utilitzarà el client genèric 000.");
            client = DBClients.buscarPerDni("000");
            if (client == null) {
                System.out.println("ERROR: Falta crear el client genèric 000 a la BD.");
                return;
            }
        }

        Tiquet tiquet = new Tiquet(client);
        while (true) {
            int idArticle = llegirEnter(esc, "ID article (0 per finalitzar venda): ");
            if (idArticle == 0) break;
            Article article = DBArticles.buscarPerId(idArticle);
            if (article == null) {
                System.out.println("Article no trobat.");
                continue;
            }
            System.out.println("Article: " + article.getId() + " - " + article.getNom() + " | Stock: " + article.getStock());
            int quantitat = llegirEnter(esc, "Quantitat: ");
            if (quantitat <= 0) {
                System.out.println("La quantitat ha de ser positiva.");
            } else if (article.getStock() <= 0) {
                System.out.println("No hi ha stock d'aquest article.");
            } else if (quantitat > article.getStock()) {
                System.out.println("Stock insuficient. Stock actual: " + article.getStock());
            } else {
                tiquet.afegirLinia(new LiniaFactura(article, quantitat));
                System.out.println("Línia afegida.");
            }
        }

        if (tiquet.getLinies().isEmpty()) {
            System.out.println("Venda cancel·lada: no hi ha línies.");
            return;
        }

        mostrarTiquet(tiquet);
        System.out.print("Confirmar venda? (s/n): ");
        String confirmar = esc.nextLine();
        if (confirmar.equalsIgnoreCase("s")) {
            if (DBVendes.guardarTiquet(tiquet)) {
                System.out.println("Venda guardada correctament. ID tiquet: " + tiquet.getId());
            }
        } else {
            System.out.println("Venda cancel·lada.");
        }
    }

    private static void mostrarTiquet(Tiquet tiquet) {
        System.out.println("\n========================================");
        System.out.println("              TIQUET DE VENDA           ");
        System.out.println("========================================");
        System.out.println("Data: " + tiquet.getDataCompra());
        System.out.println("Client: " + tiquet.getClient().getDni() + " - " + tiquet.getClient().getNom());
        System.out.println("----------------------------------------");
        for (LiniaFactura l : tiquet.getLinies()) {
            System.out.printf("%d - %s x%d | Base: %.2f | IVA: %d%% | Total: %.2f €\n",
                    l.getArticle().getId(), l.getArticle().getNom(), l.getQuantitat(), l.getPreuBase(), l.getIva(), l.getPreuFinal());
        }
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL BASE: %.2f €\n", tiquet.getTotalBase());
        System.out.printf("TOTAL IVA: %.2f €\n", tiquet.getTotalIva());
        System.out.printf("TOTAL FINAL: %.2f €\n", tiquet.getTotalFinal());
        System.out.println("========================================\n");
    }

    private static void consultarVendesClient(Scanner esc) {
        System.out.print("DNI client: ");
        DBVendes.consultaVendesPerClient(esc.nextLine().trim().toUpperCase());
    }

    private static void consultarVendesArticle(Scanner esc) {
        int id = llegirEnter(esc, "ID article: ");
        DBVendes.consultaVendesPerArticle(id);
    }

    private static int llegirEnter(Scanner esc, String missatge) {
        while (true) {
            System.out.print(missatge);
            try {
                return Integer.parseInt(esc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número enter vàlid.");
            }
        }
    }

    private static double llegirDouble(Scanner esc, String missatge) {
        while (true) {
            System.out.print(missatge);
            try {
                return Double.parseDouble(esc.nextLine().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
            }
        }
    }
}
