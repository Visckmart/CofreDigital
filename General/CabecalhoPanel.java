package General;
import javax.swing.*;

import Authentication.UserGroup;
import Authentication.UserState;
import Database.DatabaseHandler;

import java.awt.Color;
import java.awt.Font;

public class CabecalhoPanel extends JPanel {
    
    public static CabecalhoPanel panel = new CabecalhoPanel();

    private static String composeLabelText(String fieldName, String content) {
        return "<html>" + "<b>" + fieldName + ": " + "</b>" + content + "</html>";
    }
    
    private JLabel loginLabel;
    private JLabel groupLabel;
    private JLabel usernameLabel;
    private JLabel extraInfoLabel = new JLabel();
    
    CabecalhoInfo informacaoAdicional = CabecalhoInfo.TOTAL_ACESSOS;

    void setInformacaoAdicional(CabecalhoInfo info) {
        informacaoAdicional = info;
        DatabaseHandler.getInstance().updateUserState(UserState.emailAddress);
        if (info == CabecalhoInfo.TOTAL_ACESSOS) {
            updateExtraInfo("Total de acessos do usuário", Integer.toString(UserState.accesses));
        } else if (info == CabecalhoInfo.TOTAL_CONSULTAS) {
            updateExtraInfo("Total de consultas do usuário", Integer.toString(UserState.queries));
        } else if (info == CabecalhoInfo.TOTAL_USUARIOS) {
            updateExtraInfo("Total de usuários no sistema", Integer.toString(UserState.totalUsers));
        }
    }
    
    public void atualizarInformacaoAdicional() {
        setInformacaoAdicional(informacaoAdicional);
    }
    private CabecalhoPanel() {
        this.setLayout(null);

        loginLabel = new JLabel(composeLabelText("Login", UserState.emailAddress));
        loginLabel.setFont(new Font(null, Font.PLAIN, 18));
        loginLabel.setBounds(0, 0, 350, 25);
        loginLabel.setBackground(Color.pink);
        add(loginLabel);

        groupLabel = new JLabel(composeLabelText("Grupo", UserState.group == UserGroup.ADMIN ? "Administrador" : "Usuário"));
        groupLabel.setFont(new Font(null, Font.PLAIN, 18));
        groupLabel.setBounds(0, 30, 350, 25);
        groupLabel.setBackground(Color.red);
        add(groupLabel);

        usernameLabel = new JLabel(composeLabelText("Nome de usuário", UserState.username));
        usernameLabel.setFont(new Font(null, Font.PLAIN, 18));
        usernameLabel.setBounds(0, 60, 350, 25);
        usernameLabel.setBackground(Color.green);
        add(usernameLabel);

        extraInfoLabel.setFont(new Font(null, Font.PLAIN, 18));
        extraInfoLabel.setBounds(0, 95, 350, 25);
        extraInfoLabel.setBackground(Color.lightGray);
        add(extraInfoLabel);

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
