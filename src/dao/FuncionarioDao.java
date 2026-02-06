package dao;

import model.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FuncionarioDao extends DaoGenerico<Funcionario>{

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //nome do arquivo que será criado na pasta data/
    public FuncionarioDao() {
        super("funcionarios.csv");
    }

    @Override
    public String toCSV(Funcionario f) {
        // %s = String (serve para BigDecimal tbm)
        // %d = Inteiro
        // A data usamos o .format(formatter)
        return String.format("%s;%s;%s;%s;%s;%s;%s;%d;%s;%s;%s",
                f.getCpf(),
                f.getNome(),
                f.getTelefone(),
                f.getRua(),
                f.getBairro(),
                f.getCidade(),
                f.getNumero(),
                f.getMatricula(),
                f.getSalario(),       // o  BigDecimal vira String automaticamente
                f.getCargo(),
                f.getDataAdmissao().format(formatter)
        );
    }

    @Override
    public Funcionario fromCSV(String linha) {
        String[] dados = linha.split(";");

        String cpf = dados[0];
        String nome = dados[1];
        String telefone = dados[2];
        String rua = dados[3];
        String bairro = dados[4];
        String cidade = dados[5];
        String numero = dados[6];
        int matricula = Integer.parseInt(dados[7]);
        BigDecimal salario = new BigDecimal(dados[8]);
        String cargo = dados[9];
        LocalDate dataAdmissao = LocalDate.parse(dados[10], formatter);

        return new Funcionario(cpf, nome, telefone, rua, bairro, cidade, numero, matricula, salario, cargo, dataAdmissao);
    }

    private int buscarProximaMatricula(){
        List<Funcionario> lista = listar();
        int maiorMat = 0;

        for (Funcionario f : lista){
            if(f.getMatricula() > maiorMat){
                maiorMat = f.getMatricula();
            }
        }
        return maiorMat + 1;
    }

    @Override
    public boolean salvar(Funcionario f){
        //se a matricula for 0, vai gerar uma nova matricula
        if(f.getMatricula() == 0){
            int novaMatricula = buscarProximaMatricula();
            f.setMatricula(novaMatricula);
        }
        return super.salvar(f);
    }

    @Override
    public String getId(Funcionario f) {
        return String.valueOf(f.getMatricula());
    }

}
