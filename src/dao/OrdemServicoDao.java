package dao;

import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDao extends DaoGenerico<OrdemServico> {

    private PecaDao pecaDao = new PecaDao();
    private CatalogoServicoDao catalogoDao = new CatalogoServicoDao();
    private FuncionarioDao funcionarioDao = new FuncionarioDao();
    private VeiculoDao veiculoDao = new VeiculoDao();

    public OrdemServicoDao() {
        super("ordens_servico.csv");
    }

    private int gerarProximoId() {
        int maior = 0;
        for (OrdemServico os : listar()) {
            if (os.getNumero() > maior) {
                maior = os.getNumero();
            }
        }
        return maior + 1;
    }

    @Override
    public String toCSV(OrdemServico os) {
        if (os.getNumero() == 0) {
            os.setNumero(gerarProximoId());
        }

        StringBuilder sb = new StringBuilder();

        // ID;DATA;PLACA;MATRICULA_FUNC;DESCRICAO;STATUS
        sb.append(os.getNumero()).append(";");
        sb.append(os.getData()).append(";");

        // Veículo
        String placa = (os.getVeiculo() != null) ? os.getVeiculo().getPlacaCarro() : "SEM-PLACA";
        sb.append(placa).append(";");

        // --- Salvar a MATRÍCULA---
        String idFunc = "0";
        if (os.getResponsavel() != null && os.getResponsavel().getMatricula() != null) {
            idFunc = String.valueOf(os.getResponsavel().getMatricula());
        }
        sb.append(idFunc).append(";");
        // ----------------------------------------------------

        sb.append(os.getDescricao().replace(";", ",")).append(";");
        sb.append(os.getStatus()).append(";");

        // PEÇAS
        if (!os.getItensPecas().isEmpty()) {
            for (Utiliza u : os.getItensPecas()) {
                sb.append(u.getPeca().getIdPeca()).append(",");
                sb.append(u.getQuantidade()).append(",");
                sb.append(u.getPrecoUnitario()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            sb.append("VAZIO");
        }

        sb.append(";");

        // SERVIÇOS
        if (!os.getItensServicos().isEmpty()) {
            for (Executa e : os.getItensServicos()) {
                sb.append(e.getServico().getId()).append(",");
                sb.append(e.getPrecoPraticado()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            sb.append("VAZIO");
        }

        return sb.toString();
    }

    @Override
    public OrdemServico fromCSV(String linha) {
        String[] p = linha.split(";");

        int numero = Integer.parseInt(p[0]);
        LocalDate data = LocalDate.parse(p[1]);
        String placa = p[2];
        String matriculaFunc = p[3];
        String descricao = p[4];
        String status = p[5];

        // 1. Busca Veículo
        Veiculo veiculo = null;
        for(Veiculo v : veiculoDao.listar()){
            if(v.getPlacaCarro().equalsIgnoreCase(placa)){
                veiculo = v;
                break;
            }
        }
        if (veiculo == null) veiculo = new Veiculo(placa);

        // 2. Busca Funcionário pela MATRÍCULA
        Funcionario responsavel = funcionarioDao.buscarPorId(matriculaFunc);

        OrdemServico os = new OrdemServico(
                numero, data, veiculo, responsavel, descricao, status
        );

        // 3. Peças
        if (p.length > 6 && !p[6].equals("VAZIO")) {
            List<Utiliza> pecas = new ArrayList<>();
            for (String item : p[6].split("\\|")) {
                String[] d = item.split(",");
                Peca peca = pecaDao.buscarPorId(d[0]);
                if (peca != null) {
                    pecas.add(new Utiliza(peca, Integer.parseInt(d[1]), new BigDecimal(d[2])));
                }
            }
            os.setItensPecas(pecas);
        }

        // 4. Serviços
        if (p.length > 7 && !p[7].equals("VAZIO")) {
            List<Executa> servicos = new ArrayList<>();
            for (String item : p[7].split("\\|")) {
                String[] d = item.split(",");
                CatalogoServico serv = catalogoDao.buscarPorId(d[0]);
                if (serv != null) {
                    servicos.add(new Executa(serv, new BigDecimal(d[1])));
                }
            }
            os.setItensServicos(servicos);
        }

        return os;
    }

    @Override
    public String getId(OrdemServico os) {
        return String.valueOf(os.getNumero());
    }
}