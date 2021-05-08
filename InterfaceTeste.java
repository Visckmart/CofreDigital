import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

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
  static JPanel p1 = new TecladoFoneticoPanel(null);
  // static JPanel p2 = abc();
  // static JPanel login = new IdentUsuPanel(new VerificaChavePanel(p1));

  static void prepararInterfaceAutenticacao() {
    // frame.add(new IdentUsuPanel(new TecladoFoneticoPanel(new VerificaChavePanel(new MenuPrincipalPanel()))));
    frame.add(new IdentUsuPanel());
  }
  static void mostrarTela() {
    frame.invalidate();
    frame.validate();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700, 550);
    frame.setResizable(false);
    frame.setVisible(true);
  }

  static void testePasswordHandler() {
    for (int i = 0; i < 5; i++) {
      System.out.println(PasswordHandler.generateSalt());
      System.out.println(PasswordHandler.encodePassword("abc", PasswordHandler.generateSalt()).get());
    }
  }

  static List<FileInfo> testIndexHandler() {
    IndexHandler ih = new IndexHandler();
    Path p = FileSystems.getDefault().getPath("", "indice_exemplo.txt");

    List<FileInfo> index = ih.parseIndex(p);
    for (FileInfo fileInfo : index) {
      fileInfo.nomeOriginal += Character.toString((char)(new Random().nextInt(26) + 65));
      System.out.println(fileInfo);
      System.out.println(fileInfo.checkAccess("Joao", "0"));
    }
    return index;
  }
  public static void main(String args[]) {
    prepararInterfaceAutenticacao();
    mostrarTela();
  }
}