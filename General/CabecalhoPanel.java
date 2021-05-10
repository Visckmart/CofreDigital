package General;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;

public class CabecalhoPanel extends JPanel {
    
    static CabecalhoPanel panel = new CabecalhoPanel();

    
    private static String composeLabelText(String fieldName, String content) {
        return "<html>" + "<b>" + fieldName + ": " + "</b>" + content + "</html>";
    }
    
    private JLabel loginLabel;
    private JLabel groupLabel;
    private JLabel usernameLabel;
    private JLabel extraInfoLabel = new JLabel();
    
    private CabecalhoPanel() {
        this.setLayout(null);

        loginLabel = new JLabel(composeLabelText("Login", ""));
        loginLabel.setFont(new Font(null, Font.PLAIN, 18));
        loginLabel.setBounds(0, 0, 350, 25);
        loginLabel.setBackground(Color.pink);
        // loginLabel.setOpaque(true);
        add(loginLabel);

        groupLabel = new JLabel(composeLabelText("Grupo", ""));
        groupLabel.setFont(new Font(null, Font.PLAIN, 18));
        groupLabel.setBounds(0, 30, 350, 25);
        groupLabel.setBackground(Color.red);
        // groupLabel.setOpaque(true);
        add(groupLabel);

        usernameLabel = new JLabel(composeLabelText("Nome de usuário", ""));
        usernameLabel.setFont(new Font(null, Font.PLAIN, 18));
        usernameLabel.setBounds(0, 60, 350, 25);
        usernameLabel.setBackground(Color.green);
        // usernameLabel.setOpaque(true);
        add(usernameLabel);

        extraInfoLabel.setFont(new Font(null, Font.PLAIN, 18));
        extraInfoLabel.setBounds(0, 100, 350, 25);
        extraInfoLabel.setBackground(Color.lightGray);
        // extraInfoLabel.setOpaque(true);
        add(extraInfoLabel);
        
        // setBackground(Color.black);
        setBounds(0, 0, 350, 125);
    }

    public void updateLoginInfo(String loginName, String userGroup, String username) {
        loginLabel.setText(composeLabelText("Login", loginName));
        groupLabel.setText(composeLabelText("Grupo", userGroup));
        usernameLabel.setText(composeLabelText("Nome de usuário", username));
    }

    public void updateExtraInfo(String label, String content) {
        extraInfoLabel.setText(composeLabelText(label, content));
    }
    
  }
