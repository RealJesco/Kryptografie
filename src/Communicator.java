import rsa.MethodenFromRSA;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Communicator extends JFrame {
    public String name;
    public BigInteger n;
    public BigInteger e;
    private final BigInteger d;
    private String signature;
    private static JPanel panel;
    private final JTextArea inputAndOutput;
    private final DefaultListModel<Message> messageListModel;
    private final JList<Message> messageListJ;
    private final JTextField signings;
    private JTextField signingValid;
    private static GridBagConstraints c;
    private final Communicator thisInstance;
    private final ArrayList<Message> messageList = new ArrayList<>();


    public Communicator(String name, BigInteger n, BigInteger phiN, Point point){
        super(name);
        this.name = name;
        this.n = n;
        //berechne inverse zu d -> e
        try{
            this.e = MethodenFromRSA.calculateE(phiN, CommunicationPanel.getInstance().getM(), CommunicationPanel.getInstance().getMillerRabinSteps());
        } catch(Exception e){
            throw new InvalidParameterException();
        }
        this.d = MethodenFromRSA.calculateD(e, phiN);

        thisInstance = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200,700));
        setSize(new Dimension(1200,700));
        setLocation(point);
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        AtomicBoolean currentMessageIsEncrypted = new AtomicBoolean(false);
        AtomicBoolean currentMessageIsSigned = new AtomicBoolean(false);
        AtomicReference<String> currentClearMessage = new AtomicReference<>("");
        inputAndOutput = new JTextArea();
        inputAndOutput.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(inputAndOutput);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(450, 60));


        // Create the list for displaying messages
        messageListModel = new DefaultListModel<>();
        messageListJ = new JList<>(messageListModel);

        messageListJ.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Message) {
                    Message message = (Message) value;
                    setText("Message from " + message.getSender().name + ": " + message.message);
                }
                return renderer;
            }
        });


        JScrollPane messageListScrollPane = new JScrollPane(messageListJ);
        messageListScrollPane.setPreferredSize(new Dimension(450, 200));

        // Add the new components to the panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;  // Adjust this as needed
        panel.add(messageListScrollPane, c);


        JButton startEncode = new JButton("Verschlüsseln");
        startEncode.addActionListener(e -> {
            Communicator receiver = getReceiver(thisInstance);
            try{
                String encryptedMessage = MethodenFromRSA.encrypt(inputAndOutput.getText(), receiver.e, receiver.n, CommunicationPanel.getInstance().getNumberSystemBase(), CommunicationPanel.getInstance().getBlockSize());
                currentClearMessage.set(inputAndOutput.getText());
                inputAndOutput.setText(encryptedMessage);
                currentMessageIsEncrypted.set(true);
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error while Encryption" + f.toString());
                return;
            }
        });

        inputAndOutput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signature = null;
                signings.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signature = null;
                signings.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signature = null;
                signings.setText("");
            }
        });
        startEncode.setPreferredSize(new Dimension(250,25));
        JButton startDecode = new JButton("Entschlüsseln");
        startDecode = new JButton("Entschlüsseln");
        startDecode.addActionListener(e -> {
            Message selectedMessage = messageListJ.getSelectedValue(); // Get selected message
            //If none are selected, get the last one
            if(selectedMessage == null) {
                selectedMessage = messageList.get(messageList.size()-1);
            }
            if (selectedMessage != null) { // Check if a message is selected
                String messageToUse;
                if(selectedMessage.isEncrypted()){
                    //If the message belongs to me, use my private key
                    if(selectedMessage.getReceiver().equals(thisInstance)) {
                        try {
                            messageToUse = MethodenFromRSA.decrypt(selectedMessage.message, d, n, CommunicationPanel.getInstance().getBlockSize(), CommunicationPanel.getInstance().getNumberSystemBase());
                        } catch (Exception f){
                            JOptionPane.showMessageDialog(null, "Error while Decryption" + f.toString());
                            return;
                        }
                    } else {
                        messageToUse = selectedMessage.getClearMessage(thisInstance);
                    }
                } else {
                    messageToUse = selectedMessage.message;
                }
                inputAndOutput.setText(messageToUse);
                if(selectedMessage.isSigned()){
                    try {
                        if(selectedMessage.isEncrypted){
                            signingValid.setText("" + MethodenFromRSA.verifySignature(selectedMessage.message, selectedMessage.signature, selectedMessage.e, selectedMessage.n));
                        } else {
                            signingValid.setText("" + MethodenFromRSA.verifySignature(messageToUse, selectedMessage.signature, selectedMessage.e, selectedMessage.n));
                        }
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    signingValid.setText("");
                }
            } else {
                // Optionally handle the case where no message is selected
            }
        });


        startDecode.setPreferredSize(new Dimension(250,25));

        JButton sendMessage = new JButton("Nachricht versenden");
        sendMessage.addActionListener(e -> {
            Message message = new Message(inputAndOutput.getText(), signature, thisInstance.e, thisInstance.n, currentMessageIsEncrypted.get(), currentMessageIsSigned.get(), thisInstance, getReceiver(thisInstance), currentClearMessage.get());
            getReceiver(thisInstance).sendAMessage(message);
            messageListModel.addElement(message);
        });
        sendMessage.setPreferredSize(new Dimension(250,25));
        JButton clearEverything = new JButton("Alle Eingaben und Nachrichten löschen");
        clearEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputAndOutput.setText("");
                signings.setText("");
                signingValid.setText("");
                signature = null;
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

        JButton signMessage = new JButton("Signieren der Nachricht");
        signMessage.addActionListener(e -> {
            try {
                signature = MethodenFromRSA.sign(inputAndOutput.getText(), d, n);
                signings.setText(signature);
                currentMessageIsSigned.set(true);
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("Error while Signing" + ex.toString());
                throw new RuntimeException(ex);
            }
        });
        signMessage.setPreferredSize(new Dimension(250,25));
        buttons.add(signMessage,c1);

        signingValid = getNewTextfield(2, "Empfangene Signatur gültig");
        JTextArea secretField = new JTextArea();
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
        messageList.add(m); // Add the message to the sender's list
        Communicator sender = m.getSender();
        Communicator receiver = getReceiver(sender);
        SwingUtilities.invokeLater(() -> {
            receiver.messageListModel.addElement(m); // Add the message to the receiver's list model
        });
    }



    private Communicator getReceiver(Communicator sender) {
        if(sender.name.equals("Bob")){
            return CommunicationPanel.getInstance().getAlice();
        } else {
            return CommunicationPanel.getInstance().getBob();
        }
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