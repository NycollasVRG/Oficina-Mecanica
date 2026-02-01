package service;

import dao.PagamentoDao;
import model.OrdemServico;
import model.Pagamento;
import java.math.BigDecimal;
import java.util.List;

public class PagamentoService {

    private PagamentoDao pagamentoDao;
    private OrdemServicoService osService;

    public PagamentoService() {
        this.pagamentoDao = new PagamentoDao();
        this.osService = new OrdemServicoService();
    }

    // --- CREATE (Registrar) ---
    public void registrarPagamento(OrdemServico os, String formaPagamento) {
        if (os == null) {
            throw new IllegalArgumentException("Erro: Ordem de serviço inválida.");
        }

        if (!os.getStatus().equals("FINALIZADA")) {
            throw new IllegalStateException("Erro: A OS precisa estar FINALIZADA para receber pagamento.");
        }

        if (jaFoiPaga(os.getNumero())) {
            throw new IllegalStateException("Erro: Esta Ordem de Serviço já possui pagamento registrado.");
        }

        BigDecimal totalCalculado = osService.calcularTotal(os);

        if (totalCalculado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: Valor do pagamento inválido (R$ " + totalCalculado + ").");
        }

        Pagamento novoPagamento = new Pagamento(
                os,
                formaPagamento,
                totalCalculado
        );

        boolean sucesso = pagamentoDao.salvar(novoPagamento);

        if (!sucesso) {
            throw new RuntimeException("Erro crítico ao salvar o pagamento.");
        }

        System.out.println("Pagamento registrado com sucesso! Valor: R$ " + totalCalculado);
    }

    // --- READ (Listar e Buscar) ---
    public List<Pagamento> listarPagamentos() {
        return pagamentoDao.listar();
    }

    public boolean jaFoiPaga(int numeroOS) {
        List<Pagamento> todos = pagamentoDao.listar();
        for (Pagamento p : todos) {
            if (p.getOrdemServico().getNumero() == numeroOS) {
                return true;
            }
        }
        return false;
    }

    // --- UPDATE (Atualizar) ---
    public void corrigirFormaPagamento(int idPagamento, String novaForma) {
        Pagamento pag = pagamentoDao.buscarPorId(String.valueOf(idPagamento));
        if (pag == null) throw new IllegalArgumentException("Pagamento não encontrado.");

        pag.setFormaPagamento(novaForma); // O set já valida se é nulo/vazio

        if (!pagamentoDao.atualizar(pag)) {
            throw new RuntimeException("Erro ao atualizar o registro.");
        }
        System.out.println("✅ Pagamento atualizado com sucesso.");
    }

    // --- DELETE (Excluir/Estornar) ---
    public void estornarPagamento(int idPagamento) {
        if (!pagamentoDao.excluir(String.valueOf(idPagamento))) {
            throw new RuntimeException("Erro ao excluir. Pagamento não encontrado.");
        }
        System.out.println("Pagamento estornado (excluído) com sucesso.");
    }
}