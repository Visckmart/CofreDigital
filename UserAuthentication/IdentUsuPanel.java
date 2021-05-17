package UserAuthentication;
import javax.swing.*;

import Authentication.UserState;
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
    public IdentUsuPanel(String email) {
        this();
        preencherEmail(email);
        UserLoginState userState = DatabaseHandler.getInstance().verifyUserEmail(email);
        if (userState == UserLoginState.BLOCKED) {
            UserState.emailAddress = null;
            errorLabel.setText("Usuário bloqueado.");
        } else {
            UserState.emailAddress = null;
            errorLabel.setText("Usuário não identificado.");
        }
    }

    private void preencherEmail(String email) {
        this.emailTF.setText(email);
        nextStep();
    }

    private void prepararCampoEmail(int offsetX, int offsetY, int width, int height) {
        JLabel emailLabel = new JLabel("E-mail: ");
        emailLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(emailLabel);
        
        emailTF = new JTextField();
        emailTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        this.add(emailTF);
    }

    void nextStep() {
        String emailAddress = emailTF.getText();
        UserState.emailAddress = emailAddress;
        UserLoginState userState = DatabaseHandler.getInstance().verifyUserEmail(emailAddress);
        if (userState == UserLoginState.VALID) {
            LogHandler.logWithUser(2003);
            LogHandler.log(2002);
            TecladoFoneticoPanel tfp = new TecladoFoneticoPanel(emailAddress);
            FrameHandler.showPanel(tfp, tfp.loginButton);
        } else if (userState == UserLoginState.BLOCKED) {
            LogHandler.logWithUser(2004);
            UserState.emailAddress = null;
            errorLabel.setText("Usuário bloqueado.");
        } else {
            LogHandler.logWithUser(2005);
            UserState.emailAddress = null;
            errorLabel.setText("Usuário não identificado.");
        }
    }
    
    
  }
