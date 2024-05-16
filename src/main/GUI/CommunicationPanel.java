package main.GUI;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.finiteFieldEllipticCurve.SecureFiniteFieldEllipticCurve;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneService;
import main.elGamalMenezesVanstone.KeyPair;
import main.encryption.EncryptionContextParamBuilder;
import org.apache.commons.math3.util.Pair;
import main.resource.Resource;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.encryption.EncryptionContext;
import main.rsa.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class CommunicationPanel extends JFrame {
    private static JPanel panel;
    private Communicator Alice = null;
    private Communicator Bob = null;
    private EncryptionContext encryptionContext;


    private static JTextField nonCubicNumberMField;
    private static JTextField numberSystemBaseField;
    private static JTextField millerRabinStepsField;

    private static JTextField lengthField_n;
    private static JTextArea alicePublicKeyField;
    private static JTextArea bobPublicKeyField;
    private static JTextArea alicePublicNField;
    private static JTextArea bobPublicNField;

    private static JTextField lengthField_P;
    private static JTextField parameterField_n;
    private static JTextArea publicKeyField_E;
    private static JTextArea publicKeyField_p;
    private static JTextArea publicKeyField_q;
    private static JTextArea publicKeyField_g;
    private static JTextArea publicKeyField_y;
    private static JTextArea privateKeyField_x;

    private static GridBagConstraints c = new GridBagConstraints();
    private static final CommunicationPanel singleton = new CommunicationPanel();

    private CommunicationPanel() {
        super("main.GUI.CommunicationPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 700));
        setSize(new Dimension(650, 700));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        this.encryptionContext = new EncryptionContext();
        this.encryptionContext.setStrategy(new RsaStringService());

        int j = 0;
        JPanel RSAPanel = new JPanel();
        RSAPanel.setLayout(new GridBagLayout());
        lengthField_n = getNewTextfield(RSAPanel, j++, "Bit-Länge von n");
        alicePublicKeyField = getNewTextArea(RSAPanel, j++, "Öffentlicher Schlüssel e von Alice");
        alicePublicNField = getNewTextArea(RSAPanel, j++, "Öffentlicher Schlüssel n von Alice");
        bobPublicKeyField = getNewTextArea(RSAPanel, j++, "Öffentlicher Schlüssel e von Bob");
        bobPublicNField = getNewTextArea(RSAPanel, j, "Öffentlicher Schlüssel n von Bob");
        j = 0;
        JPanel ElGamalPanel = new JPanel();
        ElGamalPanel.setLayout(new GridBagLayout());
        lengthField_P = getNewTextfield(ElGamalPanel, j++, "Bit-Länge von p");
        parameterField_n = getNewTextfield(ElGamalPanel, j++, "Parameter n für ell. Kurve");
        publicKeyField_E = getNewTextArea(ElGamalPanel, j++, "Public Key E");
        publicKeyField_p = getNewTextArea(ElGamalPanel, j++, "Public Key p");
        publicKeyField_q = getNewTextArea(ElGamalPanel, j++, "Public Key q");
        publicKeyField_g = getNewTextArea(ElGamalPanel, j++, "Public Key g");
        publicKeyField_y = getNewTextArea(ElGamalPanel, j++, "Public Key y");
        privateKeyField_x = getNewTextArea(ElGamalPanel, j, "Zufallszahl x");

        JButton toggleEncryptionContext = new JButton("Change Encryptionmode - Current: RSA");
        JButton finalizeButton = new JButton("Schlüssel erzeugen und Masken öffnen");

        int i = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(toggleEncryptionContext, c);
        nonCubicNumberMField = getNewTextfield(panel, i++, "Nicht-Quadratzahl m");
        millerRabinStepsField = getNewTextfield(panel, i++, "Miller-Rabin Schritte");
        numberSystemBaseField = getNewTextfield(panel, i++, "g (g-Adisches System)");


        nonCubicNumberMField.setText("13");
        onlyAllowNumbers(nonCubicNumberMField);
        numberSystemBaseField.setText("55296");
        onlyAllowNumbers(numberSystemBaseField);
        millerRabinStepsField.setText("10");
        onlyAllowNumbers(millerRabinStepsField);
        lengthField_P.setText("128");
        onlyAllowNumbers(lengthField_P);
        lengthField_n.setText("1024");
        onlyAllowNumbers(lengthField_n);
        parameterField_n.setText("3");
        onlyAllowNumbers(parameterField_n);


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
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i;
        panel.add(RSAPanel, c);

        int finalI = i;
        toggleEncryptionContext.addActionListener(e -> {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = finalI;
            panel.remove(finalI);
            if(this.encryptionContext.getStrategy() instanceof RsaStringService){
                this.encryptionContext = new EncryptionContext();
                this.encryptionContext.setStrategy(new ElGamalMenezesVanstoneStringService());
                panel.add(ElGamalPanel, c);
                toggleEncryptionContext.setText("Change Encryptionmode - Current: El Gamal");
            } else if(this.encryptionContext.getStrategy() instanceof ElGamalMenezesVanstoneStringService){
                this.encryptionContext = new EncryptionContext();
                this.encryptionContext.setStrategy(new RsaStringService());
                panel.add(RSAPanel, c);
                toggleEncryptionContext.setText("Change Encryptionmode - Current: RSA");
            }
            panel.updateUI();
        });
        finalizeButton.addActionListener(e -> {
            EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();
            builder.withNumberBase(getIntOfField(numberSystemBaseField));
            if(this.encryptionContext.getStrategy() instanceof RsaStringService){
                disposeCommunicators();
                try{
                    Alice = new Communicator("Alice", builder.withRsaKeyPair(RsaService.generateKeyPair(getIntOfField(lengthField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField))).build(), new Point(900, 0));
                } catch (Exception f){
                    disposeCommunicators();
                    JOptionPane.showMessageDialog(null, "Error in Generation of Alice: " + e);
                    return;
                }
                try{
                    Bob = new Communicator("Bob", builder.withRsaKeyPair(RsaService.generateKeyPair(getIntOfField(lengthField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField))).build(), new Point(900, 400));
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
            } else if(this.encryptionContext.getStrategy() instanceof ElGamalMenezesVanstoneStringService){
                disposeCommunicators();
                //Schlüssel erzeugen
                SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(getBigIntegerOfField(lengthField_P), getBigIntegerOfField(parameterField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField));
                KeyPair keyPair = new KeyPair();
                //Schlüssel setzen
                keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, getBigIntegerOfField(nonCubicNumberMField));
                publicKeyField_p.setText(secureFiniteFieldEllipticCurve.getSafeEllipticCurve().getP().toString());
                publicKeyField_q.setText(secureFiniteFieldEllipticCurve.getQ().toString());
                publicKeyField_g.setText(keyPair.getPublicKey().generator().toString());
                publicKeyField_y.setText(keyPair.getPublicKey().groupElement().toString());
                publicKeyField_E.setText(keyPair.getPublicKey().toString());
                privateKeyField_x.setText(keyPair.getPrivateKey().secretMultiplierX().toString());
                builder.withElGamalMenezesVanstoneKeyPair(keyPair);
                Pair<BigInteger, EllipticCurvePoint> kAndKy = ElGamalMenezesVanstoneService.generateKandKy(keyPair.getPublicKey(), keyPair.getPublicKey().order().subtract(Resource.ONE));
                builder.withK(kAndKy.getKey());
                builder.withKy(kAndKy.getValue());
                //Schlüssel übergeben
                // builder erstmal übergeben, Werte überschreiben wo nötig TODO bessere Lösung finden
                KlartextPanel.openPanel(builder.build());
                ChiffratSignaturPanel.openPanel(builder);
            }
        });

        this.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(panel, BorderLayout.NORTH);
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

    private static JTextField getNewTextfield(JPanel p, int row, String headline) {
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
        p.add(j, c);
        return field;
    }
    private static JTextArea getNewTextArea(JPanel p, int row, String headline) {
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
        p.add(j, c);
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
}