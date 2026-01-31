package service;

import dao.CompraDao;
import model.Compra;
import model.Detalha;
import model.Peca;

import java.util.List;

public class CompraService {

    private CompraDao compraDao = new CompraDao();
    private PecaService pecaService = new PecaService();

    public void registrarCompra(Compra compra) {
        // Validações prévias
        if (compra.getItens() == null || compra.getItens().isEmpty()) {
            throw new IllegalArgumentException("A compra precisa ter itens.");
        }

        // Tenta salvar a compra no histórico (CSV de compras)
        boolean compraSalva = compraDao.salvar(compra);

        // Se a compra foi salva, atualizamos o estoque de cada item
        if (compraSalva) {
            System.out.println("Compra registrada. Atualizando estoque...");

            for (Detalha item : compra.getItens()) {
                // Chama o serviço de peça para fazer a mágica
                pecaService.adicionarEstoque(
                        item.getPeca(),
                        item.getQuantidade()
                );
            }
        } else {
            throw new RuntimeException("Erro grave: Não foi possível salvar a compra.");
        }
    }

    public void cancelarCompra(int idCompra) {
        // Busca a compra
        Compra compra = compraDao.buscarPorId(String.valueOf(idCompra));
        if (compra == null) {
            throw new IllegalArgumentException("Compra não encontrada.");
        }

        // Para cada item da compra, verifica se ainda temos estoque suficiente para devolver
        for (Detalha item : compra.getItens()) {
            Peca pecaAtual = item.getPeca(); // Peca vinda do banco (atualizada)

            // Se eu comprei 10, mas no estoque só tem 5 (porque vendi 5),
            // eu NÃO POSSO cancelar a compra, senão o estoque vira -5.
            if (pecaAtual.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Erro: Impossível cancelar compra. As peças já foram vendidas ou utilizadas. Item: " + pecaAtual.getNome());
            }
        }

        // Se passou na verificação, agora sim faz o estorno
        System.out.println("Estornando estoque...");
        for (Detalha item : compra.getItens()) {
            // Remove a quantidade do estoque
            pecaService.removerEstoque(item.getPeca(), item.getQuantidade());
        }

        // apaga o registro da compra
        compraDao.excluir(String.valueOf(idCompra));
        System.out.println("Compra cancelada com sucesso.");
    }

    public List<Compra> listarCompras() {
        return compraDao.listar();
    }
}
