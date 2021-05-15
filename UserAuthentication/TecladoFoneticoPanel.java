package UserAuthentication;
import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Authentication.PasswordHandler;
import Database.DatabaseHandler;
import Utilities.FrameHandler;
import Utilities.LogHandler;
import Utilities.UserLoginState;

public class TecladoFoneticoPanel extends LoginPanel {
    
    JButton[] keys = new JButton[6];
    PasswordField feedbackField = new PasswordField();

    String emailAddress;
    TecladoFonetico tecladoFonetico;

    public TecladoFoneticoPanel(String emailAddress) {
        this.emailAddress = emailAddress;

        this.prepararCampoDeSenha(257, 200, 185, 35);
        this.prepararLabelErro(225, 240, 250, 20);
        this.prepararBotoes(150, 270, 400, 115);
        
        feedbackField.updatePasswordFeedback(0);
        tecladoFonetico = new TecladoFonetico();
        this.atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
        updateInterface();
        LogHandler.logWithUser(3001);
    }
    
    private void prepararCampoDeSenha(int offsetX, int offsetY, int width, int height) {
        feedbackField.setBounds(offsetX, offsetY, width, height);
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
                botao.setFont(new Font("Arial", Font.PLAIN, 17));
                
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

    void buttonPressed(int index) {
        errorLabel.setText("");
        tecladoFonetico.registrarDigitacao(index);
        updateInterface();
    }

    void updateInterface() {
        int totalDigitados = tecladoFonetico.getFonemasDigitados();
        feedbackField.updatePasswordFeedback(totalDigitados * 2);
        this.atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
        this.loginButton.setEnabled(totalDigitados >= 4 && totalDigitados <= 6);
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
    
    void nextStep() {
        boolean correctPassword;
        UserLoginState newState;
        try {
            correctPassword = PasswordHandler.checkPhoneticPassword(tecladoFonetico.gruposDigitados, emailAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (correctPassword) {
            VerificaChavePanel vcp = new VerificaChavePanel(emailAddress);
            FrameHandler.showPanel(vcp, vcp.loginButton);

            LogHandler.log(3002);
        } else {
            newState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
            if (newState == UserLoginState.BLOCKED) {
                IdentUsuPanel firstPanel = new IdentUsuPanel();
                firstPanel.preencherEmail(emailAddress);
                FrameHandler.showPanel(firstPanel, firstPanel.loginButton);
            } else {
                errorLabel.setText("Senha incorreta.");
                tecladoFonetico.limparDigitacao();
                tecladoFonetico.renovarCombinacoes();
                feedbackField.updatePasswordFeedback(0);
                atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
            }
        }
        
    }
  }
