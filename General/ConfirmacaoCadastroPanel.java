package General;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Authentication.AuthenticationHandler;
import Authentication.PasswordHandler;
import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.FrameHandler;
import Utilities.LogHandler;

public class ConfirmacaoCadastroPanel extends GeneralPanel {

    String[] nomesCampos = { "Versão", "Série", "Válido de", "Válido até", "Tipo de Assinatura", "Emissor", "Sujeito",
            "E-mail" };

    @Override protected int getBackCode() {
        return 6006;
    }
    
    public ConfirmacaoCadastroPanel() {
        super("Confirmação de Cadastro", true, CabecalhoInfo.TOTAL_USUARIOS);

        this.prepararBotaoLogin(285, 450, 130, 35);
        this.prepararLabelErro(225, 420, 250, 20);
        prepararInformacoes();
    }

    private static String composeLabelText(String fieldName, String content) {
        return "<html>" + "<b>" + fieldName + ": " + "</b>" + content + "</html>";
    }

    JButton loginButton;
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        loginButton = new JButton("Cadastrar");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                LogHandler.logWithUser(6005);
                String email = AuthenticationHandler.getEmailFromCertificate(UserState.newUserCertificate);
                String salt = PasswordHandler.generateSalt();
                String encodedPassword = PasswordHandler.encodePassword(UserState.newUserPassword, salt);
                try {
                    DatabaseHandler.getInstance().registerUser(email, UserState.newUserCertificateContent, encodedPassword, salt, UserState.newUserGroup.ordinal());
                    FrameHandler.showPanel(new CadastroPanel());
                    UserState.newUserCertificate = null;
                    UserState.newUserGroup = null;
                    UserState.newUserCertificateContent = null;
                    UserState.newUserPassword = null;
                } catch (Exception exc) {
                    errorLabel.setText("Usuário já está cadastrado.");
                }
            }
        });
        this.add(loginButton);
    }
    
    JLabel errorLabel;
    void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setFont(new Font(null, Font.BOLD, 14));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }
    
    void prepararInformacoes() {
        String[] valoresCampos = AuthenticationHandler.getCertificateInfo(UserState.newUserCertificate);
        for (int i = 0; i < nomesCampos.length; i++) {
            JLabel newLabel = new JLabel(composeLabelText(nomesCampos[i], valoresCampos[i]));
            newLabel.setFont(new Font(null, Font.PLAIN, 15));
            newLabel.setBounds(120, 210 + i * 25, 500, 20);
            add(newLabel);
        }
    }
}
