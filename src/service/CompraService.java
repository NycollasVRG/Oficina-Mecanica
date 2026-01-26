package service;

import dao.CompraDao;
import model.Compra;
import model.Detalha;

public class CompraService {

    private CompraDao compraDao = new CompraDao();
    private PecaService pecaService = new PecaService();

    public boolean registrarCompra(Compra compra) {

        // Atualiza estoque automaticamente
        for (Detalha item : compra.getItens()) {
            pecaService.adicionarEstoque(
                    item.getPeca(),
                    item.getQuantidade()
            );
        }

        // Salva a compra depois da regra aplicada
        return compraDao.salvar(compra);
    }
}
