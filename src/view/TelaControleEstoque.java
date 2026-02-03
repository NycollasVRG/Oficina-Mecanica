package view;

import model.Peca;
import service.PecaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Define o visual do sistema operacional
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelPrincipal);

        // ===== TÍTULO =====
        JLabel lblTitulo = new JLabel("Gerenciamento de Estoque");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ===== TABELA =====
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço (R$)", "Estoque Atual"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        estilizarTabela(tabela); // Aplica o estilo visual

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // ===== PAINEL INFERIOR (CONTROLES) =====
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelInferior.setBackground(Color.WHITE);
        painelInferior.setBorder(BorderFactory.createTitledBorder("Ações de Estoque"));

        // Label e Campo de Texto
        JLabel lblQtd = new JLabel("Quantidade:");
        lblQtd.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtQuantidade = new JTextField(5);
        txtQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtQuantidade.setPreferredSize(new Dimension(80, 30));

        // Botões Estilizados
        JButton btnEntrada = criarBotao("Entrada (+)", new Color(46, 204, 113));  // Verde
        JButton btnSaida = criarBotao("Saída (-)", new Color(243, 156, 18));      // Laranja
        JButton btnRemover = criarBotao("Excluir Peça", new Color(231, 76, 60));  // Vermelho

        // Ações
        btnEntrada.addActionListener(e -> entradaEstoque());
        btnSaida.addActionListener(e -> saidaEstoque());
        btnRemover.addActionListener(e -> removerPeca());

        // Adicionando ao painel
        painelInferior.add(lblQtd);
        painelInferior.add(txtQuantidade);
        painelInferior.add(Box.createHorizontalStrut(20));
        painelInferior.add(btnEntrada);
        painelInferior.add(btnSaida);
        painelInferior.add(Box.createHorizontalStrut(20));
        painelInferior.add(btnRemover);

        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);
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
            JOptionPane.showMessageDialog(this, "Selecione uma peça na tabela.");
            return null;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        return pecaService.buscarPorId(id);
    }

    private void entradaEstoque() {
        try {
            Peca peca = getPecaSelecionada();
            if (peca == null) return;

            if (txtQuantidade.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite a quantidade.");
                return;
            }

            int qtd = Integer.parseInt(txtQuantidade.getText());
            pecaService.adicionarEstoque(peca, qtd);

            carregarTabela();
            txtQuantidade.setText("");
            JOptionPane.showMessageDialog(this, "Estoque atualizado!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Digite apenas números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saidaEstoque() {
        try {
            Peca peca = getPecaSelecionada();
            if (peca == null) return;

            if (txtQuantidade.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite a quantidade.");
                return;
            }

            int qtd = Integer.parseInt(txtQuantidade.getText());
            pecaService.removerEstoque(peca, qtd);

            carregarTabela();
            txtQuantidade.setText("");
            JOptionPane.showMessageDialog(this, "Saída realizada!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Digite apenas números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerPeca() {
        Peca peca = getPecaSelecionada();
        if (peca == null) return;

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja EXCLUIR a peça '" + peca.getNome() + "' do sistema?",
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = pecaService.removerPeca(peca.getIdPeca());

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Peça removida com sucesso.");
                carregarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível remover a peça (verifique se ela não está em uso).");
            }
        }
    }

    // ================= MÉTODOS DE ESTILO (PADRÃO DO SISTEMA) =================

    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35)); // Tamanho padrão
        return btn;
    }

    private void estilizarTabela(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setRowHeight(30);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.setShowVerticalLines(false);

        // Centraliza as colunas de números (ID e Estoque)
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(JLabel.CENTER);
        t.getColumnModel().getColumn(0).setCellRenderer(centro); // ID
        t.getColumnModel().getColumn(3).setCellRenderer(centro); // Estoque
    }
}