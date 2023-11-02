import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        CommunicationPanel.getInstance();/*
        for (int i = 0; i <= 1000; i = i + 20) {
            BigInteger random = MathMethods.getRandomBigInteger(i, 120);
            System.out.println("Stellen: " + i + " Zahl: " + random.toString());
        }*/
        for (int i = 0; i <= 2000; i = i + 20) {
            System.out.println("Stellen: " + i + " Primzahl: " + MathMethods.getRandomPrimeBigInteger(i,40,40));
        }

    }
}
