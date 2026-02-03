package view;

import dao.OrdemServicoDao;
import model.OrdemServico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaListaOrdemServico extends JFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private OrdemServicoDao dao = new OrdemServicoDao();
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaListaOrdemServico() {
        setTitle("Gerenciar Ordens de Serviço");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);
        setContentPane(painel);

        // ===== TÍTULO =====
        JLabel lblTitulo = new JLabel("Lista de Ordens de Serviço");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // ===== TABELA =====
        modelo = new DefaultTableModel(
                new String[]{"Nº OS", "Data", "Veículo", "Responsável", "Status", "Total Peças"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        estilizarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        painel.add(scroll, BorderLayout.CENTER);

        // ===== BOTÕES (RODAPÉ) =====
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pBotoes.setBackground(Color.WHITE);

        JButton btnNovo = criarBotao("Novo", new Color(46, 204, 113));
        JButton btnEditar = criarBotao("Editar", new Color(52, 152, 219));
        JButton btnExcluir = criarBotao("Excluir", new Color(231, 76, 60));
        JButton btnRecarregar = criarBotao("Recarregar", new Color(149, 165, 166));

        pBotoes.add(btnNovo);
        pBotoes.add(btnEditar);
        pBotoes.add(btnExcluir);
        pBotoes.add(btnRecarregar);

        painel.add(pBotoes, BorderLayout.SOUTH);

        // Carrega dados iniciais
        atualizar();

        // ================= AÇÕES DOS BOTÕES =================

        // --- Ação: NOVO ---
        btnNovo.addActionListener(e -> {
            TelaNovaOrdemServico t = new TelaNovaOrdemServico();
            t.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    atualizar();
                }
            });
            t.setVisible(true);
        });

        // --- Ação: EDITAR ---
        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                int idOS = (int) tabela.getValueAt(linha, 0);
                OrdemServico osParaEditar = dao.buscarPorId(String.valueOf(idOS));

                if (osParaEditar != null) {
                    TelaNovaOrdemServico t = new TelaNovaOrdemServico(osParaEditar);
                    t.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            atualizar();
                        }
                    });
                    t.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: OS não encontrada no arquivo.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma OS na lista para editar.");
            }
        });

        // --- Ação: EXCLUIR ---
        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                int idOS = (int) tabela.getValueAt(linha, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir a OS #" + idOS + "?",
                        "Excluir", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean apagou = dao.excluir(String.valueOf(idOS));
                        if(apagou) {
                            atualizar();
                            JOptionPane.showMessageDialog(this, "OS excluída com sucesso.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma OS para excluir.");
            }
        });

        btnRecarregar.addActionListener(e -> atualizar());
    }

    private void atualizar() {
        modelo.setRowCount(0);
        dao = new OrdemServicoDao(); // Recarrega arquivo
        List<OrdemServico> lista = dao.listar();

        // Ordena por ID decrescente (mais recentes primeiro)
        lista.sort((a, b) -> Integer.compare(b.getNumero(), a.getNumero()));

        for (OrdemServico os : lista) {
            String nomeResp = (os.getResponsavel() != null) ? os.getResponsavel().getNome() : "Não Atribuído";
            String dadosVeiculo = (os.getVeiculo() != null) ? os.getVeiculo().getPlacaCarro() + " - " + os.getVeiculo().getModelo() : "Excluído";

            // Calcula o total usando o método que criamos na Model
            BigDecimal total = os.getValorTotalPecas();
            if (total == null) total = BigDecimal.ZERO;

            modelo.addRow(new Object[]{
                    os.getNumero(),
                    os.getData().format(fmt),
                    dadosVeiculo,
                    nomeResp,
                    os.getStatus(),
                    "R$ " + total.toString().replace(".", ",") // Formatação simples de moeda
            });
        }
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    private void estilizarTabela(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setRowHeight(30);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.setShowVerticalLines(false);

        // Centralizar colunas de ID e Status
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(JLabel.CENTER);
        t.getColumnModel().getColumn(0).setCellRenderer(centro); // ID
        t.getColumnModel().getColumn(4).setCellRenderer(centro); // Status

        // Alinhar valor financeiro à Direita
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(JLabel.RIGHT);
        t.getColumnModel().getColumn(5).setCellRenderer(direita); // Total
    }
}