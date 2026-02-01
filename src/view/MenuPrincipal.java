package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Tenta aplicar o visual do Sistema Operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurações da Janela
        setTitle("Sistema de Gestão - Membro 1");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usa um painel principal com borda para dar margem nas laterais
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(5, 1, 15, 15)); // Espaço maior entre botões (15px)
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margem de 20px em volta de tudo
        painelPrincipal.setBackground(new Color(245, 245, 250)); // Fundo levemente cinza/azulado

        setContentPane(painelPrincipal); // Define esse painel como o principal

        // Título Estilizado
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTÃO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Fonte moderna
        lblTitulo.setForeground(new Color(50, 50, 100)); // Cor azul escuro

        // Criando Botões Estilizados
        JButton btnCliente = criarBotao("Gerenciar Clientes", new Color(70, 130, 180)); // Azul Aço
        JButton btnFuncionario = criarBotao("Gerenciar Funcionários", new Color(60, 179, 113)); // Verde Mar
        JButton btnVeiculo = criarBotao("Gerenciar Veículos", new Color(218, 165, 32)); // Dourado
        JButton btnSair = criarBotao("Sair do Sistema", new Color(205, 92, 92)); // Vermelho Índio

        // Ações
        btnCliente.addActionListener(e -> new TelaCliente().setVisible(true));
        btnFuncionario.addActionListener(e -> new TelaFuncionario().setVisible(true));
        btnVeiculo.addActionListener(e -> new TelaVeiculo().setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));

        // Adicionando
        add(lblTitulo);
        add(btnCliente);
        add(btnFuncionario);
        add(btnVeiculo);
        add(btnSair);
    }

    // Método Auxiliar para criar botões
    private JButton criarBotao(String texto, Color corTema) {
        JButton btn = new JButton(texto);

        // Fonte
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btn.setBackground(Color.WHITE);

        // cor de TEXTO
        btn.setForeground(corTema);

        // Remove o foco
        btn.setFocusPainted(false);

        // Cursor de mãozinha
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Borda grossa com a cor do tema
        btn.setBorder(BorderFactory.createLineBorder(corTema, 2)); // Borda de 2 pixels

        return btn;
    }
}