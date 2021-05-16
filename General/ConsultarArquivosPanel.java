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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.FileInfo;
import Utilities.LogHandler;

import java.awt.Font;
import java.awt.event.*;
import java.awt.Color;

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

        prepararLabelErro(150, 350, 400, 30);

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
    
    JLabel errorLabel;
    void prepararLabelErro(int offsetX, int offsetY, int width, int height) {
        errorLabel = new JLabel();
        errorLabel.setFont(new Font(null, Font.BOLD, 14));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(offsetX, offsetY, width, height);
        this.add(errorLabel);
    }

    File chosenDirectory;
    JLabel directoryLabel;
    void prepararDirChooser(int offsetX, int offsetY, int width, int height) {
        directoryLabel = new JLabel();
        directoryLabel.setBounds(offsetX + 150 + 10, offsetY, width - 150, height);
        add(directoryLabel);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JButton input = new JButton("Escolher caminho...");
        input.setBounds(offsetX, offsetY, 150, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int openDialogOption = fileChooser.showOpenDialog(input);
                if (openDialogOption == JFileChooser.APPROVE_OPTION) {
                    chosenDirectory = fileChooser.getSelectedFile();
                    directoryLabel.setText(chosenDirectory.getAbsolutePath());
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
            errorLabel.setText("");
        } catch (IOException ignored) {
            setFileList(new ArrayList<FileInfo>());
            errorLabel.setText("Index não encontrado.");
        } catch (InvalidKeyException ignored) {
            setFileList(new ArrayList<FileInfo>());
            errorLabel.setText("Não foi possível ler index.");
        } catch (SignatureException ignored) {
            setFileList(new ArrayList<FileInfo>());
            errorLabel.setText("Assinatura digital do index inválida.");
        } catch (Exception ignored) {
            setFileList(new ArrayList<FileInfo>());
            errorLabel.setText("Erro ao consultar.");
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
            }
        } else {
            LogHandler.logWithUserAndFile(8012, defaultName.getName());
        }

    }

    void decryptFileAndSaveAs(File file, File destination) {
        try {
            byte[] fileContent = new FileHandler().decryptAndVerifyFile(chosenDirectory.getAbsolutePath(), file.getName());
            Files.write(destination.toPath(), fileContent);
        } catch (IOException ignored) {
            JOptionPane.showMessageDialog(this, "Arquivo não encontrado.");
        } catch (InvalidKeyException ignored) {
            JOptionPane.showMessageDialog(this, "Não foi possível ler arquivo.");
        } catch (SignatureException ignored) {
            JOptionPane.showMessageDialog(this, "Assinatura digital do arquivo inválida.");
        } catch (Exception ignored) {
            JOptionPane.showMessageDialog(this, "Erro ao ler arquivo.");
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
