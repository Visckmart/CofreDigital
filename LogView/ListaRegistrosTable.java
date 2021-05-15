package LogView;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.*;
import java.util.EventObject;
import java.util.List;
import java.awt.*;

public class ListaRegistrosTable extends JTable {
    
    static String[] columnNames = {"Timestamp", "Registro"};

    public ListaRegistrosTable() {
        super(new DefaultTableModel(null, columnNames));
        // this.setBackground(Color.red);
        this.setBounds(10, 50, 680, 460);
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
        this.setFont(new Font(null, Font.PLAIN, 14));

        TableColumn column = null;
        for (int i = 0; i < columnNames.length; i++) {
            column = this.getColumnModel().getColumn(i);
            if (i == 0) { column.setMinWidth(180); column.setMaxWidth(180); }
            else { column.setPreferredWidth(300); }
        }
    }

    public void setRegisterList(List<String[]> registerInfoList) {
        DefaultTableModel tableModel = (DefaultTableModel)this.getModel();
        int selectedRow = getSelectedRow();
        tableModel.setRowCount(0);
        for (int i = registerInfoList.size() - 1; i >= 0; i--) {
            tableModel.addRow(registerInfoList.get(i));
        }
        tableModel.setRowCount(registerInfoList.size());
        setRowSelectionInterval(selectedRow, selectedRow);
    }

    public boolean editCellAt(int row, int column, EventObject e) {
        return false;
    }
}
