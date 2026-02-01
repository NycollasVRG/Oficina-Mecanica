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

    public Peca buscarPorId(int id) {
        return pecaDao.buscarPorId(String.valueOf(id));
    }

    public boolean atualizarPeca(Peca peca) {
        return pecaDao.atualizar(peca);
    }

    public boolean removerPeca(int id) {
        // Busca a peça para ver o estoque atual
        Peca peca = buscarPorId(id);

        if (peca != null && peca.getQuantidadeEstoque() > 0) {
            System.out.println("Erro: Não é possível remover uma peça que ainda possui estoque físico (" + peca.getQuantidadeEstoque() + ").");
            return false; // Bloqueia a exclusão
        }

        // Se estoque for 0, permite excluir
        return pecaDao.excluir(String.valueOf(id));
    }

    public void adicionarEstoque(Peca peca, int quantidade) {
        // Validação
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro: A quantidade para adicionar deve ser maior que zero.");
        }

        int estoqueAtual = peca.getQuantidadeEstoque();
        int novoEstoque = estoqueAtual + quantidade;

        peca.setQuantidadeEstoque(novoEstoque);

        boolean sucesso = pecaDao.atualizar(peca);

        if (!sucesso) {
            throw new RuntimeException("Erro ao atualizar o banco de dados para a peça: " + peca.getNome());
        }
    }

    public void removerEstoque(Peca peca, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro: A quantidade para remover deve ser maior que zero.");
        }

        int estoqueAtual = peca.getQuantidadeEstoque();

        if (quantidade > estoqueAtual) {
            throw new IllegalArgumentException("Erro: Estoque insuficiente. Atual: " + estoqueAtual + ", Solicitado: " + quantidade);
        }

        int novoEstoque = estoqueAtual - quantidade;

        peca.setQuantidadeEstoque(novoEstoque);

        boolean sucesso = pecaDao.atualizar(peca);

        if (!sucesso) {
            throw new RuntimeException("Erro ao atualizar o banco de dados para a peça: " + peca.getNome());
        }
    }

}
