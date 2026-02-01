package view;

import model.Cliente;
import service.ClienteService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TelaListaClientes extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private ClienteService service = new ClienteService();

    public TelaListaClientes() {
        setTitle("Gerenciar Clientes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);

        // Título
        JLabel lblTitulo = new JLabel("Lista de Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Tipo"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        estilizarTabela(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        painel.add(scroll, BorderLayout.CENTER);

        // BOTÕES COLORIDOS
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pBotoes.setBackground(Color.WHITE);

        JButton btnNovo = criarBotao("Novo", new Color(46, 204, 113));      // Verde
        JButton btnEditar = criarBotao("Editar", new Color(52, 152, 219));  // Azul
        JButton btnExcluir = criarBotao("Excluir", new Color(231, 76, 60)); // Vermelho
        JButton btnAtualizar = criarBotao("Recarregar", new Color(149, 165, 166)); // Cinza

        pBotoes.add(btnNovo);
        pBotoes.add(btnEditar);
        pBotoes.add(btnExcluir);
        pBotoes.add(btnAtualizar);

        painel.add(pBotoes, BorderLayout.SOUTH);
        add(painel);
        atualizarTabela();

        // Ações
        btnAtualizar.addActionListener(e -> atualizarTabela());
        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                String cpf = (String) tabela.getValueAt(linha, 2);
                abrirFormulario(service.buscarPorCPF(cpf));
            } else JOptionPane.showMessageDialog(this, "Selecione um cliente.");
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                if (JOptionPane.showConfirmDialog(this, "Excluir cliente?", "Atenção", JOptionPane.YES_NO_OPTION) == 0) {
                    try { service.excluir((int)tabela.getValueAt(linha, 0)); atualizarTabela(); }
                    catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
                }
            } else JOptionPane.showMessageDialog(this, "Selecione um cliente.");
        });
    }

    private void abrirFormulario(Cliente c) {
        TelaCliente tela = (c == null) ? new TelaCliente() : new TelaCliente(c);
        tela.addWindowListener(new WindowAdapter() { public void windowClosed(WindowEvent e) { atualizarTabela(); }});
        tela.setVisible(true);
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        List<Cliente> lista = service.listarClientes();
        for (Cliente c : lista) modelo.addRow(new Object[]{c.getCodCliente(), c.getNome(), c.getCpf(), c.getTelefone(), c.getTipoCliente()});
    }

    // MÉTODOS VISUAIS
    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // ISSO TIRA O ESBRANQUIÇADO
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
    }
}