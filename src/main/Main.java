package main;

import database.Connexio;
import model.Camisa;
import model.Pantalo;

public class Main {
    public static void main(String[] args) {
        Connexio.connectar();

        Camisa novaCamisa = new Camisa(1, "Camisa de Botons Blanca", 25.5, 21, 10, 42, 12);
        Pantalo nouPantalo = new Pantalo(2, "Texans Blaus", 40.0, 21, 5, 38, 40);

        System.out.println("Objectes creats:");
        System.out.println("Camisa: " + novaCamisa.getNom() + " (Talla coll: " + novaCamisa.getTallaColl() + ", Talla pit: " + novaCamisa.getAmpladaPit() + ")");
        System.out.println("Pantalo: " + nouPantalo.getNom() + " (Talla cintura: " + nouPantalo.getTallaCintura() + ", Talla camal: " + nouPantalo.getLlargadaCamal() + ")");

    
    
    
    }
}