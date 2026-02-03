package view;

import service.PecaService;
import model.Peca;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class TelaCadastroPeca extends JFrame {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtQuantidade;

    private PecaService pecaService = new PecaService();

    public TelaCadastroPeca() {
        setTitle("Cadastro de Peças");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Nome da Peça:"));
        txtNome = new JTextField();
        panel.add(txtNome);

        panel.add(new JLabel("Preço (R$):"));
        txtPreco = new JTextField();
        panel.add(txtPreco);

        panel.add(new JLabel("Quantidade Inicial:"));
        txtQuantidade = new JTextField();
        panel.add(txtQuantidade);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> salvarPeca());
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);
    }

    private void salvarPeca() {
        try {
            String nome = txtNome.getText();
            BigDecimal preco = new BigDecimal(txtPreco.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            Peca peca = new Peca(nome, preco, quantidade);

            boolean sucesso = pecaService.cadastrarPeca(peca);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Peça cadastrada com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar a peça.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
    }
}
