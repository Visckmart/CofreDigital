package General;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.EventObject;
import java.awt.Font;

public class ListaArquivosTable extends JTable {
    
    static String[] columnNames = {"Nome do Arquivo", "Dono", "Grupo"};

    ListaArquivosTable() {
        super(new DefaultTableModel(null, columnNames));

        this.setBounds(0, 0, 200, 300);
        this.setRowHeight(25);
        ListaArquivosTable t = this;
        MouseAdapter doubleClickHandler = new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    JTable target = (JTable)event.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    // JOptionPane.showMessageDialog(null, target.getValueAt(row, column));
                    final JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File("~/Downloads"));
                    fc.setSelectedFile(new File(target.getValueAt(row, 0).toString()));
                    // fc.setFileSelectionMode(JFileChooser.choo);
                    int returnVal = fc.showSaveDialog(t);
                    fc.getSelectedFile();
                        
                    System.out.println(fc.getSelectedFile());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        final String content = "a";
                        try (
                            final BufferedWriter writer = Files.newBufferedWriter(fc.getSelectedFile().toPath()
                            ,
                                StandardCharsets.UTF_8, StandardOpenOption.CREATE);
                        ) {
                            writer.write(content);
                            writer.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // consultarPasta();
                        // setFileList(ih.parseIndex(p));
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            }
        };
        this.addMouseListener(doubleClickHandler);

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
