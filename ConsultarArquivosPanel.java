import java.awt.Font;
import java.util.List;
import java.awt.event.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
  
public class ConsultarArquivosPanel extends JPanel {
    
    JTable j;
  
    // Constructor
    ConsultarArquivosPanel() {

        CabecalhoPanel cabecalho = CabecalhoPanel.panel;
        CabecalhoPanel.panel.updateLoginInfo("login", "grupo", "nome");
        cabecalho.setBounds(20, 20, cabecalho.getWidth(), cabecalho.getHeight());
        cabecalho.updateExtraInfo("Total de acessos", "10");
        add(cabecalho);
  
        // Data to be displayed in the JTable
        String[][] data = {
            { "Kundan Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Kundan Kumar Jha", "4031", "CSE" },
            { "Anand Jha", "6014", "IT" }
        };
  
        // Column Names
        String[] columnNames = { "Name do Arquivo", "Dono", "Grupo" };
  
        // Initializing the JTable
        DefaultTableModel tableModel = (DefaultTableModel) new DefaultTableModel(null, columnNames);
        j = new JTable(tableModel) {
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
               return false;
            }
         };
        j.setBounds(30, 40, 200, 300);
        j.setRowHeight(25);
        j.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
               if (me.getClickCount() == 2) {     // to detect doble click events
                  JTable target = (JTable)me.getSource();
                  int row = target.getSelectedRow(); // select a row
                  int column = target.getSelectedColumn(); // select a column
                 JOptionPane.showMessageDialog(null, j.getValueAt(row, column)); // get the value of a row and column.
               }
            }
         });

        j.getTableHeader().setFont(new Font(null, Font.PLAIN, 15));
        j.setFont(new Font(null, Font.PLAIN, 15));

        TableColumn column = null;
        for (int i = 0; i < 3; i++) {
            column = j.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(200); //third column is bigger
            } else {
                column.setPreferredWidth(50);
            }
        }
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