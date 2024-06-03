package main.GUI;

import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.GUI.HelperClasses.HeightEnum;
import main.GUI.HelperClasses.UISetUpMethods;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.encryption.EncryptionContext;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
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
    private static JTextArea anzeige_chiffrat;
    private static JTextArea anzeige_signatur;
    private static ElGamalMenezesVanstoneMessage input_cipherMessage;

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
        frame.setLocation(new Point(350, 0));
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
                    Transferable t = evt.getTransferable();

                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        // Handle dropped text
                        String droppedText = (String) t.getTransferData(DataFlavor.stringFlavor);
                        inputKlartext.setText(droppedText);
                    } else if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        // Handle dropped files
                        java.util.List<File> fileList = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : fileList) {
                            if (file.getName().toLowerCase().endsWith(".txt")) {
                                // Handle .txt file content with UTF-8 encoding
                                String content = Files.readString(file.toPath());
                                inputKlartext.setText(content);
                            } else if (file.getName().toLowerCase().endsWith(".docx")) {
                                // Handle .docx file content using Apache POI
                                String content = readDocxFile(file);
                                inputKlartext.setText(content);
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error occured: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            private String readDocxFile(File file) {
                return readStringOfFile(file);
            }
        });
        inputKlartext.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                anzeige_chiffrat.setText("");
                anzeige_signatur.setText("");
                input_cipherMessage = null;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                anzeige_chiffrat.setText("");
                anzeige_signatur.setText("");
                input_cipherMessage = null;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                anzeige_chiffrat.setText("");
                anzeige_signatur.setText("");
                input_cipherMessage = null;
            }
        });

        JButton loadFileButton = new JButton("Lade Textdatei");
        loadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String content = Files.readString(selectedFile.toPath());
                    inputKlartext.setText(content);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        int i = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(loadFileButton, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(inputScrollPane, c);
        anzeige_k = UISetUpMethods.getjTextArea(panel, c, i++, "Zufallszahl k", HeightEnum.NORMAL);
        JButton encryptButton = new JButton("Verschlüsseln");
        encryptButton.addActionListener(e -> {
            try {
                encrypt();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i++;
        panel.add(encryptButton, c);
        anzeige_chiffrat = UISetUpMethods.getjTextArea(panel, c, i++, "Chiffrat", HeightEnum.BIG);
        anzeige_signatur = UISetUpMethods.getjTextArea(panel, c, i++, "Signatur", HeightEnum.NORMAL);
        JButton copyButton = new JButton("Chiffrat + Signatur übertragen");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = i;
        copyButton.addActionListener(e -> {
            if(KlartextPanel.getInput_cipherMessage() != null){
                ChiffratSignaturPanel.receiveMessage(input_cipherMessage);
            }
        });
        panel.add(copyButton, c);

        panel.updateUI();
    }

    static String readStringOfFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private static void encrypt() throws NoSuchAlgorithmException {
        ElGamalMenezesVanstoneMessage encryptedMessage = (ElGamalMenezesVanstoneMessage) context.encrypt(inputKlartext.getText(), contextParams);
        encryptedMessage.addSignature(context.sign(inputKlartext.getText(), contextParams));
        anzeige_chiffrat.setText(encryptedMessage.getCipherMessageString());
        anzeige_signatur.setText(encryptedMessage.getSignature());
        input_cipherMessage = encryptedMessage;
    }

    public static ElGamalMenezesVanstoneMessage getInput_cipherMessage() {
        return input_cipherMessage;
    }

}
