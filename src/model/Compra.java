package model;

import java.util.List;

public class Compra {

    private String data;
    private Fornecedor fornecedor;
    private List<Detalha> itens;

    // construtor
    public Compra(String data, Fornecedor fornecedor, List<Detalha> itens) {
        setData(data);
        setFornecedor(fornecedor);
        setItens(itens);
    }

    // getters e setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: A data da compra é obrigatória.");
        }
        this.data = data;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Erro: A compra precisa de um fornecedor.");
        }
        this.fornecedor = fornecedor;
    }

    public List<Detalha> getItens() {
        return itens;
    }

    public void setItens(List<Detalha> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Erro: A compra precisa ter pelo menos um item.");
        }
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Compra em " + data + " | Fornecedor: " + fornecedor.getNome() +
                " | Itens: " + itens.size();
    }
}
