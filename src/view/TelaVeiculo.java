package view;

import model.Cliente;
import model.Veiculo;
import service.ClienteService;
import service.VeiculoService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaVeiculo extends JFrame {
    private JTextField txtPlaca = new JTextField();
    private JTextField txtModelo = new JTextField();
    private JTextField txtMarca = new JTextField();
    private JTextField txtCor = new JTextField();
    private JTextField txtCpfDono = new JTextField();

    private VeiculoService veiculoService = new VeiculoService();
    private ClienteService clienteService = new ClienteService();
    private boolean editando = false;

    public TelaVeiculo() { iniciar("Novo Veículo"); }
    public TelaVeiculo(Veiculo v) { iniciar("Editar Veículo"); preencher(v); }

    private void iniciar(String t) {
        setTitle(t); setSize(500, 500); setDefaultCloseOperation(DISPOSE_ON_CLOSE); setLocationRelativeTo(null);
        JPanel p = new JPanel(new GridLayout(7, 2, 10, 10));
        p.setBorder(new EmptyBorder(20,20,20,20)); p.setBackground(Color.WHITE);

        p.add(criarLabel("Placa:")); p.add(estilizarCampo(txtPlaca));
        p.add(criarLabel("Modelo:")); p.add(estilizarCampo(txtModelo));
        p.add(criarLabel("Marca:")); p.add(estilizarCampo(txtMarca));
        p.add(criarLabel("Cor:")); p.add(estilizarCampo(txtCor));
        p.add(criarLabel("CPF Dono:")); p.add(estilizarCampo(txtCpfDono));

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
        bLista.addActionListener(e -> new TelaListaVeiculos().setVisible(true));
    }

    private void preencher(Veiculo v) {
        editando = true;
        txtPlaca.setText(v.getPlacaCarro()); txtPlaca.setEditable(false);
        txtModelo.setText(v.getModelo()); txtMarca.setText(v.getMarca());
        txtCor.setText(v.getCor()); txtCpfDono.setText(v.getDono().getCpf());
    }

    private void salvar() {
        try {
            Cliente dono = clienteService.buscarPorCPF(txtCpfDono.getText());
            if(dono == null) { JOptionPane.showMessageDialog(this, "Dono não encontrado!"); return; }
            Veiculo v = new Veiculo(txtPlaca.getText(), txtCor.getText(), txtModelo.getText(), txtMarca.getText(), dono);
            if(editando) veiculoService.atualizar(v); else veiculoService.cadastrar(v);
            JOptionPane.showMessageDialog(this, "Salvo!");
            if(!editando) { txtPlaca.setText(""); txtModelo.setText(""); } else dispose();
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