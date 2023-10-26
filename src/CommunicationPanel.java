import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();
    private static JPanel panel;
    private static JTextField millerRabinStepsField;
    private static JTextField blocklengthField;
    private static JTextField lengthOfP1;
    private static JTextField lengthOfP2;
    private static JTextField alicePublicKeyField;
    private static JTextField bobPublicKeyField;
    private static JTextField alicePublicNField;
    private static JTextField bobPublicNField;
    private static Communicator Alice = null;
    private static Communicator Bob = null;
    private static GridBagConstraints c = null;

    private CommunicationPanel() {
        super("CommunicationPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        setSize(new Dimension(500, 400));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        JButton generateCommunicators = new JButton("Generiere Alice und Bob");

        millerRabinStepsField = getNewTextfield(0, "Miller-Rabin Schritte", true);
        blocklengthField = getNewTextfield(1, "Blocklänge",true);
        lengthOfP1 = getNewTextfield(2, "Länge von p1",true);
        lengthOfP2 = getNewTextfield(3, "Länge von p2",true);
        alicePublicKeyField = getNewTextfield(4, "Öffentlicher Schlüssel e von Alice");
        alicePublicNField = getNewTextfield(5, "Öffentlicher Schlüssel n von Alice");
        bobPublicKeyField = getNewTextfield(6, "Öffentlicher Schlüssel e von Bob");
        bobPublicNField = getNewTextfield(7, "Öffentlicher Schlüssel n von Bob");

        onlyAllowNumbers(millerRabinStepsField);
        onlyAllowNumbers(blocklengthField);
        onlyAllowNumbers(lengthOfP1);
        onlyAllowNumbers(lengthOfP2);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
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
                BigInteger n = BigInteger.ZERO;
                //BigInteger n = MathMethods.getRandomPrimeNumber();

                Alice = new Communicator("Alice", n, new Point(500, 0));
                alicePublicKeyField.setText(e.toString());
                alicePublicNField.setText(n.toString());

                Bob = new Communicator("Bob", n, new Point(500, 400));
                bobPublicKeyField.setText(e.toString());
                bobPublicNField.setText(n.toString());
            }
        });

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

    private static JTextField getNewTextfield(int row, String headline, boolean editable) {
        JTextField field = new JTextField();
        field.setEditable(editable);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200, 20));
        t.setEditable(false);
        j.add(t);
        field.setPreferredSize(new Dimension(200, 20));
        j.add(field);
        panel.add(j, c);
        return field;
    }

    private static JTextField getNewTextfield(int row, String headline) {
        return getNewTextfield(row, headline,false);
    }
}