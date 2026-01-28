package model;

public class Utiliza {

    private Peca peca;
    private int quantidade;
    private double precoUnitario;

    public Utiliza(Peca peca, int quantidade, double precoUnitario) {
        this.peca = peca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Peca getPeca() {
        return peca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }
}
