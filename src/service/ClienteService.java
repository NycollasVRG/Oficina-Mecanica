package service;

import dao.ClienteDao;
import model.Cliente;

import java.util.List;

public class ClienteService {

    private ClienteDao dao = new ClienteDao();

    //busca um cliente pelo CPF
    public Cliente buscarPorCPF(String cpf){
        List<Cliente> clientes = dao.listar();

        for(Cliente c : clientes){
            if(c.getCpf().equals(cpf)){
                return c; //no caso aqui o cliente vai ser encontrado
            }
        }
        return null; //não encontrou esse cliente
    }

    //cadastro cliente
    public void cadastrarCliente(Cliente cliente) throws Exception{
        //aqui vai fazer a busca, se ja estiver o cpf cadastrado vai ser lançado uma excecao
        if(buscarPorCPF(cliente.getCpf())!= null){
            throw new Exception("Erro: já existe um cliente com esse CPF!");
        }
        //se passar, vai ser salvo um novo cadastro
        dao.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return dao.listar();
    }

    public void atualizar(Cliente cliente) throws Exception {
        // Regra: O ID (Código) não pode mudar, pois é ele que o DAO usa para achar quem substituir
        if (cliente.getCodCliente() == null || cliente.getCodCliente() == 0) {
            throw new Exception("Erro: Não é possível atualizar um cliente que não tem Código (ID).");
        }

        boolean sucesso = dao.atualizar(cliente);
        if (!sucesso) {
            throw new Exception("Erro: Cliente não encontrado para atualização.");
        }
    }

    public void excluir(Integer codCliente) throws Exception {
        // Regra: Verificar se o cliente existe antes
        // Aqui convertemos int para String porque seu DaoGenerico usa String no ID
        boolean sucesso = dao.excluir(String.valueOf(codCliente));

        if (!sucesso) {
            throw new Exception("Erro: Não foi possível excluir. Cliente não encontrado.");
        }
    }

}
