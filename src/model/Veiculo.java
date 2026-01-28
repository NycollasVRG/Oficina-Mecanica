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
        setPlacaCarro(placaCarro);
        setCor(cor);
        setModelo(modelo);
        setMarca(marca);
        setDono(dono);
    }

    // 🔧 Construtor auxiliar para uso do DAO (leitura do CSV)
    public Veiculo(String placaCarro) {
        this.placaCarro = placaCarro;
        this.cor = "Desconhecida";
        this.modelo = "Desconhecido";
        this.marca = "Desconhecida";
        this.dono = new Cliente("00000000000");
    }

    //getter e setters
    public String getPlacaCarro() {
        return placaCarro;
    }

    public void setPlacaCarro(String placaCarro) {
        if (placaCarro == null || !placaCarro.toUpperCase().matches("[A-Z]{3}[-]?[0-9][A-Z0-9][0-9]{2}")) {
            throw new IllegalArgumentException("Erro: A placa '" + placaCarro + "' é inválida.");
        }
        this.placaCarro = placaCarro;
    }

    public Cliente getDono() {
        return dono;
    }

    public void setDono(Cliente dono) {
        //Impede se o carro não tenha dono
        if (dono == null) {
            throw new IllegalArgumentException("Erro: Todo veículo precisa de um dono associado.");
        }
        this.dono = dono;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        if (cor == null || cor.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A cor do veículo é obrigatória.");
        }
        this.cor = cor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if(modelo == null || modelo.trim().isEmpty()){
            throw new IllegalArgumentException("Erro: O modelo do veículo é obrigatório.");
        }
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if(marca == null || marca.trim().isEmpty()){
            throw new IllegalArgumentException("Erro: A marca do veículo é obrigatória.");
        }
        this.marca = marca;
    }

    @Override
    public String toString() {
        return modelo + " " + cor + " (" + placaCarro + ")";
    }
}
