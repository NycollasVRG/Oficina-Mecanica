package dao;

import model.Peca;

import java.math.BigDecimal;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

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
        );
    }

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

    // =========================================================================
    // LÓGICA DE CONTROLE DE ESTOQUE
    // =========================================================================

    // 1. DAR BAIXA (Venda / Uso na OS)
    public void baixarEstoque(int idPeca, int qtdBaixa) {
        List<Peca> lista = listar(); // Carrega tudo para a memória
        boolean alterou = false;

        for (Peca p : lista) {
            if (p.getIdPeca() == idPeca) {
                int novoEstoque = p.getQuantidadeEstoque() - qtdBaixa;

                // Garante que o estoque não fique negativo
                if (novoEstoque < 0) novoEstoque = 0;

                p.setQuantidadeEstoque(novoEstoque);
                alterou = true;
                break;
            }
        }

        if (alterou) {
            reescreverArquivo(lista);
        }
    }

    // 2. ADICIONAR / DEVOLVER (Cancelamento ou Edição)
    public void adicionarEstoque(int idPeca, int qtdDevolucao) {
        List<Peca> lista = listar();
        boolean alterou = false;

        for (Peca p : lista) {
            if (p.getIdPeca() == idPeca) {
                int novoEstoque = p.getQuantidadeEstoque() + qtdDevolucao;
                p.setQuantidadeEstoque(novoEstoque);
                alterou = true;
                break;
            }
        }

        if (alterou) {
            reescreverArquivo(lista);
        }
    }

    // Método privado para limpar o arquivo e salvar a lista atualizada
    private void reescreverArquivo(List<Peca> lista) {
        try {
            PrintWriter writer = new PrintWriter("data/pecas.csv");
            writer.print(""); // Limpa o conteúdo do arquivo
            writer.close();

            // Salva item por item novamente
            for (Peca p : lista) {
                salvar(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}