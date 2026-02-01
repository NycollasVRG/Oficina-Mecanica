package model;

import java.math.BigDecimal;

public class CatalogoServico {

    private int idCatalogoServico;
    private String descricao;
    private BigDecimal preco;

    public CatalogoServico(int id, String descricao, BigDecimal preco) {
        this.idCatalogoServico = id;
        setDescricao(descricao);
        setPreco(preco);
    }

    // Construtor sem ID (para novos cadastros)
    public CatalogoServico(String descricao, BigDecimal preco) {
        this(0, descricao, preco);
    }

    public int getId() {
        return idCatalogoServico;
    }

    public void setId(int idCatalogoServico) {
        this.idCatalogoServico = idCatalogoServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: Descrição do serviço é obrigatória.");
        }
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Erro: O preço do serviço não pode ser negativo.");
        }
        this.preco = preco;
    }

    @Override
    public String toString() {
        return idCatalogoServico + " - " + descricao + " (R$ " + preco + ")";
    }

}
