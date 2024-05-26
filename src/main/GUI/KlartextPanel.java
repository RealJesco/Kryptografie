package main.GUI;

import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.GUI.HelperClasses.UISetUpMethods;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.encryption.EncryptionContext;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/*
- Ein Feld zum Hineinkopieren des Klartextes

- Anzeige einer zufällig erzeugten Zahl k

- Den Startbutton zum Verschlüsseln

- Ein Feld mit der Anzeige des chiffrierten Textes

- Ein Feld mit der Anzeige einer automatisch mit einer Nachricht erstellten Signatur

 1 Input D&D, 3 Anzeigen, 1 Button
 */
public class KlartextPanel {
    private static final JFrame frame = new JFrame();
    private static JPanel panel;
    private static final GridBagConstraints c = new GridBagConstraints();

    private static JTextArea inputKlartext;
    private static JTextArea anzeige_k;
    private static JButton encryptButton;
    private static JTextArea anzeige_chiffrat;
    private static JTextArea anzeige_signatur;
    private static ElGamalMenezesVanstoneMessage input_cipherMessage;
    // TODO bessere Lösung
    static EncryptionContext context = new EncryptionContext();
    private static Map<String, Object> contextParams;

    public static void open(Map<String, Object> params) {
        contextParams = params;
        if(panel == null) {
            setupGraphics();
        }
        context.setStrategy(new ElGamalMenezesVanstoneStringService());

        fillParameters();

        panel.updateUI();
        frame.setVisible(true);
    }

    public static void close() {
        frame.setVisible(false);
    }

    private static void fillParameters() {
        cleanUp();
        anzeige_k.setText(contextParams.get("k").toString());
    }

    private static void cleanUp() {
        inputKlartext.setText("");
        anzeige_k.setText("");
        anzeige_chiffrat.setText("");
        inputKlartext.setText("");
        anzeige_signatur.setText("");
        input_cipherMessage = null;
    }

    private static void setupGraphics() {
        frame.setTitle("main.GUI.KlartextPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700,800));
        frame.setSize(new Dimension(700,800));
        frame.setLocation(new Point(350, 150));
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel);

        inputKlartext = new JTextArea();
        inputKlartext.setLineWrap(true);

        JScrollPane inputScrollPane = new JScrollPane(inputKlartext);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(650, 300));

        inputKlartext.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    String droppedText = (String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    inputKlartext.setText(droppedText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        inputKlartext.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(inputScrollPane, c);
        anzeige_k = UISetUpMethods.getjTextArea(panel, c, 1, "Zufallszahl k", false);
        encryptButton = new JButton("Verschlüsseln");
        encryptButton.addActionListener(e -> {
            try {
                encrypt();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(encryptButton, c);
        anzeige_chiffrat = UISetUpMethods.getjTextArea(panel, c, 3, "Chiffrat", true);
        anzeige_signatur = UISetUpMethods.getjTextArea(panel, c, 4, "Signatur", false);
        JButton copyButton = new JButton("Chiffrat + Signatur übertragen");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        copyButton.addActionListener(e -> ChiffratSignaturPanel.receiveSignaturAndChiffrat(anzeige_signatur.getText(), anzeige_chiffrat.getText(), input_cipherMessage));
        panel.add(copyButton, c);

        panel.updateUI();
    }

    private static void encrypt() throws NoSuchAlgorithmException {
        ElGamalMenezesVanstoneMessage encryptedMessage = (ElGamalMenezesVanstoneMessage) context.encrypt(inputKlartext.getText(), contextParams);
        anzeige_chiffrat.setText(encryptedMessage.getCipherMessageString());
        anzeige_signatur.setText(context.sign(inputKlartext.getText(), contextParams));
        input_cipherMessage = encryptedMessage;
    }

}
