package main.GUI;

import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.GUI.HelperClasses.HeightEnum;
import main.GUI.HelperClasses.UISetUpMethods;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.encryption.EncryptionContext;
import main.encryption.EncryptionContextParamBuilder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
    private static JTextArea anzeige_dechiffrat;
    private static JTextArea anzeige_signatur_valid;
    private static DefaultListModel<ElGamalMenezesVanstoneMessage> messageListModel;
    private static JList<ElGamalMenezesVanstoneMessage> messageListJ;
    private static final ArrayList<ElGamalMenezesVanstoneMessage> MessageList = new ArrayList<>();

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
        try{
            contextParams.remove("elGamalMenezesVanstoneCipherMessage");
        }catch(Exception ignored){}
        messageListModel.removeAllElements();
        MessageList.clear();
    }

    private static void setupGraphics() {
        frame.setTitle("main.GUI.ChiffratSignaturPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700,1000));
        frame.setSize(new Dimension(700,1000));
        frame.setLocation(new Point(1050, 0));
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
        input_chiffrat.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(inputScrollPane, c);

        input_signatur = new JTextArea();
        input_signatur.setLineWrap(true);
        input_signatur.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                anzeige_dechiffrat.setText("");
                anzeige_signatur_valid.setText("");
            }
        });
        inputScrollPane = new JScrollPane(input_signatur);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(650, 100));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(inputScrollPane, c);

        JButton decryptButton = new JButton("Entschlüsseln");
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


        messageListModel = new DefaultListModel<>();
        messageListJ = new JList<>(messageListModel);
        JScrollPane messageListScrollPane = new JScrollPane(messageListJ);
        messageListScrollPane.setPreferredSize(new Dimension(450, 150));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 6;
        panel.add(messageListScrollPane, c);

        messageListJ.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ElGamalMenezesVanstoneMessage message) {
                    setText(message.getTime() + ": " + message.getCipherMessageString());
                }
                return renderer;
            }
        });
        messageListJ.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) { // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    ElGamalMenezesVanstoneMessage selectedMessage = messageListJ.getModel().getElementAt(index);
                    contextBuilder.withElGamalMenezesVanstoneCipherMessage(selectedMessage);
                    contextParams = contextBuilder.build();
                    input_chiffrat.setText(selectedMessage.getCipherMessageString());
                    input_signatur.setText(selectedMessage.getSignature());
                }
            }
        });
    }

    private static void decrypt() throws NoSuchAlgorithmException {
        if(contextParams != null && contextParams.containsKey("elGamalMenezesVanstoneCipherMessage")){
            String decrypted = context.decrypt(input_chiffrat.getText(), contextParams);
            anzeige_dechiffrat.setText(decrypted);
            //System.out.println(input_signatur.getText());
            anzeige_signatur_valid.setText("" + context.verify(anzeige_dechiffrat.getText(), input_signatur.getText(), contextParams));
        } else {
            JOptionPane.showMessageDialog(frame, "No Message selected!");
        }
    }

    public static void receiveSignaturAndChiffrat(ElGamalMenezesVanstoneMessage cipherMessage){
        MessageList.add(cipherMessage);
        SwingUtilities.invokeLater(() -> messageListModel.addElement(cipherMessage));
    }
}