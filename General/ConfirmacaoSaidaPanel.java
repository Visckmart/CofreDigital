package General;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Authentication.UserState;
import Utilities.LogHandler;

public class ConfirmacaoSaidaPanel extends JPanel {
    
    public ConfirmacaoSaidaPanel() {
        this.setLayout(null);

        CabecalhoPanel cabecalho = CabecalhoPanel.panel;
        CabecalhoPanel.panel.updateLoginInfo("login", "grupo", "nome");
        cabecalho.setBounds(20, 20, cabecalho.getWidth(), cabecalho.getHeight());
        cabecalho.updateExtraInfo("Total de acessos", "10");
        add(cabecalho);

        prepareBackButton(250, 200, 200, 35);
        prepareQuitButton(250, 275, 200, 35);
      }
      
      void prepareBackButton(int offsetX, int offsetY, int width, int height) {
        JButton backButton = new JButton("<   Voltar ao menu");
        backButton.setBounds(offsetX, offsetY, width, height);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(9004);
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(backButton);
                frame.setContentPane(new MenuPrincipalPanel());
                frame.invalidate();
                frame.validate();
            }
        });
        add(backButton);
    }

    void prepareQuitButton(int offsetX, int offsetY, int width, int height) {
        JButton bt1 = new JButton("Sair");
        bt1.setFont(new Font(null, Font.PLAIN, 15));
        bt1.setBounds(offsetX, offsetY, width, height);
        bt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
          });
        add(bt1);
      }
  }
