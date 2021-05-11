package UserAuthentication;
import javax.crypto.BadPaddingException;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PrivateKey;

import javax.swing.JPasswordField;

import Authentication.AuthenticationHandler;
import Database.DatabaseHandler;
import General.MenuPrincipalPanel;
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
        this.prepararBotaoLogin(285, 400, 130, 35);
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
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./Pacote-T4/Keys"));
        JButton input = new JButton("Escolher arquivo da chave privada...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = fc.getSelectedFile();
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
        try {
            AuthenticationHandler ah = new AuthenticationHandler();
            byte[] content = Files.readAllBytes(chosenFile.toPath());
            PrivateKey pk = ah.privateKeyFromFile(content, new String(passwordTF.getPassword()).getBytes(StandardCharsets.UTF_8));
            UserState.privateKey = pk;
            System.out.println(pk);
            // System.out.println(DatabaseHandler.getInstance().getEncodedCertificate("aa"));
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new MenuPrincipalPanel());
            frame.invalidate();
            frame.validate();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.out.println("Frase secreta incorreta.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
  }
