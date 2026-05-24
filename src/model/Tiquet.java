package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Tiquet {
    private int id;
    private LocalDate dataCompra;
    private Client client;
    private ArrayList<LiniaFactura> linies;
    private double totalBase;
    private double totalIva;
    private double totalFinal;

    public Tiquet(Client client) {
        this.client = client;
        this.dataCompra = LocalDate.now();
        this.linies = new ArrayList<>();
    }

    public void afegirLinia(LiniaFactura linia) {
        linies.add(linia);
        recalcularTotals();
    }

    public void recalcularTotals() {
        totalBase = 0;
        totalIva = 0;
        totalFinal = 0;
        for (LiniaFactura linia : linies) {
            totalBase += linia.getPreuBase();
            totalIva += linia.getImportIva();
            totalFinal += linia.getPreuFinal();
        }
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    public LocalDate getDataCompra(){ return dataCompra; }
    public Client getClient(){ return client; }
    public ArrayList<LiniaFactura> getLinies(){ return linies; }
    public double getTotalBase(){ return totalBase; }
    public double getTotalIva(){ return totalIva; }
    public double getTotalFinal(){ return totalFinal; }
}
