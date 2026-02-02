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
        setMatricula(matricula);
        setSalario(salario);
        setCargo(cargo);
        setDataAdmissao(dataAdmissao);
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        //não deve ter matricula negativa
        if(matricula == null || matricula < 0){
            throw new IllegalArgumentException("Erro: A matricula não pode ser negativa");
        }
        this.matricula = matricula;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        //o salario deve ser maior do que 0
        if(salario == null || salario.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Erro: O salário não pode ser negativo ou nula");
        }
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

    // Esse método serve para verificar igualdade
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return matricula != null ? matricula.equals(that.matricula) : that.matricula == null;
    }

    // Esse método faz aparecer o NOME na caixinha de seleção
    @Override
    public String toString() {
        return getNome();
    }
}
