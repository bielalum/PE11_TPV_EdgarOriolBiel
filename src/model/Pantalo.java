package model;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargadaCamal;

    public Pantalo(int idArticle, String nomArticle, double preuBase, int percentatgeIva, int stockActual, int tallaCintura, int llargadaCamal) {
        super(idArticle, nomArticle, preuBase, percentatgeIva, stockActual);
        this.tallaCintura = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }

    public int getTallaCintura() { 
        return tallaCintura; 
    }
    
    public void setTallaCintura(int tallaCintura) { 
        this.tallaCintura = tallaCintura; 
    }
    
    public int getLlargadaCamal() { 
        return llargadaCamal; 
    }
    
    public void setLlargadaCamal(int llargadaCamal) { 
        this.llargadaCamal = llargadaCamal; 
    }
    
    @Override
    public int getIdTipus() { 
        return 2; 
    }

    @Override
    public String toString() {
        String text = "Pantaló (ID: " + idArticle + ", Nom: " + nomArticle + ", Preu base: " + preuBase + ", IVA: " + percentatgeIva + "%" + ", Stock: " + stockActual + ", Talla cintura: " + tallaCintura + ", Llargada camal: " + llargadaCamal + ")";
        return text;
    }
}