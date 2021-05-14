package General;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
import java.awt.event.*;

public class ConsultarArquivosPanel extends GeneralPanel {
    
    ListaArquivosTable listaArquivos;
  
    // Constructor
    public ConsultarArquivosPanel() {
        super("Consulta de Arquivos", true);
        prepararDirChooser(175, 180, 360, 35);
        prepareListButton(560, 180, 120, 35);

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
        scrollPane.setBounds(20, 225, 660, 280);
        add(scrollPane);
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
                    directoryLabel.setText(chosenDirectory.getName());
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

    public void saveRowAsFile(int row) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("~/Downloads"));
        File defaultName = new File(fileInfoList.get(row).nomeOriginal);
        fc.setSelectedFile(defaultName);
        int saveDialogOption = fc.showSaveDialog(this);
        if (saveDialogOption == JFileChooser.APPROVE_OPTION) {
            decryptFileAndSaveAs(new File(fileInfoList.get(row).nomeProtegido), fc.getSelectedFile());
            // final String content = "a";
            // try (
            //     final BufferedWriter writer = Files.newBufferedWriter(fc.getSelectedFile().toPath()
            //     ,
            //         StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            // ) {
            //     writer.write(content);
            //     writer.flush();
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            // consultarPasta();
            // setFileList(ih.parseIndex(p));
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    void decryptFileAndSaveAs(File file, File destination) {
        System.out.println(file);
        System.out.println(destination);
        System.out.println(chosenDirectory.getAbsolutePath());
        System.out.println(file.getName());
        try {
            byte[] x = new FileHandler().decryptAndVerifyFile(chosenDirectory.getAbsolutePath(), file.getName());
            // System.out.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(x)).toString());
            try {
                System.out.println(destination.toPath());
                Files.write(destination.toPath(), x);
                // writer.
                // writer.write(x);
                // writer.write(x);
                // writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void doubleClickedRow(int row) {
        System.out.println(row);
        System.out.println(fileInfoList.get(row));
    }

    List<FileInfo> fileInfoList;

    void setFileList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
        DefaultTableModel tableModel = (DefaultTableModel)listaArquivos.getModel();
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