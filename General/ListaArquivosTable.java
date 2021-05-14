package General;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.EventObject;
import java.util.concurrent.Callable;
import java.awt.Font;

public class ListaArquivosTable extends JTable {
    
    static String[] columnNames = {"Nome do Arquivo", "Dono", "Grupo"};

    // Callable<Void> rowSelectedHandler;
    ListaArquivosTable() {
        super(new DefaultTableModel(null, columnNames));

        this.setBounds(0, 0, 200, 300);
        this.setRowHeight(25);
        ListaArquivosTable t = this;
        // MouseAdapter doubleClickHandler = new MouseAdapter() {
        //     public void mouseClicked(MouseEvent event) {
        //         if (event.getClickCount() == 2) {
        //             JTable target = (JTable)event.getSource();
        //             int row = target.getSelectedRow();
        //             // rowSelectedHandler.call();
        //             // saveRowAsFile(row);
        //         }
        //     }
        // };
        // this.addMouseListener(doubleClickHandler);

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
