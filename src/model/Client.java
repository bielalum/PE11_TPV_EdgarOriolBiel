package model;

public class Client {
    private String dniClient;
    private String nomComplet;
    private String correuEmail;
    private String numeroTelefon;

    public Client(String dniClient, String nomComplet, String correuEmail, String numeroTelefon) {
        this.dniClient = dniClient;
        this.nomComplet = nomComplet;
        this.correuEmail = correuEmail;
        this.numeroTelefon = numeroTelefon;
    }

    public String getDni() { 
        return dniClient; 
    }
    
    public void setDni(String dniClient) { 
        this.dniClient = dniClient; 
    }
    
    public String getNom() { 
        return nomComplet; 
    }
    
    public void setNom(String nomComplet) { 
        this.nomComplet = nomComplet; 
    }
    
    public String getEmail() { 
        return correuEmail; 
    }
    
    public void setEmail(String correuEmail) { 
        this.correuEmail = correuEmail; 
    }
    
    public String getTelefon() { 
        return numeroTelefon; 
    }
    
    public void setTelefon(String numeroTelefon) { 
        this.numeroTelefon = numeroTelefon; 
    }

    @Override
    public String toString() {
        String text = "Client (DNI: " + dniClient + ", Nom: " + nomComplet + ", Email: " + correuEmail + ", Telèfon: " + numeroTelefon + ")";
        return text;
    }
}