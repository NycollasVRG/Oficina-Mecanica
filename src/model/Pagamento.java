package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pagamento {

    private int id; // Adicionado para controle no DAO
    private OrdemServico ordemServico;
    private String formaPagamento; // "DINHEIRO", "PIX", "CARTAO"
    private BigDecimal valorPago;
    private LocalDate dataPagamento;

    public Pagamento(int id, OrdemServico ordemServico, String formaPagamento, BigDecimal valorPago, LocalDate dataPagamento) {
        this.id = id;
        setOrdemServico(ordemServico);
        setFormaPagamento(formaPagamento);
        setValorPago(valorPago);
        setDataPagamento(dataPagamento);
    }

    // Construtor Simples (Para um NOVO pagamento agora)
    public Pagamento(OrdemServico ordemServico, String formaPagamento, BigDecimal valorPago) {
        this(0, ordemServico, formaPagamento, valorPago, LocalDate.now());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        if (ordemServico == null) {
            throw new IllegalArgumentException("Erro: Pagamento precisa estar vinculado a uma OS.");
        }
        this.ordemServico = ordemServico;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        if (formaPagamento == null || formaPagamento.isBlank()) {
            throw new IllegalArgumentException("Erro: Forma de pagamento obrigatória.");
        }
        this.formaPagamento = formaPagamento.toUpperCase(); // Padroniza para maiúsculo
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        // Validação: Não pode ser nulo e nem negativo/zero
        if (valorPago == null || valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: Valor pago inválido.");
        }
        this.valorPago = valorPago;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        if (dataPagamento == null) {
            throw new IllegalArgumentException("Erro: Data do pagamento é obrigatória.");
        }
        this.dataPagamento = dataPagamento;
    }

    @Override
    public String toString() {
        return "Pagamento #" + id + " | OS Vinculada: " + ordemServico.getNumero() +
                " | Valor: R$ " + valorPago + " (" + formaPagamento + ")";
    }
}
