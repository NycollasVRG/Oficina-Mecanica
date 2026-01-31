package model;

import java.math.BigDecimal;

public class Detalha {

    private Peca peca;
    private int quantidade;
    private BigDecimal precoUnitario;

    // construtor
    public Detalha(Peca peca, int quantidade, BigDecimal precoUnitario) {
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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: O preço unitário deve ser maior que zero.");
        }
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return peca.getNome() + " x" + quantidade + " (R$ " + precoUnitario + ")";
    }
}
