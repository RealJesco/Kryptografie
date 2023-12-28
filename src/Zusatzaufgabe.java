import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        super("Zusatzaufgabe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Percentage of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(1000, 350));
        setSize(new Dimension(1000, 350));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        calculateDivisorsOfPrimeNumber = new JButton("Berechne die Primzahl als Summe zweier Quadrate");
        calculateDivisorsOfPrimeNumber.setPreferredSize(new Dimension(690,100));
        calculateDivisorsOfPrimeNumber.setFont(new Font("Arial", Font.PLAIN, 25));
        int i = 0;
        primeNumberField = new JTextField();
        primeNumberField.setPreferredSize(new Dimension(680,100));
        primeNumberField.setFont(new Font("Arial", Font.PLAIN, 30));
        divisorsOfPrimeNumberField = new JTextArea();
        divisorsOfPrimeNumberField.setLineWrap(true);
        divisorsOfPrimeNumberField.setFont(new Font("Arial", Font.PLAIN, 30));
        divisorsOfPrimeNumberField.setPreferredSize(new Dimension(680, 100));
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
        panel.add(divisorsOfPrimeNumberField, c);
        c.gridx = 1;
        c.gridy = i++;
        panel.add(calculateDivisorsOfPrimeNumber,c);
        calculateDivisorsOfPrimeNumber.addActionListener(e -> {
            try {
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

        add(panel);
        pack();
    }
}
