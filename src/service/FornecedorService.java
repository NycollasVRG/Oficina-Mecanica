package service;

import dao.FornecedorDao;
import model.Fornecedor;

import java.util.List;

public class FornecedorService {

    private FornecedorDao fornecedorDao = new FornecedorDao();

    public boolean cadastrarFornecedor(Fornecedor fornecedor) {
        return fornecedorDao.salvar(fornecedor);
    }

    public List<Fornecedor> listarFornecedores() {
        return fornecedorDao.listar();
    }
}
