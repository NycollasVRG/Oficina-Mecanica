package service;

import dao.FuncionarioDao;
import dao.ClienteDao;
import model.Funcionario;
import model.Cliente;

import java.time.LocalDate; // Importante para pegar a data de hoje
import java.util.List;

public class FuncionarioService {

    private FuncionarioDao dao = new FuncionarioDao();
    private ClienteDao clienteDao = new ClienteDao();

    public Funcionario buscarPorCPF(String cpf){
        List<Funcionario> funcionarios = dao.listar();

        for(Funcionario f : funcionarios){
            if(f.getCpf().equals(cpf)){
                return f;
            }
        }
        return null;
    }

    public void cadastrar(Funcionario f) throws Exception {
        // --- NOVA VALIDAÇÃO DE DATA ---
        // Verifica se a data de admissão é nula ou se está no futuro
        if (f.getDataAdmissao() == null) {
            throw new Exception("Erro: A data de admissão é obrigatória.");
        }

        if (f.getDataAdmissao().isAfter(LocalDate.now())) {
            throw new Exception("Erro: A data de admissão não pode ser futura (" + f.getDataAdmissao() + "). Hoje é: " + LocalDate.now());
        }
        // ------------------------------

        // 1. Verifica se já existe na lista de FUNCIONÁRIOS
        if (buscarPorCPF(f.getCpf()) != null) {
            throw new Exception("Erro: Já existe um funcionário com este CPF: " + f.getCpf());
        }

        // 2. Verifica se já existe na lista de CLIENTES
        List<Cliente> clientes = clienteDao.listar();
        for (Cliente c : clientes) {
            if (c.getCpf().equals(f.getCpf())) {
                throw new Exception("Erro: Este CPF já está cadastrado como Cliente! Não é possível duplicar.");
            }
        }

        dao.salvar(f);
    }

    public List<Funcionario> listarFuncionarios() {
        return dao.listar();
    }

    public void atualizar(Funcionario funcionario) throws Exception {
        if (funcionario.getMatricula() == null || funcionario.getMatricula() == 0) {
            throw new Exception("Erro: Funcionário sem matrícula não pode ser atualizado.");
        }

        if (funcionario.getDataAdmissao() != null && funcionario.getDataAdmissao().isAfter(LocalDate.now())) {
            throw new Exception("Erro: A nova data de admissão não pode ser futura.");
        }

        boolean sucesso = dao.atualizar(funcionario);
        if (!sucesso) {
            throw new Exception("Erro: Funcionário não encontrado para atualização.");
        }
    }

    public void excluir(Integer matricula) throws Exception {
        boolean sucesso = dao.excluir(String.valueOf(matricula));

        if (!sucesso) {
            throw new Exception("Erro: Não foi possível excluir. Funcionário não encontrado.");
        }
    }
}