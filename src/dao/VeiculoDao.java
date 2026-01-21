package dao;

import model.Cliente;
import model.Veiculo;

public class VeiculoDao extends DaoGenerico<Veiculo> {

    //nome do arquivo que sera criado na pasta data/
    public VeiculoDao() {
        super("veiculos.txt");
    }

    @Override
    public String toCSV(Veiculo v) {
        String cpfDono = "000.000.000-00";

        //verifica se o carro tem dono
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

        Cliente dono = new Cliente(cpfDono);

        return new Veiculo(placa, cor, modelo, marca, dono);
    }
}
