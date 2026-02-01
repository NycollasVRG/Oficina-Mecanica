package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private int numero; // ID
    private LocalDate data;
    private Veiculo veiculo;
    private List<Utiliza> itensPecas;
    private List<Executa> itensServicos;
    private String status;

    public OrdemServico(int numero, LocalDate data, Veiculo veiculo, String status) {
        this.numero = numero;
        this.data = data;
        this.veiculo = veiculo;
        this.status = status;
        this.itensPecas = new ArrayList<>();
        this.itensServicos = new ArrayList<>();
    }
    // Construtor para nova OS
    public OrdemServico(Veiculo veiculo) {
        this(0, LocalDate.now(), veiculo, "ABERTA");
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

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Erro: A data é obrigatória.");
        }
        this.data = data;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        // O Model garante que não existe OS "fantasma" sem carro
        if (veiculo == null) {
            throw new IllegalArgumentException("Erro: A OS precisa estar vinculada a um veículo.");
        }
        this.veiculo = veiculo;
    }

    public List<Utiliza> getItensPecas() {
        return itensPecas;
    }

    public void setItensPecas(List<Utiliza> itensPecas) {
        this.itensPecas = (itensPecas != null) ? itensPecas : new ArrayList<>();
    }

    public List<Executa> getItensServicos() {
        return itensServicos;
    }

    public void setItensServicos(List<Executa> itensServicos) {
        this.itensServicos = (itensServicos != null) ? itensServicos : new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // O Model garante que só entram status válidos
        if (status == null ||
                (!status.equals("ABERTA") && !status.equals("FINALIZADA") && !status.equals("CANCELADA"))) {
            throw new IllegalArgumentException("Erro: Status inválido (Use ABERTA, FINALIZADA ou CANCELADA).");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        // Tenta pegar a placa, mas se o veículo estiver nulo (erro de carga), avisa
        String placa = (veiculo != null) ? veiculo.getPlacaCarro() : "[Sem Veículo]";

        return "OS #" + numero + " | Data: " + data + " | Status: " + status +
                " | Veículo: " + placa +
                " | Qtd Peças: " + itensPecas.size() +
                " | Qtd Serviços: " + itensServicos.size();
    }
}
