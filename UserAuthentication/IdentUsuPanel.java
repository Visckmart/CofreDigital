package UserAuthentication;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import Database.DatabaseHandler;
import Utilities.LogHandler;
import Utilities.UserState;

public class IdentUsuPanel extends JPanel {
    
    public IdentUsuPanel() {
        this.setLayout(null);

        add(TitlePanel.getInstance());
        this.prepararCampoEmail(225, 270, 250, 35);
        this.prepararLabelErro(225, 310, 250, 30);
        this.prepararBotaoLogin(285, 375, 130, 35);
        LogHandler.log(2001, null);
    }

    
    
    JTextField emailTF;

    private void prepararCampoEmail(int offsetX, int offsetY, int width, int height) {
        JLabel emailLabel = new JLabel("E-mail: ");
        emailLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(emailLabel);
        
        emailTF = new JTextField();
        emailTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        this.add(emailTF);
    }
    
    JLabel errorLabel;
    private void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    
    void nextStep() {
        String emailAddress = emailTF.getText();
        System.out.println(emailAddress);
        try {
            UserState x = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
            System.out.println(x);
            if (x == UserState.VALID) {
                LogHandler.log(2002, null);
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new TecladoFoneticoPanel(emailAddress));
                frame.invalidate();
                frame.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
