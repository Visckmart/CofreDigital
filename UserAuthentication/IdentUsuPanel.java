package UserAuthentication;
import javax.swing.*;

import Authentication.UserState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

import Database.DatabaseHandler;
import Utilities.FrameHandler;
import Utilities.LogHandler;
import Utilities.UserLoginState;

public class IdentUsuPanel extends LoginPanel {
    
    JTextField emailTF;
    JLabel errorLabel;
    public IdentUsuPanel() {
        this.prepararCampoEmail(225, 270, 250, 35);
        this.prepararLabelErro(225, 310, 250, 30);
        LogHandler.log(2001);
    }

    private void prepararCampoEmail(int offsetX, int offsetY, int width, int height) {
        JLabel emailLabel = new JLabel("E-mail: ");
        emailLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(emailLabel);
        
        emailTF = new JTextField();
        emailTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        emailTF.setText("user01@inf1416.puc-rio.br");
        this.add(emailTF);
    }

    void nextStep() {
        String emailAddress = emailTF.getText();
        try {
            UserLoginState userState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
            if (userState == UserLoginState.VALID) {
                LogHandler.log(2003);
                UserState.emailAddress = emailAddress;
                
                TecladoFoneticoPanel tfp = new TecladoFoneticoPanel(emailAddress);
                FrameHandler.showPanel(tfp, tfp.loginButton);
                
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
