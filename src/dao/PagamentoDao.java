package dao;

import model.OrdemServico;
import model.Pagamento;
import model.Veiculo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PagamentoDao extends DaoGenerico<Pagamento> {

    // Dependência vital: Precisamos buscar a OS pelo ID dela
    private OrdemServicoDao osDao;

    public PagamentoDao() {
        super("pagamentos.csv");
        this.osDao = new OrdemServicoDao();
    }

    private int gerarProximoId() {
        List<Pagamento> lista = listar();
        int maiorId = 0;
        for (Pagamento p : lista) {
            if (p.getId() > maiorId) {
                maiorId = p.getId();
            }
        }
        return maiorId + 1;
    }

    @Override
    public String toCSV(Pagamento p) {
        // Gera ID se for novo
        if (p.getId() == 0) {
            p.setId(gerarProximoId());
        }

        // Formato: ID;ID_OS;FormaPagamento;Valor;Data
        return p.getId() + ";" +
                p.getOrdemServico().getNumero() + ";" +
                p.getFormaPagamento() + ";" +
                p.getValorPago() + ";" +
                p.getDataPagamento();
    }

    @Override
    public Pagamento fromCSV(String linha) {
        String[] dados = linha.split(";");

        // Recupera dados primitivos
        int id = Integer.parseInt(dados[0]);
        int idOs = Integer.parseInt(dados[1]); // O número da OS
        String forma = dados[2];
        BigDecimal valor = new BigDecimal(dados[3]);
        LocalDate data = LocalDate.parse(dados[4]);

        // BUSCA A ORDEM DE SERVIÇO REAL
        OrdemServico os = osDao.buscarPorId(String.valueOf(idOs));

        // Fallback de segurança: Se a OS foi apagada do arquivo, cria uma fake para não dar erro
        if (os == null) {
            // Cria um veículo fake e uma OS fake só para o pagamento não ficar órfão na memória
            Veiculo veiculoFake = new Veiculo("N/A");
            os = new OrdemServico(idOs, data, veiculoFake, null, "DESCONHECIDA", "N/A");
        }

        // Retorna o pagamento reconstruído
        return new Pagamento(id, os, forma, valor, data);
    }

    @Override
    public String getId(Pagamento p) {
        return String.valueOf(p.getId());
    }
}
