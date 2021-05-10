package UserAuthentication;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPasswordField;

import General.MenuPrincipalPanel;

public class VerificaChavePanel extends JPanel {
    
    String emailAddress;
    public VerificaChavePanel(String emailAddress) {
        this.setLayout(null);
        this.emailAddress = emailAddress;
        add(TitlePanel.getInstance());
        this.prepararBotaoArquivo(210, 250, 280, 35);
        this.prepararCampoFraseSecreta(210, 300, 280, 35);
        this.prepararBotaoLogin(285, 375, 130, 35);
    }
    
    private void prepararCampoFraseSecreta(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Frase secreta: ");
        passwordLabel.setBounds(offsetX, offsetY, width*4/10, height);
        this.add(passwordLabel);
        
        JPasswordField passwordTF = new JPasswordField();
        passwordTF.setBounds(offsetX + width*4/10 + 10, offsetY, (width*6/10 - 10), height);
        this.add(passwordTF);
    }
    
    void prepararBotaoArquivo(int offsetX, int offsetY, int width, int height) {
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        JButton input = new JButton("Escolher arquivo da chave privada...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(input);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + ".");
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
        });
        this.add(input);
        JButton clear = new JButton("X");
        clear.setBounds(offsetX + width + 5, offsetY, height, height);
        clear.setEnabled(false);
        this.add(clear);
    }
    
    void nextStep() {
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new MenuPrincipalPanel());
        frame.invalidate();
        frame.validate();
    }

    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        JButton loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        this.add(loginButton);
    }
    
    
  }
