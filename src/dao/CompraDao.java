package dao;

import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraDao extends DaoGenerico<Compra> {

    // Dependências para buscar os objetos reais
    private FornecedorDao fornecedorDao;
    private PecaDao pecaDao;

    public CompraDao() {
        super("compras.csv");
        this.fornecedorDao = new FornecedorDao();
        this.pecaDao = new PecaDao();
    }

    // Gerador de ID igual ao da Peça
    public int gerarProximoId() {
        List<Compra> lista = listar();
        int maiorId = 0;
        for (Compra c : lista) {
            if (c.getIdCompra() > maiorId) {
                maiorId = c.getIdCompra();
            }
        }
        return maiorId + 1;
    }

    @Override
    public String toCSV(Compra compra) {
        if (compra.getIdCompra() == 0) {
            compra.setIdCompra(gerarProximoId());
        }

        StringBuilder sb = new StringBuilder();
        // Cabeçalho da Compra: ID;Data;CNPJ_Fornecedor;
        sb.append(compra.getIdCompra()).append(";");
        sb.append(compra.getData().toString()).append(";"); // LocalDate iso (YYYY-MM-DD)
        sb.append(compra.getFornecedor().getCnpj()).append(";");

        // Lista de Itens: ID_Peca,Qtd,Preco|ID_Peca,Qtd,Preco
        for (Detalha d : compra.getItens()) {
            sb.append(d.getPeca().getIdPeca()).append(","); // Salva só o ID da peça
            sb.append(d.getQuantidade()).append(",");
            sb.append(d.getPrecoUnitario()).append("|");
        }

        // Remove o último "|" se houver itens
        if (!compra.getItens().isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    @Override
    public Compra fromCSV(String linha) {
        String[] partes = linha.split(";");

        int id = Integer.parseInt(partes[0]);
        LocalDate data = LocalDate.parse(partes[1]); // Converte String p/ LocalDate
        String cnpjFornecedor = partes[2];

        // BUSCA O FORNECEDOR REAL NO ARQUIVO DELE
        Fornecedor fornecedor = fornecedorDao.buscarPorId(cnpjFornecedor);
        if (fornecedor == null) {
            // Fallback caso o fornecedor tenha sido deletado (segurança)
            fornecedor = new Fornecedor(cnpjFornecedor, "[Removido]", "N/A");
        }

        List<Detalha> itens = new ArrayList<>();

        // Verifica se tem itens (pode estar vazio)
        if (partes.length > 3 && !partes[3].isEmpty()) {
            String[] itensCSV = partes[3].split("\\|");

            for (String itemStr : itensCSV) {
                String[] dadosItem = itemStr.split(",");

                int idPeca = Integer.parseInt(dadosItem[0]);
                int quantidade = Integer.parseInt(dadosItem[1]);
                BigDecimal preco = new BigDecimal(dadosItem[2]);

                // BUSCA A PEÇA REAL
                Peca peca = pecaDao.buscarPorId(String.valueOf(idPeca));
                if (peca == null) {
                    // Fallback
                    peca = new Peca(idPeca, "[Peça Removida]", BigDecimal.ZERO, 0);
                }

                Detalha detalha = new Detalha(peca, quantidade, preco);
                itens.add(detalha);
            }
        }

        return new Compra(id, data, fornecedor, itens);
    }

    @Override
    public String getId(Compra c) {
        return String.valueOf(c.getIdCompra());
    }
}
