package dao;

import model.Peca;

public class PecaDao extends DaoGenerico<Peca> {

    public PecaDao() {
        super("pecas.csv");
    }

    @Override
    public String toCSV(Peca p) {
        return p.getNome() + ";" +
                p.getPreco() + ";" +
                p.getQuantidadeEstoque();
    }

    @Override
    public Peca fromCSV(String linha) {
        String[] dados = linha.split(";");
        return new Peca(
                dados[0],
                Double.parseDouble(dados[1]),
                Integer.parseInt(dados[2])
        );
    }

    @Override
    public String getId(Peca objeto) {
        return "";
    }
}
