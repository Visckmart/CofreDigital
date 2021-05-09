import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TecladoFoneticoPanel extends JPanel {
    
    JButton[] keys = new JButton[6];
    JTextPane feedbackField = new JTextPane();

    String emailAddress;
    public TecladoFoneticoPanel(String emailAddress) {
        this.setLayout(null);
        
        this.emailAddress = emailAddress;
        this.prepareTitle();
        this.prepararCampoDeSenha(257, 200, 185, 35);
        this.prepararBotoes(150, 250, 400, 115);
        this.prepararBotaoLogin(285, 375, 130, 35);
        
        // String[] z = {"X", "Y", "NO-TA-10", "J", "K", "L"};
        // this.atualizarBotoes(z);
        updatePasswordFeedback(0);
        tf = new TecladoFonetico();
        // ArrayList<List<String>> gerarCombinacoes = tf.gerarCombinacoes();
        // String[] s = new String[6];
        // for (int i = 0; i < 6; i++) {
        //     String b = "";
        //     for (String f : gerarCombinacoes.get(i)) {
        //         b += f + "-";
        //     }
        //     b = b.substring(0, b.length()-1);
        //     s[i] = b;
        // }
        tf.atualizarCombinacoes();
        this.atualizarBotoes(tf.obterTextoDosBotoes());
    }
    TecladoFonetico tf;
    void prepareTitle() {
        JLabel title = new JLabel("Cofre Digital");
        title.setBounds(175, 25, 350, 75);
        title.setFont(new Font(null, Font.BOLD, 50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JLabel authors = new JLabel("<html><p style='text-align:center;'>Thiago Lamenza e Victor Martins<br>Segurança da Informação – Grupo 7</p></html>");
        authors.setBounds(150, 100, 400, 50);
        authors.setFont(new Font(null, Font.PLAIN, 18));
        authors.setHorizontalAlignment(SwingConstants.CENTER);
        add(authors);
    }

    private void updatePasswordFeedback(int filledCharacters) {
        SimpleAttributeSet attributeSet;
        StyledDocument doc;
        try {
            attributeSet = new SimpleAttributeSet();
            feedbackField.setCharacterAttributes(attributeSet, true);

            feedbackField.setText("•".repeat(filledCharacters));
            
            int remaining = 8-filledCharacters;
            if (remaining > 0) {
                doc = feedbackField.getStyledDocument();
                attributeSet = new SimpleAttributeSet();  
                StyleConstants.setForeground(attributeSet, Color.gray);
                doc.insertString(doc.getLength(), "•".repeat(remaining), attributeSet);  
            }
            
            int remainingExtra;
            if (remaining > 0) {
                remainingExtra = 4;
            } else {
                remainingExtra = 12-filledCharacters;
            }
            if (remainingExtra > 0) {
                attributeSet = new SimpleAttributeSet();  
                StyleConstants.setForeground(attributeSet, Color.DARK_GRAY);
                doc = feedbackField.getStyledDocument();  
                doc.insertString(doc.getLength(), "•".repeat(remainingExtra), attributeSet); 
            }

            doc = feedbackField.getStyledDocument(); 
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            
            loginButton.setEnabled(remaining <= 0);
        } catch (Exception e) {

        }
    }
      
    private void prepararCampoDeSenha(int offsetX, int offsetY, int width, int height) {
        feedbackField.setBounds(offsetX, offsetY, width, height);
        feedbackField.setFont(new Font(null, Font.PLAIN, 25));
        feedbackField.setBackground(Color.black);
        feedbackField.setForeground(Color.white);
        add(feedbackField);
    }
    int x = 0;
    private void prepararBotoes(int xOffset, int yOffset, int width, int height) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                int xi = i;
                int xj = j;
                JButton botao = new JButton(Integer.toString(i * 3 + j));
                botao.setFont(new Font(null, Font.PLAIN, 17));
                botao.setBounds(xOffset + (width/3) * j, yOffset + (height/2) * i, width/3 - 5, height/2 - 5);
                TecladoFoneticoPanel t = this;
                botao.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        x += 2;
                        updatePasswordFeedback(x);
                        tf.registrarDigitacao(xi * 3 + xj);
                        // System.out.println(tf.checarSenha("BABECACE"));
                        tf.atualizarCombinacoes();
                        t.atualizarBotoes(tf.obterTextoDosBotoes());
                        // frame.setContentPane(p2);
                        // frame.invalidate();
                        // frame.validate();
                    }
                });
                this.keys[i * 3 + j] = botao;
                this.add(botao);
            }
        }
    }
    
    JButton loginButton;
    void prepararBotaoLogin(int offsetX, int offsetY, int width, int height) {
        loginButton = new JButton("Continuar   >");
        loginButton.setBounds(offsetX, offsetY, width, height);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextStep();
                // JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(loginButton);
                // frame.setContentPane(next);
                // frame.invalidate();
                // frame.validate();
            }
        });
        this.add(loginButton);
    }
    void nextStep() {
        boolean correctPassword;
        try {
            correctPassword = UserLoginHandler.checkPhoneticPassword(tf.gruposDigitados, emailAddress);
            if (correctPassword == true) {
                System.out.println("Correct");
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new VerificaChavePanel(emailAddress));
                frame.invalidate();
                frame.validate();
            } else {
                System.out.println("Wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void atualizarBotoes(List<String> fonemas) {
        assert fonemas.size() == keys.length : "Atualização de botões não contém o número correto de fonemas";
        for (int i = 0; i < keys.length; i++) {
            keys[i].setText(fonemas.get(i));
        }
    }
  }
