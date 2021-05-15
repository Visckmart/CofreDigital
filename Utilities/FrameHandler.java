package Utilities;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameHandler {

    private static JFrame mainFrame;

    public static void setMainFrame(JFrame frame) {
        mainFrame = frame;
    }
    
    public static void showPanel(JPanel panel) {
        mainFrame.setContentPane(panel);
        mainFrame.invalidate();
        mainFrame.validate();
    }

    public static void showPanel(JPanel panel, JButton defaultButton) {
        showPanel(panel);
        mainFrame.getRootPane().setDefaultButton(defaultButton);
    }
}
