package main;

import database.Connexio;
import model.Camisa;
import model.Pantalo;

public class Main {
    public static void main(String[] args) {
        Connexio.connectar();

        Camisa novaCamisa = new Camisa(1, "Camisa de Botons Blanca", 25.5, 10, 42);
        Pantalo nouPantalo = new Pantalo(2, "Texans Blaus", 40.0, 5, 38);

        System.out.println("Objectes creats:");
        System.out.println("Camisa: " + novaCamisa.getNom() + " (Talla: " + novaCamisa.getTallaColl() + ")");
        System.out.println("Pantalo: " + nouPantalo.getNom() + " (Amplada: " + nouPantalo.getAmpladaPit() + ")");
    }
}
