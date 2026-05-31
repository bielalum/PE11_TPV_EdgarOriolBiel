package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Tiquet {
    private int idTiquet;
    private LocalDate dataCompra;
    private Client clientAssociat;
    private ArrayList<LiniaFactura> llistaLinies;
    private double totalBaseAcumulat;
    private double totalIvaAcumulat;
    private double totalFinalAcumulat;

    public Tiquet(Client clientAssociat) {
        this.clientAssociat = clientAssociat;
        this.dataCompra = LocalDate.now();
        this.llistaLinies = new ArrayList<LiniaFactura>();
    }

    public void afegirLinia(LiniaFactura novaLinia) {
        this.llistaLinies.add(novaLinia);
        this.recalcularTotals();
    }

    public void recalcularTotals() {
        this.totalBaseAcumulat = 0.0;
        this.totalIvaAcumulat = 0.0;
        this.totalFinalAcumulat = 0.0;
        
        for (int i = 0; i < this.llistaLinies.size(); i++) {
            LiniaFactura liniaActual = this.llistaLinies.get(i);
            this.totalBaseAcumulat = this.totalBaseAcumulat + liniaActual.getPreuBase();
            this.totalIvaAcumulat = this.totalIvaAcumulat + liniaActual.getImportIva();
            this.totalFinalAcumulat = this.totalFinalAcumulat + liniaActual.getPreuFinal();
        }
    }

    public int getId() { 
        return idTiquet; 
    }
    
    public void setId(int idTiquet) { 
        this.idTiquet = idTiquet; 
    }
    
    public LocalDate getDataCompra() { 
        return dataCompra; 
    }
    
    public Client getClient() { 
        return clientAssociat; 
    }
    
    public ArrayList<LiniaFactura> getLinies() { 
        return llistaLinies; 
    }
    
    public double getTotalBase() { 
        return totalBaseAcumulat; 
    }
    
    public double getTotalIva() { 
        return totalIvaAcumulat; 
    }
    
    public double getTotalFinal() { 
        return totalFinalAcumulat; 
    }
}