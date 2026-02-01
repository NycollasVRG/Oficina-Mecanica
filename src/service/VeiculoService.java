package service;

import dao.VeiculoDao;
import model.Veiculo;
import java.util.List;

public class VeiculoService {

    private VeiculoDao dao = new VeiculoDao();

    // instanciando o Serviço do Cliente pode fazer a consulta se o dono existe
    private ClienteService clienteService = new ClienteService();

    public Veiculo buscarPorPlaca(String placa) {
        List<Veiculo> lista = dao.listar();
        for (Veiculo v : lista) {
            //Ignora letras maiusculas e minusculas na busca da placa
            if (v.getPlacaCarro().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    public void cadastrar(Veiculo v) throws Exception {
        // Placa de ser única
        if (buscarPorPlaca(v.getPlacaCarro()) != null) {
            throw new Exception("Erro: Já existe um veículo com a placa " + v.getPlacaCarro());
        }

        // O carro tem que ter um dono
        if (v.getDono() == null) {
            throw new Exception("Erro: O veículo precisa de um dono vinculado.");
        }

        // Usa o serviço de cliente para verificar se o dono existe
        if (clienteService.buscarPorCPF(v.getDono().getCpf()) == null) {
            throw new Exception("Erro: Não existe nenhum cliente cadastrado com o CPF " + v.getDono().getCpf());
        }

        // Se passou por tudo, salva!
        dao.salvar(v);
    }

    public List<Veiculo> listarVeiculos() {
        return dao.listar();
    }

    public void atualizar(Veiculo veiculo) throws Exception {
        // Valida se o veículo existe
        if (buscarPorPlaca(veiculo.getPlacaCarro()) == null) {
            throw new Exception("Erro: Veículo não encontrado para atualização.");
        }

        // regra para verificar se o carro houver um novo dono ele deve estar cadastrado
        if (clienteService.buscarPorCPF(veiculo.getDono().getCpf()) == null) {
            throw new Exception("Erro: O novo dono informado não existe no cadastro.");
        }

        boolean sucesso = dao.atualizar(veiculo);
        if (!sucesso) {
            throw new Exception("Erro ao atualizar veículo.");
        }
    }

    public void excluir(String placa) throws Exception {
        boolean sucesso = dao.excluir(placa);
        if (!sucesso) {
            throw new Exception("Erro: Veículo não encontrado para exclusão.");
        }
    }

}