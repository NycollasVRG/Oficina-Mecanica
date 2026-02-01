package view;

import model.Funcionario;
import service.FuncionarioService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelaListaFuncionarios extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private FuncionarioService service = new FuncionarioService();

    public TelaListaFuncionarios() {
        setTitle("Gerenciar Funcionários");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Lista de Funcionários");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painel.add(lblTitulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"Matrícula", "Nome", "CPF", "Cargo", "Salário"}, 0) {
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
            if(row >= 0) {
                String cpf = (String) tabela.getValueAt(row, 2);
                abrir(service.buscarPorCPF(cpf));
            } else JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
        });

        btnExcluir.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if(row >= 0) {
                if(JOptionPane.showConfirmDialog(this, "Excluir funcionário?", "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
                    try { service.excluir((int)tabela.getValueAt(row, 0)); atualizar(); }
                    catch(Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
                }
            } else JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
        });
    }

    private void abrir(Funcionario f) {
        TelaFuncionario t = (f == null) ? new TelaFuncionario() : new TelaFuncionario(f);
        t.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { atualizar(); }});
        t.setVisible(true);
    }

    private void atualizar() {
        modelo.setRowCount(0);
        for(Funcionario f : service.listarFuncionarios())
            modelo.addRow(new Object[]{f.getMatricula(), f.getNome(), f.getCpf(), f.getCargo(), f.getSalario()});
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