package General;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Authentication.PasswordHandler;
import General.TecladoFoneticoFullPanel.SuccessHandler;
import Utilities.LogHandler;

public class AlterarCertificadoPanel extends GeneralPanel {
    
    JButton[] keys = new JButton[18];
    JTextArea pathLabel;

    public AlterarCertificadoPanel() {
        super("Alterar Certificado", true);
        
        this.prepararCertificateChooser(225, 250, 250, 35);
        this.prepararPathLabel(150, 290, 400, 50);
        this.prepararLabelErro(225, 240, 250, 20);
        this.prepararBotaoLogin(285, 350, 130, 35);
        
        LogHandler.logWithUser(3001);
    }
    
    File chosenFile;
    private void prepararCertificateChooser(int offsetX, int offsetY, int width, int height) {
        JButton chooseButton = new JButton("Escolher arquivo do certificado...");
        chooseButton.setBounds(offsetX, offsetY, width, height);
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("./Pacote-T4/Keys"));
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
        // pathLabel.setText("<html>asdadasdfasfsafsXdafasfsafsafsadfdasdasdXasdasdadadsadaXsdadsadas</html>");
        pathLabel.setLineWrap(true);
        pathLabel.setOpaque(false);
        pathLabel.setEditable(false);
        pathLabel.setWrapStyleWord(true);
        pathLabel.setFocusable(false);
        pathLabel.setFont(new Font(null, Font.PLAIN, 15));
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
        // updatePasswordFeedback(fonemasDigitados.size() * 2);
        if (chosenFile == null) {
            pathLabel.setText("");
            return;
        }
        pathLabel.setText(chosenFile.getAbsolutePath());
        // this.atualizarBotoes(tecladoFonetico.obterTextoDosBotoes());
    }
    
    
    void nextStep() {
        if (chosenFile != null) {
            // Atualizar certificado
        }
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        TecladoFoneticoFullPanel vcp = new TecladoFoneticoFullPanel("Nova Senha", null);
        vcp.setSuccessHandler(SuccessHandler.ALTERAR);
        frame.setContentPane(vcp);
        frame.invalidate();
        frame.validate();
    }
        
  }
