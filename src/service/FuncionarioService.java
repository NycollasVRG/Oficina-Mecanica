package service;

import dao.FuncionarioDao;
import model.Funcionario;

import java.util.List;

public class FuncionarioService {

    private FuncionarioDao dao = new FuncionarioDao();

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
        if (buscarPorCPF(f.getCpf()) != null) {
            throw new Exception("Erro: Já existe um funcionário com este CPF: " + f.getCpf());
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

        boolean sucesso = dao.atualizar(funcionario);
        if (!sucesso) {
            throw new Exception("Erro: Funcionário não encontrado para atualização.");
        }
    }

    public void excluir(Integer matricula) throws Exception {
        // Converte Integer para String para o DAO entender
        boolean sucesso = dao.excluir(String.valueOf(matricula));

        if (!sucesso) {
            throw new Exception("Erro: Não foi possível excluir. Funcionário não encontrado.");
        }
    }

}
