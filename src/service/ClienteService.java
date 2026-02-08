package service;

import dao.ClienteDao;
import dao.FuncionarioDao; // Importe o DAO de funcionário
import model.Cliente;
import model.Funcionario; // Importe o Model de funcionário

import java.util.List;

public class ClienteService {

    private ClienteDao dao = new ClienteDao();
    private FuncionarioDao funcionarioDao = new FuncionarioDao(); // AQUI ESTÁ A MÁGICA

    //busca um cliente pelo CPF
    public Cliente buscarPorCPF(String cpf){
        List<Cliente> clientes = dao.listar();

        for(Cliente c : clientes){
            if(c.getCpf().equals(cpf)){
                return c;
            }
        }
        return null;
    }

    //cadastro cliente
    public void cadastrarCliente(Cliente cliente) throws Exception{
        // Verifica se já existe na lista de CLIENTES
        if(buscarPorCPF(cliente.getCpf())!= null){
            throw new Exception("Erro: já existe um cliente com esse CPF!");
        }

        // Verifica se já existe na lista de FUNCIONÁRIOS
        List<Funcionario> funcionarios = funcionarioDao.listar();
        for (Funcionario f : funcionarios) {
            if (f.getCpf().equals(cliente.getCpf())) {
                throw new Exception("Erro: Este CPF já está cadastrado como Funcionário! Não é possível duplicar.");
            }
        }

        //se passar nas duas verificações, salva
        dao.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return dao.listar();
    }

    public void atualizar(Cliente cliente) throws Exception {
        if (cliente.getCodCliente() == null || cliente.getCodCliente() == 0) {
            throw new Exception("Erro: Não é possível atualizar um cliente que não tem Código (ID).");
        }

        boolean sucesso = dao.atualizar(cliente);
        if (!sucesso) {
            throw new Exception("Erro: Cliente não encontrado para atualização.");
        }
    }

    public void excluir(Integer codCliente) throws Exception {
        boolean sucesso = dao.excluir(String.valueOf(codCliente));

        if (!sucesso) {
            throw new Exception("Erro: Não foi possível excluir. Cliente não encontrado.");
        }
    }
}