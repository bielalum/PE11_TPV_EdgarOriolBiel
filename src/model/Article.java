package model;
public abstract class Article {
    protected int id;
    protected String nom;
    protected double preu;
    protected int stock;

    public Article(int id, String nom, double preu, int stock){
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.stock = stock;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



    public String getNom(){
        return nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }



    public double getPreu(){
        return preu;
    }

    public void setPreu(double preu){
        this.preu = preu;
    }



    public int getStock(){
        return stock;
    }

    public void setStock(int stock){
        this.stock = stock;
    }
}
