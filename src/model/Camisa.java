package model;

public class Camisa extends Article {
    private int tallaColl;
    private int ampladaPit;

    public Camisa(int id, String nom, double preuBase, int iva, int stock, int tallaColl, int ampladaPit){
        super(id, nom, preuBase, iva, stock);
        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }

    public int getTallaColl(){ return tallaColl; }
    public void setTallaColl(int tallaColl){ this.tallaColl = tallaColl; }
    public int getAmpladaPit(){ return ampladaPit; }
    public void setAmpladaPit(int ampladaPit){ this.ampladaPit = ampladaPit; }
    @Override
    public int getIdTipus(){ return 1; }

    @Override
    public String toString(){
        return "Camisa (ID: " + id + ", Nom: " + nom + ", Preu base: " + preuBase + ", IVA: " + iva + "%" + ", Stock: " + stock + ", Talla coll: " + tallaColl + ", Amplada pit: " + ampladaPit + ")";
    }
}
