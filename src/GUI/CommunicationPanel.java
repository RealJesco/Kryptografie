package GUI;

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
    private static JTextField lengthField_P;
    private static JTextField parameterField_n;

    private static JTextArea publicKeyField_E;
    private static JTextArea publicKeyField_p;
    private static JTextArea publicKeyField_q;
    private static JTextArea publicKeyField_g;
    private static JTextArea publicKeyField_y;
    private static JTextArea privateKeyField_x;

    private static GridBagConstraints c = null;

    private CommunicationPanel() {
        super("GUI.CommunicationPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 700));
        setSize(new Dimension(650, 700));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        JButton finalizeButton = new JButton("Schlüssel erzeugen");
        int i = 0;
        nonCubicNumberMField = getNewTextfield(i++, "Nicht-Quadratzahl m");
        millerRabinStepsField = getNewTextfield(i++, "Miller-Rabin Schritte");
        numberSystemBaseField = getNewTextfield(i++, "g (g-Adisches System)");
        lengthField_P = getNewTextfield(i++, "Bit-Länge von p");
        parameterField_n = getNewTextfield(i++, "Parameter n für ell. Kurve");
        publicKeyField_E = getNewTextArea(i++, "Public Key E");
        publicKeyField_p = getNewTextArea(i++, "Public Key p");
        publicKeyField_q = getNewTextArea(i++, "Public Key q");
        publicKeyField_g = getNewTextArea(i++, "Public Key g");
        publicKeyField_y = getNewTextArea(i++, "Public Key y");
        publicKeyField_y = getNewTextArea(i++, "Zufallszahl x");

        nonCubicNumberMField.setText("845");
        onlyAllowNumbers(nonCubicNumberMField);
        numberSystemBaseField.setText("55296");
        onlyAllowNumbers(numberSystemBaseField);
        millerRabinStepsField.setText("100");
        onlyAllowNumbers(millerRabinStepsField);
        lengthField_P.setText("1024");
        onlyAllowNumbers(lengthField_P);
        onlyAllowNumbers(parameterField_n);


        finalizeButton.addActionListener(e -> {
            //Schlüssel erzeugen
            //Schlüssel setzen
            //Schlüssel übergeben
            KlartextPanel.openPanel();
            ChiffratSignaturPanel.openPanel();
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
        panel.add(finalizeButton, c);
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
        return getBigIntegerOfField(lengthField_P);
    }



}