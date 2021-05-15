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
    public IdentUsuPanel() {
        this.prepararCampoEmail(200, 270, 300, 35);
        this.prepararLabelErro(200, 310, 300, 30);
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
        UserLoginState userState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
        if (userState == UserLoginState.VALID) {
            LogHandler.log(2002);
            TecladoFoneticoPanel tfp = new TecladoFoneticoPanel(emailAddress);
            FrameHandler.showPanel(tfp, tfp.loginButton);
        } else if (userState == UserLoginState.BLOCKED) {
            errorLabel.setText("Usuário bloqueado.");
        } else {
            errorLabel.setText("Usuário não identificado.");
        }
    }
    
    
  }
