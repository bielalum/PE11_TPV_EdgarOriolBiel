package model;

public class Client {
    private String dni;
    private String nom;
    private String email;
    private String telefon;

    public Client(String dni, String nom, String email, String telefon) {
        this.dni = dni;
        this.nom = nom;
        this.email = email;
        this.telefon = telefon;
    }

    public String getDni(){ return dni; }
    public void setDni(String dni){ this.dni = dni; }
    public String getNom(){ return nom; }
    public void setNom(String nom){ this.nom = nom; }
    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }
    public String getTelefon(){ return telefon; }
    public void setTelefon(String telefon){ this.telefon = telefon; }

    @Override
    public String toString() {
        return "Client (DNI: " + dni + ", Nom: " + nom + ", Email: " + email + ", Telèfon: " + telefon + ")";
    }
}
