package General;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Authentication.PasswordHandler;
import Authentication.UserState;
import Database.DatabaseHandler;
import UserAuthentication.TecladoFonetico;
import Utilities.FrameHandler;
import Utilities.LogHandler;

public class TecladoFoneticoFullPanel extends GeneralPanel {
    
    JButton[] keys = new JButton[18];
    JTextPane feedbackField = new JTextPane();

    List<String> fonemasDigitados = new ArrayList<String>();
    List<String> fonemasCorretos;

    public enum PasswordGoal {
        CADASTRAR, ALTERAR
    }
    PasswordGoal goal;
    public TecladoFoneticoFullPanel(String title, List<String> newPassword, PasswordGoal goal) {
        super(title, true, goal == PasswordGoal.CADASTRAR ? CabecalhoInfo.TOTAL_USUARIOS : CabecalhoInfo.TOTAL_ACESSOS);
        this.goal = goal;
        this.fonemasCorretos = newPassword;

        this.prepararCampoDeSenha(257, 180, 185, 35);
        this.prepararLabelErro(225, 220, 250, 20);
        this.prepararBotoes(250, 245, 200, 200);
        this.prepararBotaoLogin(285, 450, 130, 35);
        updatePasswordFeedback(0);
        
        LogHandler.logWithUser(3001);
    }

    @Override protected int getBackCode() {
        if(this.goal == PasswordGoal.CADASTRAR) {
            return 6007;
        } else {
            return 7006;
        }
    }
    
