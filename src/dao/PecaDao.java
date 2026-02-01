package dao;

import model.Peca;

import java.math.BigDecimal;
import java.util.List;

public class PecaDao extends DaoGenerico<Peca> {

    public PecaDao() {
        super("pecas.csv");
    }

    @Override
    public String toCSV(Peca p) {
        // Se a peça for nova (ID 0), gera um ID novo antes de salvar string
        if (p.getIdPeca() == 0) {
            p.setId(gerarProximoId());
        }

        // Formato: ID;Nome;Preco;Estoque
        return p.getIdPeca() + ";" +
                p.getNome() + ";" +
                p.getPreco().toString() + ";" + // BigDecimal para String
                p.getQuantidadeEstoque();
    }

    @Override
    public Peca fromCSV(String linha) {
        String[] dados = linha.split(";");
        // Reconstrói o objeto convertendo String para os tipos corretos
        return new Peca(
                Integer.parseInt(dados[0]),      // ID
                dados[1],                        // Nome
                new BigDecimal(dados[2]),        // Preco (BigDecimal)
                Integer.parseInt(dados[3])       // Estoque
        );    }

    @Override
    public String getId(Peca objeto) {
        return String.valueOf(objeto.getIdPeca());
    }

    // Método auxiliar para gerar ID automático (Auto-Incremento manual)
    public int gerarProximoId() {
        List<Peca> lista = listar();
        int maiorId = 0;
        for (Peca p : lista) {
            if (p.getIdPeca() > maiorId) {
                maiorId = p.getIdPeca();
            }
        }
        return maiorId + 1;
    }

}
