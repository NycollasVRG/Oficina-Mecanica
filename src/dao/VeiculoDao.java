package dao;

import model.Cliente;
import model.Veiculo;
import java.util.List;

public class VeiculoDao extends DaoGenerico<Veiculo> {


    private ClienteDao clienteDao;

    public VeiculoDao() {
        super("veiculos.txt");
        this.clienteDao = new ClienteDao(); // Inicializa o DAO de cliente
    }

    @Override
    public String toCSV(Veiculo v) {
        String cpfDono = "000.000.000-00";

        if(v.getDono() != null){
            cpfDono = v.getDono().getCpf();
        }

        return String.format("%s;%s;%s;%s;%s",
                v.getPlacaCarro(),
                v.getCor(),
                v.getModelo(),
                v.getMarca(),
                cpfDono
        );
    }

    @Override
    public Veiculo fromCSV(String linha) {
        String[] dados = linha.split(";");

        String placa = dados[0];
        String cor = dados[1];
        String modelo = dados[2];
        String marca = dados[3];
        String cpfDono = dados[4];


        //Buscamos o cliente real na lista
        Cliente donoReal = null;
        List<Cliente> listaClientes = clienteDao.listar();

        for (Cliente c : listaClientes) {
            // Compara o CPF do arquivo do carro com o CPF dos clientes cadastrados
            if (c.getCpf().equals(cpfDono)) {
                donoReal = c;
                break;
            }
        }

        // 2. Fallback: Se o cliente foi apagado ou não achou, cria o temporário para não dar erro
        if (donoReal == null) {
            donoReal = new Cliente(cpfDono);
        }

        return new Veiculo(placa, cor, modelo, marca, donoReal);
    }

    @Override
    public String getId(Veiculo v) {
        return v.getPlacaCarro();
    }
}