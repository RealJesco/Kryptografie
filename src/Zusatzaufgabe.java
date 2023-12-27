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
        double widthPercentage = 0.5;
        double heightPercentage = 0.5;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * widthPercentage);
        int height = (int) (screenSize.height * heightPercentage);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        setVisible(true);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        calculateDivisorsOfPrimeNumber = new JButton("Berechne die Primzahl als Summe zweier Quadrate");
        calculateDivisorsOfPrimeNumber.setFont(new Font("Arial", Font.PLAIN, 40));
        int i = 0;
        primeNumberField = new JTextField();
        //Percentage of the width
        primeNumberField.setPreferredSize(new Dimension(width / 2, height / 10));
        //Placeholder text
        primeNumberField.setText("Primzahl");
        primeNumberField.setFont(new Font("Arial", Font.PLAIN, 40));
        c.gridx = 0;
        c.gridy = i++;
        panel.add(primeNumberField, c);
        divisorsOfPrimeNumberField = new JTextArea();
        //Calculate width percentage
        divisorsOfPrimeNumberField.setPreferredSize(new Dimension(width / 2, height / 10));
        divisorsOfPrimeNumberField.setText("Summe zweier Quadrate");
        divisorsOfPrimeNumberField.setFont(new Font("Arial", Font.PLAIN, 40));
        c.gridx = 0;
        c.gridy = i++;
        panel.add(divisorsOfPrimeNumberField, c);
        c.gridx = 0;
        c.gridy = i++;
        panel.add(calculateDivisorsOfPrimeNumber, c);
        calculateDivisorsOfPrimeNumber.addActionListener(e -> {
            try {
                //Check if input is a number only
                if(!primeNumberField.getText().matches("[0-9]+")) {
                    throw new Exception("Bitte geben Sie eine Zahl ein");
                }
                BigInteger primeNumber = new BigInteger(primeNumberField.getText());
                GaussianInteger divisors = MathMethods.representPrimeAsSumOfSquares(primeNumber);
                divisorsOfPrimeNumberField.setText(divisors.real + "^2 + " + divisors.imaginary + "^2");
            } catch (Exception ex) {
                divisorsOfPrimeNumberField.setText(ex.getMessage());
            }
        });

        add(panel);
        pack();
    }
}