    private void prepararCampoDeSenha(int offsetX, int offsetY, int width, int height) {
        feedbackField.setBounds(offsetX, offsetY, width, height);
        feedbackField.setFont(new Font(null, Font.PLAIN, 25));
        feedbackField.setBackground(Color.black);
        feedbackField.setForeground(Color.white);
        add(feedbackField);
        JButton clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 10, offsetY, height + 35, height);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fonemasDigitados.clear();
                updateInterface();
            }
        });
        add(clearButton);
    }
    
    int linhasBotoes = 6;
    int colunasBotoes = 3;
    private void prepararBotoes(int xOffset, int yOffset, int width, int height) {
        int btnHSpace = width/colunasBotoes;
        int btnVSpace = height/linhasBotoes;
        for (int linha = 0; linha < linhasBotoes; linha++) {
            for (int coluna = 0; coluna < colunasBotoes; coluna++) {
                int btnIndex = (linha * colunasBotoes) + coluna;
                JButton botao = new JButton(TecladoFonetico.fonemas[btnIndex]);
                int btnX = xOffset + (btnHSpace * coluna);
                int btnY = yOffset + (btnVSpace * linha);
                botao.setBounds(btnX, btnY, btnHSpace - 2, btnVSpace - 2);
                botao.setFont(new Font("Arial", Font.PLAIN, 17));
                
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
        if (fonemasCorretos != null && goal == PasswordGoal.ALTERAR) {
            loginButton.setText("Confirmar alterações");
        }
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        this.add(loginButton);
    }

    private void updatePasswordFeedback(int filledCharacters) {
        SimpleAttributeSet attributeSet;
        StyledDocument doc;
        try {
            attributeSet = new SimpleAttributeSet();
            feedbackField.setCharacterAttributes(attributeSet, true);

            feedbackField.setText(new String(new char[filledCharacters]).replace("\0", "•"));
            
            int remaining = 8-filledCharacters;
            if (remaining > 0) {
                doc = feedbackField.getStyledDocument();
                attributeSet = new SimpleAttributeSet();  
                StyleConstants.setForeground(attributeSet, Color.gray);

                doc.insertString(doc.getLength(),new String(new char[remaining]).replace("\0", "•"), attributeSet);
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

                doc.insertString(doc.getLength(), new String(new char[remainingExtra]).replace("\0", "•"), attributeSet);
            }

            doc = feedbackField.getStyledDocument(); 
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            
            loginButton.setEnabled(remaining <= 0 || goal == PasswordGoal.ALTERAR);
        } catch (Exception e) {

        }
    }
    void prepareBackButton(int offsetX, int offsetY, int width, int height) {
        JButton backButton = new JButton("<   Voltar ao menu");
        backButton.setBounds(offsetX, offsetY, width, height);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(backButton);
                frame.setContentPane(new MenuPrincipalPanel());
                frame.invalidate();
                frame.validate();
            }
        });
        add(backButton);
    }
    void updateInterface() {
        int totalFonemasDigitados = fonemasDigitados.size();
        updatePasswordFeedback(totalFonemasDigitados * 2);
        boolean bloquear = totalFonemasDigitados * 2 >= 12;
        for (int i = 0; i < keys.length; i++) {
            if (bloquear) {
                keys[i].setEnabled(false);
            } else {
                keys[i].setEnabled(true);
            }
        }
    }
    void buttonPressed(int index) {
        errorLabel.setText("");
        fonemasDigitados.add(TecladoFonetico.fonemas[index]);
        updateInterface();
    }
    
    boolean verificarSenha() {
        if (goal == PasswordGoal.ALTERAR && ignorarConfirmacao == true) {
            if (fonemasDigitados.size() == 0) {
                return true;
            }
        }
        if (!(fonemasDigitados.size() >= 4 && fonemasDigitados.size() <= 6)) {
            if (goal == PasswordGoal.CADASTRAR) {
                LogHandler.logWithUser(6003);
            } else {
                LogHandler.logWithUser(7002);
            }
            errorLabel.setText("Quantidade de fonemas inválida.");
            return false;
        }
        boolean noSequentialDuplicates = true;

        String ultimoFonema="";
        for (String fonema : fonemasDigitados) {
            if (fonema.equals(ultimoFonema)) {
                noSequentialDuplicates = false;
                break;
            }
            ultimoFonema = fonema;
        }
        if (noSequentialDuplicates) {
            return true;
        } else {
            if (goal == PasswordGoal.CADASTRAR) {
                LogHandler.logWithUser(6003);
            } else {
                LogHandler.logWithUser(7002);
            }
            errorLabel.setText("Não use fonemas iguais em sequência.");
            return false;
        }
    }
    boolean ignorarConfirmacao = false;
    void nextStep() {
        if (goal == PasswordGoal.ALTERAR) {
            if (fonemasDigitados.size() == 0 && ignorarConfirmacao == false) {
                TecladoFoneticoFullPanel confirmarSenhaPanel = new TecladoFoneticoFullPanel("Confirmar Senha", fonemasDigitados, goal);
                confirmarSenhaPanel.ignorarConfirmacao = true;
                FrameHandler.showPanel(confirmarSenhaPanel, confirmarSenhaPanel.loginButton);
                return;
            }
        }
        if (!verificarSenha()) {
            fonemasDigitados.clear();
            updateInterface();
            return;
        }
        if (fonemasCorretos == null && ignorarConfirmacao == false) {
            TecladoFoneticoFullPanel confirmarSenhaPanel = new TecladoFoneticoFullPanel("Confirmar Senha", fonemasDigitados, goal);
            FrameHandler.showPanel(confirmarSenhaPanel, confirmarSenhaPanel.loginButton);
            return;
        }
        if (fonemasCorretos.equals(fonemasDigitados) == false) {
            fonemasDigitados.clear();
            updateInterface();
            errorLabel.setText("Confirmação de senha incorreta");
            return;
        }
        if (goal == PasswordGoal.CADASTRAR) {
            LogHandler.logWithUser(6002);
            String newUserPassword = String.join("", fonemasDigitados);
            UserState.newUserPassword = newUserPassword;
            FrameHandler.showPanel(new ConfirmacaoCadastroPanel());
            return;
        } else {
            if (ignorarConfirmacao == false) {
                if (UserState.newUserCertificateContent != null) {
                    DatabaseHandler.getInstance().updateUserCertificate(UserState.emailAddress, UserState.newUserCertificateContent);
                    UserState.newUserCertificateContent = null;
                }
                LogHandler.logWithUser(7004);
                String salt = PasswordHandler.generateSalt();
                String senha = PasswordHandler.encodePassword(String.join("", fonemasDigitados), salt);
                DatabaseHandler.getInstance().updateUserPassword(UserState.emailAddress, senha, salt);
            }
            FrameHandler.showPanel(new MenuPrincipalPanel());
        }
    }
  }
