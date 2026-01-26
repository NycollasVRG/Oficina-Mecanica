package service;

import dao.PecaDao;
import model.Peca;

import java.util.List;

public class PecaService {

    private PecaDao pecaDao = new PecaDao();

    public boolean cadastrarPeca(Peca peca) {
        return pecaDao.salvar(peca);
    }

    public List<Peca> listarPecas() {
        return pecaDao.listar();
    }

    // ENTRADA de estoque (Compra)
    public void adicionarEstoque(Peca peca, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro: Quantidade inválida para entrada de estoque.");
        }
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() + quantidade);
        pecaDao.salvar(peca);
    }

    // SAÍDA de estoque (Ordem de Serviço - Membro 3)
    public void removerEstoque(Peca peca, int quantidade) {
        if (quantidade <= 0 || quantidade > peca.getQuantidadeEstoque()) {
            throw new IllegalArgumentException("Erro: Estoque insuficiente.");
        }
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - quantidade);
        pecaDao.salvar(peca);
    }
}
