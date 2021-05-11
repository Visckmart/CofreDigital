package UserAuthentication;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Authentication.PasswordHandler;
import Utilities.LogHandler;

public class TecladoFoneticoPanel extends JPanel {
    
    JButton[] keys = new JButton[6];
    JTextPane feedbackField = new JTextPane();

    String emailAddress;
    TecladoFonetico tecladoFonetico;

    public TecladoFoneticoPanel(String emailAddress) {
        this.emailAddress = emailAddress;

        this.setLayout(null);
        add(TitlePanel.getInstance());
        this.prepararCampoDeSenha(257, 200, 185, 35);
        this.prepararLabelErro(225, 240, 250, 20);
        this.prepararBotoes(150, 270, 400, 115);
        this.prepararBotaoLogin(285, 400, 130, 35);
        
        updatePasswordFeedback(0);
        tecladoFonetico = new TecladoFonetico();
        this.atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
        
        LogHandler.log(3001, emailAddress);
    }
    
    private void prepararCampoDeSenha(int offsetX, int offsetY, int width, int height) {
        feedbackField.setBounds(offsetX, offsetY, width, height);
        feedbackField.setFont(new Font(null, Font.PLAIN, 25));
        feedbackField.setBackground(Color.black);
        feedbackField.setForeground(Color.white);
        add(feedbackField);
        JButton clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 10, offsetY, height, height);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tecladoFonetico.limparDigitacao();
                updateInterface();
            }
        });
        add(clearButton);
    }
    
    int linhasBotoes = 2;
    int colunasBotoes = 3;
    private void prepararBotoes(int xOffset, int yOffset, int width, int height) {
        int btnHSpace = width/colunasBotoes;
        int btnVSpace = height/linhasBotoes;
        for (int linha = 0; linha < linhasBotoes; linha++) {
            for (int coluna = 0; coluna < colunasBotoes; coluna++) {
                JButton botao = new JButton("-");
                int btnX = xOffset + (btnHSpace * coluna);
                int btnY = yOffset + (btnVSpace * linha);
                botao.setBounds(btnX, btnY, btnHSpace - 5, btnVSpace - 5);
                botao.setFont(new Font(null, Font.PLAIN, 17));
                
                int btnIndex = (linha * colunasBotoes) + coluna;
                botao.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttonPressed(btnIndex);
                    }
                });
                this.keys[btnIndex] = botao;
                this.add(botao);
            }
        }
    }
    
    JLabel errorLabel;
    private void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    
    JButton loginButton;
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        this.add(loginButton);
    }

    void atualizarBotoes(List<String> fonemas) {
        assert fonemas.size() == keys.length : "Atualização de botões não contém o número correto de fonemas";
        boolean bloquear = tecladoFonetico.getFonemasDigitados() * 2 >= 12;
        for (int i = 0; i < keys.length; i++) {
            if (bloquear) {
                keys[i].setEnabled(false);
            } else {
                keys[i].setEnabled(true);
                keys[i].setText(fonemas.get(i));
            }
        }
    }

    private void updatePasswordFeedback(int filledCharacters) {
        SimpleAttributeSet attributeSet;
        StyledDocument doc;
        try {
            attributeSet = new SimpleAttributeSet();
            feedbackField.setCharacterAttributes(attributeSet, true);

            feedbackField.setText("•".repeat(filledCharacters));
            
            int remaining = 8-filledCharacters;
            if (remaining > 0) {
                doc = feedbackField.getStyledDocument();
                attributeSet = new SimpleAttributeSet();  
                StyleConstants.setForeground(attributeSet, Color.gray);
                doc.insertString(doc.getLength(), "•".repeat(remaining), attributeSet);  
            }
            
            int remainingExtra;
            if (remaining > 0) {
                remainingExtra = 4;
            } else {
                remainingExtra = 12-filledCharacters;
            }
            if (remainingExtra > 0) {
                attributeSet = new SimpleAttributeSet();  
                StyleConstants.setForeground(attributeSet, Color.DARK_GRAY);
                doc = feedbackField.getStyledDocument();  
                doc.insertString(doc.getLength(), "•".repeat(remainingExtra), attributeSet); 
            }

            doc = feedbackField.getStyledDocument(); 
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            
            loginButton.setEnabled(remaining <= 0);
        } catch (Exception e) {

        }
    }
    
    void updateInterface() {
        updatePasswordFeedback(tecladoFonetico.getFonemasDigitados() * 2);
        this.atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
    }
    void buttonPressed(int index) {
        errorLabel.setText("");
        tecladoFonetico.registrarDigitacao(index);
        updateInterface();
    }
    
    void nextStep() {
        boolean correctPassword;
        try {
            correctPassword = PasswordHandler.checkPhoneticPassword(tecladoFonetico.gruposDigitados, emailAddress);
            if (correctPassword == true) {
                System.out.println("Correct");
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new VerificaChavePanel(emailAddress));
                frame.invalidate();
                frame.validate();
            } else {
                System.out.println("Wrong");
                errorLabel.setText("Senha incorreta.");
                tecladoFonetico.limparDigitacao();
                tecladoFonetico.renovarCombinacoes();
                updatePasswordFeedback(0);
                atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }
