package dao;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDao extends DaoGenerico<OrdemServico> {

    public OrdemServicoDao() {
        super("ordens_servico.csv");
    }

    @Override
    public String toCSV(OrdemServico os) {
        StringBuilder sb = new StringBuilder();

        sb.append(os.getNumero()).append(";");
        sb.append(os.getVeiculo().getPlacaCarro()).append(";");
        sb.append(os.getTotal()).append(";");

        // PEÇAS UTILIZADAS
        for (Utiliza u : os.getPecas()) {
            sb.append(u.getPeca().getNome()).append(",");
            sb.append(u.getQuantidade()).append(",");
            sb.append(u.getPrecoUnitario()).append("|");
        }

        sb.deleteCharAt(sb.length() - 1); // remove último |

        sb.append(";");

        // SERVIÇOS EXECUTADOS
        for (Executa e : os.getServicos()) {
            sb.append(e.getServico().getDescricao()).append(",");
            sb.append(e.getPreco()).append("|");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public OrdemServico fromCSV(String linha) {
        String[] partes = linha.split(";");

        int numero = Integer.parseInt(partes[0]);
        String placa = partes[1];
        double total = Double.parseDouble(partes[2]);

        Veiculo veiculo = new Veiculo(placa);

        List<Utiliza> pecas = new ArrayList<>();
        List<Executa> servicos = new ArrayList<>();

        // Reconstrói peças
        String[] pecasCSV = partes[3].split("\\|");
       /* for (String p : pecasCSV) {
            String[] d = p.split(",");
            Peca peca = new Peca(d[0], Double.parseDouble(d[2]), 0);
            pecas.add(new Utiliza(
                    peca,
                    Integer.parseInt(d[1]),
                    Double.parseDouble(d[2])
            ));
        }

        */
        // Reconstrói serviços
        String[] servicosCSV = partes[4].split("\\|");
        for (String s : servicosCSV) {
            String[] d = s.split(",");
            CatalogoServico cs = new CatalogoServico(d[0], Double.parseDouble(d[1]));
            servicos.add(new Executa(cs, Double.parseDouble(d[1])));
        }

        OrdemServico os = new OrdemServico(numero, veiculo, pecas, servicos);
        os.setTotal(total);
        return os;
    }

    @Override
    public String getId(OrdemServico objeto) {
        return "";
    }
}
