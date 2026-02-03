package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private int numero; // ID
    private LocalDate data;
    private Veiculo veiculo;
    private Funcionario responsavel;
    private String descricao;
    private String status;

    private List<Utiliza> itensPecas;
    private List<Executa> itensServicos;

    // Construtor completo
    public OrdemServico(int numero, LocalDate data, Veiculo veiculo,
                        Funcionario responsavel, String descricao, String status) {

        this.numero = numero;
        this.data = data;
        this.veiculo = veiculo;
        this.responsavel = responsavel;
        this.descricao = descricao;
        this.status = status;

        this.itensPecas = new ArrayList<>();
        this.itensServicos = new ArrayList<>();
    }

    // Construtor para nova OS (tela)
    public OrdemServico(Veiculo veiculo, Funcionario responsavel, String descricao) {
        this(0, LocalDate.now(), veiculo, responsavel, descricao, "ABERTA");
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getData() {
        return data;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Funcionario getResponsavel() {
        return responsavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("ABERTA") &&
                !status.equals("FINALIZADA") &&
                !status.equals("CANCELADA")) {
            throw new IllegalArgumentException("Status inválido");
        }
        this.status = status;
    }

    public List<Utiliza> getItensPecas() {
        return itensPecas;
    }

    public void setItensPecas(List<Utiliza> itensPecas) {
        this.itensPecas = itensPecas;
    }

    public List<Executa> getItensServicos() {
        return itensServicos;
    }

    public void setItensServicos(List<Executa> itensServicos) {
        this.itensServicos = itensServicos;
    }

    @Override
    public String toString() {
        return "OS #" + numero +
                " | Data: " + data +
                " | Veículo: " + veiculo.getPlacaCarro() +
                " | Resp: " + responsavel.getNome() +
                " | Status: " + status;
    }
}
