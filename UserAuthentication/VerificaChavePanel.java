package UserAuthentication;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

import javax.swing.JPasswordField;

import Authentication.AuthenticationHandler;
import Database.DatabaseHandler;
import General.MenuPrincipalPanel;
import Utilities.FrameHandler;
import Utilities.LogHandler;
import Utilities.UserLoginState;
import Authentication.UserState;

public class VerificaChavePanel extends LoginPanel {

    String emailAddress;
    public VerificaChavePanel(String emailAddress) {
        this.emailAddress = emailAddress;
        this.prepararBotaoArquivo(175, 250, 350, 35);
        this.prepararTextoArquivo(175, 280, 350, 35);
        this.prepararCampoFraseSecreta(210, 320, 280, 25);
        this.prepararLabelErro(175, 355, 400, 30);

        LogHandler.logWithUser(4001);
    }
    
    JPasswordField passwordTF;
    private void prepararCampoFraseSecreta(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Frase secreta: ");
        passwordLabel.setBounds(offsetX, offsetY, width*4/10, height);
        this.add(passwordLabel);
        
        passwordTF = new JPasswordField();
        passwordTF.setBounds(offsetX + width*4/10 + 10, offsetY, (width*6/10 - 10), height);
        this.add(passwordTF);
    }

    File chosenFile;
    void prepararBotaoArquivo(int offsetX, int offsetY, int width, int height) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        JButton input = new JButton("Escolher arquivo da chave privada...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = fc.getSelectedFile();
                } else {
                    chosenFile = null;
                }
                atualizarTextoArquivo();
            }
        });
        this.add(input);
        
        clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 5, offsetY, height + 25, height);
        clearButton.setEnabled(false);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chosenFile = null;
                atualizarTextoArquivo();
            }
        });
        this.add(clearButton);
    }
    JButton clearButton;
    JLabel labelCaminho;
    private void prepararTextoArquivo(int offsetX, int offsetY, int width, int height) {
        labelCaminho = new JLabel("Nenhum arquivo escolhido.");
        labelCaminho.setBounds(offsetX, offsetY, width, height);
        add(labelCaminho);
    }
    
    private void atualizarTextoArquivo() {
        if (chosenFile != null) {
            labelCaminho.setText(chosenFile.getPath());
            clearButton.setEnabled(true);
        } else {
            labelCaminho.setText("Nenhum arquivo escolhido.");
            clearButton.setEnabled(false);
        }
    }
    
    boolean checkUserBlocked(String emailAddress) {
        UserLoginState newState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
        if (newState == UserLoginState.BLOCKED) {
            LogHandler.logWithUser(4007);
            LogHandler.logWithUser(4002);
            IdentUsuPanel firstStep = new IdentUsuPanel(emailAddress);
            FrameHandler.showPanel(firstStep, firstStep.loginButton);
            return true;
        } else {
            return false;
        }
    }

    void nextStep() {
        AuthenticationHandler authHandler;
        try {
            authHandler = new AuthenticationHandler();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String fraseSecreta = new String(passwordTF.getPassword());
        byte[] privateKeyContent;
        try {
            privateKeyContent = Files.readAllBytes(chosenFile.toPath());
        } catch (Exception e) {
            LogHandler.logWithUser(4004);
            DatabaseHandler.getInstance().registerAttempts(UserState.emailAddress, false);
            if (checkUserBlocked(emailAddress) == false) {
                errorLabel.setText("Caminho da chave privada inv??lido.");
                chosenFile = null;
                atualizarTextoArquivo();
                passwordTF.setText("");
            }
            return;
        }
        boolean validPrivateKey = authHandler.verifyUserPrivateKey(privateKeyContent, fraseSecreta, emailAddress);
        if (validPrivateKey) {
            LogHandler.logWithUser(4003);
            LogHandler.logWithUser(4002);
            DatabaseHandler.getInstance().registerAccess(UserState.emailAddress);
            DatabaseHandler.getInstance().registerAttempts(UserState.emailAddress, true);
            DatabaseHandler.getInstance().updateUserState(emailAddress);
            FrameHandler.showPanel(new MenuPrincipalPanel());
        } else {
            DatabaseHandler.getInstance().registerAttempts(UserState.emailAddress, false);
            if (checkUserBlocked(emailAddress) == false) {
                errorLabel.setText("<html>Assinatura digital inv??lida ou frase<br>secreta inv??lida.</html>");
                chosenFile = null;
                atualizarTextoArquivo();
                passwordTF.setText("");
            }
        }
    }
    
    
  }
