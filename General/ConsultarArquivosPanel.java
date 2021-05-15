package General;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.FileInfo;
import Utilities.LogHandler;

import java.awt.event.*;

public class ConsultarArquivosPanel extends GeneralPanel {
    
    ListaArquivosTable listaArquivos;

    @Override protected int getBackCode() {
        return 8002;
    }

    // Constructor
    public ConsultarArquivosPanel() {
        super("Consulta de Arquivos", true, CabecalhoInfo.TOTAL_CONSULTAS);
        prepararDirChooser(20, 190, 530, 35);
        prepareListButton(560, 190, 120, 35);

        listaArquivos = new ListaArquivosTable();
        add(listaArquivos);

        MouseAdapter doubleClickHandler = new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    JTable target = (JTable)event.getSource();
                    int row = target.getSelectedRow();
                    saveRowAsFile(row);
                }
            }
        };
        listaArquivos.addMouseListener(doubleClickHandler);
        JScrollPane scrollPane = new JScrollPane(listaArquivos);
        scrollPane.setBounds(20, 230, 660, 275);
        add(scrollPane);

        LogHandler.logWithUser(8001);
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
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./Pacote-T4"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JButton input = new JButton("Escolher caminho...");
        input.setBounds(offsetX, offsetY, 150, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int openDialogOption = fileChooser.showOpenDialog(input);
                if (openDialogOption == JFileChooser.APPROVE_OPTION) {
                    chosenDirectory = fileChooser.getSelectedFile();
                    directoryLabel.setText(chosenDirectory.getAbsolutePath());
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        add(input);
    }

    void consultarPasta() {
        LogHandler.logWithUser(8003);
        if (chosenDirectory == null) {
            setFileList(new ArrayList<FileInfo>());
            return;
        }
        String directory = chosenDirectory.getAbsolutePath();
        try {
            byte[] fileContent = new FileHandler().decryptAndVerifyFile(directory, "index");

            List<FileInfo> fileInfos = new IndexHandler().parseIndexContent(fileContent);
            setFileList(fileInfos);
            LogHandler.logWithUser(8009);
            DatabaseHandler.getInstance().registerQuery(UserState.emailAddress);
            CabecalhoPanel.panel.atualizarInformacaoAdicional();
        } catch (IOException ignored) {
            // TODO: Avisar para o usuário
        } catch (InvalidKeyException ignored) {
            // TODO: Avisar para o usuário
        } catch (SignatureException ignored) {
            // TODO: Avisar para o usuário
        } catch (Exception ignored) {
            // TODO: Avisar para o usuário
        }
    }

    public void saveRowAsFile(int row) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("~/Downloads"));
        File defaultName = new File(fileInfoList.get(row).nomeOriginal);
        fc.setSelectedFile(defaultName);
        LogHandler.logWithUserAndFile(8010, defaultName.getName());
        int saveDialogOption = fc.showSaveDialog(this);

        if(fileInfoList.get(row).checkAccess()) {
            LogHandler.logWithUserAndFile(8011, defaultName.getName());
            if (saveDialogOption == JFileChooser.APPROVE_OPTION) {
                decryptFileAndSaveAs(new File(fileInfoList.get(row).nomeProtegido), fc.getSelectedFile());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        } else {
            LogHandler.logWithUserAndFile(8012, defaultName.getName());
        }

    }

    void decryptFileAndSaveAs(File file, File destination) {
        try {
            byte[] fileContent = new FileHandler().decryptAndVerifyFile(chosenDirectory.getAbsolutePath(), file.getName());
            Files.write(destination.toPath(), fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<FileInfo> fileInfoList;

    void setFileList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
        DefaultTableModel tableModel = (DefaultTableModel)listaArquivos.getModel();
        tableModel.setRowCount(0);
        for (FileInfo fileInfo : fileInfoList) {
            String[] rowInfo = {
                fileInfo.nomeOriginal, fileInfo.nomeProtegido, fileInfo.dono, fileInfo.grupo };
            tableModel.addRow(rowInfo);
        }
        tableModel.setRowCount(fileInfoList.size());
    }
}
