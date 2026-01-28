package service;

import dao.PagamentoDao;
import model.OrdemServico;
import model.Pagamento;

public class PagamentoService {

    private PagamentoDao pagamentoDao = new PagamentoDao();

    public boolean registrarPagamento(OrdemServico os, String formaPagamento) {

        if (os == null) {
            throw new IllegalArgumentException("Ordem de serviço inválida.");
        }

        Pagamento pagamento = new Pagamento(
                os,
                formaPagamento,
                os.getTotal()
        );

        return pagamentoDao.salvar(pagamento);
    }
}
