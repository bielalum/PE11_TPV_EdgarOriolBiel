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
        int opcioMenu = 0;

        do {
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
            opcioMenu = llegirEnterRang(esc, "\nSelecciona una opció: ", 1, 9);

            switch (opcioMenu) {
                case 1:
                    importarArticles(esc);
                    break;
                case 2:
                    menuArticles(esc);
                    break;
                case 3:
                    menuClients(esc);
                    break;
                case 4:
                    ferVendaTPV(esc);
                    break;
                case 5:
                    consultarVendesClient(esc);
                    break;
                case 6:
                    consultarVendesArticle(esc);
                    break;
                case 7:
                    calcularBeneficisTotals(esc);
                    break;
                case 8:
                    recompraAutomatica(esc);
                    break;
                case 9:
                    System.out.println("\nSortint del programa...");
                    break;
                default:
                    System.out.println("ERROR: Opció no vàlida.");
                    break;
            }
        } while (opcioMenu != 9);

        esc.close();
    }

    private static void importarArticles(Scanner esc) {
        String rutaFitxer = "resources/PE11_articles.json";
        System.out.print("Ruta JSON [ENTER per resources/PE11_articles.json]: ");
        String entradaUsuari = esc.nextLine();
        
        if (!entradaUsuari.trim().isEmpty()) {
            rutaFitxer = entradaUsuari.trim();
        }

        ArrayList<Article> llistaArticles = GestorJSON.llegirArticles(rutaFitxer);
        if (llistaArticles.isEmpty()) {
            System.out.println("ERROR: No s'ha importat cap article. Revisa la ruta o el format del JSON.");
            return;
        }

        int comptadorCamises = 0;
        int comptadorPantalons = 0;
        
        for (int i = 0; i < llistaArticles.size(); i++) {
            Article articleActual = llistaArticles.get(i);
            if (articleActual.getIdTipus() == 1) {
                comptadorCamises = comptadorCamises + 1;
            } else if (articleActual.getIdTipus() == 2) {
                comptadorPantalons = comptadorPantalons + 1;
            }
        }

        System.out.println("Articles llegits del JSON: " + llistaArticles.size());
        System.out.println("Camises: " + comptadorCamises + " | Pantalons: " + comptadorPantalons);

        boolean confirmarGuardar = preguntarSiNo(esc, "Vols volcar-los a la base de dades? (s/n): ");
        if (!confirmarGuardar) {
            return;
        }

        int articlesAfegits = 0;
        int articlesActualitzats = 0;
        
        for (int i = 0; i < llistaArticles.size(); i++) {
            Article articleActual = llistaArticles.get(i);
            boolean existiaArticle = DBArticles.existeix(articleActual.getId());
            
            if (DBArticles.guardarOActualitzar(articleActual)) {
                if (existiaArticle) {
                    articlesActualitzats = articlesActualitzats + 1;
                } else {
                    articlesAfegits = articlesAfegits + 1;
                }
            }
        }
        System.out.println("Importació finalitzada. Afegits: " + articlesAfegits + " | Actualitzats: " + articlesActualitzats);
    }

    public static void menuArticles(Scanner esc) {
        int opcioSubmenu = 0;
        do {
            System.out.println("\nGESTIÓ ARTICLES");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar article");
            System.out.println("4. Consultar articles");
            System.out.println("5. Tornar al menú principal");
            opcioSubmenu = llegirEnterRang(esc, "\nSelecciona una opció: ", 1, 5);

            switch (opcioSubmenu) {
                case 1:
                    altaArticle(esc);
                    break;
                case 2:
                    baixaArticle(esc);
                    break;
                case 3:
                    modificarArticle(esc);
                    break;
                case 4:
                    consultarArticles();
                    break;
                case 5:
                    System.out.println("\nTornant al menú principal...");
                    break;
                default:
                    System.out.println("ERROR: Opció no vàlida.");
                    break;
            }
        } while (opcioSubmenu != 5);
    }

    private static void altaArticle(Scanner esc) {
        Article nouArticle = demanarArticleAlta(esc);
        if (nouArticle != null) {
            if (DBArticles.insertar(nouArticle)) {
                System.out.println("Article creat correctament.");
            } else {
                System.out.println("ERROR: No s'ha pogut insertar l'article.");
            }
        } 
    }

    private static Article demanarArticleAlta(Scanner esc) {
        int idArticle = demanarIdArticleNou(esc);
        String nomArticle = llegirTextObligatori(esc, "Nom: ");
        int tipusArticle = llegirEnterRang(esc, "Tipus (1 Camisa / 2 Pantaló): ", 1, 2);
        double preuBase = llegirDoubleMin(esc, "Preu base: ", 0.01);
        int ivaArticle = llegirEnterRang(esc, "IVA (4-21): ", 4, 21);
        int stockArticle = llegirEnterMin(esc, "Stock: ", 0);

        if (tipusArticle == 1) {
            int tallaColl = llegirEnterRang(esc, "Talla coll (36-52): ", 36, 52);
            int ampladaPit = llegirEnterRang(esc, "Amplada pit (10-15): ", 10, 15);
            Camisa novaCamisa = new Camisa(idArticle, nomArticle, preuBase, ivaArticle, stockArticle, tallaColl, ampladaPit);
            return novaCamisa;
        } else {
            int tallaCintura = llegirEnterRang(esc, "Talla cintura (24-56): ", 24, 56);
            int llargadaCamal = llegirEnterRang(esc, "Llargada camal (32-46): ", 32, 46);
            Pantalo nouPantalo = new Pantalo(idArticle, nomArticle, preuBase, ivaArticle, stockArticle, tallaCintura, llargadaCamal);
            return nouPantalo;
        }
    }

    private static int demanarIdArticleNou(Scanner esc) {
        int idIntroduit = 0;
        boolean idValid = false;
        
        while (idValid == false) {
            idIntroduit = llegirEnterMin(esc, "ID article: ", 1);
            boolean existeixJa = DBArticles.existeix(idIntroduit);
            
            if (existeixJa == false) {
                idValid = true;
            } else {
                System.out.println("ERROR: Ja existeix un article amb aquest ID. Introdueix un altre ID.");
            }
        }
        return idIntroduit;
    }

    private static void modificarArticle(Scanner esc) {
        int idBuscat = llegirEnterMin(esc, "ID article a modificar: ", 1);
        Article articleAModificar = DBArticles.buscarPerId(idBuscat);
        
        if (articleAModificar == null) {
            System.out.println("ERROR: Article no trobat.");
            return;
        } 

        boolean modificatCamp = false;
        int opcioCamp = 0;
        
        do {
            System.out.println("\nArticle actual: " + articleAModificar);
            System.out.println("Quin camp vols modificar?");
            System.out.println("1. Nom");
            System.out.println("2. Preu base");
            System.out.println("3. IVA");
            System.out.println("4. Stock");
            
            if (articleAModificar.getIdTipus() == 1) {
                System.out.println("5. Talla coll");
                System.out.println("6. Amplada pit");
            } else {
                System.out.println("5. Talla cintura");
                System.out.println("6. Llargada camal");
            }
            System.out.println("0. Tornar");

            opcioCamp = llegirEnterRang(esc, "Opció: ", 0, 6);
            
            switch (opcioCamp) {
                case 1:
                    articleAModificar.setNom(llegirTextObligatori(esc, "Nou nom: "));
                    modificatCamp = true;
                    break;
                case 2:
                    articleAModificar.setPreuBase(llegirDoubleMin(esc, "Nou preu base: ", 0.01));
                    modificatCamp = true;
                    break;
                case 3:
                    articleAModificar.setIva(llegirEnterRang(esc, "Nou IVA (4-21): ", 4, 21));
                    modificatCamp = true;
                    break;
                case 4:
                    articleAModificar.setStock(llegirEnterMin(esc, "Nou stock: ", 0));
                    modificatCamp = true;
                    break;
                case 5:
                    if (articleAModificar.getIdTipus() == 1) {
                        Camisa castCamisa = (Camisa) articleAModificar;
                        castCamisa.setTallaColl(llegirEnterRang(esc, "Nova talla coll (36-52): ", 36, 52));
                    } else {
                        Pantalo castPantalo = (Pantalo) articleAModificar;
                        castPantalo.setTallaCintura(llegirEnterRang(esc, "Nova talla cintura (24-56): ", 24, 56));
                    }
                    modificatCamp = true;
                    break;
                case 6:
                    if (articleAModificar.getIdTipus() == 1) {
                        Camisa castCamisa = (Camisa) articleAModificar;
                        castCamisa.setAmpladaPit(llegirEnterRang(esc, "Nova amplada pit (10-15): ", 10, 15));
                    } else {
                        Pantalo castPantalo = (Pantalo) articleAModificar;
                        castPantalo.setLlargadaCamal(llegirEnterRang(esc, "Nova llargada camal (32-46): ", 32, 46));
                    }
                    modificatCamp = true;
                    break;
                default:
                    System.out.println("ERROR: Opció no vàlida.");
                    break;
            }
        } while (opcioCamp != 0);

        if (modificatCamp == false) {
            System.out.println("ERROR: No s'ha modificat cap camp.");
            return;
        } 

        if (DBArticles.modificar(articleAModificar)) {
            System.out.println("Article modificat correctament.");
        } else {
            System.out.println("ERROR: No s'han pogut guardar les modificacions a la BD.");
        }
    }

    private static void baixaArticle(Scanner esc) {
        int idAEliminar = llegirEnterMin(esc, "ID article a eliminar: ", 1);
        if (DBArticles.eliminar(idAEliminar)) {
            System.out.println("Article eliminat correctament.");
        } else {
            System.out.println("ERROR: No s'ha pogut eliminar l'article.");
        }
    }

    private static void consultarArticles() {
        ArrayList<Article> llistaArticles = DBArticles.llistarTot();
        if (llistaArticles.isEmpty()) {
            System.out.println("ERROR: No hi ha articles.");
        } else {
            for (int i = 0; i < llistaArticles.size(); i++) {
                Article articleActual = llistaArticles.get(i);
                System.out.println(articleActual);
            }
        }
    }

    public static void menuClients(Scanner esc) {
        int opcioClientMenu = 0;
        do {
            System.out.println("\nGESTIÓ CLIENTS");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar client");
            System.out.println("4. Consultar clients");
            System.out.println("5. Tornar al menú principal");
            opcioClientMenu = llegirEnterRang(esc, "\nSelecciona una opció: ", 1, 5);

            switch (opcioClientMenu) {
                case 1:
                    altaClient(esc);
                    break;
                case 2:
                    baixaClient(esc);
                    break;
                case 3:
                    modificarClient(esc);
                    break;
                case 4:
                    consultarClients();
                    break;
                case 5:
                    System.out.println("\nTornant al menú principal...");
                    break;
                default:
                    System.out.println("ERROR: Opció no vàlida.");
                    break;
            } 
        } while (opcioClientMenu != 5);
    }

    public static void altaClient(Scanner esc) {
        System.out.println("----- DONAR D'ALTA NOU CLIENT -----");
        String dniClient = demanarDniClientNou(esc);
        String nomComplet = llegirTextObligatori(esc, "Nom complet: ");
        String correuEmail = llegirEmail(esc, "Email: ");
        String numeroTelefon = llegirTextObligatori(esc, "Telèfon: ");

        Client nouClient = new Client(dniClient, nomComplet, correuEmail, numeroTelefon);
        if (DBClients.insertar(nouClient)) {
            System.out.println("Client creat i guardat correctament.");
        } else {
            System.out.println("ERROR: Error al guardar el client.");
        }
    }

    private static String demanarDniClientNou(Scanner esc) {
        String dniIntroduit = "";
        boolean dniValid = false;
        
        while (dniValid == false) {
            dniIntroduit = llegirDni(esc, "DNI (8 números i 1 lletra): ");
            boolean jaExisteixDni = DBClients.existeix(dniIntroduit);
            
            if (jaExisteixDni == false) {
                dniValid = true;
            } else {
                System.out.println("ERROR: Ja existeix un client amb aquest DNI. Introdueix-ne un altre.");
            }
        }
        return dniIntroduit;
    }

    private static void modificarClient(Scanner esc) {
        String dniBuscat = llegirDniOGeneric(esc, "DNI del client a modificar: ");
        Client clientAModificar = DBClients.buscarPerDni(dniBuscat);
        
        if (clientAModificar == null) {
            System.out.println("ERROR: Client no trobat.");
            return;
        } 

        boolean modificatCampClient = false;
        int opcioCampClient = 0;
        
        do {
            System.out.println("\nClient actual: " + clientAModificar);
            System.out.println("Quin camp vols modificar?");
            System.out.println("1. Nom");
            System.out.println("2. Email");
            System.out.println("3. Telèfon");
            System.out.println("0. Guardar i tornar");
            opcioCampClient = llegirEnterRang(esc, "Opció: ", 0, 3);

            switch (opcioCampClient) {
                case 1:
                    clientAModificar.setNom(llegirTextObligatori(esc, "Nou nom: "));
                    modificatCampClient = true;
                    break;
                case 2:
                    clientAModificar.setEmail(llegirEmail(esc, "Nou email: "));
                    modificatCampClient = true;
                    break;
                case 3:
                    clientAModificar.setTelefon(llegirTextObligatori(esc, "Nou telèfon: "));
                    modificatCampClient = true;
                    break;
                default:
                    System.out.println("ERROR: Opció no vàlida");
                    break;
            } 

        } while (opcioCampClient != 0);

        if (modificatCampClient == false) {
            System.out.println("ERROR: No s'ha modificat cap camp.");
            return;
        } 

        if (DBClients.modificar(clientAModificar)) {
            System.out.println("Client modificat correctament.");
        } else {
            System.out.println("ERROR: Error al guardar les dades del client.");
        }
    }

    private static void baixaClient(Scanner esc) {
        String dniAEliminar = llegirDniOGeneric(esc, "DNI client a eliminar: ");
        if (DBClients.eliminar(dniAEliminar)) {
            System.out.println("Client eliminat correctament.");
        } else {
            System.out.println("ERROR: No s'ha pogut eliminar el client.");
        }
    }

    private static void consultarClients() {
        ArrayList<Client> llistaClients = DBClients.llistarTot();
        if (llistaClients.isEmpty()) {
            System.out.println("ERROR: No hi ha clients.");
        } else {
            for (int i = 0; i < llistaClients.size(); i++) {
                Client clientActual = llistaClients.get(i);
                System.out.println(clientActual);
            }
        }
    }

