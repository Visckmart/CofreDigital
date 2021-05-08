import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

class InterfaceTeste {

  // static JPanel login() {
  //   JPanel p = new JPanel();
  //   p.setLayout(null);

  //   JLabel  namelabel = new JLabel("E-mail: ");
  //   JLabel  passwordLabel = new JLabel("Senha: ");
  //   namelabel.setBounds(50, 50, 100, 25);
  //   passwordLabel.setBounds(50, 80, 100, 25);
  //   p.add(namelabel);
  //   p.add(passwordLabel);
    
  //   JTextField emailTF = new JTextField();
  //   JPasswordField passwordTF = new JPasswordField();
  //   emailTF.setBounds(150, 50, 100, 25);
  //   passwordTF.setBounds(150, 80, 100, 25);
  //   p.add(emailTF);
  //   p.add(passwordTF);

  //   JButton bt = new JButton("Login");
  //   bt.setBounds(150, 200, 125, 25);
  //   bt.addActionListener(new ActionListener() {
  //     public void actionPerformed(ActionEvent e) {
  //       frame.setContentPane(p1);
  //       frame.invalidate();
  //       frame.validate();
  //     }
  //   });
  //   p.add(bt);
  //   return p;
  // }

  // static JPanel teclado() {
  //   JPanel p = new JPanel();
  //   p.setLayout(null);
  //   for (int i = 0; i < 2; i++) {
  //     for (int j = 0; j < 3; j++) {
  //       JButton bt = new JButton(Integer.toString(i * 2 + j));
  //       bt.setBounds(150 + 135 * j, 100 + 55 * i, 125, 45);
  //       bt.addActionListener(new ActionListener() {
  //         public void actionPerformed(ActionEvent e) {
  //           frame.setContentPane(p2);
  //           frame.invalidate();
  //           frame.validate();
  //         }
  //       });
  //       p.add(bt);
  //     }
  //   }

  //   JButton bt = new JButton("Login");
  //   bt.setBounds(250, 500, 125, 25);
  //   bt.addActionListener(new ActionListener() {
  //     public void actionPerformed(ActionEvent e) {
  //       frame.setContentPane(chave);
  //       frame.invalidate();
  //       frame.validate();
  //     }
  //   });
  //   p.add(bt);
  //   return p;
  // }

  // static JPanel chave = chave();
  // static JPanel chave() {
  //   JPanel p = new JPanel();
  //   p.setLayout(null);
  //   //Create a file chooser
  //   final JFileChooser fc = new JFileChooser();
  //   JButton input = new JButton("Abrir");
  //   input.setBounds(250, 200, 125, 25);
  //   input.addActionListener(new ActionListener() {
  //     public void actionPerformed(ActionEvent e) {
  //       int returnVal = fc.showOpenDialog(p);

  //       if (returnVal == JFileChooser.APPROVE_OPTION) {
  //         File file = fc.getSelectedFile();
  //         //This is where a real application would open the file.
  //         System.out.println("Opening: " + file.getName() + ".");
  //       } else {
  //         System.out.println("Open command cancelled by user.");
  //       }
  //     }
  //   });
  //   p.add(input);

  //   JLabel  passwordLabel = new JLabel("Frase secreta: ");
  //   passwordLabel.setBounds(50, 80, 100, 25);
  //   p.add(passwordLabel);
    
  //   JPasswordField passphraseTF = new JPasswordField();
  //   passphraseTF.setBounds(150, 80, 100, 25);
  //   p.add(passphraseTF);
    
  //   JButton bt = new JButton("Login");
  //   bt.setBounds(300, 400, 125, 25);
  //   bt.addActionListener(new ActionListener() {
  //     public void actionPerformed(ActionEvent e) {
  //       frame.setContentPane(menuPrincipal);
  //       frame.invalidate();
  //       frame.validate();
  //     }
  //   });
  //   p.add(bt);
  //   return p;
  // }

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
  
  // static JPanel abc() {
  //   JPanel p = new JPanel();

  //   JButton bt = new JButton("Back");
  //   bt.setBounds(50, 50, 125, 45);
  //   bt.addActionListener(new ActionListener() {
  //     public void actionPerformed(ActionEvent e) {
  //       frame.setContentPane(p1);
  //       frame.invalidate();
  //       frame.validate();
  //     }
  //   });
  //   p.add(bt);
  //   return p;
  // }

  static JFrame frame = new JFrame("Cofre Digital por Thiago Lamenza e Victor Martins");
  static JPanel p1 = new TecladoFoneticoPanel(null);
  // static JPanel p2 = abc();
  static JPanel login = new IdentUsuPanel(new VerificaChavePanel(p1));

  static void prepararInterfaceAutenticacao() {
    frame.add(new IdentUsuPanel(new TecladoFoneticoPanel(new VerificaChavePanel(new MenuPrincipalPanel()))));
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