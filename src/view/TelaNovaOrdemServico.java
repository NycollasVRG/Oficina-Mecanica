package view;

import dao.FuncionarioDao;
import dao.OrdemServicoDao;
import model.Funcionario;
import model.OrdemServico;
import model.Veiculo;
import service.VeiculoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaNovaOrdemServico extends JFrame {

    // Componentes da Tela
    private JTable tabelaVeiculos;
    private DefaultTableModel modeloVeiculos;
    private JComboBox<Funcionario> comboFuncionario;
    private JComboBox<String> comboStatus; // <--- NOVO COMPONENTE
    private JTextArea txtDescricao;
    private JLabel lblTitulo;

    // Conexão com Dados
    private VeiculoService veiculoService = new VeiculoService();
    private FuncionarioDao funcionarioDao = new FuncionarioDao();
    private OrdemServicoDao osDao = new OrdemServicoDao();

    // Variável de controle para EDIÇÃO
    private OrdemServico ordemEdicao = null;

    // ================== CONSTRUTOR 1: NOVA OS ==================
    public TelaNovaOrdemServico() {
        montarTela();
        // O status padrão é ABERTA e fica bloqueado
        comboStatus.setSelectedItem("ABERTA");
        comboStatus.setEnabled(false);
    }

    // ================== CONSTRUTOR 2: EDIÇÃO ==================
    public TelaNovaOrdemServico(OrdemServico osParaEditar) {
        this.ordemEdicao = osParaEditar;
        montarTela();
        preencherDadosParaEdicao();
    }

    private void montarTela() {
        setTitle("Formulário de Ordem de Serviço");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);
        setContentPane(painel);

        // TÍTULO
        lblTitulo = new JLabel("Nova Ordem de Serviço");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        painel.add(lblTitulo, BorderLayout.NORTH);

        // CENTRO (DIVIDIDO)
        JPanel centro = new JPanel(new GridLayout(2, 1, 0, 20));
        centro.setBackground(Color.WHITE);
        painel.add(centro, BorderLayout.CENTER);

        // --- PARTE 1: TABELA DE VEÍCULOS ---
        JPanel painelVeiculos = new JPanel(new BorderLayout());
        painelVeiculos.setBackground(Color.WHITE);
        painelVeiculos.setBorder(BorderFactory.createTitledBorder("1. Selecione o Veículo"));

        modeloVeiculos = new DefaultTableModel(
                new String[]{"Placa", "Modelo", "Cor", "Cliente"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelaVeiculos = new JTable(modeloVeiculos);
        estilizarTabela(tabelaVeiculos);
        painelVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);
        centro.add(painelVeiculos);

        // --- PARTE 2: FORMULÁRIO ---
        JPanel painelForm = new JPanel(new BorderLayout(10, 10));
        painelForm.setBackground(Color.WHITE);
        painelForm.setBorder(BorderFactory.createTitledBorder("2. Detalhes da OS"));

        // PAINEL SUPERIOR (Responsável + Status)
        JPanel painelTopForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        painelTopForm.setBackground(Color.WHITE);

        // Combo Funcionário
        JLabel lblFunc = new JLabel("Responsável Técnico:");
        lblFunc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comboFuncionario = new JComboBox<>();
        comboFuncionario.setPreferredSize(new Dimension(250, 30));

        painelTopForm.add(lblFunc);
        painelTopForm.add(comboFuncionario);

        // Combo Status
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comboStatus = new JComboBox<>(new String[]{"ABERTA", "FINALIZADA", "CANCELADA"});
        comboStatus.setPreferredSize(new Dimension(150, 30));

        painelTopForm.add(lblStatus);
        painelTopForm.add(comboStatus);

        painelForm.add(painelTopForm, BorderLayout.NORTH);

        // Descrição
        JPanel painelDesc = new JPanel(new BorderLayout());
        painelDesc.setBackground(Color.WHITE);
        JLabel lblDesc = new JLabel("  Descrição do Problema / Serviço:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtDescricao = new JTextArea();
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);

        painelDesc.add(lblDesc, BorderLayout.NORTH);
        painelDesc.add(new JScrollPane(txtDescricao), BorderLayout.CENTER);

        painelForm.add(painelDesc, BorderLayout.CENTER);
        centro.add(painelForm);

        // RODAPÉ (BOTÕES)
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Color.WHITE);
        painel.add(rodape, BorderLayout.SOUTH);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
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
        rodape.add(botoes, BorderLayout.EAST);

        // CARREGAR DADOS INICIAIS
        carregarVeiculos();
        carregarFuncionarios();
    }

    private void preencherDadosParaEdicao() {
        if (ordemEdicao != null) {
            lblTitulo.setText("Editar Ordem de Serviço #" + ordemEdicao.getNumero());
            txtDescricao.setText(ordemEdicao.getDescricao());

            // Seleciona Funcionário
            comboFuncionario.setSelectedItem(ordemEdicao.getResponsavel());

            // Seleciona e Libera o Status
            comboStatus.setSelectedItem(ordemEdicao.getStatus());
            comboStatus.setEnabled(true);

            // Seleciona Veículo
            if (ordemEdicao.getVeiculo() != null) {
                String placaAlvo = ordemEdicao.getVeiculo().getPlacaCarro();
                for (int i = 0; i < tabelaVeiculos.getRowCount(); i++) {
                    String placaLinha = (String) tabelaVeiculos.getValueAt(i, 0);
                    if (placaLinha.equalsIgnoreCase(placaAlvo)) {
                        tabelaVeiculos.setRowSelectionInterval(i, i);
                        tabelaVeiculos.scrollRectToVisible(tabelaVeiculos.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        }
    }

    private void salvarOS() {
        int linha = tabelaVeiculos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um veículo na tabela.");
            return;
        }
        String placa = (String) tabelaVeiculos.getValueAt(linha, 0);
        Veiculo veiculoSelecionado = veiculoService.buscarPorPlaca(placa);

        Funcionario funcionarioSelecionado = (Funcionario) comboFuncionario.getSelectedItem();
        if (funcionarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário.");
            return;
        }

        String descricao = txtDescricao.getText();
        if (descricao.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite a descrição.");
            return;
        }

        // Pega o status escolhido
        String statusEscolhido = (String) comboStatus.getSelectedItem();

        try {
            if (ordemEdicao == null) {
                // === MODO CRIAR ===
                // Criação padrão
                OrdemServico novaOS = new OrdemServico(
                        veiculoSelecionado,
                        funcionarioSelecionado,
                        descricao
                );
                osDao.salvar(novaOS);
                JOptionPane.showMessageDialog(this, "OS criada com sucesso!");

            } else {
                // === MODO EDITAR ===
                OrdemServico osAtualizada = new OrdemServico(
                        ordemEdicao.getNumero(),
                        ordemEdicao.getData(),
                        veiculoSelecionado,
                        funcionarioSelecionado,
                        descricao,
                        statusEscolhido
                );

                osAtualizada.setItensPecas(ordemEdicao.getItensPecas());
                osAtualizada.setItensServicos(ordemEdicao.getItensServicos());

                osDao.atualizar(osAtualizada);
                JOptionPane.showMessageDialog(this, "OS atualizada para: " + statusEscolhido);
            }

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // --- Métodos Auxiliares de Carregamento e Estilo ---
    private void carregarVeiculos() {
        modeloVeiculos.setRowCount(0);
        for (Veiculo v : veiculoService.listarVeiculos()) {
            modeloVeiculos.addRow(new Object[]{
                    v.getPlacaCarro(),
                    v.getModelo(),
                    v.getCor(),
                    v.getDono().getNome()
            });
        }
    }

    private void carregarFuncionarios() {
        comboFuncionario.removeAllItems();
        for (Funcionario f : funcionarioDao.listar()) {
            comboFuncionario.addItem(f);
        }
    }

    private JButton criarBotao(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 38));
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