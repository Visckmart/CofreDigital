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

    void nextStep() {
        String emailAddress = emailTF.getText();
        try {
            UserState userState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
            if (userState == UserState.VALID) {
                LogHandler.log(2003);
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new TecladoFoneticoPanel(emailAddress));
                frame.invalidate();
                frame.validate();
                LogHandler.log(2002);
            } else if (userState == UserState.BLOCKED) {
                LogHandler.log(2004);
                errorLabel.setText("Usuário bloqueado.");
            } else {
                LogHandler.log(2005);
                errorLabel.setText("Usuário não identificado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
  }
