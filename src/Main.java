import mathMethods.MathMethods;
import rsa.RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        CommunicationPanel.getInstance();

        // Calculate block size as the floor of the logarithm base 55296 of 2^bitLengthN using change of base formula
        RSA rsa = new RSA(40, 3320, 55926);
        String message = "Mathematik ist spannend";
//        Clock time
        long timeTotal = 0;
//            long start = System.nanoTime();

//        for(int i = 0; i < 6; i++){
//            long start = System.nanoTime();
//            RSA.generatePrimeNumbers();
//            long end = System.nanoTime();
//            timeTotal += (end-start);
//            System.out.println("Time to generate prime numbers: " + (end-start));
//        }
//            long end = System.nanoTime();
RSA.generatePrimeNumbers();
//        System.out.println("Average time to generate prime numbers: " + timeTotal/6);
        System.out.println("Time to generate prime numbers: " + timeTotal);
//        long start = System.nanoTime();
//        RSA.generatePrimeNumbers();
//        long end = System.nanoTime();
//        System.out.println("Time to generate prime numbers: " + (end-start));
        BigInteger n = rsa.getN();
        System.out.println("n: " + n);
        BigInteger p = rsa.getP();
        System.out.println("p: " + p);
        BigInteger e = rsa.getE();
//        System.out.println("e: " + e);
        String encryptedMessage = RSA.encrypt(message, e, n);
        BigInteger d = rsa.getD();
//        System.out.println("d: " + d);
        String decryptedMessage = RSA.decrypt(encryptedMessage, d, n);
        System.out.println("Decrypted message: " + decryptedMessage);
        RSA.calculateP(BigInteger.valueOf(9));
    }
    public static BigInteger getRandomBigInteger(BigInteger upperLimit){
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), new Random());
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }
}
