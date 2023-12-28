import rsa.MethodenFromRSA;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Communicator extends JFrame {
    public String name;
    public BigInteger n;
    public BigInteger e;
    private BigInteger d;
    private String signatur;
    private static JPanel panel;
    private JTextArea secretField;
    private JTextArea inputAndOutput;
    private JButton startEncode;
    private JButton startDecode;
    private JButton signMessage;
    private JButton sendMessage;
    private JTextField signings;
    private JTextField signingValid;
    private JButton clearEverything;
    private static GridBagConstraints c;
    private Communicator thisInstance;
    private ArrayList<Message> messageList = new ArrayList<>();
    private int currentMessageIndex = 0;

    public Communicator(String name, BigInteger n, BigInteger phiN, Point point){
        super(name);
        this.name = name;
        this.n = n;
        //berechne inverse zu d -> e
        this.e = MethodenFromRSA.calculateE(phiN, CommunicationPanel.getInstance().getM(), CommunicationPanel.getInstance().getMillerRabinSteps());
        this.d = MethodenFromRSA.calculateD(e, phiN);

        thisInstance = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000,400));
        setSize(new Dimension(1000,400));
        setLocation(point);
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        AtomicBoolean currentMessageIsEncrypted = new AtomicBoolean(false);
        AtomicBoolean currentMessageIsSigned = new AtomicBoolean(false);
        inputAndOutput = new JTextArea();
        inputAndOutput.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(inputAndOutput);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(450, 60));
        startEncode = new JButton("Verschlüsseln");
        startEncode.addActionListener(e -> {
            Communicator receiver = getReceiver();
            inputAndOutput.setText(MethodenFromRSA.encrypt(inputAndOutput.getText(),receiver.e, receiver.n, CommunicationPanel.getInstance().getNumberSystemBase(), CommunicationPanel.getInstance().getBlockSize()));
            currentMessageIsEncrypted.set(true);
        });
        inputAndOutput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signatur = null;
                signings.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signatur = null;
                signings.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signatur = null;
                signings.setText("");
            }
        });
        startEncode.setPreferredSize(new Dimension(250,25));
        startDecode = new JButton("Entschlüsseln");
        startDecode.addActionListener(e -> {
            if(messageList.size() > currentMessageIndex){
                Message message = messageList.get(currentMessageIndex++);
                String messageToUse;
                if(message.isEncrypted()){
                    messageToUse = MethodenFromRSA.decrypt(message.message, d, n, CommunicationPanel.getInstance().getBlockSize(), CommunicationPanel.getInstance().getNumberSystemBase());
                    System.out.println("Decrypted Message: " + messageToUse);
                } else {
                    messageToUse = message.message;
                    System.out.println("Message not encrypted: " + messageToUse);
                }
                System.out.println("Received Message: " + message);
                System.out.println("Signature: " + message.signature);
                inputAndOutput.setText(messageToUse);
                System.out.println(message.isSigned());
                if(message.isSigned()){
                    try {
                        System.out.println(message.signature);
                        if(message.isEncrypted){
                            signingValid.setText("" + MethodenFromRSA.verifySignature(message.message, message.signature, message.e, message.n));
                        } else {
                            signingValid.setText("" + MethodenFromRSA.verifySignature(messageToUse, message.signature, message.e, message.n));
                        }
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    signingValid.setText("");
                }
            }
        });
        startDecode.setPreferredSize(new Dimension(250,25));
        signMessage = new JButton("Signieren der Nachricht");
        signMessage.addActionListener(e -> {
            try {
                System.out.println("Nachricht: " + inputAndOutput.getText());
                System.out.println("Signatur: " + MethodenFromRSA.sign(inputAndOutput.getText(), d, n));
                signatur = MethodenFromRSA.sign(inputAndOutput.getText(), d, n);
                signings.setText(signatur);
                currentMessageIsSigned.set(true);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        signMessage.setPreferredSize(new Dimension(250,25));
        sendMessage = new JButton("Nachricht versenden");
        sendMessage.addActionListener(e -> {
            String message = inputAndOutput.getText();

            getReceiver().sendAMessage(new Message(message, signings.getText(), thisInstance.e, thisInstance.n, currentMessageIsEncrypted.get(), currentMessageIsSigned.get()));
        });
        sendMessage.setPreferredSize(new Dimension(250,25));
        clearEverything = new JButton("Alle Eingaben und Nachrichten löschen");
        clearEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputAndOutput.setText("");
                signings.setText("");
                signingValid.setText("");
                signatur = null;
            }
        });
        clearEverything.setPreferredSize(new Dimension(250,25));

        JPanel buttons = new JPanel();
        GridBagConstraints c1 = new GridBagConstraints();
        int i = 0;
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = i++;
        buttons.add(startEncode,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = i++;
        buttons.add(startDecode,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = i++;
        buttons.add(signMessage,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = i++;
        buttons.add(sendMessage,c1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = i++;
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

    public void sendAMessage(Message m) {
        messageList.add(m);
        inputAndOutput.setText(m.message);
        signingValid.setText("");
        System.out.println("Received Message: " + m.message);
    }

    private Communicator getReceiver() {
        Communicator receiver;
        if(name.equals("Bob")){
            receiver = CommunicationPanel.getInstance().getAlice();
        } else {
            receiver = CommunicationPanel.getInstance().getBob();
        }
        System.out.println("Receiver: " + receiver.name); // Print the name of the receiver
        return receiver;
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
