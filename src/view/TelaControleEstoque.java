package view;

import model.Peca;
import service.PecaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaControleEstoque extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtQuantidade;

    private PecaService pecaService = new PecaService();

    public TelaControleEstoque() {
        setTitle("Controle de Estoque");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel painelInferior = new JPanel();

        txtQuantidade = new JTextField(5);

        JButton btnEntrada = new JButton("Entrada");
        JButton btnSaida = new JButton("Saída");
        JButton btnRemover = new JButton("Remover Peça");

        btnEntrada.addActionListener(e -> entradaEstoque());
        btnSaida.addActionListener(e -> saidaEstoque());
        btnRemover.addActionListener(e -> removerPeca());

        painelInferior.add(new JLabel("Quantidade:"));
        painelInferior.add(txtQuantidade);
        painelInferior.add(btnEntrada);
        painelInferior.add(btnSaida);
        painelInferior.add(btnRemover);

        add(scrollPane, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        List<Peca> pecas = pecaService.listarPecas();

        for (Peca p : pecas) {
            modelo.addRow(new Object[]{
                    p.getIdPeca(),
                    p.getNome(),
                    p.getPreco(),
                    p.getQuantidadeEstoque()
            });
        }
    }

    private Peca getPecaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma peça.");
            return null;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        return pecaService.buscarPorId(id);
    }

    private void entradaEstoque() {
        try {
            Peca peca = getPecaSelecionada();
            if (peca == null) return;

            int qtd = Integer.parseInt(txtQuantidade.getText());
            pecaService.adicionarEstoque(peca, qtd);

            carregarTabela();
            txtQuantidade.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saidaEstoque() {
        try {
            Peca peca = getPecaSelecionada();
            if (peca == null) return;

            int qtd = Integer.parseInt(txtQuantidade.getText());
            pecaService.removerEstoque(peca, qtd);

            carregarTabela();
            txtQuantidade.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerPeca() {
        Peca peca = getPecaSelecionada();
        if (peca == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja remover a peça?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = pecaService.removerPeca(peca.getIdPeca());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Peça removida.");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível remover a peça.");
            }
        }
    }
}
