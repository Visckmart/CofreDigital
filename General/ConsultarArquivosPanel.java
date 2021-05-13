package General;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import Authentication.AuthenticationHandler;
import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.FileInfo;
  
public class ConsultarArquivosPanel extends JPanel {
    
    JTable j;
  
    // Constructor
    public ConsultarArquivosPanel() {

        CabecalhoPanel cabecalho = CabecalhoPanel.panel;
        CabecalhoPanel.panel.updateLoginInfo("login", "grupo", "nome");
        cabecalho.setBounds(20, 20, cabecalho.getWidth(), cabecalho.getHeight());
        cabecalho.updateExtraInfo("Total de acessos", "10");
        add(cabecalho);

        j = new ListaArquivosTable();
        add(j);

        File path = new File("./Pacote-T4/Files");
        File[] files = path.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
        
        this.setLayout(null);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        sp.setBounds(15, 215, 670, 280);
        add(sp);
        prepararDirChooser(165, 175, 360, 35);
        prepareListButton(550, 175, 140, 35);
        prepareBackButton(15, 175, 140, 35);
    }
    void prepareListButton(int offsetX, int offsetY, int width, int height) {
        JButton listButton = new JButton("Consultar");
        listButton.setBounds(offsetX, offsetY, width, height);
        listButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarPasta();
            }
        });
        add(listButton);
    }

    File chosenDirectory;
    JLabel directoryLabel;
    void prepararDirChooser(int offsetX, int offsetY, int width, int height) {
        directoryLabel = new JLabel();
        directoryLabel.setBounds(offsetX + 150 + 10, offsetY, width - 150, height);
        add(directoryLabel);
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./Pacote-T4"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton input = new JButton("Escolher caminho...");
        input.setBounds(offsetX, offsetY, 150, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);
                chosenDirectory = fc.getSelectedFile();
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    directoryLabel.setText(chosenDirectory.getAbsolutePath());
                    // consultarPasta();
                    // setFileList(ih.parseIndex(p));
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        add(input);
    }

    void prepareBackButton(int offsetX, int offsetY, int width, int height) {
        JButton backButton = new JButton("<   Voltar ao menu");
        backButton.setBounds(offsetX, offsetY, width, height);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(backButton);
                frame.setContentPane(new MenuPrincipalPanel());
                frame.invalidate();
                frame.validate();
            }
        });
        add(backButton);
    }

    void consultarPasta() {
        if (chosenDirectory == null) {
            setFileList(new ArrayList<FileInfo>());
            return;
        }
        String directory = chosenDirectory.getAbsolutePath();
        try {
            byte[] fileContent = new FileHandler().decryptAndVerifyFile(directory, "index");

            System.out.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(fileContent)));
            List<FileInfo> fileInfos = new IndexHandler().parseIndexContent(fileContent);
            System.out.println(fileInfos);
            setFileList(fileInfos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setFileList(List<FileInfo> fileInfoList) {
        DefaultTableModel tableModel = (DefaultTableModel)j.getModel();
        tableModel.setRowCount(0);
        for (int i = 0; i < fileInfoList.size(); i++) {
            String[] rowInfo = {
                fileInfoList.get(i).nomeOriginal,
                fileInfoList.get(i).dono,
                fileInfoList.get(i).grupo
            };
            tableModel.addRow(rowInfo);
        }
        tableModel.setRowCount(fileInfoList.size());
        // tableModel.fireTableDataChanged();
    }
}