import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPasswordField;

public class IdentUsuPanel extends JPanel {
    
    public IdentUsuPanel(JPanel next) {
        this.setLayout(null);

        this.prepareTitle();
        this.prepararCampoEmail(225, 270, 250, 35);
        // this.prepararCampoSenha(225, 150, 250, 35);
        this.prepararBotaoLogin(285, 375, 130, 35, next);
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
    JPasswordField passwordTF;
    private void prepararCampoEmail(int offsetX, int offsetY, int width, int height) {
        JLabel emailLabel = new JLabel("E-mail: ");
        emailLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(emailLabel);
        
        emailTF = new JTextField();
        emailTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        this.add(emailTF);
    }
    
    private void prepararCampoSenha(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Senha: ");
        passwordLabel.setBounds(offsetX, offsetY, width*2/10, height);
        this.add(passwordLabel);
        
        passwordTF = new JPasswordField();
        passwordTF.setBounds(offsetX + width*2/10 + 10, offsetY, (width*8/10 - 10), height);
        this.add(passwordTF);
    }
    
    private boolean checkEmailAddress(String emailAddress) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_]+@[a-zA-Z0-9_]+.[a-zA-Z0-9_]{2,64}");//. represents single character  
        Matcher m = p.matcher(emailTF.getText());  
        boolean b = m.matches();
        return b;
    }
    
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height, JPanel next) {
        JButton loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(emailTF.getText());
                boolean b = checkEmailAddress(emailTF.getText());
                System.out.println(b);  
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(loginButton);
                frame.setContentPane(next);
                frame.invalidate();
                frame.validate();
            }
        });
        this.add(loginButton);
    }
    
    
  }
