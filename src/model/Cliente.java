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
        setTipoCliente(tipoCliente);
        setCodCliente(codCliente);
    }

    //construtor criado para satisfazer a relacao de Cliente e Veiculo, para a classe VeiculoDao
    public Cliente(String cpf) {
        //para satisfazer as novas regras vai ser passado um nome desconhecido e um telefone falso
        super( cpf, "Cliente Temporario", "00000000", "", "", "", ""); // Chama Pessoa passando CPF e o resto vazio
        this.tipoCliente = "Indefinido"; // Valor padrão para não ficar null
        this.codCliente = 0;             // Valor padrão
    }

    //getters e setters

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        if (tipoCliente == null || tipoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro: O tipo de cliente não pode ser vazio.");
        }
        this.tipoCliente = tipoCliente;
    }

    public Integer getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Integer codCliente) {
        // aceita 0 (novo) ou positivo e não aceita negativo
        if (codCliente == null || codCliente < 0) {
            throw new IllegalArgumentException("Erro: Código do cliente inválido.");
        }
        this.codCliente = codCliente;
    }
}
