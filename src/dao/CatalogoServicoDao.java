package dao;

import model.CatalogoServico;

import java.util.ArrayList;
import java.util.List;

public class CatalogoServicoDao {

    private static List<CatalogoServico> servicos = new ArrayList<>();

    public boolean salvar(CatalogoServico servico) {
        servicos.add(servico);
        return true;
    }

    public List<CatalogoServico> listar() {
        return servicos;
    }

    public boolean remover(CatalogoServico servico) {
        return servicos.remove(servico);
    }
}
