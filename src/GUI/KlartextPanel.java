package GUI;

import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import elGamalMenezesVanstone.KeyPair;
import encryption.EncryptionContext;
import encryption.EncryptionContextParamBuilder;

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

public class KlartextPanel {
    private static JFrame frame = new JFrame();
    private static JPanel panel;
    private static GridBagConstraints c;

    private static JTextArea inputKlartext;
    private static JTextField anzeige_k;
    private static JButton encryptButton;
    private static JTextField anzeige_chiffrat;
    private static JTextField anzeige_signatur;
    private static ElGamalMenezesVanstoneMessage input_cipherMessage;
    // TODO bessere Lösung
    static EncryptionContext context = new EncryptionContext();
    static EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();
    static KeyPair keyPair = new KeyPair();

    public static void openPanel() {
        if(panel == null) {
            setupGraphics();
        }
        context.setStrategy(new ElGamalMenezesVanstoneStringService());
        fillParameters();
        panel.updateUI();
    }

    private static void fillParameters() {
    }

    private static void setupGraphics() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200,700));
        frame.setSize(new Dimension(1200,700));
        frame.setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.add(panel);
        c = new GridBagConstraints();
        /*
        - Ein Feld zum Hineinkopieren des Klartextes

        - Anzeige einer zufällig erzeugten Zahl k

        - Den Startbutton zum Verschlüsseln

        - Ein Feld mit der Anzeige des chiffrierten Textes

        - Ein Feld mit der Anzeige einer automatisch mit einer Nachricht erstellten Signatur

         1 Input D&D, 3 Anzeigen, 1 Button
         */

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
        anzeige_k = getNewTextfield(1, "Zufallszahl k");
        encryptButton = new JButton("Verschlüsseln");
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    encrypt();
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(encryptButton, c);
        anzeige_chiffrat = getNewTextfield(3, "Chiffrat");
        anzeige_signatur = getNewTextfield(4, "Signatur");
        JButton copyButton = new JButton("Chiffrat + Signatur übertragen");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        copyButton.addActionListener(e -> ChiffratSignaturPanel.receiveSignaturAndChiffrat(anzeige_signatur.getText(), anzeige_chiffrat.getText(), input_cipherMessage));
        panel.add(copyButton, c);

        panel.updateUI();
    }

    private static void encrypt() throws NoSuchAlgorithmException {
        builder.setData(inputKlartext.getText());
        Map<String, Object> encryptionParams = builder.build();
        ElGamalMenezesVanstoneMessage encryptedMessage = (ElGamalMenezesVanstoneMessage) context.encrypt(builder.getData(), encryptionParams);
        anzeige_chiffrat.setText(encryptedMessage.getCipherMessageString());
        anzeige_signatur.setText(context.sign(builder.getData(), encryptionParams));
        input_cipherMessage = encryptedMessage;
    }


    private static JTextField getNewTextfield(int row, String headline, boolean editable)
    {
        JTextField field = new JTextField();
        field.setEditable(editable);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200,50));
        t.setEditable(false);
        j.add(t);
        field.setPreferredSize(new Dimension(450, 50));
        j.add(field);
        panel.add(j,c);
        return field;
    }
    private static JTextField getNewTextfield(int row, String headline) {
        return getNewTextfield(row, headline,false);
    }
}
