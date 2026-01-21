package model;

public class Cliente extends Pessoa{
    //atributos
    private String tipoCliente;
    private Integer codCliente;

    //construtor
    public Cliente(String cpf, String nome,
                   String telefone, String rua,
                   String bairro, String cidade, String numero,
                   String tipoCliente, Integer codCliente) {
        super(cpf, nome, telefone, rua, bairro, cidade, numero);
        this.tipoCliente = tipoCliente;
        this.codCliente = codCliente;
    }

    //construtor criado para satisfazer a relacao de Cliente e Veiculo, para a classe VeiculoDao
    public Cliente(String cpf) {
        super( cpf, "", "", "", "", "", ""); // Chama Pessoa passando CPF e o resto vazio
        this.tipoCliente = "Indefinido"; // Valor padrão para não ficar null
        this.codCliente = 0;             // Valor padrão
    }

    //getters e setters

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        this.codCliente = codCliente;
    }
}
