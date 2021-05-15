package General;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Utilities.LogHandler;

public class MenuPrincipalPanel extends GeneralPanel {
    
      public MenuPrincipalPanel() {
        super("Menu principal", false);
        prepararCorpo(210, 190, 280, 300);
        LogHandler.logWithUser(5001);
      }
      
      void prepararCorpo(int offsetX, int offsetY, int width, int height) {
        JButton bt1 = new JButton("Cadastrar um novo usuário");
        bt1.setFont(new Font(null, Font.PLAIN, 15));
        bt1.setBounds(offsetX, offsetY, width, height/4 - 5);
        bt1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              LogHandler.logWithUser(5002);
              JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bt1);
              CadastroPanel cap = new CadastroPanel();
              // cap.setFileList(IndexHandler.testIndexHandler());
              frame.setContentPane(cap);
              frame.invalidate();
              frame.validate();
          }
        });
        add(bt1);
        
        JButton bt2 = new JButton("<html><p style='text-align:center;'>Alterar senha pessoal e<br>certificado digital do usuário</p.</html>");
        bt2.setFont(new Font(null, Font.PLAIN, 15));
        bt2.setBounds(offsetX, offsetY + height/4, width, height/4 - 5);
        bt2.setHorizontalAlignment(SwingConstants.CENTER);
        bt2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              LogHandler.logWithUser(5003);
              JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bt2);
              AlterarCertificadoPanel cap = new AlterarCertificadoPanel();
              // cap.setFileList(IndexHandler.testIndexHandler());
              frame.setContentPane(cap);
              frame.invalidate();
              frame.validate();
          }
        });
        add(bt2);
        
        JButton bt3 = new JButton("<html><p style='text-align:center;'>Consultar pasta de arquivos<br>secretos do usuário</p></html>");
        bt3.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(5004);
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bt3);
                ConsultarArquivosPanel cap = new ConsultarArquivosPanel();
                // cap.setFileList(IndexHandler.testIndexHandler());
                frame.setContentPane(cap);
                frame.invalidate();
                frame.validate();
              }
            });
        bt3.setFont(new Font(null, Font.PLAIN, 15));
        bt3.setBounds(offsetX, offsetY + (height/4) * 2, width, height/4 - 5);
        add(bt3);

        JButton bt4 = new JButton("Sair do Sistema");
        bt4.setFont(new Font(null, Font.PLAIN, 15));
        bt4.setBounds(offsetX, offsetY + (height/4) * 3, width, height/4 - 5);
        bt4.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(5005);
                LogHandler.logWithUser(9003);
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(bt4);
                ConfirmacaoSaidaPanel cap = new ConfirmacaoSaidaPanel();
                // cap.setFileList(IndexHandler.testIndexHandler());
                frame.setContentPane(cap);
                frame.invalidate();
                frame.validate();
              }
            });
        add(bt4);
      }
  }
