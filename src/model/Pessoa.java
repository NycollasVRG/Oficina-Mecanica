package model;

public abstract class Pessoa {
    //atributos
    private String cpf;
    private String nome;
    private String telefone;
    private String rua;
    private String bairro;
    private String cidade;
    private String numero;

    //construtor
    public Pessoa(String cpf, String nome,
                  String telefone, String rua,
                  String bairro, String cidade, String numero) {
        setCpf(cpf);
        setNome(nome);
        setTelefone(telefone);
        setRua(rua);
        setBairro(bairro);
        setCidade(cidade);
        setNumero(numero);
    }

    //getters e setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        //Regra do cpf ter 11 numeros ou estar no formato 000.000.000-00
        String apenasNumeros = cpf != null ? cpf.replaceAll("[^0-9]", "") : "";

        if (apenasNumeros.length() != 11){
            throw new IllegalArgumentException("Erro: O CPF deve conter 11 dígitos");
        }
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        //Deve ter um nome
        if(nome == null || nome.trim().length() < 3){
            throw new IllegalArgumentException("Erro: O nome dever ter no mínimo 3 letras");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        //no telefone deve ter um tamanho minimo
        if(telefone == null || telefone.length() < 8){
            throw new IllegalArgumentException("Erro: Telefone inválido");
        }
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return this.nome + " (" + this.cpf + ")";
    }
}
