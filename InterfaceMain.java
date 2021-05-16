import javax.swing.*;
import java.awt.event.*;

import Database.DatabaseHandler;
import UserAuthentication.IdentUsuPanel;
import Utilities.FrameHandler;
import Utilities.LogHandler;

class InterfaceMain {

  static JPanel menuPrincipal = menuPrincipal();

  static JPanel menuPrincipal() {
    JPanel p = new JPanel();
    p.setLayout(null);

    JButton bt1 = new JButton("Cadastrar um novo usuário");
    bt1.setBounds(150, 150, 250, 50);
    p.add(bt1);
    JButton bt2 = new JButton("<html>Alterar senha pessoal e<br>certificado digital do usuário</html>");
    bt2.setBounds(150, 200, 250, 50);
    p.add(bt2);
    JButton bt3 = new JButton("<html>Consultar pasta de arquivos<br>secretos do usuário</html>");
    bt3.setBounds(150, 250, 250, 50);
    p.add(bt3);
    JButton bt4 = new JButton("Sair do Sistema");
    bt4.setBounds(150, 300, 250, 50);
    p.add(bt4);

    return p;
  }

  static JFrame frame = new JFrame("Cofre Digital por Thiago Lamenza e Victor Martins");

  static void prepararInterfaceAutenticacao() {
    IdentUsuPanel iup = new IdentUsuPanel();
    frame.add(iup);
    frame.getRootPane().setDefaultButton(iup.loginButton);
  }
  static void mostrarTela() {
    frame.invalidate();
    frame.validate();
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
          LogHandler.log(1002);
          frame.setVisible(false);
          frame.dispose();
    }
  });
    frame.setSize(700, 550);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    FrameHandler.setMainFrame(frame);
  }

  public static void main(String args[]) {
    LogHandler.log(1001);
    DatabaseHandler.getInstance().seedUsers();
    prepararInterfaceAutenticacao();
    mostrarTela();
  }
}
