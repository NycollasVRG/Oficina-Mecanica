package model;

import java.util.List;

public class OrdemServico {

    private int numero;
    private Veiculo veiculo;
    private List<Utiliza> pecas;
    private List<Executa> servicos;
    private double total;

    public OrdemServico(int numero, Veiculo veiculo,
                        List<Utiliza> pecas,
                        List<Executa> servicos) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.pecas = pecas;
        this.servicos = servicos;
        this.total = 0;
    }

    public int getNumero() {
        return numero;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public List<Utiliza> getPecas() {
        return pecas;
    }

    public List<Executa> getServicos() {
        return servicos;
    }

    public double getTotal() {
        return total;
    }

        public void setTotal(double total) {
        this.total = total;
    }
}
