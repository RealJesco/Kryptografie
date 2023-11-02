import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.BigInteger;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();
    private static JPanel panel;
    private static JButton generateCommunicators;
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

    private CommunicationPanel() {
        super("CommunicationPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 500));
        setSize(new Dimension(650, 500));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        generateCommunicators = new JButton("Generiere Alice und Bob");

        millerRabinStepsField = getNewTextfield(0, "Miller-Rabin Schritte");
        blocklengthField = getNewTextfield(1, "Blocklänge");
        lengthOfP1 = getNewTextfield(2, "Länge von p1");
        lengthOfP2 = getNewTextfield(3, "Länge von p2");
        alicePublicKeyField = getNewTextArea(4, "Öffentlicher Schlüssel e von Alice");
        alicePublicNField = getNewTextArea(5, "Öffentlicher Schlüssel n von Alice");
        bobPublicKeyField = getNewTextArea(6, "Öffentlicher Schlüssel e von Bob");
        bobPublicNField = getNewTextArea(7, "Öffentlicher Schlüssel n von Bob");

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
                BigInteger TempP1;
                BigInteger TempP2;
                do{
                    TempP1 = MathMethods.getRandomBigInteger(BigInteger.TEN.pow(Integer.parseInt(lengthOfP1.getText())));
                }while (! MathMethods.millerRabinTest(TempP1, Integer.parseInt(millerRabinStepsField.getText())));
                do{
                    TempP2 = MathMethods.getRandomBigInteger(BigInteger.TEN.pow(Integer.parseInt(lengthOfP2.getText())));
                }while (! MathMethods.millerRabinTest(TempP2, Integer.parseInt(millerRabinStepsField.getText())));
                BigInteger n = TempP1.multiply(TempP2);
                Alice = new Communicator("Alice", TempP1, TempP2, new Point(900, 0));
                alicePublicKeyField.setText(Alice.e.toString());
                alicePublicNField.setText(Alice.n.toString());
                Bob = new Communicator("Bob", TempP1, TempP2, new Point(900, 400));
                bobPublicKeyField.setText(Bob.e.toString());
                bobPublicNField.setText(Bob.n.toString());
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
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

    public int getMillerRabinSteps() {
        return Integer.parseInt(millerRabinStepsField.getText());
    }

    public int getBlockLength(){
        return Integer.parseInt(blocklengthField.getText());
    }
}