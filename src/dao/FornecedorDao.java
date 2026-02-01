package dao;

import model.Fornecedor;

public class FornecedorDao extends DaoGenerico<Fornecedor> {

    public FornecedorDao() {
        super("fornecedores.csv");
    }

    @Override
    public String toCSV(Fornecedor f) {
        return f.getCnpj() + ";" + f.getNome() + ";" + f.getTelefone();
    }

    @Override
    public Fornecedor fromCSV(String linha) {
        String[] dados = linha.split(";");
        return new Fornecedor(
                dados[0], // CNPJ
                dados[1], // Nome
                dados[2] // Telefone
        );
    }

    @Override
    public String getId(Fornecedor objeto) {
        return objeto.getCnpj();
    }
}
