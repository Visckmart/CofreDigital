package General;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Authentication.AuthenticationHandler;
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
        sp.setBounds(230, 175, 450, 330);
        add(sp);
        prepararDirChooser(50, 175, 200, 50);
    }

    void prepararDirChooser(int offsetX, int offsetY, int width, int height) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./Pacote-T4"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton input = new JButton("Escolher arquivo da chave privada...");
        input.setBounds(offsetX, offsetY, width, height);
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(input);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    
                    // IndexHandler ih = new IndexHandler();
                    // Path p = new Path(fc.getSelectedFile().getAbsolutePath(), "index."
                
                    // setFileList(ih.parseIndex(p));
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        add(input);
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