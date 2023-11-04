import mathMethods.MathMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class Communicator extends JFrame {
    public BigInteger n;
    public BigInteger e;
    private BigInteger d;
    private String signatur;
    private static JPanel panel;
    private JTextArea secretField;
    private JTextArea inputAndOutput;
    private JButton startEncode;
    private JButton signMessage;
    private JButton sendMessage;
    private JTextField signings;
    private JTextField signingValid;
    private JButton clearEverything;
    private static GridBagConstraints c;

    public Communicator(String name, BigInteger p1, BigInteger p2, Point point){
        super(name);
        this.n = p1.multiply(p2);
        //berechne inverse zu d -> e
        BigInteger p0p1 = p1.subtract(BigInteger.ONE).multiply(p2.subtract(BigInteger.ONE));
        do{
            this.e = MathMethods.getRandomBigInteger(p0p1);
        } while(MathMethods.extendedEuclidean(e,p0p1)[0].compareTo(BigInteger.ONE)==0);
        this.d=e;




        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000,400));
        setSize(new Dimension(1000,400));
        setLocation(point);
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();


        inputAndOutput = new JTextArea();
        inputAndOutput.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(inputAndOutput);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(450, 60));
        startEncode = new JButton("Verschlüsseln");
        startEncode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mathMethods.MathMethods.blockCipherEncrypt(inputAndOutput.getText(), CommunicationPanel.getInstance().getBlockLength());
            }
        });
        startEncode.setPreferredSize(new Dimension(250,25));
        signMessage = new JButton("Signieren der Nachricht");
        signMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        signMessage.setPreferredSize(new Dimension(250,25));
        sendMessage = new JButton("Nachricht versenden");
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        sendMessage.setPreferredSize(new Dimension(250,25));
        clearEverything = new JButton("Alle Eingaben und Nachrichten löschen");
        clearEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputAndOutput.setText("");
                signings.setText("");
                signingValid.setText("");
            }
        });
        clearEverything.setPreferredSize(new Dimension(250,25));

        JPanel buttons = new JPanel();
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 0;
        buttons.add(startEncode,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 1;
        buttons.add(signMessage,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 2;
        buttons.add(sendMessage,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 3;
        buttons.add(clearEverything,c1);



        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        scrollPane.setPreferredSize(new Dimension(650,200));
        panel.add(scrollPane,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        buttons.setPreferredSize(new Dimension(300,200));
        panel.add(buttons,c);
        signings = getNewTextfield(1, "Eigene Signatur");
        signingValid = getNewTextfield(2, "Empfangene Signatur gültig");
            secretField = new JTextArea();
            secretField.setLineWrap(true);
            secretField.setEditable(false);
            secretField.setBackground(new Color(238,238,238));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 3;
            JPanel j = new JPanel();
            JTextField t = new JTextField("Geheimer Schlüssel d");
            t.setPreferredSize(new Dimension(200, 60));
            t.setEditable(false);
            j.add(t);
            scrollPane = new JScrollPane(secretField);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(450, 60));
            j.add(scrollPane);
            panel.add(j, c);
        secretField.setText(this.d.toString());

        add(panel);
        panel.updateUI();
    }

    private static JTextField getNewTextfield(int row, String headline, boolean editable) {
        JTextField field = new JTextField();
        field.setEditable(editable);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200,20));
        t.setEditable(false);
        j.add(t);
        field.setPreferredSize(new Dimension(450, 20));
        j.add(field);
        panel.add(j,c);
        return field;
    }
    private static JTextField getNewTextfield(int row, String headline) {
        return getNewTextfield(row, headline,false);
    }
}
