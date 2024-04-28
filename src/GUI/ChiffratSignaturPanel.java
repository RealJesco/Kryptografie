package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChiffratSignaturPanel {
    private static JFrame frame = new JFrame();
    private static JPanel panel;
    private static GridBagConstraints c;

    private static JTextField anzeige_public_key;
    private static JTextArea input_chiffrat;
    private static JTextArea input_signatur;
    private static JButton decryptButton;
    private static JTextField anzeige_dechiffrat;
    private static JTextField anzeige_signatur_valid;

    private ChiffratSignaturPanel() {
/*
- Ein Feld mit der Anzeige des öffentlichen Schlüssels aus der Maske 1

- Ein Feld zum Hineinkopieren des empfangenen Chiffrats

- Ein Feld zum Hineinkopieren der empfangenen Signatur

- Den Startbutton zum Entschlüsseln

- Ein Feld mit der Anzeige des dechiffrierten Textes

- Ein Feld mit der Anzeige, ob die Signatur gültig ist oder nicht

2 Input D&D, 1 Button, 3 Anzeigen
 */
    }

    public static void openPanel() {
        if(panel == null) {
            setupGraphics();
        }
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

        anzeige_public_key = getNewTextfield(0, "Öffentlicher Schlüssel");

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
        decryptButton.addActionListener(e -> decrypt());
        decryptButton.setPreferredSize(new Dimension(250,25));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(decryptButton, c);

        anzeige_dechiffrat = getNewTextfield(4, "Entschlüsseltes Chiffrat");
        anzeige_signatur_valid = getNewTextfield(5, "Signaturvalidierung");
    }

    private static void decrypt() {

    }

    public static void receiveSignaturAndChiffrat(String signatur, String chiffrat){
        input_signatur.setText(signatur);
        input_chiffrat.setText(chiffrat);
    }


    private static JTextField getNewTextfield(int row, String headline, boolean editable) {
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