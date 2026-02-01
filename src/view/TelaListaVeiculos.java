package view;

import model.Veiculo;
import service.VeiculoService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaListaVeiculos extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private VeiculoService service = new VeiculoService();

    public TelaListaVeiculos() {
        setTitle("Gerenciar Veículos");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Lista de Veículos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painel.add(lblTitulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Placa", "Modelo", "Cor", "Dono (Nome)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        estilizarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        painel.add(scroll, BorderLayout.CENTER);

        // BOTÕES
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pBotoes.setBackground(Color.WHITE);

        JButton btnNovo = criarBotao("Novo", new Color(46, 204, 113));
        JButton btnEditar = criarBotao("Editar", new Color(52, 152, 219));
        JButton btnExcluir = criarBotao("Excluir", new Color(231, 76, 60));
        JButton btnAtualizar = criarBotao("Recarregar", new Color(149, 165, 166));

        pBotoes.add(btnNovo); pBotoes.add(btnEditar); pBotoes.add(btnExcluir); pBotoes.add(btnAtualizar);
        painel.add(pBotoes, BorderLayout.SOUTH);

        add(painel);
        atualizar();

        // Ações
        btnAtualizar.addActionListener(e -> atualizar());
        btnNovo.addActionListener(e -> abrir(null));

        btnEditar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row >= 0) abrir(service.buscarPorPlaca((String)tabela.getValueAt(row, 0)));
            else JOptionPane.showMessageDialog(this, "Selecione um veículo.");
        });

        btnExcluir.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row >= 0) {
                if(JOptionPane.showConfirmDialog(this, "Excluir veículo?", "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
                    try { service.excluir((String)tabela.getValueAt(row, 0)); atualizar(); }
                    catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
                }
            } else JOptionPane.showMessageDialog(this, "Selecione um veículo.");
        });
    }

    private void abrir(Veiculo v) {
        TelaVeiculo t = (v == null) ? new TelaVeiculo() : new TelaVeiculo(v);
        t.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { atualizar(); }});
        t.setVisible(true);
    }

    private void atualizar() {
        modelo.setRowCount(0);
        for(Veiculo v : service.listarVeiculos())
            modelo.addRow(new Object[]{v.getPlacaCarro(), v.getModelo(), v.getCor(), v.getDono().getNome()});
    }

    private JButton criarBotao(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false); btn.setBorderPainted(false); // CORREÇÃO AQUI
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    private void estilizarTabela(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); t.setRowHeight(30);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.setShowVerticalLines(false);
    }
}