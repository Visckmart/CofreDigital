package General;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Utilities.FileInfo;
  
public class ConsultarArquivosPanel extends JPanel {
    
    JTable j;
  
    // Constructor
    ConsultarArquivosPanel() {

        CabecalhoPanel cabecalho = CabecalhoPanel.panel;
        CabecalhoPanel.panel.updateLoginInfo("login", "grupo", "nome");
        cabecalho.setBounds(20, 20, cabecalho.getWidth(), cabecalho.getHeight());
        cabecalho.updateExtraInfo("Total de acessos", "10");
        add(cabecalho);

        j = new ListaArquivosTable();
        add(j);

        this.setLayout(null);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        sp.setBounds(230, 175, 450, 330);
        add(sp);
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