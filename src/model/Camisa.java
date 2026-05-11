package model;

public class Camisa extends Article {
    private int tallaColl;

    public Camisa(int id, String nom, double preu, int stock, int tallaColl){
        super(id, nom, preu, stock);
        this.tallaColl = tallaColl;
    }

    public int getTallaColl(){
        return tallaColl;
    }

    public void setTallaColl(int tallaColl){
        this.tallaColl = tallaColl;
    }
}
