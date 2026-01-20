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
