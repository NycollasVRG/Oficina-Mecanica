package view;

import model.Funcionario;
import service.FuncionarioService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaFuncionario extends JFrame {
    private JTextField txtCpf = new JTextField();
    private JTextField txtNome = new JTextField();
    private JTextField txtTelefone = new JTextField();
    private JTextField txtRua = new JTextField();
    private JTextField txtBairro = new JTextField();
    private JTextField txtCidade = new JTextField();
    private JTextField txtNumero = new JTextField();
    private JTextField txtCargo = new JTextField();
    private JTextField txtSalario = new JTextField();
    private JTextField txtData = new JTextField();

    private FuncionarioService service = new FuncionarioService();
    private Integer matriculaEdicao = null;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaFuncionario() { iniciar("Novo Funcionário"); }
    public TelaFuncionario(Funcionario f) { iniciar("Editar Funcionário"); preencher(f); }

    private void iniciar(String titulo) {
        setTitle(titulo); setSize(600, 700); setDefaultCloseOperation(DISPOSE_ON_CLOSE); setLocationRelativeTo(null);
        JPanel p = new JPanel(new GridLayout(12, 2, 10, 10));
        p.setBorder(new EmptyBorder(20,20,20,20)); p.setBackground(Color.WHITE);

        p.add(criarLabel("CPF:")); p.add(estilizarCampo(txtCpf));
        p.add(criarLabel("Nome:")); p.add(estilizarCampo(txtNome));
        p.add(criarLabel("Telefone:")); p.add(estilizarCampo(txtTelefone));
        p.add(criarLabel("Rua:")); p.add(estilizarCampo(txtRua));
        p.add(criarLabel("Bairro:")); p.add(estilizarCampo(txtBairro));
        p.add(criarLabel("Cidade:")); p.add(estilizarCampo(txtCidade));
        p.add(criarLabel("Número:")); p.add(estilizarCampo(txtNumero));
        p.add(criarLabel("Cargo:")); p.add(estilizarCampo(txtCargo));
        p.add(criarLabel("Salário:")); p.add(estilizarCampo(txtSalario));
        p.add(criarLabel("Data (dd/MM/yyyy):")); p.add(estilizarCampo(txtData));

        // BOTÕES PADRONIZADOS
        JButton bSalvar = criarBotao("Salvar", new Color(46, 204, 113));   // VERDE
        JButton bCancelar = criarBotao("Cancelar", new Color(231, 76, 60)); // VERMELHO
        JButton bLista = criarBotao("Ver Lista", new Color(52, 152, 219)); // AZUL

        p.add(bSalvar);
        p.add(bCancelar);
        p.add(bLista);
        p.add(new JLabel(""));

        add(p);

        bSalvar.addActionListener(e -> salvar());
        bCancelar.addActionListener(e -> dispose());
        bLista.addActionListener(e -> new TelaListaFuncionarios().setVisible(true));
    }

    private void preencher(Funcionario f) {
        matriculaEdicao = f.getMatricula();
        txtCpf.setText(f.getCpf()); txtCpf.setEditable(false);
        txtNome.setText(f.getNome()); txtTelefone.setText(f.getTelefone());
        txtRua.setText(f.getRua()); txtBairro.setText(f.getBairro());
        txtCidade.setText(f.getCidade()); txtNumero.setText(f.getNumero());
        txtCargo.setText(f.getCargo()); txtSalario.setText(f.getSalario().toString());
        txtData.setText(f.getDataAdmissao().format(fmt));
    }

    private void salvar() {
        try {
            int mat = (matriculaEdicao == null) ? 0 : matriculaEdicao;
            Funcionario f = new Funcionario(
                    txtCpf.getText(), txtNome.getText(), txtTelefone.getText(),
                    txtRua.getText(), txtBairro.getText(), txtCidade.getText(), txtNumero.getText(),
                    mat, new BigDecimal(txtSalario.getText().replace(",", ".")), txtCargo.getText(), LocalDate.parse(txtData.getText(), fmt)
            );
            if(mat == 0) service.cadastrar(f); else service.atualizar(f);
            JOptionPane.showMessageDialog(this, "Salvo!");
            if(matriculaEdicao == null) { txtCpf.setText(""); txtNome.setText(""); } else dispose();
        } catch(Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }

    // --- ESTILOS ---
    private JLabel criarLabel(String t) { JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 13)); return l; }
    private JTextField estilizarCampo(JTextField t) { t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); return t; }

    private JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}