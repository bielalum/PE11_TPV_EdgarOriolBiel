package main;

import database.Connexio;
import database.DBArticles;
import database.DBClients;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connexio.connectar();

        Scanner esc = new Scanner(System.in);
        int opcio = 0;

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
            System.out.print("\nSelecciona una opció: ");
            opcio = esc.nextInt();
            esc.nextLine();

            switch (opcio) {
                case 1:
                    System.out.println();
                    break;

                case 2:
                    menuArticles(esc);
                    break;

                case 3:
                    menuClients(esc);
                    break;

                case 4:
                    System.out.println();
                    break;

                case 5:
                    System.out.println();
                    break;

                case 6:
                    System.out.println();
                    break;

                case 7:
                    System.out.println();
                    break;

                case 8:
                    System.out.println();
                    break;

                case 9:
                    System.out.println("\nSortint del programa...");
                    break;                    
            
                default:
                    System.out.println("\nERROR: Opció no vàlida. Tria una entre 1-9");
                    break;
            }
        }while (opcio != 9);

        esc.close();
    }

    public static void menuArticles(Scanner esc){
        int opcio = 0;

        do{
            System.out.println("\nGESTIÓ ARTICLES");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar article");
            System.out.println("4. Consultar article");
            System.out.println("5. Tornar al menú principal");
            System.out.print("\nSelecciona una opció: ");
            opcio = esc.nextInt();
            esc.nextLine();


            switch (opcio) {
                case 1:
                    System.out.println();
                    break;

                case 2:
                    System.out.println();
                    break;

                case 3:
                    System.out.println();
                    break;

                case 4:
                    System.out.println();
                    break;

                case 5:
                    System.out.println("\nTornant al menú principal...");
                    break;

                default:
                    System.out.println("ERROR: Opció no vàlida. Tria una entre 1-5");
                    break;
            }
        }while (opcio !=5);
    }


    public static void menuClients(Scanner esc){
        int opcio = 0;

        do{
            System.out.println("\nGESTIÓ CLIENTS");
            System.out.println("1. Donar d'alta");
            System.out.println("2. Donar de baixa");
            System.out.println("3. Modificar client");
            System.out.println("4. Consultar client");
            System.out.println("5. Tornar al menú principal");
            System.out.print("\nSelecciona una opció: ");
            opcio = esc.nextInt();
            esc.nextLine();


            switch (opcio) {
                case 1:
                    System.out.println();
                    break;

                case 2:
                    System.out.println();
                    break;

                case 3:
                    System.out.println();
                    break;

                case 4:
                    System.out.println();
                    break;

                case 5:
                    System.out.println("\nTornant al menú principal...");
                    break;

                default:
                    System.out.println("ERROR: Opció no vàlida. Tria una entre 1-5");
                    break;
            }
        }while (opcio !=5);
    }
}