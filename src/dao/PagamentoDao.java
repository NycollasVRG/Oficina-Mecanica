package dao;

import model.Pagamento;

public class PagamentoDao extends DaoGenerico<Pagamento> {

    public PagamentoDao() {
        super("pagamentos.csv");
    }

    @Override
    public String toCSV(Pagamento p) {
        return p.getOrdemServico().getNumero() + ";" +
                p.getFormaPagamento() + ";" +
                p.getValorPago() + ";" +
                p.getDataPagamento();
    }

    @Override
    public Pagamento fromCSV(String linha) {
        throw new UnsupportedOperationException(
                "Reconstrução completa não necessária para o projeto"
        );
    }
}
