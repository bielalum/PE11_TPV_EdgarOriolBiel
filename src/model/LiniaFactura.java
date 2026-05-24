package model;

public class LiniaFactura {
    private Article article;
    private int quantitat;
    private double preuBase;
    private int iva;
    private double preuFinal;

    public LiniaFactura(Article article, int quantitat) {
        this.article = article;
        this.quantitat = quantitat;
        this.preuBase = article.getPreuBase() * quantitat;
        this.iva = article.getIva();
        this.preuFinal = preuBase + (preuBase * iva / 100.0);
    }

    public Article getArticle(){ return article; }
    public int getQuantitat(){ return quantitat; }
    public double getPreuBase(){ return preuBase; }
    public int getIva(){ return iva; }
    public double getPreuFinal(){ return preuFinal; }
    public double getImportIva(){ return preuFinal - preuBase; }
}
