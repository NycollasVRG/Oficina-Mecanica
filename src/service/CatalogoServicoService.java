package service;

import dao.CatalogoServicoDao;
import model.CatalogoServico;

import java.math.BigDecimal;
import java.util.List;

public class CatalogoServicoService {

    private CatalogoServicoDao dao = new CatalogoServicoDao();

    // CREATE
    public void cadastrarServico(CatalogoServico servico) {
        // Validação de Negócio
        if (servico.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do serviço deve ser maior que zero.");
        }

        dao.salvar(servico);
    }

    // Sobrecarga útil para a Main (Cria e Salva)
    public void cadastrarServico(String descricao, BigDecimal preco) {
        CatalogoServico novo = new CatalogoServico(descricao, preco);
        cadastrarServico(novo);
    }

    // READ
    public List<CatalogoServico> listarServicos() {
        return dao.listar();
    }

    public CatalogoServico buscarPorId(int id) {
        return dao.buscarPorId(String.valueOf(id));
    }

    // UPDATE
    public void atualizarServico(CatalogoServico servico) {
        // Validação antes de gravar
        if (servico.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo ou zero.");
        }

        boolean sucesso = dao.atualizar(servico);
        if (!sucesso) {
            throw new RuntimeException("Erro ao atualizar o serviço no banco de dados.");
        }
    }

    // DELETE
    public void removerServico(int id) {
        boolean sucesso = dao.excluir(String.valueOf(id));

        if (!sucesso) {
            throw new RuntimeException("Não foi possível excluir. Serviço não encontrado.");
        }
    }
}
