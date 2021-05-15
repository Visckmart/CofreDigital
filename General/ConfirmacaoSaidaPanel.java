package General;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Utilities.LogHandler;

public class ConfirmacaoSaidaPanel extends GeneralPanel {

    @Override protected int getBackCode() {
        return 9004;
    }

    public ConfirmacaoSaidaPanel() {
        super("Confirmação de Saída", true);

        prepararMensagem(125, 275, 450, 35);
        prepareQuitButton(250, 350, 200, 35);

        LogHandler.logWithUser(9001);
    }
    void prepararMensagem(int offsetX, int offsetY, int width, int height) {
      JLabel messageLabel = new JLabel("Pressione o botão Sair para confirmar.");
      messageLabel.setBounds(offsetX, offsetY, width, height);
      messageLabel.setFont(new Font(null, Font.PLAIN, 15));
      messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
      add(messageLabel);
    }

    void prepareQuitButton(int offsetX, int offsetY, int width, int height) {
        JButton bt1 = new JButton("Sair");
        bt1.setFont(new Font(null, Font.BOLD, 15));
        bt1.setBounds(offsetX, offsetY, width, height);
        bt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(9003);
                LogHandler.log(1002);
                System.exit(0);
            }
          });
        add(bt1);
      }
  }
