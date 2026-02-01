package model;

import java.math.BigDecimal;

public class Utiliza {

    private Peca peca;
    private int quantidade;
    private BigDecimal precoUnitario; // Preço de VENDA na hora da OS

    public Utiliza(Peca peca, int quantidade, BigDecimal precoUnitario) {
        if (peca == null) throw new IllegalArgumentException("Peça inválida.");
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva.");

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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}
