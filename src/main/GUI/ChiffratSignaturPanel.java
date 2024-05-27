package main.GUI;

import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.GUI.HelperClasses.HeightEnum;
import main.GUI.HelperClasses.UISetUpMethods;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.encryption.EncryptionContext;
import main.encryption.EncryptionContextParamBuilder;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
/*
- Ein Feld mit der Anzeige des öffentlichen Schlüssels aus der Maske 1

- Ein Feld zum Hineinkopieren des empfangenen Chiffrats

- Ein Feld zum Hineinkopieren der empfangenen Signatur

- Den Startbutton zum Entschlüsseln

- Ein Feld mit der Anzeige des dechiffrierten Textes

- Ein Feld mit der Anzeige, ob die Signatur gültig ist oder nicht

2 Input D&D, 1 Button, 3 Anzeigen
 */
public class ChiffratSignaturPanel {
    private static final JFrame frame = new JFrame();
    private static JPanel panel;
    private static final GridBagConstraints c = new GridBagConstraints();

    private static JTextArea anzeige_public_key;
    private static JTextArea input_chiffrat;
    private static JTextArea input_signatur;
    private static JButton decryptButton;
    private static JTextArea anzeige_dechiffrat;
    private static JTextArea anzeige_signatur_valid;
    private static ElGamalMenezesVanstoneMessage input_cipherMessage;

    static EncryptionContext context = new EncryptionContext();
    private static Map<String, Object> contextParams;
    private static EncryptionContextParamBuilder contextBuilder;

    public static void open(EncryptionContextParamBuilder builder, PublicKey publicKey) {
        contextBuilder = builder;
        if(panel == null) {
            setupGraphics();
        }
        context.setStrategy(new ElGamalMenezesVanstoneStringService());

        fillParameters(publicKey);

        panel.updateUI();
        frame.setVisible(true);
    }

    public static void close() {
        frame.setVisible(false);
    }

    private static void fillParameters(PublicKey publicKey) {
        cleanUp();
        //öffentlicher Key
        anzeige_public_key.setText(publicKey.toString().replace("PublicKey[", "PublicKey[\n").replace("}, ", "}, \n"));
    }

    private static void cleanUp() {
        anzeige_public_key.setText("");
        input_chiffrat.setText("");
        input_signatur.setText("");
        anzeige_dechiffrat.setText("");
        anzeige_signatur_valid.setText("");
        input_cipherMessage = null;
    }

    private static void setupGraphics() {
        frame.setTitle("main.GUI.ChiffratSignaturPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700,800));
        frame.setSize(new Dimension(700,800));
        frame.setLocation(new Point(1050, 150));
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel);

        anzeige_public_key = UISetUpMethods.getjTextArea(panel, c, 0, "Public Key (E,p,q,g,y)", HeightEnum.BIG);
        anzeige_public_key.setToolTipText("E = ellipticCurve, " +
                "p = module prime, " +
                "q = order, " +
                "g = generator, " +
                "y = groupElement");

        input_chiffrat = new JTextArea();
        input_chiffrat.setLineWrap(true);
        JScrollPane inputScrollPane = new JScrollPane(input_chiffrat);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(650, 150));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(inputScrollPane, c);

        input_signatur = new JTextArea();
        input_signatur.setLineWrap(true);
        inputScrollPane = new JScrollPane(input_signatur);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(650, 100));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(inputScrollPane, c);

        decryptButton = new JButton("Entschlüsseln");
        decryptButton.addActionListener(e -> {
            try {
                decrypt();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }
        });
        decryptButton.setPreferredSize(new Dimension(250,25));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(decryptButton, c);

        anzeige_dechiffrat = UISetUpMethods.getjTextArea(panel, c, 4, "Entschlüsseltes Chiffrat", HeightEnum.BIG);
        anzeige_signatur_valid = UISetUpMethods.getjTextArea(panel, c, 5, "Signaturvalidierung", HeightEnum.NORMAL);
    }

    private static void decrypt() throws NoSuchAlgorithmException {
        String decrypted = context.decrypt(input_chiffrat.getText(), contextParams);
        anzeige_dechiffrat.setText(decrypted);
        //TODO Signaturvalidierung
        System.out.println(input_signatur.getText());
        anzeige_signatur_valid.setText("" + context.verify(anzeige_dechiffrat.getText(), input_signatur.getText(), contextParams));
    }

    public static void receiveSignaturAndChiffrat(String signatur, String chiffrat, ElGamalMenezesVanstoneMessage cipherMessage){
        input_signatur.setText(signatur);
        input_chiffrat.setText(chiffrat);
        input_cipherMessage = cipherMessage;
        contextBuilder.withElGamalMenezesVanstoneCipherMessage(input_cipherMessage);
        contextParams = contextBuilder.build();
    }
}