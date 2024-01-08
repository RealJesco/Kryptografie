package GUI;

import GUI.HelperClasses.Message;
import rsa.RSA;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.awt.datatransfer.DataFlavor;

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
    private final JTextField signingValid;
    private final JTextField receivedSignature;
    private static GridBagConstraints c;
    private final Communicator thisInstance;
    private final ArrayList<Message> messageList = new ArrayList<>();


    public Communicator(String name, BigInteger n, BigInteger phiN, Point point){
        super(name);
        this.name = name;
        this.n = n;
        //berechne inverse zu d -> e
        this.e = RSA.getE();
        this.d = RSA.getD();

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


        JScrollPane messageListScrollPane = new JScrollPane(messageListJ);
        messageListScrollPane.setPreferredSize(new Dimension(450, 200));

        // Add the new components to the panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;  // Adjust this as needed
        panel.add(messageListScrollPane, c);


        JButton startEncode = new JButton("Verschlüsseln");
        startEncode.setPreferredSize(new Dimension(250,25));
        JButton startDecode = new JButton("Entschlüsseln & Verifizieren");


        startDecode.setPreferredSize(new Dimension(250,25));

        JButton sendMessage = new JButton("Nachricht versenden");
        sendMessage.setPreferredSize(new Dimension(250,25));
        JButton clearEverything = new JButton("Alle Eingaben und Nachrichten löschen");
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
        signMessage.setPreferredSize(new Dimension(250,25));
        buttons.add(signMessage,c1);
        JButton loadTextFileButton = new JButton("Load Text File");

        loadTextFileButton.setPreferredSize(new Dimension(250, 25));
        c1.gridy = i++;
        buttons.add(loadTextFileButton, c1);

        signingValid = getNewTextfield(2, "Empfangene Signatur gültig");
        receivedSignature = getNewTextfield(3, "Empfangene Signatur");

        JTextArea secretField = new JTextArea();
        secretField.setLineWrap(true);
        secretField.setEditable(false);
        secretField.setBackground(new Color(238,238,238));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
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


        //ADDING LISTENERS
        inputAndOutput.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    String droppedText = (String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    inputAndOutput.setText(droppedText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        messageListJ.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Message) {
                    Message message = (Message) value;
                    setText("GUI.HelperClasses.Message from " + message.getSender().name + ": " + message.message);
                }
                return renderer;
            }
        });

        messageListJ.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) { // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    Message selectedMessage = messageListJ.getModel().getElementAt(index);
                    inputAndOutput.setText(selectedMessage.message);
                }
            }
        });
        startEncode.addActionListener(e -> {
            Communicator receiver = getReceiver(thisInstance);
            try{
                String encryptedMessage = RSA.encrypt(inputAndOutput.getText(), receiver.e, receiver.n);
                currentClearMessage.set(inputAndOutput.getText());
                inputAndOutput.setText(encryptedMessage);
                currentMessageIsEncrypted.set(true);
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error while Encryption" + f);
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
                receivedSignature.setText("");
                signingValid.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signature = null;
                signings.setText("");
                receivedSignature.setText("");
                signingValid.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                //Remove the signature if the message is changed
                signature = null;
                signings.setText("");
                receivedSignature.setText("");
                signingValid.setText("");
            }
        });
        sendMessage.addActionListener(e -> {
            Message message = new Message(inputAndOutput.getText(), signature, thisInstance.e, thisInstance.n, currentMessageIsEncrypted.get(), currentMessageIsSigned.get(), thisInstance, getReceiver(thisInstance), currentClearMessage.get());
            getReceiver(thisInstance).sendAMessage(message);
            messageListModel.addElement(message);
            inputAndOutput.setText("");
        });
        clearEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputAndOutput.setText("");
                signings.setText("");
                signingValid.setText("");
                signature = null;
                currentMessageIsEncrypted.set(false);
                currentMessageIsSigned.set(false);
                currentClearMessage.set("");
                messageListModel.clear();
            }
        });
        signMessage.addActionListener(e -> {
            try {
                signature = RSA.sign(inputAndOutput.getText(), d, n);
                signings.setText(signature);
                currentMessageIsSigned.set(true);
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("Error while Signing" + ex);
                throw new RuntimeException(ex);
            }
        });
        loadTextFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

                int option = fileChooser.showOpenDialog(Communicator.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        String content = new String(Files.readAllBytes(((File) selectedFile).toPath()), StandardCharsets.UTF_8);
                        inputAndOutput.setText(content);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Communicator.this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        startDecode.addActionListener(e -> {
            Message selectedMessage = messageListJ.getSelectedValue(); // Get selected message
            //If none are selected, get the last one
            if(selectedMessage == null) {
                if(messageList.size() != 0){
                    selectedMessage = messageList.get(messageList.size()-1);
                } else {
                    selectedMessage = null;
                }
            }
            if (selectedMessage != null) { // Check if a message is selected
                String messageToUse;
                if(selectedMessage.isEncrypted()){
                    //If the message belongs to me, use my private key
                    if(selectedMessage.getReceiver().equals(thisInstance)) {
                        try {
                            messageToUse = RSA.decrypt(selectedMessage.message, d, n);
                        } catch (Exception f){
                            JOptionPane.showMessageDialog(null, "Error while Decryption" + f);
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
                            signingValid.setText("" + RSA.verifySignature(selectedMessage.message, selectedMessage.getSignature(), selectedMessage.getE(), selectedMessage.getN()));
                        } else {
                            signingValid.setText("" + RSA.verifySignature(messageToUse, selectedMessage.getSignature(), selectedMessage.getE(), selectedMessage.getN()));
                        }
                        receivedSignature.setText(selectedMessage.getSignature());
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