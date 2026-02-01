package view;

import model.Cliente;
import service.ClienteService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaCliente extends JFrame {

    private JTextField txtCpf = new JTextField();
    private JTextField txtNome = new JTextField();
    private JTextField txtTelefone = new JTextField();
    private JTextField txtRua = new JTextField();
    private JTextField txtBairro = new JTextField();
    private JTextField txtCidade = new JTextField();
    private JTextField txtNumero = new JTextField();
    private JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Comum", "Mensalista"});

    private ClienteService service = new ClienteService();
    private Integer idEdicao = null;

    public TelaCliente() { iniciar("Novo Cliente"); }
    public TelaCliente(Cliente c) { iniciar("Editar Cliente"); preencher(c); }

    private void iniciar(String titulo) {
        setTitle(titulo);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(10, 2, 10, 15));
        painel.setBackground(Color.WHITE);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        painel.add(criarLabel("CPF:")); painel.add(estilizarCampo(txtCpf));
        painel.add(criarLabel("Nome:")); painel.add(estilizarCampo(txtNome));
        painel.add(criarLabel("Telefone:")); painel.add(estilizarCampo(txtTelefone));
        painel.add(criarLabel("Rua:")); painel.add(estilizarCampo(txtRua));
        painel.add(criarLabel("Bairro:")); painel.add(estilizarCampo(txtBairro));
        painel.add(criarLabel("Cidade:")); painel.add(estilizarCampo(txtCidade));
        painel.add(criarLabel("Número:")); painel.add(estilizarCampo(txtNumero));
        painel.add(criarLabel("Tipo:")); cbTipo.setBackground(Color.WHITE); painel.add(cbTipo);

        // BOTÕES PADRONIZADOS
        JButton btnSalvar = criarBotao("Salvar", new Color(46, 204, 113));   // VERDE
        JButton btnCancelar = criarBotao("Cancelar", new Color(231, 76, 60)); // VERMELHO
        JButton btnLista = criarBotao("Ver Lista", new Color(52, 152, 219)); // AZUL

        painel.add(btnSalvar);
        painel.add(btnCancelar);
        painel.add(btnLista);
        painel.add(new JLabel("")); // Espaço vazio para alinhar

        add(painel);

        // Ações
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        btnLista.addActionListener(e -> new TelaListaClientes().setVisible(true));
    }

    private void preencher(Cliente c) {
        this.idEdicao = c.getCodCliente();
        txtCpf.setText(c.getCpf()); txtCpf.setEditable(false);
        txtNome.setText(c.getNome()); txtTelefone.setText(c.getTelefone());
        txtRua.setText(c.getRua()); txtBairro.setText(c.getBairro());
        txtCidade.setText(c.getCidade()); txtNumero.setText(c.getNumero());
        cbTipo.setSelectedItem(c.getTipoCliente());
    }

    private void salvar() {
        try {
            int id = (idEdicao == null) ? 0 : idEdicao;
            Cliente c = new Cliente(txtCpf.getText(), txtNome.getText(), txtTelefone.getText(),
                    txtRua.getText(), txtBairro.getText(), txtCidade.getText(),
                    txtNumero.getText(), (String) cbTipo.getSelectedItem(), id);

            if (id == 0) service.cadastrarCliente(c); else service.atualizar(c);
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
            if(idEdicao == null) limpar(); else dispose();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()); }
    }

    private void limpar() {
        txtCpf.setText(""); txtNome.setText(""); txtTelefone.setText("");
        txtRua.setText(""); txtBairro.setText(""); txtCidade.setText(""); txtNumero.setText("");
    }

    // ESTILOS
    private JLabel criarLabel(String t) { JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 13)); return l; }
    private JTextField estilizarCampo(JTextField t) { t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); return t; }

    // MÉTODO QUE PADRONIZA OS BOTÕES
    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE); // TEXTO BRANCO
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}