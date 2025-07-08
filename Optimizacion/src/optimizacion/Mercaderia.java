package optimizacion;

public class Mercaderia {
    private int idMercaderia = 1;
    private double peso = 0.0;
    private double volumen;
    private String destino;
    private int distancia;

    public Mercaderia(int id, double peso, double volumen, String destino, int distancia) {
        this.idMercaderia = id;
        this.peso = peso;
        this.volumen = volumen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getIdMercaderia() {
        return idMercaderia;
    }

    public void setIdMercaderia(int idMercaderia) {
        this.idMercaderia = idMercaderia;
    }

    
    
    public int getId() { return idMercaderia; }
    public double getPeso() { return peso; }
    public double getVolumen() { return volumen; }
    public String getDestino() { return destino; }
}

