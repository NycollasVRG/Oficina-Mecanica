package dao;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class CompraDao extends DaoGenerico<Compra> {

    public CompraDao() {
        super("compras.csv");
    }

    @Override
    public String toCSV(Compra compra) {
        StringBuilder sb = new StringBuilder();

        sb.append(compra.getData()).append(";");
        sb.append(compra.getFornecedor().getCnpj()).append(";");

        for (Detalha d : compra.getItens()) {
            sb.append(d.getPeca().getNome()).append(",");
            sb.append(d.getQuantidade()).append(",");
            sb.append(d.getPrecoUnitario()).append("|");
        }

        // remove o último "|"
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public Compra fromCSV(String linha) {
        String[] partes = linha.split(";");
        String data = partes[0];
        String cnpjFornecedor = partes[1];

        // fornecedor “fake” só para reconstrução
        Fornecedor fornecedor = new Fornecedor(cnpjFornecedor, "Fornecedor carregado", "N/A");

        List<Detalha> itens = new ArrayList<>();

        String[] itensCSV = partes[2].split("\\|");
        for (String item : itensCSV) {
            String[] dadosItem = item.split(",");

            Peca peca = new Peca(
                    dadosItem[0],
                    Double.parseDouble(dadosItem[2]),
                    0
            );

            Detalha detalha = new Detalha(
                    peca,
                    Integer.parseInt(dadosItem[1]),
                    Double.parseDouble(dadosItem[2])
            );

            itens.add(detalha);
        }

        return new Compra(data, fornecedor, itens);
    }

    @Override
    public String getId(Compra objeto) {
        return "";
    }
}
