import mathMethods.MathMethods;
import rsa.RSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        CommunicationPanel.getInstance();
        // Calculate block size as the floor of the logarithm base 55296 of 2^bitLengthN using change of base formula
        // Clock time
        long start = System.nanoTime();
        RSA rsa = new RSA(40, 1024, 55926, BigInteger.valueOf(844));
        RSA.generatePrimeNumbers();
        System.out.println("Time needed only to generate primes: " + (System.nanoTime()-start));
        System.out.println("p: " + rsa.getP());
        System.out.println("q: " + RSA.getQ());
        System.out.println("n2: " + rsa.getN());
        String message = "Mathematik ist spannend";
        String encryptedMsg = RSA.encrypt(message, BigInteger.valueOf(65537), rsa.getN());
        System.out.println("Encrypted message: " + encryptedMsg);
        String decryptedMsg = RSA.decrypt(encryptedMsg, rsa.getD(), rsa.getN());
        System.out.println(decryptedMsg);
        System.out.println("Time needed to generate and encrypt and decrypt: " + (System.nanoTime()-start));

    }

}
