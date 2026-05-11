import database.Connexio;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciant prova de connexió...");
        Connexio.conectar();
    }
}