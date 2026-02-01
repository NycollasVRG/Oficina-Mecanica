package service;

import dao.CompraDao;
import dao.FornecedorDao;
import model.Compra;
import model.Fornecedor;

import java.util.List;

public class FornecedorService {

    private FornecedorDao fornecedorDao = new FornecedorDao();
    private CompraDao compraDao = new CompraDao();

    public boolean cadastrarFornecedor(Fornecedor fornecedor) {
        // Validação: Antes de salvar, verifica se o CNPJ já existe
        if (buscarPorCnpj(fornecedor.getCnpj()) != null) {
            System.out.println("Erro: Fornecedor já cadastrado com este CNPJ.");
            return false;
        }
        return fornecedorDao.salvar(fornecedor);
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        return fornecedorDao.buscarPorId(cnpj);
    }

    public List<Fornecedor> listarFornecedores() {
        return fornecedorDao.listar();
    }

    public boolean atualizarFornecedor(Fornecedor fornecedor) {
        return fornecedorDao.atualizar(fornecedor);
    }

    public boolean removerFornecedor(String cnpj) {
        // Verifica se o fornecedor existe nas compras
        List<Compra> todasCompras = compraDao.listar();

        for (Compra c : todasCompras) {
            if (c.getFornecedor().getCnpj().equals(cnpj)) {
                System.out.println("Erro: Não é possível remover este fornecedor pois ele possui compras registradas.");
                return false; // Bloqueia a exclusão
            }
        }

        // Se passou, exclui
        return fornecedorDao.excluir(cnpj);
    }

}
