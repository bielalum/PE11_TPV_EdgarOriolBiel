package model;
public abstract class Article {
    protected int id;
    protected String nom;
    protected double preuBase;
    protected int iva;
    protected int stock;

    public Article(int id, String nom, double preuBase, int iva, int stock){
        this.id = id;
        this.nom = nom;
        this.preuBase = preuBase;
        this.iva = iva;
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



    public double getPreuBase(){
        return preuBase;
    }

    public void setPreuBase(double preuBase){
        this.preuBase = preuBase;
    }



    public int getIva(){
        return iva;
    }

    public void setIva(int iva){
        this.iva = iva;
    }



    public int getStock(){
        return stock;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    @Override
    public abstract String toString();
}
