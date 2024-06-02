package main.GUI;

import main.GUI.HelperClasses.HeightEnum;
import main.GUI.HelperClasses.RSAMessage;
import main.GUI.HelperClasses.UISetUpMethods;
import main.encryption.EncryptionContext;
import main.rsa.PrivateKeyRsa;
import main.rsa.PublicKeyRsa;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.awt.datatransfer.DataFlavor;
import main.rsa.RsaStringService;

import static main.GUI.KlartextPanel.readStringOfFile;

public class Communicator extends JFrame {
    public String name;
    public BigInteger n;
    public BigInteger e;
    private final BigInteger d;
    private String signature;
    private static JPanel panel;
    private final JTextArea inputAndOutput;
    private final DefaultListModel<RSAMessage> messageListModel;
    private final JList<RSAMessage> messageListJ;
    private final JTextField signings;
    private final JTextField signingValid;
    private final JTextField receivedSignature;
    private final GridBagConstraints c = new GridBagConstraints();
    private final Communicator thisInstance;
    private final ArrayList<RSAMessage> RSAMessageList = new ArrayList<>();
    private final EncryptionContext context = new EncryptionContext();
    Map<String, Object> contextParams;


    public Communicator(String name, Map<String, Object> contextParams, Point point){
        super(name);
        this.name = name;
        context.setStrategy(new RsaStringService());

        this.n = ((PublicKeyRsa) contextParams.get("PublicKey")).n();
        this.e = ((PublicKeyRsa) contextParams.get("PublicKey")).e();
        this.d = ((PrivateKeyRsa) contextParams.get("PrivateKey")).d();
        this.contextParams = contextParams;

        thisInstance = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(990,700));
        setSize(new Dimension(990,700));
        setLocation(point);
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

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
        signings = UISetUpMethods.getjTextField(panel, c, 1, "Eigene Signatur", false, HeightEnum.TINY);

        JButton signMessage = new JButton("Signieren der Nachricht");
        signMessage.setPreferredSize(new Dimension(250,25));
        buttons.add(signMessage,c1);
        JButton loadTextFileButton = new JButton("Load Text File");

        loadTextFileButton.setPreferredSize(new Dimension(250, 25));
        c1.gridy = i;
        buttons.add(loadTextFileButton, c1);

        signingValid = UISetUpMethods.getjTextField(panel, c, 2, "Empfangene Signatur gültig", false, HeightEnum.TINY);
        receivedSignature = UISetUpMethods.getjTextField(panel, c, 3, "Empfangene Signatur", false, HeightEnum.TINY);

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




        inputAndOutput.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable t = evt.getTransferable();

                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        // Handle dropped text
                        String droppedText = (String) t.getTransferData(DataFlavor.stringFlavor);
                        inputAndOutput.setText(droppedText);
                    } else if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        // Handle dropped files
                        java.util.List<File> fileList = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : fileList) {
                            if (file.getName().toLowerCase().endsWith(".txt")) {
                                // Handle .txt file content with UTF-8 encoding
                                String content = Files.readString(file.toPath());
                                inputAndOutput.setText(content);
                        } else if (file.getName().toLowerCase().endsWith(".docx")) {
                                // Handle .docx file content using Apache POI
                                String content = readDocxFile(file);
                                inputAndOutput.setText(content);
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Communicator.this, "Error occured: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        messageListJ.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof RSAMessage) {
                    RSAMessage RSAMessage = (RSAMessage) value;
                    setText("Message from " + RSAMessage.getSender().name + ": " + RSAMessage.message);
                }
                return renderer;
            }
        });

        messageListJ.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) { // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    RSAMessage selectedRSAMessage = messageListJ.getModel().getElementAt(index);
                    inputAndOutput.setText(selectedRSAMessage.message);
                }
            }
        });
        startEncode.addActionListener(ex -> {
            try{
                String encryptedMessage = (String) context.encrypt(inputAndOutput.getText(), contextParams);
                currentClearMessage.set(inputAndOutput.getText());
                inputAndOutput.setText(encryptedMessage);
                currentMessageIsEncrypted.set(true);
            } catch (Exception f){
                JOptionPane.showMessageDialog(null, "Error while Encryption" + f);
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
        sendMessage.addActionListener(ex -> {
            RSAMessage RSAMessage = new RSAMessage(inputAndOutput.getText(), signature, thisInstance.e, thisInstance.n, currentMessageIsEncrypted.get(), currentMessageIsSigned.get(), thisInstance, getReceiver(thisInstance), currentClearMessage.get());
            getReceiver(thisInstance).sendAMessage(RSAMessage);
            messageListModel.addElement(RSAMessage);
            inputAndOutput.setText("");
        });
        clearEverything.addActionListener(e -> {
            inputAndOutput.setText("");
            signings.setText("");
            signingValid.setText("");
            signature = null;
            currentMessageIsEncrypted.set(false);
            currentMessageIsSigned.set(false);
            currentClearMessage.set("");
            messageListModel.clear();
        });
        signMessage.addActionListener(ex -> {
            try {
                signature = context.sign(inputAndOutput.getText(), contextParams);
                signings.setText(signature);
                currentMessageIsSigned.set(true);
            } catch (NoSuchAlgorithmException exc) {
                throw new RuntimeException(exc);
            }
        });
        loadTextFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

            int option = fileChooser.showOpenDialog(Communicator.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String content = Files.readString((selectedFile).toPath());
                    inputAndOutput.setText(content);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Communicator.this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        startDecode.addActionListener(ex -> {
            RSAMessage selectedRSAMessage = messageListJ.getSelectedValue(); // Get selected message
            //If none are selected, get the last one
            if(selectedRSAMessage == null) {
                if(!RSAMessageList.isEmpty()){
                    selectedRSAMessage = RSAMessageList.getLast();
                }
            }
            if (selectedRSAMessage != null) { // Check if a message is selected
                String messageToUse;
                if(selectedRSAMessage.isEncrypted()){
                    //If the message belongs to me, use my private key
                    if(selectedRSAMessage.getReceiver().equals(thisInstance)) {
                        try {
                            messageToUse = context.decrypt(selectedRSAMessage.message, contextParams);
                        } catch (Exception f){
                            JOptionPane.showMessageDialog(null, "Error while Decryption" + f);
                            return;
                        }
                    } else {
                        messageToUse = selectedRSAMessage.getClearMessage(thisInstance);
                    }
                } else {
                    messageToUse = selectedRSAMessage.message;
                }
                inputAndOutput.setText(messageToUse);
                if(selectedRSAMessage.isSigned()){
                    try {
                        if(selectedRSAMessage.isEncrypted){
                            signingValid.setText("" + context.verify(selectedRSAMessage.message, selectedRSAMessage.getSignature(), contextParams));
                        } else {
                            signingValid.setText("" + context.verify(messageToUse, selectedRSAMessage.getSignature(), contextParams));
                        }
                        receivedSignature.setText(selectedRSAMessage.getSignature());
                    } catch (NoSuchAlgorithmException exc) {
                        throw new RuntimeException(exc);
                    }
                } else {
                    signingValid.setText("");
                }
            }
        });

        add(panel);
        panel.updateUI();
    }

    public void sendAMessage(RSAMessage m) {
        RSAMessageList.add(m); // Add the message to the sender's list
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

    private String readDocxFile(File file) {
        return readStringOfFile(file);
    }
}