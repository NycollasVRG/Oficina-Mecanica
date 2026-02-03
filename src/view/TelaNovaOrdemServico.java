package view;

import dao.FuncionarioDao;
import dao.OrdemServicoDao;
import dao.PecaDao;
import model.Funcionario;
import model.OrdemServico;
import model.Peca;
import model.Utiliza;
import model.Veiculo;
import service.VeiculoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaNovaOrdemServico extends JFrame {

    // Componentes da Tela
    private JTable tabelaVeiculos;
    private DefaultTableModel modeloVeiculos;
    private JComboBox<Funcionario> comboFuncionario;
    private JComboBox<String> comboStatus;
    private JTextArea txtDescricao;
    private JLabel lblTitulo;

    // --- COMPONENTES DE PEÇAS ---
    private JComboBox<Peca> comboPecas;
    private JTextField txtQtdPeca;
    private JTable tabelaItens;
    private DefaultTableModel modeloItens;
    private JLabel lblTotalPecas;

    // Conexão com Dados
    private VeiculoService veiculoService = new VeiculoService();
    private FuncionarioDao funcionarioDao = new FuncionarioDao();
    private OrdemServicoDao osDao = new OrdemServicoDao();
    private PecaDao pecaDao = new PecaDao();

    // Variáveis de controle
    private OrdemServico ordemEdicao = null;
    private List<Utiliza> listaPecasTemporaria = new ArrayList<>();

    // ================== CONSTRUTORES ==================
    public TelaNovaOrdemServico() {
        montarTela();
        comboStatus.setSelectedItem("ABERTA");
        comboStatus.setEnabled(false);
    }

    public TelaNovaOrdemServico(OrdemServico osParaEditar) {
        this.ordemEdicao = osParaEditar;
        if (osParaEditar.getItensPecas() != null) {
            this.listaPecasTemporaria.addAll(osParaEditar.getItensPecas());
        }
        montarTela();
        preencherDadosParaEdicao();
        atualizarTabelaItens();
    }

    private void montarTela() {
        setTitle("Nova Ordem de Serviço");
        setSize(1000, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelPrincipal.setBackground(Color.WHITE);
        setContentPane(painelPrincipal);

        // TÍTULO
        lblTitulo = new JLabel("Nova Ordem de Serviço", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // --- DIVISÃO DA TELA ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);

        // PARTE 1 (CIMA): Tabela de Veículos
        JPanel pVeiculos = new JPanel(new BorderLayout());
        pVeiculos.setBackground(Color.WHITE);
        pVeiculos.setBorder(BorderFactory.createTitledBorder("1. Selecione o Veículo"));

        modeloVeiculos = new DefaultTableModel(new String[]{"Placa", "Modelo", "Cor", "Cliente"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaVeiculos = new JTable(modeloVeiculos);
        estilizarTabela(tabelaVeiculos);
        pVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);

        splitPane.setTopComponent(pVeiculos);

        // PARTE 2 (BAIXO): Detalhes + Peças
        JPanel pDetalhes = new JPanel(new BorderLayout(10, 10));
        pDetalhes.setBackground(Color.WHITE);

        pDetalhes.add(criarPainelFormulario(), BorderLayout.NORTH);
        pDetalhes.add(criarPainelPecas(), BorderLayout.CENTER);

        splitPane.setBottomComponent(pDetalhes);
        painelPrincipal.add(splitPane, BorderLayout.CENTER);

        // RODAPÉ (BOTÕES PRINCIPAIS)
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);

        // Loads
        carregarVeiculos();
        carregarFuncionarios();
        carregarPecas();
    }

    // --- SUB-PAINÉIS ---

    private JPanel criarPainelFormulario() {
        JPanel pForm = new JPanel(new BorderLayout(5, 5));
        pForm.setBackground(Color.WHITE);
        pForm.setBorder(BorderFactory.createTitledBorder("2. Dados do Serviço"));

        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBackground(Color.WHITE);

        comboFuncionario = new JComboBox<>();
        comboFuncionario.setPreferredSize(new Dimension(250, 30));
        comboFuncionario.setBackground(Color.WHITE);

        comboStatus = new JComboBox<>(new String[]{"ABERTA", "FINALIZADA", "CANCELADA"});
        comboStatus.setPreferredSize(new Dimension(150, 30));
        comboStatus.setBackground(Color.WHITE);

        pTop.add(new JLabel("Responsável: ")); pTop.add(comboFuncionario);
        pTop.add(new JLabel("  Status: ")); pTop.add(comboStatus);

        pForm.add(pTop, BorderLayout.NORTH);

        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setBorder(BorderFactory.createTitledBorder("Descrição do Problema"));

        pForm.add(scrollDesc, BorderLayout.CENTER);
        return pForm;
    }

    private JPanel criarPainelPecas() {
        JPanel pPecas = new JPanel(new BorderLayout(5, 5));
        pPecas.setBackground(Color.WHITE);
        pPecas.setBorder(BorderFactory.createTitledBorder("3. Peças e Materiais Utilizados"));

        // === BARRA DE FERRAMENTAS (TOPO) ===
        JPanel pTopFerramentas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTopFerramentas.setBackground(new Color(245, 245, 250));

        comboPecas = new JComboBox<>();
        comboPecas.setPreferredSize(new Dimension(280, 30));
        comboPecas.setBackground(Color.WHITE);

        txtQtdPeca = new JTextField("1", 3);
        txtQtdPeca.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- BOTÕES ---
        JButton btnAdd = criarBotao("Adicionar", new Color(52, 152, 219)); // Azul
        btnAdd.setPreferredSize(new Dimension(100, 30));

        JButton btnAlterar = criarBotao("Alterar Qtd", new Color(243, 156, 18)); // Laranja
        btnAlterar.setPreferredSize(new Dimension(110, 30));

        JButton btnRemover = criarBotao("Remover", new Color(231, 76, 60)); // Vermelho
        btnRemover.setPreferredSize(new Dimension(100, 30));

        // Ações
        btnAdd.addActionListener(e -> adicionarPecaNaLista());
        btnAlterar.addActionListener(e -> alterarQuantidadeItem());
        btnRemover.addActionListener(e -> removerItem());

        // Adiciona tudo na barra
        pTopFerramentas.add(new JLabel("Peça:"));
        pTopFerramentas.add(comboPecas);
        pTopFerramentas.add(Box.createHorizontalStrut(10)); // Espaço
        pTopFerramentas.add(new JLabel("Qtd:"));
        pTopFerramentas.add(txtQtdPeca);

        pTopFerramentas.add(Box.createHorizontalStrut(10)); // Espaço antes dos botões
        pTopFerramentas.add(btnAdd);
        pTopFerramentas.add(Box.createHorizontalStrut(10)); // Espaço entre botões
        pTopFerramentas.add(btnAlterar);
        pTopFerramentas.add(Box.createHorizontalStrut(10)); // Espaço entre botões
        pTopFerramentas.add(btnRemover);

        pPecas.add(pTopFerramentas, BorderLayout.NORTH);

        // Tabela de Itens (CENTRO)
        modeloItens = new DefaultTableModel(new String[]{"Peça", "Preço Un.", "Qtd", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaItens = new JTable(modeloItens);
        estilizarTabela(tabelaItens);
        pPecas.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        // Rodapé da Tabela
        JPanel pTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pTotal.setBackground(Color.WHITE);

        lblTotalPecas = new JLabel("Total Peças: R$ 0,00  ");
        lblTotalPecas.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalPecas.setHorizontalAlignment(SwingConstants.RIGHT);

        pTotal.add(lblTotalPecas);
        pPecas.add(pTotal, BorderLayout.SOUTH);

        return pPecas;
    }

    private JPanel criarPainelBotoes() {
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.setBackground(Color.WHITE);

        JButton btnVerLista = criarBotao("Ver Lista", new Color(52, 152, 219));
        JButton btnSalvar = criarBotao("Salvar OS", new Color(46, 204, 113));
        JButton btnCancelar = criarBotao("Cancelar", new Color(231, 76, 60));

        btnVerLista.addActionListener(e -> new TelaListaOrdemServico().setVisible(true));
        btnSalvar.addActionListener(e -> salvarOS());
        btnCancelar.addActionListener(e -> dispose());

        botoes.add(btnVerLista);
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);
        return botoes;
    }

    // --- LÓGICA DE MANIPULAÇÃO DA LISTA ---

    private void adicionarPecaNaLista() {
        try {
            Peca pecaSelecionada = (Peca) comboPecas.getSelectedItem();
            int qtd = Integer.parseInt(txtQtdPeca.getText());

            if (pecaSelecionada == null) return;
            if (qtd <= 0) { JOptionPane.showMessageDialog(this, "Quantidade inválida."); return; }

            if (qtd > pecaSelecionada.getQuantidadeEstoque()) {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente! Disponível: " + pecaSelecionada.getQuantidadeEstoque());
                return;
            }

            Utiliza item = new Utiliza(pecaSelecionada, qtd, pecaSelecionada.getPreco());
            listaPecasTemporaria.add(item);
            atualizarTabelaItens();
            txtQtdPeca.setText("1");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um número válido.");
        }
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para remover.");
            return;
        }

        listaPecasTemporaria.remove(linha);
        atualizarTabelaItens();
    }

    private void alterarQuantidadeItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para alterar.");
            return;
        }

        Utiliza itemAtual = listaPecasTemporaria.get(linha);

        String input = JOptionPane.showInputDialog(this,
                "Digite a nova quantidade para: " + itemAtual.getPeca().getNome(),
                itemAtual.getQuantidade());

        if (input != null && !input.isEmpty()) {
            try {
                int novaQtd = Integer.parseInt(input);
                if (novaQtd <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero.");
                    return;
                }

                if (novaQtd > itemAtual.getPeca().getQuantidadeEstoque()) {
                    JOptionPane.showMessageDialog(this, "Estoque insuficiente! Disponível: " + itemAtual.getPeca().getQuantidadeEstoque());
                    return;
                }

                Utiliza novoItem = new Utiliza(itemAtual.getPeca(), novaQtd, itemAtual.getPrecoUnitario());
                listaPecasTemporaria.set(linha, novoItem);

                atualizarTabelaItens();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Número inválido.");
            }
        }
    }

    private void atualizarTabelaItens() {
        modeloItens.setRowCount(0);
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (Utiliza item : listaPecasTemporaria) {
            modeloItens.addRow(new Object[]{
                    item.getPeca().getNome(),
                    "R$ " + item.getPrecoUnitario(),
                    item.getQuantidade(),
                    "R$ " + item.getSubtotal()
            });
            total = total.add(item.getSubtotal());
        }
        lblTotalPecas.setText("Total Peças: R$ " + total + "  ");
    }

    // --- SALVAR OS (MÉTODO ATUALIZADO COM LÓGICA DE ESTOQUE) ---
    private void salvarOS() {
        int linha = tabelaVeiculos.getSelectedRow();
        if (linha < 0) { JOptionPane.showMessageDialog(this, "Selecione um veículo na tabela acima."); return; }

        String placa = (String) tabelaVeiculos.getValueAt(linha, 0);
        Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
        Funcionario func = (Funcionario) comboFuncionario.getSelectedItem();
        String desc = txtDescricao.getText();
        String statusNovo = (String) comboStatus.getSelectedItem();

        if (desc.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Preencha a descrição."); return; }

        try {
            // === 1. LÓGICA DE ESTORNO (Devolver estoque anterior se for edição) ===
            if (ordemEdicao != null) {
                // Só devolvemos o estoque se a OS antiga NÃO estava cancelada.
                // Se já estava cancelada, as peças já estavam no estoque, então não mexe.
                if (!ordemEdicao.getStatus().equals("CANCELADA")) {
                    for (Utiliza itemAntigo : ordemEdicao.getItensPecas()) {
                        pecaDao.adicionarEstoque(itemAntigo.getPeca().getIdPeca(), itemAntigo.getQuantidade());
                    }
                }
            }

            // === 2. MONTAR O OBJETO ATUALIZADO ===
            OrdemServico osSalvar;
            if (ordemEdicao == null) {
                osSalvar = new OrdemServico(veiculo, func, desc);
            } else {
                osSalvar = new OrdemServico(ordemEdicao.getNumero(), ordemEdicao.getData(), veiculo, func, desc, statusNovo);
                osSalvar.setItensServicos(ordemEdicao.getItensServicos());
            }
            // Atualiza a lista de peças com o que está na tela agora
            osSalvar.setItensPecas(listaPecasTemporaria);

            // === 3. BAIXA NO ESTOQUE (Se não for cancelada) ===
            // Se o novo status for CANCELADA, não fazemos nada (as peças ficam no estoque).
            // Se for ABERTA ou FINALIZADA, consumimos as peças do estoque.
            if (!statusNovo.equals("CANCELADA")) {
                for (Utiliza itemNovo : listaPecasTemporaria) {
                    pecaDao.baixarEstoque(itemNovo.getPeca().getIdPeca(), itemNovo.getQuantidade());
                }
            }

            // === 4. SALVAR NO ARQUIVO ===
            if (ordemEdicao == null) osDao.salvar(osSalvar);
            else osDao.atualizar(osSalvar);

            JOptionPane.showMessageDialog(this, "OS Salva com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // --- CARREGAMENTOS E ESTILOS ---
    private void carregarVeiculos() {
        modeloVeiculos.setRowCount(0);
        for (Veiculo v : veiculoService.listarVeiculos()) {
            modeloVeiculos.addRow(new Object[]{v.getPlacaCarro(), v.getModelo(), v.getCor(), v.getDono().getNome()});
        }
    }
    private void carregarFuncionarios() {
        comboFuncionario.removeAllItems();
        for (Funcionario f : funcionarioDao.listar()) comboFuncionario.addItem(f);
    }
    private void carregarPecas() {
        comboPecas.removeAllItems();
        for (Peca p : pecaDao.listar()) comboPecas.addItem(p);
    }
    private void preencherDadosParaEdicao() {
        if (ordemEdicao != null) {
            txtDescricao.setText(ordemEdicao.getDescricao());
            comboFuncionario.setSelectedItem(ordemEdicao.getResponsavel());
            comboStatus.setSelectedItem(ordemEdicao.getStatus());
            comboStatus.setEnabled(true);
            if (ordemEdicao.getVeiculo() != null) {
                String placa = ordemEdicao.getVeiculo().getPlacaCarro();
                for (int i=0; i<tabelaVeiculos.getRowCount(); i++) {
                    if (tabelaVeiculos.getValueAt(i, 0).equals(placa)) {
                        tabelaVeiculos.setRowSelectionInterval(i, i);
                        tabelaVeiculos.scrollRectToVisible(tabelaVeiculos.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        }
    }
    private JButton criarBotao(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    private void estilizarTabela(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setRowHeight(25);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.setShowVerticalLines(false);
    }
}