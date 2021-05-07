import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
        j = new JTable(data, columnNames);
        j.setBounds(30, 40, 200, 300);

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
}