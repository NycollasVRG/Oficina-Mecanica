package view;

import model.Peca;
import service.PecaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

public class TelaCadastroPeca extends JFrame {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtQuantidade;

    private PecaService pecaService = new PecaService();

    public TelaCadastroPeca() {
        setTitle("Cadastro de Peças");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Define visual do sistema
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        initComponents();
    }

    private void initComponents() {
        // Painel Principal com fundo branco e bordas
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        setContentPane(contentPane);

        // ===== TÍTULO =====
        JLabel lblTitulo = new JLabel("Nova Peça", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(50, 50, 50));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // ===== FORMULÁRIO (CENTRO) =====
        // GridLayout: 6 linhas (Label, Campo, Label, Campo...)
        JPanel panelForm = new JPanel(new GridLayout(6, 1, 5, 5));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(20, 0, 20, 0));

        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQuantidade = new JTextField();

        panelForm.add(criarLabel("Nome da Peça:"));
        panelForm.add(estilizarCampo(txtNome));

        panelForm.add(criarLabel("Preço (R$):"));
        panelForm.add(estilizarCampo(txtPreco));

        panelForm.add(criarLabel("Quantidade Inicial:"));
        panelForm.add(estilizarCampo(txtQuantidade));

        contentPane.add(panelForm, BorderLayout.CENTER);

        // ===== BOTÕES (RODAPÉ) =====
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotoes.setBackground(Color.WHITE);

        JButton btnSalvar = criarBotao("Salvar", new Color(46, 204, 113));   // Verde
        JButton btnCancelar = criarBotao("Cancelar", new Color(231, 76, 60)); // Vermelho

        btnSalvar.addActionListener(e -> salvarPeca());
        btnCancelar.addActionListener(e -> dispose());

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnCancelar);

        contentPane.add(panelBotoes, BorderLayout.SOUTH);
    }

    private void salvarPeca() {
        try {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome é obrigatório.");
                return;
            }

            String nome = txtNome.getText();
            // Troca vírgula por ponto para evitar erro de conversão
            String precoStr = txtPreco.getText().replace(",", ".");
            BigDecimal preco = new BigDecimal(precoStr);
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            Peca peca = new Peca(nome, preco, quantidade);

            boolean sucesso = pecaService.cadastrarPeca(peca);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Peça cadastrada com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar a peça.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os números digitados (Preço ou Quantidade).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
        txtNome.requestFocus();
    }

    // ================= MÉTODOS DE ESTILO (PADRÃO) =================

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(50, 50, 50));
        // Alinha o texto na parte inferior do espaço do grid para ficar perto do campo
        lbl.setVerticalAlignment(SwingConstants.BOTTOM);
        return lbl;
    }

    private JTextField estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding interno no texto
        ));
        return txt;
    }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }
}