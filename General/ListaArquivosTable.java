package General;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.util.EventObject;
import java.awt.Font;

public class ListaArquivosTable extends JTable {
    
    static String[] columnNames = {"Nome do Arquivo", "Dono", "Grupo"};

    // Callable<Void> rowSelectedHandler;
    ListaArquivosTable() {
        super(new DefaultTableModel(null, columnNames));

        this.setBounds(0, 0, 200, 300);
        this.setRowHeight(25);

        this.getTableHeader().setFont(new Font(null, Font.PLAIN, 15));
        this.setFont(new Font(null, Font.PLAIN, 15));

        TableColumn column = null;
        for (int i = 0; i < columnNames.length; i++) {
            column = this.getColumnModel().getColumn(i);
            if (i == 0) { column.setPreferredWidth(150); }
            else { column.setPreferredWidth(50); }
        }
    }

    public boolean editCellAt(int row, int column, EventObject e) {
        return false;
    }
}
