package model;

public class Camisa extends Article {
    private int tallaColl;
    private int ampladaPit;

    public Camisa(int idArticle, String nomArticle, double preuBase, int percentatgeIva, int stockActual, int tallaColl, int ampladaPit) {
        super(idArticle, nomArticle, preuBase, percentatgeIva, stockActual);
        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }

    public int getTallaColl() { 
        return tallaColl; 
    }
    
    public void setTallaColl(int tallaColl) { 
        this.tallaColl = tallaColl; 
    }
    
    public int getAmpladaPit() { 
        return ampladaPit; 
    }
    
    public void setAmpladaPit(int ampladaPit) { 
        this.ampladaPit = ampladaPit; 
    }
    
    @Override
    public int getIdTipus() { 
        return 1; 
    }

    @Override
    public String toString() {
        String text = "Camisa (ID: " + idArticle + ", Nom: " + nomArticle + ", Preu base: " + preuBase + ", IVA: " + percentatgeIva + "%" + ", Stock: " + stockActual + ", Talla coll: " + tallaColl + ", Amplada pit: " + ampladaPit + ")";
        return text;
    }
}