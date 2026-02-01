package model;

import java.math.BigDecimal;

public class Executa {

    private CatalogoServico servico;
    private BigDecimal precoPraticado; // O preço NA HORA da OS (pode ser diferente do catálogo atual)

    public Executa(CatalogoServico servico, BigDecimal precoPraticado) {
        setServico(servico);
        setPrecoPraticado(precoPraticado);
    }

    public CatalogoServico getServico() {
        return servico;
    }

    public void setServico(CatalogoServico servico) {
        // Validação de Integridade: Não existe "execução" sem serviço
        if (servico == null) {
            throw new IllegalArgumentException("Erro: O serviço não pode ser nulo.");
        }
        this.servico = servico;
    }

    public BigDecimal getPrecoPraticado() {
        return precoPraticado;
    }

    public void setPrecoPraticado(BigDecimal precoPraticado) {
        // Validação de Integridade: Dinheiro não pode ser negativo
        if (precoPraticado == null || precoPraticado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Erro: O preço praticado não pode ser negativo.");
        }
        this.precoPraticado = precoPraticado;
    }
}
