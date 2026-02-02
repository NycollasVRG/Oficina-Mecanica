package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Visual do Sistema Operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurações da Janela
        setTitle("Sistema de Gestão");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(6, 1, 15, 15));
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(new Color(245, 245, 250));

        setContentPane(painelPrincipal);

        // Título
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTÃO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(50, 50, 100));

        // Botões
        JButton btnNovaOS = criarBotao("Nova Ordem de Serviço", new Color(138, 43, 226));
        JButton btnCliente = criarBotao("Gerenciar Clientes", new Color(70, 130, 180));
        JButton btnFuncionario = criarBotao("Gerenciar Funcionários", new Color(60, 179, 113));
        JButton btnVeiculo = criarBotao("Gerenciar Veículos", new Color(218, 165, 32));
        JButton btnSair = criarBotao("Sair do Sistema", new Color(205, 92, 92));

        // Ações
        btnNovaOS.addActionListener(e -> new TelaNovaOrdemServico().setVisible(true));
        btnCliente.addActionListener(e -> new TelaCliente().setVisible(true));
        btnFuncionario.addActionListener(e -> new TelaFuncionario().setVisible(true));
        btnVeiculo.addActionListener(e -> new TelaVeiculo().setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));

        // Adicionando componentes
        painelPrincipal.add(lblTitulo);
        painelPrincipal.add(btnNovaOS);
        painelPrincipal.add(btnCliente);
        painelPrincipal.add(btnFuncionario);
        painelPrincipal.add(btnVeiculo);
        painelPrincipal.add(btnSair);
    }

    // Método auxiliar para criar botões padronizados
    private JButton criarBotao(String texto, Color corTema) {
        JButton btn = new JButton(texto);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(corTema);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(corTema, 2));

        return btn;
    }

    // Main para teste
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
