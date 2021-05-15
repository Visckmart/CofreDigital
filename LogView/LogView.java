package LogView;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.util.Timer;

import Database.DatabaseHandler;

public class LogView {

    static JFrame frame = new JFrame("LogView por Thiago Lamenza e Victor Martins");
    static ListaRegistrosTable t = new ListaRegistrosTable();
    LogView() {
        
        JPanel p = new JPanel();
        p.setBounds(0, 0, 700, 550);
        // p.setLayout(null);
        p.setLayout(new GridLayout());
        p.add(t);
        try {
            // t.setRegisterList(DatabaseHandler.getInstance().getAllRegisters());
            Timer timer = new Timer();
            timer.schedule(new AtualizarRegistros(), 5000, 2000);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(0, 0, 700, 550);
        p.add(sp);
        frame.add(p);
        frame.invalidate();
        frame.validate();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }


    public static void main(String args[]) {
        new LogView();
        // ArrayList<String[]> l = new ArrayList<String[]>();
        // String[] a = {"a", "b", "c", "d"};
        // l.add(a);
        // lv.t.setRegisterList(l);
    }
}