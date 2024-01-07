import GUI.CommunicationPanel;
import GUI.Zusatzaufgabe;
import rsa.RSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        CommunicationPanel.getInstance();
        Zusatzaufgabe.getInstance();
        // Calculate block size as the floor of the logarithm base 55296 of 2^bitLengthN using change of base formula
        // Clock time
        long start = System.nanoTime();
        RSA rsa = new RSA(40, 1024, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        System.out.println("Time needed only to generate primes: " + (System.nanoTime()-start));
        System.out.println("p: " + RSA.getP());
        System.out.println("q: " + RSA.getQ());
        System.out.println("n2: " + RSA.getN());
        String message = "Mathematik ist spannend";
        String encryptedMsg = RSA.encrypt(message, BigInteger.valueOf(65537), RSA.getN());
        System.out.println("Encrypted message: " + encryptedMsg);
        String decryptedMsg = RSA.decrypt(encryptedMsg, RSA.getD(), RSA.getN());
        System.out.println(decryptedMsg);
        System.out.println("Time needed to generate and encrypt and decrypt: " + (System.nanoTime()-start));

    }

}
