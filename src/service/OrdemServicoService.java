package service;

import dao.OrdemServicoDao;
import model.Executa;
import model.OrdemServico;
import model.Utiliza;

import java.math.BigDecimal;
import java.util.List;

public class OrdemServicoService {

    private OrdemServicoDao ordemDao;
    private PecaService pecaService;

    public OrdemServicoService() {
        this.ordemDao = new OrdemServicoDao();
        this.pecaService = new PecaService();
    }

    public void abrirOrdemServico(OrdemServico os) {
        os.setStatus("ABERTA");
        ordemDao.salvar(os);
    }

    public OrdemServico buscarPorId(int id) {
        return ordemDao.buscarPorId(String.valueOf(id));
    }

    public List<OrdemServico> listarTodas() {
        return ordemDao.listar();
    }

    public void adicionarPeca(OrdemServico os, Utiliza item) {
        if (!os.getStatus().equals("ABERTA")) {
            throw new IllegalStateException("Erro: Não pode alterar uma OS finalizada.");
        }
        os.getItensPecas().add(item);
        ordemDao.atualizar(os); // Salva a alteração
    }

    public void adicionarServico(OrdemServico os, Executa item) {
        if (!os.getStatus().equals("ABERTA")) {
            throw new IllegalStateException("Erro: Não pode alterar uma OS finalizada.");
        }
        os.getItensServicos().add(item);
        ordemDao.atualizar(os); // Salva a alteração
    }

    public BigDecimal calcularTotal(OrdemServico os) {
        BigDecimal total = BigDecimal.ZERO;

        // Soma peças (Preço * Quantidade)
        for (Utiliza u : os.getItensPecas()) {
            total = total.add(u.getSubtotal());
        }

        // Soma serviços
        for (Executa e : os.getItensServicos()) {
            total = total.add(e.getPrecoPraticado());
        }

        return total;
    }

    public void finalizarOrdem(OrdemServico os) {
        if (!os.getStatus().equals("ABERTA")) {
            throw new IllegalStateException("OS já está finalizada ou cancelada.");
        }
        if (os.getItensPecas().isEmpty() && os.getItensServicos().isEmpty()) {
            throw new IllegalStateException("Não é possível fechar uma OS vazia.");
        }

        System.out.println("Baixando estoque das peças...");
        for (Utiliza item : os.getItensPecas()) {
            try {
                pecaService.removerEstoque(item.getPeca(), item.getQuantidade());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao baixar estoque da peça " + item.getPeca().getNome() + ": " + e.getMessage());
            }
        }

        // Atualiza Status e Salva
        os.setStatus("FINALIZADA");
        ordemDao.atualizar(os);

        System.out.println("Ordem de Serviço #" + os.getNumero() + " finalizada com sucesso!");
        System.out.println("Valor Final: R$ " + calcularTotal(os));
    }

    public void cancelarOrdem(int id) {
        OrdemServico os = buscarPorId(id);

        if (os == null) {
            throw new IllegalArgumentException("Ordem de Serviço não encontrada.");
        }

        // Só pode excluir se ainda não foi finalizada
        // Se já finalizou, as peças já saíram do estoque, então não deixamos apagar.
        if (!os.getStatus().equals("ABERTA")) {
            throw new IllegalStateException("Não é possível excluir uma OS Finalizada. O estoque já foi baixado.");
        }

        boolean sucesso = ordemDao.excluir(String.valueOf(id));
        if (!sucesso) {
            throw new RuntimeException("Erro ao excluir a OS do banco de dados.");
        }
        System.out.println("Ordem de Serviço #" + id + " cancelada e excluída com sucesso.");
    }

    // Remover Peça da Lista (Caso tenha lançado errado)
    public void removerItemPeca(OrdemServico os, Utiliza item) {
        if (!os.getStatus().equals("ABERTA")) {
            throw new IllegalStateException("Erro: Não pode alterar uma OS finalizada.");
        }

        // Remove da lista em memória
        os.getItensPecas().remove(item);

        // Salva a lista atualizada no arquivo
        ordemDao.atualizar(os);
    }
}

