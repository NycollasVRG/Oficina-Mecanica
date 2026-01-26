package model;

public class Detalha {

    private Peca peca;
    private int quantidade;
    private double precoUnitario;

    // construtor
    public Detalha(Peca peca, int quantidade, double precoUnitario) {
        setPeca(peca);
        setQuantidade(quantidade);
        setPrecoUnitario(precoUnitario);
    }

    // getters e setters
    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        if (peca == null) {
            throw new IllegalArgumentException("Erro: O item da compra precisa de uma peça associada.");
        }
        this.peca = peca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro: A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        if (precoUnitario <= 0) {
            throw new IllegalArgumentException("Erro: O preço unitário deve ser maior que zero.");
        }
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return peca.getNome() + " x" + quantidade + " (R$ " + precoUnitario + ")";
    }
}
