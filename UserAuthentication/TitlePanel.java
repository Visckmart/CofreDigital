package UserAuthentication;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TitlePanel extends JPanel {
    
    static TitlePanel instance;
    static TitlePanel getInstance() {
        if (instance == null) {
           instance = new TitlePanel(700, 150);
        }
        return instance;
    }
    
    private TitlePanel(int width, int height) {
        this.setLayout(null);
        
        JLabel title = new JLabel("Cofre Digital");
        title.setBounds(0, 25, 700, 75);
        title.setFont(new Font(null, Font.BOLD, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JLabel authors = new JLabel("<html><p style='text-align:center;'>Thiago Lamenza e Victor Martins<br>Segurança da Informação – Grupo 7</p></html>");
        authors.setBounds(150, 100, 400, 50);
        authors.setFont(new Font(null, Font.PLAIN, 18));
        authors.setHorizontalAlignment(SwingConstants.CENTER);
        add(authors);
        
        this.setBounds(0, 0, width, height);
    }
}
