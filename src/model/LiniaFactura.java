package model;

public class LiniaFactura {
    private Article articleAssociat;
    private int quantitatVenguda;
    private double preuBaseTotal;
    private int percentatgeIva;
    private double preuFinalTotal;

    public LiniaFactura(Article articleAssociat, int quantitatVenguda) {
        this.articleAssociat = articleAssociat;
        this.quantitatVenguda = quantitatVenguda;
        this.preuBaseTotal = articleAssociat.getPreuBase() * quantitatVenguda;
        this.percentatgeIva = articleAssociat.getIva();
        this.preuFinalTotal = this.preuBaseTotal + (this.preuBaseTotal * this.percentatgeIva / 100.0);
    }

    public Article getArticle() { 
        return articleAssociat; 
    }
    
    public int getQuantitat() { 
        return quantitatVenguda; 
    }
    
    public double getPreuBase() { 
        return preuBaseTotal; 
    }
    
    public int getIva() { 
        return percentatgeIva; 
    }
    
    public double getPreuFinal() { 
        return preuFinalTotal; 
    }
    
    public double getImportIva() { 
        double importIva = this.preuFinalTotal - this.preuBaseTotal;
        return importIva; 
    }
}