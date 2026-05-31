package model;

public abstract class Article {
    protected int idArticle;
    protected String nomArticle;
    protected double preuBase;
    protected int percentatgeIva;
    protected int stockActual;

    public Article(int idArticle, String nomArticle, double preuBase, int percentatgeIva, int stockActual) {
        this.idArticle = idArticle;
        this.nomArticle = nomArticle;
        this.preuBase = preuBase;
        this.percentatgeIva = percentatgeIva;
        this.stockActual = stockActual;
    }

    public int getId() { 
        return idArticle; 
    }
    
    public void setId(int idArticle) { 
        this.idArticle = idArticle; 
    }
    
    public String getNom() { 
        return nomArticle; 
    }
    
    public void setNom(String nomArticle) { 
        this.nomArticle = nomArticle; 
    }
    
    public double getPreuBase() { 
        return preuBase; 
    }
    
    public void setPreuBase(double preuBase) { 
        this.preuBase = preuBase; 
    }
    
    public int getIva() { 
        return percentatgeIva; 
    }
    
    public void setIva(int percentatgeIva) { 
        this.percentatgeIva = percentatgeIva; 
    }
    
    public int getStock() { 
        return stockActual; 
    }
    
    public void setStock(int stockActual) { 
        this.stockActual = stockActual; 
    }

    public double calcularPreuFinalUnitari() {
        double preuFinal = this.preuBase + (this.preuBase * this.percentatgeIva / 100.0);
        return preuFinal;
    }

    public abstract int getIdTipus();
    
    @Override
    public abstract String toString();
}