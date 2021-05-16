package General;
import javax.swing.*;

import Authentication.AuthenticationHandler;
import Authentication.UserGroup;
import Authentication.UserState;
import General.TecladoFoneticoFullPanel.PasswordGoal;
import Utilities.FrameHandler;
import Utilities.LogHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.Optional;

public class CadastroPanel extends GeneralPanel {
    
    String emailAddress;
    public CadastroPanel() {
        super("Cadastrar Novo Usuário", true, CabecalhoInfo.TOTAL_USUARIOS);
        this.prepararBotaoArquivo(210, 250, 280, 35);
        this.prepararTextoArquivo(210, 280, 280, 35);
        this.prepararCampoGrupo(210, 320, 320, 50);
        this.prepararBotaoLogin(285, 400, 130, 35);

        
        this.prepararLabelErro(150, 370, 400, 30);
        LogHandler.log(6001);
    }
    
    JList<String> groupList;
    private void prepararCampoGrupo(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Grupo do usuário: ");
        passwordLabel.setBounds(offsetX, offsetY, width*4/10, height);
        this.add(passwordLabel);
        
        String[] grupos = {"Administrador", "Usuário"};
        groupList = new JList<String>(grupos);
        groupList.setBounds(offsetX + width*4/10 + 10, offsetY, (width*6/10 - 10), height);
        groupList.setFixedCellHeight(25);
        groupList.setSelectedIndex(1);
        this.add(groupList);
    }
    
    File chosenFile;
    void prepararBotaoArquivo(int offsetX, int offsetY, int width, int height) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        JButton input = new JButton("Escolher arquivo do certificado...");
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
    JLabel errorLabel;
    void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setFont(new Font(null, Font.BOLD, 14));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    
    void nextStep() {
        if (chosenFile == null) {
            LogHandler.logWithUser(6004);
            errorLabel.setText("Caminho do certificado digital inválido.");
            return;
        }
        byte[] certificateContent;
        try {
            certificateContent = Files.readAllBytes(chosenFile.toPath());
        } catch (Exception e) {
            LogHandler.logWithUser(6004);
            errorLabel.setText("Caminho do certificado digital inválido.");
            return;
        }
        Certificate cert;
        try {
            cert = new AuthenticationHandler().certificateFromFile(certificateContent);
        } catch (Exception e) {
            errorLabel.setText("Certificado inválido");
            return;
        }
        try {
            AuthenticationHandler.checkCertificate(cert, Optional.empty());
        } catch (CertificateNotYetValidException | CertificateExpiredException e) {
            errorLabel.setText("Certificado fora do prazo.");
            return;
        } catch (CertificateException e) {
            errorLabel.setText("Certificado não pertence ao usuário.");
            return;
        }

        UserState.newUserCertificate = cert;
        UserState.newUserCertificateContent = certificateContent;
        UserState.newUserGroup = groupList.getSelectedIndex() == 1 ? UserGroup.USER : UserGroup.ADMIN;
        TecladoFoneticoFullPanel tecladoNovaSenhaCadastro = new TecladoFoneticoFullPanel("Senha do Novo Usuário", null, PasswordGoal.CADASTRAR);
        FrameHandler.showPanel(tecladoNovaSenhaCadastro);
    }
  }
