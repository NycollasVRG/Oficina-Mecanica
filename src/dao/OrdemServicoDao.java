package dao;

import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDao extends DaoGenerico<OrdemServico> {

    private PecaDao pecaDao;
    private CatalogoServicoDao catalogoDao;

    public OrdemServicoDao() {
        super("ordens_servico.csv");
        this.pecaDao = new PecaDao();
        this.catalogoDao = new CatalogoServicoDao();
    }

    private int gerarProximoId() {
        List<OrdemServico> lista = listar();
        int maiorId = 0;
        for (OrdemServico os : lista) {
            if (os.getNumero() > maiorId) {
                maiorId = os.getNumero();
            }
        }
        return maiorId + 1;
    }

    @Override
    public String toCSV(OrdemServico os) {
        // Se for nova OS (ID 0), gera um número novo
        if (os.getNumero() == 0) {
            os.setNumero(gerarProximoId());
        }

        StringBuilder sb = new StringBuilder();

        // CABEÇALHO (ID;Data;Placa;Status)
        sb.append(os.getNumero()).append(";");
        sb.append(os.getData()).append(";");
        // ATENÇÃO: Usando getPlacaCarro() conforme seu modelo
        sb.append(os.getVeiculo().getPlacaCarro()).append(";");
        sb.append(os.getStatus()).append(";");

        // LISTA DE PEÇAS (Separadas por | e campos por ,)
        if (os.getItensPecas() != null && !os.getItensPecas().isEmpty()) {
            for (Utiliza u : os.getItensPecas()) {
                sb.append(u.getPeca().getIdPeca()).append(","); // Salva o ID da peça
                sb.append(u.getQuantidade()).append(",");
                sb.append(u.getPrecoUnitario()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove último pipe |
        } else {
            sb.append("VAZIO");
        }

        sb.append(";"); // Separador entre a lista de peças e a de serviços

        // LISTA DE SERVIÇOS
        if (os.getItensServicos() != null && !os.getItensServicos().isEmpty()) {
            for (Executa e : os.getItensServicos()) {
                sb.append(e.getServico().getId()).append(","); // Salva o ID do serviço
                sb.append(e.getPrecoPraticado()).append("|");
            }
            sb.deleteCharAt(sb.length() - 1); // Remove último pipe |
        } else {
            sb.append("VAZIO");
        }

        return sb.toString();
    }

    @Override
    public OrdemServico fromCSV(String linha) {
        String[] partes = linha.split(";");

        // RECUPERA DADOS BÁSICOS
        int numero = Integer.parseInt(partes[0]);
        LocalDate data = LocalDate.parse(partes[1]);
        String placa = partes[2];
        String status = partes[3];

        Veiculo veiculo = new Veiculo(placa);

        // Cria a OS básica
        OrdemServico os = new OrdemServico(numero, data, veiculo, status);

        // RECUPERA LISTA DE PEÇAS
        List<Utiliza> listaPecas = new ArrayList<>();
        String campoPecas = partes[4];

        if (!campoPecas.equals("VAZIO") && !campoPecas.isEmpty()) {
            String[] itens = campoPecas.split("\\|");
            for (String itemStr : itens) {
                String[] dadosItem = itemStr.split(",");

                int idPeca = Integer.parseInt(dadosItem[0]);
                int qtd = Integer.parseInt(dadosItem[1]);
                BigDecimal preco = new BigDecimal(dadosItem[2]);

                // Busca a PEÇA COMPLETA no arquivo de peças usando o ID
                Peca peca = pecaDao.buscarPorId(String.valueOf(idPeca));

                // Se a peça foi apagada do sistema, cria uma "fake" para não dar erro
                if (peca == null) {
                    peca = new Peca(idPeca, "[Peça Excluída]", BigDecimal.ZERO, 0);
                }

                listaPecas.add(new Utiliza(peca, qtd, preco));
            }
        }
        os.setItensPecas(listaPecas);

        // RECUPERA LISTA DE SERVIÇOS
        List<Executa> listaServicos = new ArrayList<>();

        // Verifica se existe a parte de serviços na linha (segurança)
        if (partes.length > 5) {
            String campoServicos = partes[5];

            if (!campoServicos.equals("VAZIO") && !campoServicos.isEmpty()) {
                String[] itens = campoServicos.split("\\|");
                for (String itemStr : itens) {
                    String[] dadosItem = itemStr.split(",");

                    int idServico = Integer.parseInt(dadosItem[0]);
                    BigDecimal preco = new BigDecimal(dadosItem[1]);

                    // Busca o SERVIÇO COMPLETO no catálogo
                    CatalogoServico servico = catalogoDao.buscarPorId(String.valueOf(idServico));

                    if (servico != null) {
                        listaServicos.add(new Executa(servico, preco));
                    }
                }
            }
        }
        os.setItensServicos(listaServicos);

        return os;
    }

    @Override
    public String getId(OrdemServico os) {
        return String.valueOf(os.getNumero());
    }
}
