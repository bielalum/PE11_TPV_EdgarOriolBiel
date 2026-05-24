package model;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargadaCamal;

    public Pantalo(int id, String nom, double preuBase, int iva, int stock, int tallaCintura, int llargadaCamal){
        super(id, nom, preuBase, iva, stock);
        this.tallaCintura = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }

    public int getTallaCintura(){ return tallaCintura; }
    public void setTallaCintura(int tallaCintura){ this.tallaCintura = tallaCintura; }
    public int getLlargadaCamal(){ return llargadaCamal; }
    public void setLlargadaCamal(int llargadaCamal){ this.llargadaCamal = llargadaCamal; }
    @Override
    public int getIdTipus(){ return 2; }

    @Override
    public String toString(){
        return "Pantaló (ID: " + id + ", Nom: " + nom + ", Preu base: " + preuBase + ", IVA: " + iva + "%" + ", Stock: " + stock + ", Talla cintura: " + tallaCintura + ", Llargada camal: " + llargadaCamal + ")";
    }
}
