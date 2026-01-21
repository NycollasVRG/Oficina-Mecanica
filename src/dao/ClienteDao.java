package dao;

import model.Cliente;

public class ClienteDao extends DaoGenerico<Cliente>{
    public ClienteDao() {
        //informa para o pai o nome do arquivo
        super("clientes.txt");
    }

    @Override
    //ensina a transformar Cliente(classe) em String
    public String toCSV(Cliente c) {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%d",
                c.getCpf(),
                c.getNome(),
                c.getTelefone(),
                c.getRua(),
                c.getBairro(),
                c.getCidade(),
                c.getNumero(),
                c.getTipoCliente(),
                0
        );
    }

    @Override
    public Cliente fromCSV(String linha) {
        //ensina como transformar String em Cliente
        String[] dados = linha.split(";");

        //a ordem deve ser EXATAMENTE a mesma do toCSV
        return new Cliente(
                dados[0], // CPF
                dados[1], // Nome
                dados[2], // Telefone
                dados[3], // Rua
                dados[4], // Bairro
                dados[5], // Cidade
                dados[6], // Numero
                dados[7], // Tipo Cliente
                Integer.parseInt(dados[8]) // Codigo
        );
    }
}
