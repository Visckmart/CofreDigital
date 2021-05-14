package UserAuthentication;
import javax.crypto.BadPaddingException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.awt.Color;

import javax.swing.JPasswordField;

import Authentication.AuthenticationHandler;
import Database.DatabaseHandler;
import General.MenuPrincipalPanel;
import Utilities.LogHandler;
import Utilities.UserLoginState;
import Authentication.UserState;

public class VerificaChavePanel extends JPanel {
    
    String emailAddress;
    public VerificaChavePanel(String emailAddress) {
        this.setLayout(null);
        this.emailAddress = emailAddress;
        add(TitlePanel.getInstance());
        this.prepararBotaoArquivo(210, 250, 280, 35);
        this.prepararTextoArquivo(210, 280, 280, 35);
        this.prepararCampoFraseSecreta(210, 320, 280, 25);
        this.prepararLabelErro(210, 350, 280, 30);
        this.prepararBotaoLogin(285, 400, 130, 35);
    }
    
    JPasswordField passwordTF;
    private void prepararCampoFraseSecreta(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Frase secreta: ");
        passwordLabel.setBounds(offsetX, offsetY, width*4/10, height);
        this.add(passwordLabel);
        
        passwordTF = new JPasswordField();
        passwordTF.setBounds(offsetX + width*4/10 + 10, offsetY, (width*6/10 - 10), height);
        passwordTF.setText("user01");
        this.add(passwordTF);
    }
    
    JLabel errorLabel;
    private void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    
    File chosenFile = new File("/Users/victormartins/Documents/CofreDigital/./Pacote-T4/Keys/user01-pkcs8-des.key");
    void prepararBotaoArquivo(int offsetX, int offsetY, int width, int height) {
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        // fc.setCurrentDirectory(new File("./Pacote-T4/Keys"));
        JButton input = new JButton("Escolher arquivo da chave privada...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = fc.getSelectedFile();
                    System.out.println(chosenFile);
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + chosenFile.getName() + ".");
                } else {
                    chosenFile = null;
                    System.out.println("Open command cancelled by user.");
                }
                atualizarTextoArquivo();
            }
        });
        this.add(input);
        
        clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 5, offsetY, height, height);
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
    
    void nextStep() {
        AuthenticationHandler authHandler;
        boolean validPrivateKey;
        try {
            authHandler = new AuthenticationHandler();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String fraseSecreta = new String(passwordTF.getPassword());
        try {
            validPrivateKey = authHandler.verifyUserPrivateKey(chosenFile.toPath(), fraseSecreta, emailAddress);
            if (validPrivateKey) {
                LogHandler.logWithUser(4002);
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new MenuPrincipalPanel());
                frame.invalidate();
                frame.validate();
                return;
            } else {
            }
        } catch (BadPaddingException e) {
            errorLabel.setText("Frase secreta incorreta.");
            System.out.println("Frase secreta incorreta.");
            try {
                boolean userBlocked = DatabaseHandler.getInstance().verifyUserEmail(emailAddress) == UserLoginState.BLOCKED;
                if (userBlocked) {
                    LogHandler.logWithUser(4007);
                    JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                    IdentUsuPanel vcp = new IdentUsuPanel();
                    frame.setContentPane(vcp);
                    frame.invalidate();
                    frame.validate();
                    frame.getRootPane().setDefaultButton(vcp.loginButton);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    
  }
