package model;

import java.time.LocalDate;

public class Pagamento {

    private OrdemServico ordemServico;
    private String formaPagamento; // dinheiro, cartão, pix
    private double valorPago;
    private LocalDate dataPagamento;

    public Pagamento(OrdemServico ordemServico, String formaPagamento, double valorPago) {
        setOrdemServico(ordemServico);
        setFormaPagamento(formaPagamento);
        setValorPago(valorPago);
        this.dataPagamento = LocalDate.now();
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        if (ordemServico == null) {
            throw new IllegalArgumentException("Pagamento precisa estar vinculado a uma OS.");
        }
        this.ordemServico = ordemServico;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.isBlank()) {
            throw new IllegalArgumentException("Forma de pagamento inválida.");
        }
        this.formaPagamento = formaPagamento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        if (valorPago <= 0) {
            throw new IllegalArgumentException("Valor pago inválido.");
        }
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }
}
