import mathMethods.MathMethods;
import rsa.RSA;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        //CommunicationPanel.getInstance();

        // Calculate block size as the floor of the logarithm base 55296 of 2^bitLengthN using change of base formula
//        Clock time
        int primelength = 500;
        int bitLength = BigInteger.TEN.pow(primelength).subtract(BigInteger.ONE).bitLength();
        RSA rsa = new RSA(40, bitLength, 55926);
        double timer1 = 0;
        double timer2 = 0;
        int i;
        Random random = new SecureRandom();
        for(i = 0; i<10; i++){
            long start = System.currentTimeMillis();
            System.out.println(MathMethods.getRandomPrimeBigInteger(primelength,50, 40, random));
            long end = System.currentTimeMillis();
            timer1 += end-start;
            System.out.println("t1: " +timer1);
            start = System.currentTimeMillis();
            System.out.println(RSA.generateRandomPrime(BigInteger.ZERO,BigInteger.TEN.pow(primelength).subtract(BigInteger.ONE)));
            end = System.currentTimeMillis();
            timer2 += end-start;
            System.out.println("t2: " +timer2);
        }
        System.out.println(timer1/i);
        System.out.println(timer2/i);
        /*
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
        System.out.println("Message: " + decryptedMessage);

         */
    }




}
