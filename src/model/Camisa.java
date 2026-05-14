package model;

public class Camisa extends Article {
    private int tallaColl;
    private int ampladaPit;

    public Camisa(int id, String nom, double preu, int iva, int stock, int tallaColl, int ampladaPit){
        super(id, nom, preu, iva, stock);
        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }

    public int getTallaColl(){
        return tallaColl;
    }

    public void setTallaColl(int tallaColl){
        this.tallaColl = tallaColl;
    }



    public int getAmpladaPit(){
        return ampladaPit;
    }

    public void setAmpladaPit(int ampladaPit){
        this.ampladaPit = ampladaPit;
    }


    @Override
    public String toString(){
        return "Camisa (ID: " + id + ", Nom: " + nom + ", Preu: " + preuBase + ", IVA: " + iva + "% " + ", Stock: " + stock + ", Talla Coll: " + tallaColl + ", Amplada Pit: " + ampladaPit + ")";
    }
}
