package Masks;

import javax.swing.*;
import java.awt.*;

public class Communicator extends JFrame {
    //BigInteger secretKey;
    String name;

    JPanel panel;

    JTextField secretField;
    JTextArea inputAndOutput;
    JButton startEncode;
    JButton signMessage;
    JButton sendMessage;
    JTextField signings;
    JTextField signingValid;
    JButton clearEverything;

    public Communicator(String name){
        super(name);
        this.name = name;
        // muss in Unicode gespeichert werden: secretKey = signingKey;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800,500));
        setSize(new Dimension(800,500));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        secretField = new JTextField();
        inputAndOutput = new JTextArea();
        startEncode = new JButton("Verschlüsseln");
        signMessage = new JButton("Signieren der Nachricht");
        sendMessage = new JButton("Nachricht versenden");
        signings = new JTextField();
        signingValid = new JTextField();
        clearEverything = new JButton("Alle Eingaben und Nachrichten löschen");

        JPanel buttons = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        buttons.add(startEncode,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        buttons.add(signMessage,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        buttons.add(sendMessage,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        buttons.add(clearEverything,c);



        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        inputAndOutput.setPreferredSize(new Dimension(400,200));
        panel.add(inputAndOutput,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        buttons.setPreferredSize(new Dimension(300,200));
        panel.add(buttons,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JPanel j = new JPanel();
        JTextField t = new JTextField("Eigene Signatur");
        t.setPreferredSize(new Dimension(200,20));
        t.setEditable(false);
        j.add(t);
        signings.setPreferredSize(new Dimension(200, 20));
        j.add(signings);
        panel.add(j,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        j = new JPanel();
        t = new JTextField("Empfangene Signatur gültig");
        t.setPreferredSize(new Dimension(200,20));
        t.setEditable(false);
        j.add(t);
        signingValid.setPreferredSize(new Dimension(200, 20));
        j.add(signingValid);
        panel.add(j,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        j = new JPanel();
        t = new JTextField("Geheimer Schlüssel d");
        t.setPreferredSize(new Dimension(200,20));
        t.setEditable(false);
        j.add(t);
        secretField.setPreferredSize(new Dimension(200, 20));
        j.add(secretField);
        panel.add(j,c);

        add(panel);
        panel.updateUI();
    }
}
