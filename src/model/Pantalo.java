package model;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargadaCamal;


    public Pantalo(int id, String nom, double preu, int iva, int stock, int tallaCintura, int llargadaCamal){
        super(id, nom, preu, iva, stock);
        this.tallaCintura = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }

    public int getTallaCintura(){
        return tallaCintura;
    }

    public void setTallaCintura(int tallaCintura){
        this.tallaCintura = tallaCintura;
    }



    public int getLlargadaCamal(){
        return llargadaCamal;
    }

    public void setLlargadaCamal(int llargadaCamal){
        this.llargadaCamal = llargadaCamal;
    }


    @Override
    public String toString(){
        return "Camisa (ID: " + id + ", Nom: " + nom + ", Preu: " + preuBase + ", IVA: " + iva + "% " + ", Stock: " + stock + ", Talla Cintura: " + tallaCintura + ", Llargada Camal: " + llargadaCamal + ")";
    }

}
