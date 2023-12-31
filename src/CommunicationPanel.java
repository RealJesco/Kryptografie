
import rsa.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class CommunicationPanel extends JFrame {
    private static final CommunicationPanel singleton = new CommunicationPanel();


    private static JPanel panel;
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

        JButton generateCommunicators = new JButton("Generiere Alice und Bob");
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
        numberSystemBaseField.setText("55296");
        onlyAllowNumbers(numberSystemBaseField);
        millerRabinStepsField.setText("100");
        onlyAllowNumbers(millerRabinStepsField);
        primeBitLengthField.setText("1024");
        onlyAllowNumbers(primeBitLengthField);


        generateCommunicators.addActionListener(e -> {
            disposeCommunicators();
            try{

                RSA.setNumberSystemBase(getIntOfField(numberSystemBaseField));
                RSA.setBitLengthN(getIntOfField(primeBitLengthField));
                RSA.setMillerRabinSteps(getIntOfField(millerRabinStepsField));
                RSA.setM(getBigIntegerOfField(nonCubicNumberMField));
                // Generate unique prime numbers and keys for Alice
                RSA.generateRSAKeys();
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error in Generation of RSA-Instance: " + e);
                disposeCommunicators();
                return;
            }

            try{
                Alice = new Communicator("Alice", RSA.getN(), RSA.getPhiN(), new Point(900, 0));
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error in Generation of Alice: " + e);
                disposeCommunicators();
                return;
            }

            RSA.generateRSAKeys();

            try{
                Bob = new Communicator("Bob", RSA.getN(), RSA.getPhiN(), new Point(900, 400));
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error in Generation of Alice: " + e);
                disposeCommunicators();
                return;
            }

            // Update fields with public keys
            alicePublicKeyField.setText(Alice.e.toString());
            alicePublicNField.setText(Alice.n.toString());
            bobPublicKeyField.setText(Bob.e.toString());
            bobPublicNField.setText(Bob.n.toString());
        });

        nonCubicNumberMField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int value = Integer.parseInt(nonCubicNumberMField.getText());
                double sqrt = Math.sqrt(value);
                if(sqrt == Math.floor(sqrt)) {
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

    private void disposeCommunicators( ) {
        try {
            Bob.dispose();
        } catch (Exception ignored) {
        }
        try {
            Alice.dispose();
        } catch (Exception ignored) {
        }
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
    private int getIntOfField(JTextField field) throws NumberFormatException{
        return Integer.parseInt(field.getText());
    }

    public int getMillerRabinSteps() throws NumberFormatException {
        return getBigIntegerOfField(millerRabinStepsField).intValue();
    }

    public BigInteger getM() throws NumberFormatException {
        return getBigIntegerOfField(nonCubicNumberMField);
    }

    public BigInteger getPrimeBitLength() throws NumberFormatException {
        return getBigIntegerOfField(primeBitLengthField);
    }



}