package model;

public class Pantalo extends Article {
    private int ampladaPit;

    public Pantalo(int id, String nom, double preu, int stock, int ampladaPit){
        super(id, nom, preu, stock);
        this.ampladaPit = ampladaPit;
    }

    public int getAmpladaPit(){
        return ampladaPit;
    }

    public void setAmpladaPit(int ampladaPit){
        this.ampladaPit = ampladaPit;
    }
}
