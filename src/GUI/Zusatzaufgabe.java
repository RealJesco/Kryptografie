package GUI;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import mathMethods.MathMethods;
import mathMethods.GaussianInteger;
public class Zusatzaufgabe extends JFrame {
    private static final Zusatzaufgabe singleton = new Zusatzaufgabe();
    private static JPanel panel;
    private static GridBagConstraints c = null;
    private static JButton calculateDivisorsOfPrimeNumber;

    private static JTextField primeNumberField;
    private static JTextArea divisorsOfPrimeNumberField;

    public static Zusatzaufgabe getInstance() {
        return singleton;
    }

    private Zusatzaufgabe() {
        super("GUI.Zusatzaufgabe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Percentage of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Responsive width and height
        int[] frameWidth = {(int) Math.round(screenSize.width * 0.5)};
        int[] frameHeight = {(int) Math.round(screenSize.height * 0.5)};
        setPreferredSize(new Dimension(frameWidth[0], frameHeight[0]));
        setSize(new Dimension(frameWidth[0], frameHeight[0]));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        calculateDivisorsOfPrimeNumber = new JButton("Berechne die Primzahl als Summe zweier Quadrate");
        calculateDivisorsOfPrimeNumber.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));
        calculateDivisorsOfPrimeNumber.setFont(new Font("Arial", Font.PLAIN, 25));
        int i = 0;
        primeNumberField = new JTextField();
        primeNumberField.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));
        primeNumberField.setFont(new Font("Arial", Font.PLAIN, frameHeight[0] / 15));
//        Make it scrollable

        divisorsOfPrimeNumberField = new JTextArea();
        divisorsOfPrimeNumberField.setLineWrap(true);
        divisorsOfPrimeNumberField.setFont(new Font("Arial", Font.PLAIN, frameHeight[0] / 15));

        JScrollPane divisorsScrollPane = new JScrollPane(divisorsOfPrimeNumberField);
        divisorsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        divisorsScrollPane.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));


        //Placeholder text
        c.gridx = 0;
        c.gridy = i;
        panel.add(new JLabel("Primzahl"), c);
        c.gridx = 1;
        c.gridy = i++;
        panel.add(primeNumberField, c);
        c.gridx = 0;
        c.gridy = i;
        panel.add(new JLabel("Summe zweier Quadrate"), c);
        c.gridx = 1;
        c.gridy = i++;
        panel.add(divisorsScrollPane, c);
//        panel.add(divisorsOfPrimeNumberField, c);
        c.gridx = 1;
        c.gridy = i++;
        panel.add(calculateDivisorsOfPrimeNumber,c);
        calculateDivisorsOfPrimeNumber.addActionListener(e -> {
            try {
                //Remove all whitespaces
                primeNumberField.setText(primeNumberField.getText().replaceAll("\\s+",""));
                //Check if input is a number only
                if(!primeNumberField.getText().matches("[0-9]+")) {
                    throw new Exception("Bitte geben Sie eine Zahl ein");
                }
                BigInteger primeNumber = new BigInteger(primeNumberField.getText());
                GaussianInteger divisors = MathMethods.representPrimeAsSumOfSquares(primeNumber);
                divisorsOfPrimeNumberField.setText(divisors.real + "² + " + divisors.imaginary + "²");
            } catch (Exception ex) {
                divisorsOfPrimeNumberField.setText(ex.getMessage());
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                frameWidth[0] = getSize().width;
                frameHeight[0] = getSize().height;

                calculateDivisorsOfPrimeNumber.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));
                primeNumberField.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));
                divisorsScrollPane.setPreferredSize(new Dimension((int) (frameWidth[0] * 0.7), (int) (frameHeight[0] * 0.2)));
                primeNumberField.setFont(new Font("Arial", Font.PLAIN, frameHeight[0] / 15));
                divisorsOfPrimeNumberField.setFont(new Font("Arial", Font.PLAIN, frameHeight[0] / 15));
                calculateDivisorsOfPrimeNumber.setFont(new Font("Arial", Font.PLAIN, 25));

                panel.revalidate();
                panel.repaint();
            }
        });


        add(panel);
        pack();
    }
}
