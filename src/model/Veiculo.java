package model;

public class Veiculo {
    //atributos
    private String placaCarro;
    private String cor;
    private String modelo;
    private String marca;

    //atributo associativo com a classe Cliente, pois todo veículo deve ter um cliente
    private Cliente dono;

    //construtor
    public Veiculo(String placaCarro, String cor, String modelo,
                   String marca, Cliente dono) {
        this.placaCarro = placaCarro;
        this.cor = cor;
        this.modelo = modelo;
        this.marca = marca;
        this.dono = dono;
    }

    //getter e setters
    public String getPlacaCarro() {
        return placaCarro;
    }

    public void setPlacaCarro(String placaCarro) {
        this.placaCarro = placaCarro;
    }

    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    public String getCor() {
        return cor;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }
}
