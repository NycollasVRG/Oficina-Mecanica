package model;

import java.time.LocalDate;
import java.util.List;

public class Compra {

    private int idCompra;
    private LocalDate data;
    private Fornecedor fornecedor;
    private List<Detalha> itens;

    public Compra(int idCompra, LocalDate data, Fornecedor fornecedor, List<Detalha> itens) {
        this.idCompra = idCompra;
        setData(data);
        setFornecedor(fornecedor);
        setItens(itens);
    }

    // Construtor para nova compra (sem ID ainda)
    public Compra(LocalDate data, Fornecedor fornecedor, List<Detalha> itens) {
        this(0, data, fornecedor, itens);
    }

    // getters e setters


    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Erro: A data da compra é obrigatória.");
        }
        //não permitir compras com data futura, se for regra de negócio
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Erro: A data da compra não pode ser futura.");
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
