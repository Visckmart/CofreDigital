package General;
import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.Optional;

import Authentication.AuthenticationHandler;
import Authentication.UserState;
import General.TecladoFoneticoFullPanel.PasswordGoal;
import Utilities.FrameHandler;
import Utilities.LogHandler;

public class AlterarCertificadoPanel extends GeneralPanel {
    
    JButton[] keys = new JButton[18];
    JTextArea pathLabel;

    @Override protected int getBackCode() {
        return 7006;
    }

    public AlterarCertificadoPanel() {
        super("Alterar Certificado", true);
        
        this.prepararCertificateChooser(225, 250, 250, 35);
        this.prepararPathLabel(150, 290, 400, 50);
        this.prepararLabelErro(225, 240, 250, 20);
        this.prepararBotaoLogin(285, 350, 130, 35);
        
        LogHandler.logWithUser(7001);
    }
    
    File chosenFile;
    private void prepararCertificateChooser(int offsetX, int offsetY, int width, int height) {
        JButton chooseButton = new JButton("Escolher arquivo do certificado...");
        chooseButton.setBounds(offsetX, offsetY, width, height);
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                int returnVal = fc.showOpenDialog(chooseButton);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = fc.getSelectedFile();
                } else {

                }
                updateInterface();
            }
        });
        add(chooseButton);
        JButton clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 5, offsetY, height, height);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chosenFile = null;
                updateInterface();
            }
        });
        add(clearButton);
    }

    private void prepararPathLabel(int xOffset, int yOffset, int width, int height) {
        pathLabel = new JTextArea();
        pathLabel.setLineWrap(true);
        pathLabel.setOpaque(false);
        pathLabel.setEditable(false);
        pathLabel.setWrapStyleWord(true);
        pathLabel.setFocusable(false);
        pathLabel.setBounds(xOffset, yOffset, width, height);
        add(pathLabel);
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
        if (chosenFile == null) {
            pathLabel.setText("");
            return;
        }
        pathLabel.setText(chosenFile.getAbsolutePath());
    }
    
    
    void nextStep() {
        if (chosenFile != null) {
            // Atualizar certificado
            byte[] certificateContent;
            try {
                certificateContent = Files.readAllBytes(chosenFile.toPath());
            } catch (Exception e) {
                LogHandler.logWithUser(7003);
                errorLabel.setText("Caminho para o certificado inválido.");
                return;
            }
            AuthenticationHandler handler;
            try {
                handler = new AuthenticationHandler();
            } catch(Exception e) {
                return;
            }
            Certificate cert;
            try {
                cert = handler.certificateFromFile(certificateContent);
            } catch (Exception e) {
                errorLabel.setText("Certificado inválido.");
                LogHandler.logWithUser(7005);
                return;
            }
            try {
                AuthenticationHandler.checkCertificate(cert, Optional.of(UserState.emailAddress));
            } catch (CertificateNotYetValidException | CertificateExpiredException e) {
                errorLabel.setText("Certificado fora do prazo.");
                LogHandler.logWithUser(7005);
                return;
            } catch (CertificateException e) {
                errorLabel.setText("Certificado não pertence ao usuário.");
                LogHandler.logWithUser(7005);
                return;
            }
            UserState.newUserCertificateContent = certificateContent;
        }
        TecladoFoneticoFullPanel tecladoNovaSenhaPanel = new TecladoFoneticoFullPanel("Nova Senha", null, PasswordGoal.ALTERAR);
        FrameHandler.showPanel(tecladoNovaSenhaPanel);
    }
        
  }
