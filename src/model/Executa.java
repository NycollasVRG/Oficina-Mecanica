package model;

public class Executa {

    private CatalogoServico servico;
    private double preco;

    public Executa(CatalogoServico servico, double preco) {
        this.servico = servico;
        this.preco = preco;
    }

    public CatalogoServico getServico() {
        return servico;
    }

    public double getPreco() {
        return preco;
    }
}
