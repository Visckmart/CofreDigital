package General;

import Utilities.LogHandler;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public abstract class GeneralPanel extends JPanel {
    protected  int getBackCode() {return -1;}

    GeneralPanel(String title, boolean backToMenu, CabecalhoInfo infoAdicional) {
        this.setLayout(null);
        
        CabecalhoPanel cabecalho = CabecalhoPanel.panel;
        cabecalho.setBounds(20, 20, cabecalho.getWidth(), cabecalho.getHeight());
        cabecalho.setInformacaoAdicional(infoAdicional);
        add(cabecalho);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(10, cabecalho.getHeight() + 25, 680, 30);
        titleLabel.setFont(new Font(null, Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        if (backToMenu) {
            prepareBackButton(20, cabecalho.getHeight() + 25, 150, 35);
        }
    }

    void prepareBackButton(int offsetX, int offsetY, int width, int height) {
        JButton backButton = new JButton("<   Voltar ao menu");
        backButton.setBounds(offsetX, offsetY, width, height);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(backButton);
                frame.setContentPane(new MenuPrincipalPanel());
                frame.invalidate();
                frame.validate();
            }
        });
        add(backButton);
        if(getBackCode() >= 0) {
            LogHandler.logWithUser(getBackCode());
        }
    }

    GeneralPanel(String title, boolean canGoBack) {
        this(title, canGoBack, CabecalhoInfo.TOTAL_ACESSOS);
    }
}
