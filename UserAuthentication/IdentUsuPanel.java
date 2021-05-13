package UserAuthentication;
import javax.swing.*;

import Authentication.UserState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import Database.DatabaseHandler;
import Utilities.LogHandler;
import Utilities.UserLoginState;

public class IdentUsuPanel extends JPanel {
    
    public IdentUsuPanel() {
        this.setLayout(null);

        add(TitlePanel.getInstance());
        this.prepararCampoEmail(225, 270, 250, 35);
        this.prepararLabelErro(225, 310, 250, 30);
        this.prepararBotaoLogin(285, 375, 130, 35);
        LogHandler.log(2001);
    }

    
    
    JTextField emailTF;

    private void prepararCampoEmail(int offsetX, int offsetY, int width, int height) {
        JLabel emailLabel = new JLabel("E-mail: ");
        emailLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(emailLabel);
        
        emailTF = new JTextField();
        emailTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        emailTF.setText("user01@inf1416.puc-rio.br");
        this.add(emailTF);
    }
    
    JLabel errorLabel;
    private void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    public JButton loginButton;
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
        String emailAddress = emailTF.getText();
        try {
            UserLoginState userState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
            if (userState == UserLoginState.VALID) {
                LogHandler.log(2003);
                UserState.emailAddress = emailAddress;
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                TecladoFoneticoPanel tfp = new TecladoFoneticoPanel(emailAddress);
                frame.setContentPane(tfp);
                frame.invalidate();
                frame.validate();
                frame.getRootPane().setDefaultButton(tfp.loginButton);
                LogHandler.log(2002);
            } else if (userState == UserLoginState.BLOCKED) {
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
