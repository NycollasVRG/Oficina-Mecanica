package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Funcionario extends Pessoa{
    private Integer matricula;
    private BigDecimal salario;
    private String cargo;
    private LocalDate dataAdmissao;

    public Funcionario(String cpf, String nome,
                       String telefone, String rua, String bairro, String cidade, String numero, Integer matricula, BigDecimal salario,
                       String cargo, LocalDate dataAdmissao) {
        super(cpf, nome, telefone, rua, bairro, cidade, numero);
        this.matricula = matricula;
        this.salario = salario;
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
}
