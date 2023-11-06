import mathMethods.MathMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();
    private static JPanel panel;
    private static JButton generateCommunicators;
    private static JTextField randomNumberM;
    private static JTextField millerRabinStepsField;
    private static JTextField blocklengthField;
    private static JTextField lengthOfP1;
    private static JTextField lengthOfP2;
    private static JTextArea alicePublicKeyField;
    private static JTextArea bobPublicKeyField;
    private static JTextArea alicePublicNField;
    private static JTextArea bobPublicNField;
    private static Communicator Alice = null;
    private static Communicator Bob = null;
    private static GridBagConstraints c = null;
    private static Random random;

    private CommunicationPanel() {
        super("CommunicationPanel");
        random = new SecureRandom();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 500));
        setSize(new Dimension(650, 500));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        generateCommunicators = new JButton("Generiere Alice und Bob");
        int i = 0;
        randomNumberM = getNewTextfield(i++, "Nicht-Quadratzahl m");
        millerRabinStepsField = getNewTextfield(i++, "Miller-Rabin Schritte");
        blocklengthField = getNewTextfield(i++, "Blocklänge");
        lengthOfP1 = getNewTextfield(i++, "Länge von p1");
        lengthOfP2 = getNewTextfield(i++, "Länge von p2");
        alicePublicKeyField = getNewTextArea(i++, "Öffentlicher Schlüssel e von Alice");
        alicePublicNField = getNewTextArea(i++, "Öffentlicher Schlüssel n von Alice");
        bobPublicKeyField = getNewTextArea(i++, "Öffentlicher Schlüssel e von Bob");
        bobPublicNField = getNewTextArea(i++, "Öffentlicher Schlüssel n von Bob");

        millerRabinStepsField.setText("10");
        onlyAllowNumbers(millerRabinStepsField);
        blocklengthField.setText("10");
        onlyAllowNumbers(blocklengthField);
        lengthOfP1.setText("10");
        onlyAllowNumbers(lengthOfP1);
        lengthOfP2.setText("10");
        onlyAllowNumbers(lengthOfP2);

        generateCommunicators.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Bob.dispose();
                } catch (Exception f) {
                }
                try {
                    Alice.dispose();
                } catch (Exception f) {
                }
                //Übergang für random Prime
                BigInteger TempP1 = MathMethods.getRandomPrimeBigInteger(Integer.parseInt(lengthOfP1.getText()),Integer.parseInt(randomNumberM.getText()),Integer.parseInt(millerRabinStepsField.getText()),random);
                BigInteger TempP2;
                do {
                    TempP2 = MathMethods.getRandomPrimeBigInteger(Integer.parseInt(lengthOfP2.getText()),Integer.parseInt(randomNumberM.getText()),Integer.parseInt(millerRabinStepsField.getText()),random);
                }while (TempP1.equals(TempP2));

                BigInteger n = TempP1.multiply(TempP2);
                Alice = new Communicator("Alice", TempP1, TempP2, random, new Point(900, 0));
                Bob = new Communicator("Bob", TempP1, TempP2, random, new Point(900, 400));
                alicePublicKeyField.setText(Alice.e.toString());
                alicePublicNField.setText(Alice.n.toString());
                bobPublicKeyField.setText(Bob.e.toString());
                bobPublicNField.setText(Bob.n.toString());
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(generateCommunicators, c);
        add(panel);
        panel.updateUI();
    }

    private void onlyAllowNumbers(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                jTextFieldKeyTyped(e);
            }
        });
    }

    public static CommunicationPanel getInstance() {
        return singleton;
    }

    private void jTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }

    private static JTextField getNewTextfield(int row, String headline) {
        JTextField field = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200, 20));
        t.setEditable(false);
        j.add(t);
        field.setPreferredSize(new Dimension(400, 20));
        j.add(field);
        panel.add(j, c);
        return field;
    }
    private static JTextArea getNewTextArea(int row, String headline) {
        JTextArea field = new JTextArea();
        field.setLineWrap(true);
        field.setEditable(false);
        field.setBackground(new Color(238,238,238));
        JScrollPane scrollPane = new JScrollPane(field);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200, 60));
        t.setEditable(false);
        j.add(t);
        scrollPane.setPreferredSize(new Dimension(400, 60));
        j.add(scrollPane);
        panel.add(j, c);
        return field;
    }
}