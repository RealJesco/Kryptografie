package main.GUI;

import main.GUI.HelperClasses.HeightEnum;
import main.GUI.HelperClasses.UISetUpMethods;
import main.encryption.StringEncryptionStrategy;
import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.finiteFieldEllipticCurve.SecureFiniteFieldEllipticCurve;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneService;
import main.elGamalMenezesVanstone.KeyPair;
import main.encryption.EncryptionContextParamBuilder;
import org.apache.commons.math3.util.Pair;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.encryption.EncryptionContext;
import main.rsa.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
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
    private static JTextArea publicKeyField_Epqgy;
    private static JTextArea privateKeyField_x;

    private static final GridBagConstraints c = new GridBagConstraints();
    private static final CommunicationPanel singleton = new CommunicationPanel();

    private CommunicationPanel() {
        super("main.GUI.CommunicationPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 550));
        setSize(new Dimension(700, 550));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        this.encryptionContext = new EncryptionContext();
        this.encryptionContext.setStrategy(new RsaStringService());

        int j = 0;
        JPanel RSAPanel = new JPanel();
        RSAPanel.setLayout(new GridBagLayout());
        lengthField_n = UISetUpMethods.getjTextField(RSAPanel, c, j++, "Bit-Länge von n", true);
        alicePublicKeyField = UISetUpMethods.getjTextArea(RSAPanel, c, j++, "Öffentlicher Schlüssel e von Alice", HeightEnum.NORMAL);
        alicePublicNField = UISetUpMethods.getjTextArea(RSAPanel, c, j++, "Öffentlicher Schlüssel n von Alice", HeightEnum.NORMAL);
        bobPublicKeyField = UISetUpMethods.getjTextArea(RSAPanel, c, j++, "Öffentlicher Schlüssel e von Bob", HeightEnum.NORMAL);
        bobPublicNField = UISetUpMethods.getjTextArea(RSAPanel, c, j, "Öffentlicher Schlüssel n von Bob", HeightEnum.NORMAL);
        j = 0;
        JPanel ElGamalPanel = new JPanel();
        ElGamalPanel.setLayout(new GridBagLayout());
        lengthField_P = UISetUpMethods.getjTextField(ElGamalPanel, c, j++, "Bit-Länge von p", true);
        parameterField_n = UISetUpMethods.getjTextField(ElGamalPanel, c, j++, "Parameter n für ell. Kurve", true);
        publicKeyField_Epqgy = UISetUpMethods.getjTextArea(ElGamalPanel, c, j++, "Public Key (E,p,q,g,y)", HeightEnum.BIG);
        publicKeyField_Epqgy.setToolTipText("E = ellipticCurve, " +
                "p = module prime, " +
                "q = order, " +
                "g = generator, " +
                "y = groupElement");
        privateKeyField_x = UISetUpMethods.getjTextArea(ElGamalPanel, c, j, "Zufallszahl x", HeightEnum.NORMAL);

        JButton toggleEncryptionContext = new JButton("Change Encryptionmode - Current: RSA");
        toggleEncryptionContext.setBackground(Color.pink);
        JButton finalizeButton = new JButton("Schlüssel erzeugen und Masken öffnen");

        int i = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(toggleEncryptionContext, c);
        nonCubicNumberMField = UISetUpMethods.getjTextField(panel, c, i++, "Nicht-Quadratzahl m", true);
        millerRabinStepsField = UISetUpMethods.getjTextField(panel, c, i++, "Miller-Rabin Schritte", true);
        numberSystemBaseField = UISetUpMethods.getjTextField(panel, c, i++, "g (g-Adisches System)", true);


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
                toggleEncryptionContext.setText("Change Encryptionmode - Current: ElGamal - Menezes Vanstone");
            } else if(this.encryptionContext.getStrategy() instanceof ElGamalMenezesVanstoneStringService){
                this.encryptionContext = new EncryptionContext();
                this.encryptionContext.setStrategy(new RsaStringService());
                panel.add(RSAPanel, c);
                toggleEncryptionContext.setText("Change Encryptionmode - Current: RSA");
            }
            panel.updateUI();
        });
        finalizeButton.addActionListener(e -> {
            cleanUp(this.encryptionContext.getStrategy());
            EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();
            builder.withNumberBase(getIntOfField(numberSystemBaseField));
            if(this.encryptionContext.getStrategy() instanceof RsaStringService){
                disposeCommunicators();
                try{
                    Alice = new Communicator("Alice", builder.withRsaKeyPair(RsaService.generateKeyPair(getIntOfField(lengthField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField))).build(), new Point(-10, 100));
                } catch (Exception f){
                    disposeCommunicators();
                    JOptionPane.showMessageDialog(null, "Error in Generation of Alice: " + e);
                    return;
                }
                try{
                    Bob = new Communicator("Bob", builder.withRsaKeyPair(RsaService.generateKeyPair(getIntOfField(lengthField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField))).build(), new Point(965, 100));
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
                //Schlüssel erzeugen
                SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(getBigIntegerOfField(lengthField_P), getBigIntegerOfField(parameterField_n), getIntOfField(millerRabinStepsField), getBigIntegerOfField(nonCubicNumberMField));
                KeyPair keyPair = new KeyPair();
                //Schlüssel setzen
                keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, getBigIntegerOfField(nonCubicNumberMField));
                publicKeyField_Epqgy.setText(keyPair.getPublicKey().toString().replace("PublicKey[", "PublicKey[\n").replace("}, ", "}, \n"));
                privateKeyField_x.setText(keyPair.getPrivateKey().secretMultiplierX().toString());
                builder.withElGamalMenezesVanstoneKeyPair(keyPair);
                Pair<BigInteger, EllipticCurvePoint> kAndKy = ElGamalMenezesVanstoneService.generateKandKy(keyPair.getPublicKey(), getBigIntegerOfField(nonCubicNumberMField));
                builder.withK(kAndKy.getKey());
                builder.withKy(kAndKy.getValue());
                builder.withM(getBigIntegerOfField(nonCubicNumberMField));
                //Schlüssel übergeben
                // builder erstmal übergeben, Werte überschreiben wo nötig
                ChiffratSignaturPanel.open(builder, keyPair.getPublicKey());
                KlartextPanel.open(builder.build());
            }
        });

        this.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(panel, BorderLayout.NORTH);
        panel.updateUI();
    }

    private void cleanUp(StringEncryptionStrategy strategy) {
        disposeCommunicators();
        KlartextPanel.close();
        ChiffratSignaturPanel.close();
        if(strategy instanceof RsaStringService){
            //cleanup ElGamal
            lengthField_P.setText("128");
            parameterField_n.setText("3");
            publicKeyField_Epqgy.setText("");
            privateKeyField_x.setText("");
        }else{
            //cleanup RSA
            lengthField_n.setText("1024");
            alicePublicKeyField.setText("");
            alicePublicNField.setText("");
            bobPublicKeyField.setText("");
            bobPublicNField.setText("");
        }
    }

    private void onlyAllowNumbers(JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                jTextFieldKeyTyped(e);
            }
        });
        field.getDocument().addDocumentListener(new DocumentListener() {
            private String lastValidText = field.getText();
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleUpdate(e);
            }
            private void handleUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        String newText = e.getDocument().getText(0, e.getDocument().getLength());
                        Integer.parseInt(newText);
                        lastValidText = newText;
                    } catch (BadLocationException | NumberFormatException ex) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                e.getDocument().removeDocumentListener(this);
                                e.getDocument().remove(0, e.getDocument().getLength());
                                e.getDocument().insertString(0, lastValidText, null);
                            } catch (BadLocationException ignore) {
                            } finally {
                                e.getDocument().addDocumentListener(this);
                            }
                        });
                    }
                });
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