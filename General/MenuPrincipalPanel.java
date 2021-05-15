package General;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Authentication.UserGroup;
import Authentication.UserState;
import Utilities.FrameHandler;
import Utilities.LogHandler;

public class MenuPrincipalPanel extends GeneralPanel {
    
    public MenuPrincipalPanel() {
        super("Menu principal", false);
        prepararCorpo(210, 190, 280, 300);
        LogHandler.logWithUser(5001);
    }
      
    void prepararCorpo(int offsetX, int offsetY, int width, int height) {
        boolean mostrarCadastro = UserState.group == UserGroup.ADMIN;
        int qtdBotoes = mostrarCadastro ? 4 : 3;
        int index = 0;
        int newYoffset;

        if (mostrarCadastro) {
            JButton bt1 = new JButton("Cadastrar um novo usuário");
            bt1.setFont(new Font(null, Font.PLAIN, 15));
            newYoffset = offsetY + (height/qtdBotoes) * index;
            bt1.setBounds(offsetX, offsetY, width, height/qtdBotoes - 5);
            bt1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LogHandler.logWithUser(5002);
                    FrameHandler.showPanel(new CadastroPanel());
                }
            });
            add(bt1);
            index += 1;
        }
        
        JButton bt2 = new JButton("<html><p style='text-align:center;'>Alterar senha pessoal e<br>certificado digital do usuário</p.</html>");
        bt2.setFont(new Font(null, Font.PLAIN, 15));
        newYoffset = offsetY + (height/qtdBotoes) * index;
        bt2.setBounds(offsetX, newYoffset, width, height/qtdBotoes - 5);
        bt2.setHorizontalAlignment(SwingConstants.CENTER);
        bt2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              LogHandler.logWithUser(5003);
              FrameHandler.showPanel(new AlterarCertificadoPanel());
          }
        });
        add(bt2);
        index += 1;
        
        JButton bt3 = new JButton("<html><p style='text-align:center;'>Consultar pasta de arquivos<br>secretos do usuário</p></html>");
        bt3.setFont(new Font(null, Font.PLAIN, 15));
        newYoffset = offsetY + (height/qtdBotoes) * index;
        bt3.setBounds(offsetX, newYoffset, width, height/qtdBotoes - 5);
        bt3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(5004);
                FrameHandler.showPanel(new ConsultarArquivosPanel());
            }
        });
        add(bt3);
        index += 1;

        JButton bt4 = new JButton("Sair do Sistema");
        bt4.setFont(new Font(null, Font.PLAIN, 15));
        newYoffset = offsetY + (height/qtdBotoes) * index;
        bt4.setBounds(offsetX, newYoffset, width, height/qtdBotoes - 5);
        bt4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogHandler.logWithUser(5005);
                LogHandler.logWithUser(9003);
                FrameHandler.showPanel(new ConfirmacaoSaidaPanel());
            }
        });
        add(bt4);
      }
  }
