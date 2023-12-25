import mathMethods.MathMethods;
import rsa.MethodenFromRSA;
import rsa.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();
    private int millerRabinSteps = 100;
    private int numberSystemBase = 55926;
    private BigInteger m = BigInteger.valueOf(844);
    private BigInteger primeBitLength = BigInteger.valueOf(1024);
    private static JPanel panel;
    private static JButton generateCommunicators;
    private static JTextField nonCubicNumberMField;
    private static JTextField numberSystemBaseField;
    private static JTextField millerRabinStepsField;
    private static JTextField primeBitLengthField;
    private static JTextArea alicePublicKeyField;
    private static JTextArea bobPublicKeyField;
    private static JTextArea alicePublicNField;
    private static JTextArea bobPublicNField;
    private Communicator Alice = null;
    private Communicator Bob = null;
    private static GridBagConstraints c = null;
    private static RSA rsa;
    private int blockSize;

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
        int i = 0;
        nonCubicNumberMField = getNewTextfield(i++, "Nicht-Quadratzahl m");
        millerRabinStepsField = getNewTextfield(i++, "Miller-Rabin Schritte");
        numberSystemBaseField = getNewTextfield(i++, "Adisches System: Das g");
        primeBitLengthField = getNewTextfield(i++, "Bit-Länge von n");
        alicePublicKeyField = getNewTextArea(i++, "Öffentlicher Schlüssel e von Alice");
        alicePublicNField = getNewTextArea(i++, "Öffentlicher Schlüssel n von Alice");
        bobPublicKeyField = getNewTextArea(i++, "Öffentlicher Schlüssel e von Bob");
        bobPublicNField = getNewTextArea(i++, "Öffentlicher Schlüssel n von Bob");

        nonCubicNumberMField.setText(String.valueOf(m));
        onlyAllowNumbers(nonCubicNumberMField);
        numberSystemBaseField.setText(String.valueOf(numberSystemBase));
        onlyAllowNumbers(numberSystemBaseField);
        millerRabinStepsField.setText(String.valueOf(millerRabinSteps));
        onlyAllowNumbers(millerRabinStepsField);
        primeBitLengthField.setText(String.valueOf(primeBitLength));
        onlyAllowNumbers(primeBitLengthField);

        calculateBlockSize(primeBitLength.intValue(), numberSystemBase);

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
                BigInteger TempP = MethodenFromRSA.calculatePrimeByBitLength(primeBitLength.divide(BigInteger.TWO), m, millerRabinSteps);
                BigInteger TempQ = MethodenFromRSA.calculatePrimeByBitLength(primeBitLength.divide(BigInteger.TWO), m,millerRabinSteps, TempP);

                BigInteger n = TempP.multiply(TempQ);
                BigInteger phiN = (TempP.subtract(BigInteger.ONE)).multiply(TempQ.subtract(BigInteger.ONE));

                Alice = new Communicator("Alice", n, phiN, m, new Point(900, 0));
                Bob = new Communicator("Bob", n, phiN, m, new Point(900, 400));
                alicePublicKeyField.setText(Alice.e.toString());
                alicePublicNField.setText(Alice.n.toString());
                bobPublicKeyField.setText(Bob.e.toString());
                bobPublicNField.setText(Bob.n.toString());
            }
        });

        nonCubicNumberMField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int value = getIntegerOfField(nonCubicNumberMField);
                System.out.println(value);
                double sqrt = Math.sqrt(value);
                if(sqrt != Math.floor(sqrt) && value > 2) {
                    m = BigInteger.valueOf(value);
                } else {
                    // Handle the case where the input is a perfect square
                    // For example, you could reset the field and show an error message
                    nonCubicNumberMField.setText("");
                    JOptionPane.showMessageDialog(null, "Input must be a non-square number");
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(generateCommunicators, c);
        add(panel);
        panel.updateUI();
    }

    private int getIntegerOfField(JTextField field) {
        try{
            return Integer.parseInt(field.getText());
        } catch (Exception e) {
            JFrame frame = new JFrame();
            frame.setSize(new Dimension(200, 100));
            JPanel p = new JPanel();
            p.add(new JLabel("Fehler beim Auslesen eines Feldes -> Parsen ins Integer fehlgeschlagen: " + e.getStackTrace()));
            frame.add(p);
            frame.setVisible(true);
        }
        return 1;
    }


    public Communicator getAlice() {
        return this.Alice;
    }

    public Communicator getBob() {
        return this.Bob;
    }
    public static CommunicationPanel getInstance() {
        return singleton;
    }

    public int getMillerRabinSteps() {
        return millerRabinSteps;
    }

    public BigInteger getM() {
        return m;
    }

    public BigInteger getPrimeBitLength() {
        return primeBitLength;
    }

    public int getNumberSystemBase() {
        return numberSystemBase;
    }
    public void calculateBlockSize(int bitLengthN, int numberSystemBase){
        this.blockSize = (int)(bitLengthN * (Math.log(2) / Math.log(numberSystemBase)));
    }

    public static RSA getRsa() {
        return rsa;
    }

    private void onlyAllowNumbers(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                jTextFieldKeyTyped(e);
            }
        });
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

    public int getBlockSize() {
        return this.blockSize;
    }
}