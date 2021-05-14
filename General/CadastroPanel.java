package General;
import javax.swing.*;

import General.TecladoFoneticoFullPanel.SuccessHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CadastroPanel extends GeneralPanel {
    
    String emailAddress;
    public CadastroPanel() {
        super("Cadastrar Novo Usuário", true);
        this.prepararBotaoArquivo(210, 250, 280, 35);
        this.prepararTextoArquivo(210, 280, 280, 35);
        this.prepararCampoGrupo(210, 320, 320, 50);
        this.prepararBotaoLogin(285, 400, 130, 35);
    }
    
    JList<String> groupTF;
    private void prepararCampoGrupo(int offsetX, int offsetY, int width, int height) {
        JLabel passwordLabel = new JLabel("Grupo do usuário: ");
        passwordLabel.setBounds(offsetX, offsetY, width*4/10, height);
        this.add(passwordLabel);
        
        String[] grupos = {"Administrador", "Usuário"};
        groupTF = new JList<String>(grupos);
        groupTF.setBounds(offsetX + width*4/10 + 10, offsetY, (width*6/10 - 10), height);
        groupTF.setFixedCellHeight(25);
        groupTF.setSelectedIndex(1);
        this.add(groupTF);
    }
    
    File chosenFile;
    void prepararBotaoArquivo(int offsetX, int offsetY, int width, int height) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./Pacote-T4/Keys"));
        JButton input = new JButton("Escolher arquivo do certificado...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chosenFile = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + chosenFile.getName() + ".");
                } else {
                    chosenFile = null;
                    System.out.println("Open command cancelled by user.");
                }
                atualizarTextoArquivo();
            }
        });
        this.add(input);
        
        clearButton = new JButton("X");
        clearButton.setBounds(offsetX + width + 5, offsetY, height, height);
        clearButton.setEnabled(false);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chosenFile = null;
                atualizarTextoArquivo();
            }
        });
        this.add(clearButton);
    }
    JButton clearButton;
    JLabel labelCaminho;
    private void prepararTextoArquivo(int offsetX, int offsetY, int width, int height) {
        labelCaminho = new JLabel("Nenhum arquivo escolhido.");
        labelCaminho.setBounds(offsetX, offsetY, width, height);
        add(labelCaminho);
    }
    
    private void atualizarTextoArquivo() {
        if (chosenFile != null) {
            labelCaminho.setText(chosenFile.getPath());
            clearButton.setEnabled(true);
        } else {
            labelCaminho.setText("Nenhum arquivo escolhido.");
            clearButton.setEnabled(false);
        }
    }
    JButton loginButton;
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        this.add(loginButton);
    }
    
    void nextStep() {
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        TecladoFoneticoFullPanel vcp = new TecladoFoneticoFullPanel("Senha do Novo Usuário", null);
        vcp.setSuccessHandler(SuccessHandler.CADASTRAR);
        frame.setContentPane(vcp);
        frame.invalidate();
        frame.validate();
    }
    
    
  }
