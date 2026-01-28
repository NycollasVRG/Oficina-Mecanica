package service;

import dao.CatalogoServicoDao;
import model.CatalogoServico;

import java.util.List;

public class CatalogoServicoService {

    private CatalogoServicoDao dao = new CatalogoServicoDao();

    // CREATE
    public boolean cadastrarServico(String descricao, double preco) {
        CatalogoServico servico = new CatalogoServico(descricao, preco);
        return dao.salvar(servico);
    }

    // READ
    public List<CatalogoServico> listarServicos() {
        return dao.listar();
    }

    // UPDATE
    public boolean atualizarPreco(CatalogoServico servico, double novoPreco) {
        servico.setPreco(novoPreco);
        return true;
    }

    // DELETE
    public boolean removerServico(CatalogoServico servico) {
        return dao.remover(servico);
    }
}
