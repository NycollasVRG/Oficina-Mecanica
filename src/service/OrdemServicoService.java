package service;

import dao.OrdemServicoDao;
import model.Executa;
import model.OrdemServico;
import model.Utiliza;

public class OrdemServicoService {

    private OrdemServicoDao ordemDao = new OrdemServicoDao();
    private PecaService pecaService = new PecaService();

    public boolean criarOrdemServico(OrdemServico os) {

        double total = 0;

        // 🔻 Baixa estoque das peças
        for (Utiliza u : os.getPecas()) {
            pecaService.removerEstoque(
                    u.getPeca(),
                    u.getQuantidade()
            );
            total += u.getPeca().getPreco() * u.getQuantidade();
        }

        // 💰 Soma valor dos serviços
        for (Executa e : os.getServicos()) {
            total += e.getServico().getPreco();
        }

        os.setTotal(total);

        return ordemDao.salvar(os);
    }
}

