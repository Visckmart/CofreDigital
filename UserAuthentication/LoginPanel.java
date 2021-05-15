package UserAuthentication;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    
    public JButton loginButton;
    LoginPanel() {
        this.setLayout(null);
        add(TitlePanel.getInstance());
        this.prepararBotaoLogin(285, 400, 130, 35);
    }
    
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
        System.out.println("Erro");
    }
}
