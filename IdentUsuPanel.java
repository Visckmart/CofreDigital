import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;
import javax.swing.JPasswordField;

public class IdentUsuPanel extends JPanel {
    
    public IdentUsuPanel() {
        this.setLayout(null);

        this.prepareTitle();
        this.prepararCampoEmail(225, 270, 250, 35);
        this.prepararLabelErro(225, 310, 250, 30);
        this.prepararBotaoLogin(285, 375, 130, 35);
    }

    void prepareTitle() {
        JLabel title = new JLabel("Cofre Digital");
        title.setBounds(175, 25, 350, 75);
        title.setFont(new Font(null, Font.BOLD, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JLabel authors = new JLabel("<html><p style='text-align:center;'>Thiago Lamenza e Victor Martins<br>Segurança da Informação – Grupo 7</p></html>");
        authors.setBounds(150, 100, 400, 50);
        authors.setFont(new Font(null, Font.PLAIN, 18));
        authors.setHorizontalAlignment(SwingConstants.CENTER);
        add(authors);
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
        boolean b = UserLoginHandler.checkEmailAddress(emailAddress);
        System.out.println(b);
        if (b == false) {
            errorLabel.setText("E-mail inválido.");
        }
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new TecladoFoneticoPanel(emailAddress));
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
