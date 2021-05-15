package General;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Authentication.AuthenticationHandler;
import Authentication.PasswordHandler;
import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.LogHandler;

public class ConfirmacaoCadastroPanel extends GeneralPanel {

    String[] nomesCampos = { "Versão", "Série", "Válido de", "Válido até", "Tipo de Assinatura", "Emissor", "Sujeito",
            "E-mail" };

    public ConfirmacaoCadastroPanel() {
        super("Confirmação de Cadastro", true, CabecalhoInfo.TOTAL_USUARIOS);

        this.prepararBotaoLogin(285, 450, 130, 35);
        prepararInformacoes();
    }

    private static String composeLabelText(String fieldName, String content) {
        return "<html>" + "<b>" + fieldName + ": " + "</b>" + content + "</html>";
    }

    JButton loginButton;
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AuthenticationHandler authHandler = new AuthenticationHandler();
                String email = AuthenticationHandler.getEmailFromCertificate(UserState.newUserCertificate);
                String salt = PasswordHandler.generateSalt();
                try {
                String encodedPassword = PasswordHandler.encodePassword(UserState.newUserPassword, salt).get();
                DatabaseHandler.getInstance().registerUser(email, User.newUserCertificate, encodedPassword, salt, 1);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        this.add(loginButton);
    }
    void prepararInformacoes() {
        String[] valoresCampos = AuthenticationHandler.getCertificateInfo(UserState.newUserCertificate);
        for (int i = 0; i < nomesCampos.length; i++) {
            JLabel newLabel = new JLabel(composeLabelText(nomesCampos[i], valoresCampos[i]));
            newLabel.setFont(new Font(null, Font.PLAIN, 18));
            newLabel.setBounds(125, 200 + i * 30, 450, 25);
            // newLabel.setBackground(Color.red);
            // groupLabel.setOpaque(true);
            add(newLabel);
        }
    }
}
