package dao;

import model.CatalogoServico;


import java.math.BigDecimal;
import java.util.List;

public class CatalogoServicoDao extends DaoGenerico<CatalogoServico>{

    public CatalogoServicoDao() {
        super("catalogo_servicos.csv");
    }

    private int gerarProximoId() {
        List<CatalogoServico> lista = listar();
        int maiorId = 0;
        for (CatalogoServico cs : lista) {
            if (cs.getId() > maiorId) {
                maiorId = cs.getId();
            }
        }
        return maiorId + 1;
    }

    @Override
    public String toCSV(CatalogoServico cs) {
        // Se for novo (ID 0), gera um ID
        if (cs.getId() == 0) {
            cs.setId(gerarProximoId());
        }
        // Formato: ID;Descricao;Preco
        return cs.getId() + ";" +
                cs.getDescricao() + ";" +
                cs.getPreco();
    }

    @Override
    public CatalogoServico fromCSV(String linha) {
        String[] dados = linha.split(";");
        // Reconstrói o objeto: ID, Descrição, Preço (BigDecimal)
        return new CatalogoServico(
                Integer.parseInt(dados[0]),
                dados[1],
                new BigDecimal(dados[2])
        );
    }

    @Override
    public String getId(CatalogoServico cs) {
        return String.valueOf(cs.getId());
    }
}
