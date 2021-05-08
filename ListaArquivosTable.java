import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.*;
import java.util.EventObject;
import java.awt.Font;

public class ListaArquivosTable extends JTable {
    
    static String[] columnNames = {"Nome do Arquivo", "Dono", "Grupo"};

    ListaArquivosTable() {
        super(new DefaultTableModel(null, columnNames));

        this.setBounds(30, 40, 200, 300);
        this.setRowHeight(25);
        
        MouseAdapter doubleClickHandler = new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    JTable target = (JTable)event.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    JOptionPane.showMessageDialog(null, target.getValueAt(row, column));
                }
            }
        };
        this.addMouseListener(doubleClickHandler);

        this.getTableHeader().setFont(new Font(null, Font.PLAIN, 15));
        this.setFont(new Font(null, Font.PLAIN, 15));

        TableColumn column = null;
        for (int i = 0; i < columnNames.length; i++) {
            column = this.getColumnModel().getColumn(i);
            if (i == 0) { column.setPreferredWidth(200); }
            else { column.setPreferredWidth(50); }
        }
    }

    public boolean editCellAt(int row, int column, EventObject e) {
        return false;
    }
}
