
import rsa.MethodenFromRSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();

    private int blockSize;

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

        nonCubicNumberMField.setText("844");
        onlyAllowNumbers(nonCubicNumberMField);
        numberSystemBaseField.setText("55926");
        onlyAllowNumbers(numberSystemBaseField);
        millerRabinStepsField.setText("100");
        onlyAllowNumbers(millerRabinStepsField);
        primeBitLengthField.setText("1024");
        onlyAllowNumbers(primeBitLengthField);

        calculateBlockSize(getPrimeBitLength(), getNumberSystemBase());

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
                BigInteger TempP = MethodenFromRSA.calculatePrimeByBitLength(getPrimeBitLength().divide(BigInteger.TWO), getM(), getMillerRabinSteps());
                BigInteger TempQ = MethodenFromRSA.calculatePrimeByBitLength(getPrimeBitLength().divide(BigInteger.TWO), getM(), getMillerRabinSteps(), TempP);

                BigInteger n = TempP.multiply(TempQ);
                BigInteger phiN = (TempP.subtract(BigInteger.ONE)).multiply(TempQ.subtract(BigInteger.ONE));

                Alice = new Communicator("Alice", n, phiN, new Point(900, 0));
                Bob = new Communicator("Bob", n, phiN, new Point(900, 400));
                alicePublicKeyField.setText(Alice.e.toString());
                alicePublicNField.setText(Alice.n.toString());
                bobPublicKeyField.setText(Bob.e.toString());
                bobPublicNField.setText(Bob.n.toString());
            }
        });
        nonCubicNumberMField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int value = Integer.parseInt(nonCubicNumberMField.getText());
                double sqrt = Math.sqrt(value);
                if(sqrt != Math.floor(sqrt) && value > 2) {
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
    public static CommunicationPanel getInstance() {
        return singleton;
    }

    public Communicator getAlice() {
        return this.Alice;
    }

    public Communicator getBob() {
        return this.Bob;
    }

    private BigInteger getBigIntegerOfField(JTextField field) throws NumberFormatException{
        return BigInteger.valueOf(Long.parseLong(field.getText()));
    }

    public int getMillerRabinSteps() throws NumberFormatException {return getBigIntegerOfField(millerRabinStepsField).intValue();
    }
    public BigInteger getM() throws NumberFormatException {
        BigInteger m = getBigIntegerOfField(nonCubicNumberMField);
        BigDecimal mSqrt = new BigDecimal(m).sqrt(new MathContext(100));
        if(m != BigInteger.TWO && mSqrt.divide(BigDecimal.ONE) != BigDecimal.ZERO) {
            return getBigIntegerOfField(nonCubicNumberMField);
        } else {
            throw new NumberFormatException("M ist eine Quadratzahl");
        }
    }
    public int getNumberSystemBase() throws NumberFormatException {
        return Integer.parseInt(numberSystemBaseField.getText());
    }
    public BigInteger getPrimeBitLength() throws NumberFormatException {
        return getBigIntegerOfField(primeBitLengthField);
    }
    public void calculateBlockSize(BigInteger bitLengthN, int numberSystemBase){
        this.blockSize = (int)(bitLengthN.doubleValue() * (Math.log(2) / Math.log(numberSystemBase)));
    }
    public int getBlockSize() {
        return blockSize;
    }
}