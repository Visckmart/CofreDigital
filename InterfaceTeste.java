import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.List;

import Authentication.AuthenticationHandler;
import Authentication.PasswordHandler;
import Database.DatabaseHandler;
import General.AlterarCertificadoPanel;
import General.CadastroPanel;
import General.ConsultarArquivosPanel;
import General.FileHandler;
import General.MenuPrincipalPanel;
import General.TecladoFoneticoFullPanel;
import UserAuthentication.IdentUsuPanel;
import Utilities.FrameHandler;
import Utilities.LogHandler;

class InterfaceTeste {

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
  // static JPanel p1 = new TecladoFoneticoPanel(null);
  // static JPanel p2 = abc();
  // static JPanel login = new IdentUsuPanel(new VerificaChavePanel(p1));

  static void prepararInterfaceAutenticacao() {
    // frame.add(new IdentUsuPanel(new TecladoFoneticoPanel(new VerificaChavePanel(new MenuPrincipalPanel()))));
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

  static void testePasswordHandler() {
    for (int i = 0; i < 5; i++) {
      System.out.println(PasswordHandler.generateSalt());
      System.out.println(PasswordHandler.encodePassword("abc", PasswordHandler.generateSalt()).get());
    }
  }

  public static void main(String args[]) {
    // DatabaseHandler.createNewDatabase("test.db");
    // System.out.println(PasswordHandler.encodePassword("DABECADA", "sal99"));
    // 064f3b5c5e8ea7a79a27a87cdc0ec8dacd0c9741
    // LogHandler.log(1001);
    // Path privateKeyFilePath = FileSystems.getDefault().getPath("Pacote-T4/Keys/", "admin-pkcs8-des.key");
    // try {
    //   AuthenticationHandler ah = new AuthenticationHandler();
    //   byte[] content = Files.readAllBytes(privateKeyFilePath);
    //   PrivateKey pk = ah.privateKeyFromFile(content, "admin".getBytes());
    //   System.out.println(pk);
    //   System.out.println(DatabaseHandler.getInstance().getEncodedCertificate("aa"));
    // } catch (Exception e) {
    //   e.printStackTrace();
    // }
    try {
      new DatabaseHandler().seedUsers();
    //   new FileHandler().decryptAndVerifyFile("./Pacote-T4/Files/", "index.enc");
    } catch (Exception e) {
    //   //TODO: handle exception
    }
    prepararInterfaceAutenticacao();
    // frame.add(new MenuPrincipalPanel());
    // frame.add(new AlterarCertificadoPanel());
    // frame.add(new CadastroPanel());
    // frame.add(new ConsultarArquivosPanel());
    mostrarTela();
  }
}