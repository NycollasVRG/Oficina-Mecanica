package model;

public class Fornecedor {

    private String cnpj;
    private String nome;
    private String telefone;

    // construtor
    public Fornecedor(String cnpj, String nome, String telefone) {
        setCnpj(cnpj);
        setNome(nome);
        setTelefone(telefone);
    }

    // getters e setters
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O CNPJ do fornecedor é obrigatório.");
        }
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O nome do fornecedor é obrigatório.");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O telefone do fornecedor é obrigatório.");
        }
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome + " (" + cnpj + ")";
    }
}