private static void ferVendaTPV(Scanner esc) {
        String dniClientVenda = llegirDniOGeneric(esc, "Codi client/DNI (si no existeix fes servir 000): ");
        Client clientVenda = DBClients.buscarPerDni(dniClientVenda);
        
        if (clientVenda == null) {
            System.out.println("ERROR: Client no trobat. S'utilitzarà el client genèric 000.");
            clientVenda = DBClients.buscarPerDni("000");
            if (clientVenda == null) {
                System.out.println("ERROR: Falta crear el client genèric 000 a la BD.");
                return;
            } 
        } 

        Tiquet nouTiquet = new Tiquet(clientVenda);
        boolean continuarAfegint = true;
        
        while (continuarAfegint == true) {
            int idArticleVenda = llegirEnterMin(esc, "ID article (0 per finalitzar venda): ", 0);
            if (idArticleVenda == 0) {
                continuarAfegint = false;
            } else {
                Article articleTrobat = DBArticles.buscarPerId(idArticleVenda);
                if (articleTrobat == null) {
                    System.out.println("ERROR: Article no trobat.");
                } else {
                    System.out.println("Article: " + articleTrobat.getId() + " - " + articleTrobat.getNom() + " | Stock: " + articleTrobat.getStock());
                    int quantitatDemanada = llegirEnterMin(esc, "Quantitat: ", 1);
                    
                    if (articleTrobat.getStock() <= 0) {
                        System.out.println("ERROR: No hi ha stock d'aquest article.");
                    } else if (quantitatDemanada > articleTrobat.getStock()) {
                        System.out.println("ERROR: Stock insuficient. Stock actual: " + articleTrobat.getStock());
                    } else {
                        LiniaFactura novaLinia = new LiniaFactura(articleTrobat, quantitatDemanada);
                        nouTiquet.afegirLinia(novaLinia);
                        System.out.println("Línia afegida.");
                    }
                }
            }
        }

        if (nouTiquet.getLinies().isEmpty()) {
            System.out.println("ERROR: Venda cancel·lada");
            return;
        } 

        mostrarTiquet(nouTiquet);
        boolean confirmarVenda = preguntarSiNo(esc, "Confirmar venda? (s/n): ");
        
        if (confirmarVenda == true) {
            if (DBVendes.guardarTiquet(nouTiquet)) {
                System.out.println("Venda guardada correctament. ID tiquet: " + nouTiquet.getId());
            } else {
                System.out.println("ERROR: Error en guardar el tiquet a la base de dades.");
            }
        } else {
            System.out.println("Venda cancel·lada.");
        }
    }

    private static void mostrarTiquet(Tiquet tiquetAMostrar) {
        System.out.println("\n========================================");
        System.out.println("              TIQUET DE VENDA           ");
        System.out.println("========================================");
        System.out.println("Data: " + tiquetAMostrar.getDataCompra());
        System.out.println("Client: " + tiquetAMostrar.getClient().getDni() + " - " + tiquetAMostrar.getClient().getNom());
        System.out.println("----------------------------------------");
        
        ArrayList<LiniaFactura> liniesTiquet = tiquetAMostrar.getLinies();
        for (int i = 0; i < liniesTiquet.size(); i++) {
            LiniaFactura liniaActual = liniesTiquet.get(i);
            System.out.printf("%d - %s x%d | Base: %.2f | IVA: %d%% | Total: %.2f €\n",
                    liniaActual.getArticle().getId(), liniaActual.getArticle().getNom(), liniaActual.getQuantitat(), liniaActual.getPreuBase(), liniaActual.getIva(), liniaActual.getPreuFinal());
        }
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL BASE: %.2f €\n", tiquetAMostrar.getTotalBase());
        System.out.printf("TOTAL IVA: %.2f €\n", tiquetAMostrar.getTotalIva());
        System.out.printf("TOTAL FINAL: %.2f €\n", tiquetAMostrar.getTotalFinal());
        System.out.println("========================================\n");
    }

    private static void consultarVendesClient(Scanner esc) {
        String dniClientABuscar = llegirDniOGeneric(esc, "DNI client: ");
        DBVendes.consultaVendesPerClient(dniClientABuscar);
    }

    private static void consultarVendesArticle(Scanner esc) {
        int idArticleABuscar = llegirEnterMin(esc, "ID article: ", 1);
        DBVendes.consultaVendesPerArticle(idArticleABuscar);
    }

    private static void calcularBeneficisTotals(Scanner esc) {
        System.out.println("\n--- CÀLCUL DE BENEFICIS ---");
        System.out.println("Com que la base de dades no té camp de cost, s'introdueix el cost com a percentatge del preu base.");
        double costCamisa = llegirDoubleRang(esc, "Cost camises (% sobre preu base): ", 0, 100);
        double costPantalo = llegirDoubleRang(esc, "Cost pantalons (% sobre preu base): ", 0, 100);
        System.out.println("1. Ordenar de més benefici a menys");
        System.out.println("2. Ordenar de menys benefici a més");
        int opcioOrdre = llegirEnterRang(esc, "Opció: ", 1, 2);
        
        boolean ordreInvers = false;
        switch (opcioOrdre) {
            case 1:
                ordreInvers = false;
                break;
            case 2:
                ordreInvers = true;
                break;
            default:
                System.out.println("ERROR: Opció no vàlida");
                break;
        }
        DBVendes.mostrarInformeBeneficis(costCamisa, costPantalo, ordreInvers);
    }

    private static void recompraAutomatica(Scanner esc) {
        System.out.println("\n--- RECOMPRA AUTOMÀTICA D'ARTICLES ---");
        int llindarMinim = llegirEnterMin(esc, "Llindar mínim d'estoc: ", 1);
        int stockObjectiu = llegirEnterMin(esc, "Stock objectiu després de la recompra: ", llindarMinim);

        ArrayList<Article> totsArticles = DBArticles.llistarTot();
        ArrayList<Article> articlesBaixStock = new ArrayList<Article>();
        
        for (int i = 0; i < totsArticles.size(); i++) {
            Article articleActual = totsArticles.get(i);
            if (articleActual.getStock() < llindarMinim) {
                articlesBaixStock.add(articleActual);
            } 
        }

        if (articlesBaixStock.isEmpty()) {
            System.out.println("ERROR: No hi ha articles per sota del llindar indicat.");
            return;
        } 

        System.out.println("\nProposta de recompra:");
        System.out.printf("%-5s %-25s %12s %12s %12s\n", "ID", "Article", "Stock", "Comprar", "Stock final");
        System.out.println("--------------------------------------------------------------------");
        
        for (int i = 0; i < articlesBaixStock.size(); i++) {
            Article articleActual = articlesBaixStock.get(i);
            int unitatsRecompra = stockObjectiu - articleActual.getStock();
            System.out.printf("%-5d %-25s %12d %12d %12d\n", articleActual.getId(), retallar(articleActual.getNom(), 25), articleActual.getStock(), unitatsRecompra, stockObjectiu);
        }

        boolean confirmarRecompra = preguntarSiNo(esc, "Vols confirmar la recompra i actualitzar l'estoc? (s/n): ");
        if (confirmarRecompra == false) {
            System.out.println("Recompra cancel·lada.");
            return;
        } 

        String rutaFitxerRecompra = "resources/recompra_articles.json";
        boolean jsonCreatCorrectament = GestorJSON.escriureRecompra(rutaFitxerRecompra, articlesBaixStock, stockObjectiu);

        int comptadorActualitzats = 0;
        for (int i = 0; i < articlesBaixStock.size(); i++) {
            Article articleActual = articlesBaixStock.get(i);
            if (DBArticles.actualitzarStock(articleActual.getId(), stockObjectiu)) {
                comptadorActualitzats = comptadorActualitzats + 1;
            } 
        }

        if (jsonCreatCorrectament == true) {
            System.out.println("JSON de recompra generat a: " + rutaFitxerRecompra);
        } else {
            System.out.println("ERROR: No s'ha pogut generar el fitxer JSON de recompra.");
        }
        System.out.println("Articles actualitzats: " + comptadorActualitzats);
    }

    private static boolean validarDniBasic(String dniValidar) {
        if (dniValidar.length() != 9) {
            return false;
        } 
        
        for (int i = 0; i < 8; i++) {
            char caracterActual = dniValidar.charAt(i);
            if (Character.isDigit(caracterActual) == false) {
                return false;
            } 
        }
        
        char darreraLletra = dniValidar.charAt(8);
        if (Character.isLetter(darreraLletra) == false) {
            return false;
        } 
        
        return true;
    }

    private static String llegirDni(Scanner esc, String missatge) {
        String dniRetorn = "";
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            dniRetorn = esc.nextLine().trim().toUpperCase();
            if (validarDniBasic(dniRetorn) == true) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: El DNI ha de tenir 8 números i una lletra. Exemple: 12345678A");
            }
        }
        return dniRetorn;
    }

    private static String llegirDniOGeneric(Scanner esc, String missatge) {
        String dniRetorn = "";
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            dniRetorn = esc.nextLine().trim().toUpperCase();
            if (dniRetorn.equals("000") == true) {
                bucleActiu = false;
            } else if (validarDniBasic(dniRetorn) == true) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Escriu un DNI vàlid de 8 números i una lletra, o 000 per al client genèric.");
            }
        }
        return dniRetorn;
    }

    private static String llegirTextObligatori(Scanner esc, String missatge) {
        String textRetorn = "";
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            textRetorn = esc.nextLine().trim();
            if (textRetorn.isEmpty() == false) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Aquest camp no pot estar buit.");
            }
        }
        return textRetorn;
    }

    private static String llegirEmail(Scanner esc, String missatge) {
        String emailRetorn = "";
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            emailRetorn = esc.nextLine().trim();
            if (emailRetorn.isEmpty() == true) {
                bucleActiu = false;
            } else if (emailRetorn.contains("@") == true && emailRetorn.contains(".") == true) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Email no vàlid. Exemple: nom@correu.com");
            }
        }
        return emailRetorn;
    }

    private static boolean preguntarSiNo(Scanner esc, String missatge) {
        String respostaUsuari = "";
        boolean bucleActiu = true;
        boolean resultatFinal = false;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            respostaUsuari = esc.nextLine().trim().toLowerCase();
            if (respostaUsuari.equals("s") == true) {
                resultatFinal = true;
                bucleActiu = false;
            } else if (respostaUsuari.equals("n") == true) {
                resultatFinal = false;
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Respon només amb s o n.");
            }
        }
        return resultatFinal;
    }

    private static int llegirEnter(Scanner esc, String missatge) {
        int numeroRetorn = 0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            try {
                numeroRetorn = Integer.parseInt(esc.nextLine().trim());
                bucleActiu = false;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Introdueix un número enter vàlid.");
            }
        }
        return numeroRetorn;
    }

    private static int llegirEnterMin(Scanner esc, String missatge, int valorMinim) {
        int numeroValidat = 0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            numeroValidat = llegirEnter(esc, missatge);
            if (numeroValidat >= valorMinim) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: El valor mínim és " + valorMinim + ".");
            }
        }
        return numeroValidat;
    }

    private static int llegirEnterRang(Scanner esc, String missatge, int valorMinim, int valorMaxim) {
        int numeroValidat = 0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            numeroValidat = llegirEnter(esc, missatge);
            if (numeroValidat >= valorMinim && numeroValidat <= valorMaxim) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Introdueix un valor entre " + valorMinim + " i " + valorMaxim + ".");
            }
        }
        return numeroValidat;
    }

    private static double llegirDouble(Scanner esc, String missatge) {
        double realRetorn = 0.0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            System.out.print(missatge);
            try {
                realRetorn = Double.parseDouble(esc.nextLine().trim().replace(',', '.'));
                bucleActiu = false;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Introdueix un número vàlid.");
            }
        }
        return realRetorn;
    }

    private static double llegirDoubleMin(Scanner esc, String missatge, double valorMinim) {
        double realValidat = 0.0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            realValidat = llegirDouble(esc, missatge);
            if (realValidat >= valorMinim) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: El valor mínim és " + valorMinim + ".");
            }
        }
        return realValidat;
    }

    private static double llegirDoubleRang(Scanner esc, String missatge, double valorMinim, double valorMaxim) {
        double realValidat = 0.0;
        boolean bucleActiu = true;
        
        while (bucleActiu == true) {
            realValidat = llegirDouble(esc, missatge);
            if (realValidat >= valorMinim && realValidat <= valorMaxim) {
                bucleActiu = false;
            } else {
                System.out.println("ERROR: Introdueix un valor entre " + valorMinim + " i " + valorMaxim + ".");
            }
        }
        return realValidat;
    }

    private static String retallar(String textOriginal, int longitudMaxima) {
        if (textOriginal == null) {
            return "";
        }
        
        if (textOriginal.length() <= longitudMaxima) {
            return textOriginal;
        } else {
            String textResultat = textOriginal.substring(0, longitudMaxima - 3) + "...";
            return textResultat;
        }
    }
}