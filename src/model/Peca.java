package model;

import java.math.BigDecimal;

public class Peca {

    private int idPeca;
    private String nome;
    private BigDecimal preco;
    private int quantidadeEstoque;

    // construtor

    public Peca(int idPeca, String nome, BigDecimal preco, int quantidadeEstoque) {
        this.idPeca = idPeca;
        setNome(nome);
        setPreco(preco);
        setQuantidadeEstoque(quantidadeEstoque);
    }

    // Construtor sem ID (útil para quando for criar uma peça nova que ainda não foi pro banco)
    public Peca(String nome, BigDecimal preco, int quantidadeEstoque) {
        this(0, nome, preco, quantidadeEstoque);
    }

    // getters e setters

    public int getIdPeca() {
        return idPeca;
    }

    public void setId(int idPeca) {
        this.idPeca = idPeca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O nome da peça é obrigatório.");
        }
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Erro: O preço da peça deve ser maior que zero.");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Erro: A quantidade em estoque não pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    @Override
    public String toString() {
        return nome + " | R$ " + preco + " | Estoque: " + quantidadeEstoque;
    }
}
