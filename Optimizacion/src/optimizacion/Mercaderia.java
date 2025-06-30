package optimizacion;

public class Mercaderia {
    private int idMercaderia = 1;
    private double peso = 0.0;
    private double volumen;
    private String destino;

    public Mercaderia(int id, double peso, double volumen, String destino) {
        this.idMercaderia = id;
        this.peso = peso;
        this.volumen = volumen;
        this.destino = destino;
    }

    public int getId() { return idMercaderia; }
    public double getPeso() { return peso; }
    public double getVolumen() { return volumen; }
    public String getDestino() { return destino; }
}
