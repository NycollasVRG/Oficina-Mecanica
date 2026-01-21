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
        return String.format("%s;%s;%s;%s;%d",
                c.getNome(),
                c.getCpf(),
                c.getTelefone(),
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
                dados[0], // Nome
                dados[1], // CPF
                dados[2], // Telefone
                dados[3], // Tipo
                Integer.parseInt(dados[4]) // Codigo
        );
    }
}
