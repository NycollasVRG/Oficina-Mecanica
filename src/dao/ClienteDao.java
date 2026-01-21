package dao;

import model.Cliente;

import java.util.List;

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
                c.getCodCliente()
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

    //metodo que vai servir para auxiliar na busca do proximo numero
    private int buscarProximoCodigo(){
        List<Cliente> lista = listar();
        int maiorCod = 0;

        for(Cliente c : lista){
            //verifica qual é o maior Codigo que já existe no arquivo csv
            if(c.getCodCliente() > maiorCod){
                maiorCod = c.getCodCliente();
            }
        }

        //após descobrir o maior Id irá ser somado 1 pois o proximo valor id será o valor "antigo + 1"
        return maiorCod + 1;
    }

    @Override
    public boolean salvar(Cliente c){
        //se o codigo for 0 ou nulo, significa que é um cliente novo
        if(c.getCodCliente() == null || c.getCodCliente() == 0){
            int novoCod = buscarProximoCodigo();
            c.setCodCliente(novoCod);
        }

        //chama o salvar do Dao generico para escrever no arquivo
        return super.salvar(c);
    }
}
