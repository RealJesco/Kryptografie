import Masks.*;

import javax.swing.*;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        new Bob();



        System.out.println(MathMethods.alternativeQuickExponentation(new BigInteger("5"), new BigInteger("1"), new BigInteger("11")).toString());
        System.out.print("\n");
        Boolean noMistake = true;
        for(int i = 0; i<1; i++){
            BigInteger n1 = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000"));
            BigInteger n2 = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000"));
//            noMistake = noMistake && MathMethods.expandedEuklid(n1,n2).equals(n1.gcd(n2));
            long start = System.nanoTime();
            MathMethods.extendedEuclidean(n1,n2);
            long end = System.nanoTime();
            System.out.println(end-start);
            BigInteger a = new BigInteger("56");
            BigInteger b = new BigInteger("15");
            BigInteger[] result = MathMethods.extendedEuclidean(a,b);
            System.out.println(result[0].toString());
            System.out.println(result[1].toString());
            System.out.println(result[2].toString());
        }
        System.out.println(noMistake);

        noMistake = true;
        for(int i = 0; i<1; i++){
//            BigInteger n = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
//            BigInteger exp = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
//            BigInteger mod = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
            BigInteger n = new BigInteger("5345890").pow(50).pow(40);
            BigInteger exp = new BigInteger("561563").pow(50);
            BigInteger mod = new BigInteger("402").pow(453);
//            noMistake = noMistake && MathMethods.alternativeQuickExponentation(n,exp,mod).equals(n.modPow(exp,mod));
            long start = System.nanoTime();
            MathMethods.alternativeQuickExponentation(n,exp,mod);
            long end = System.nanoTime();
            System.out.println(end-start);
        }
        System.out.println(noMistake);


        BigInteger n = new BigInteger("13");
        System.out.println(MathMethods.millerRabinTest(n, 0.7));

        /*
        System.out.println(MathMethods.expandedEuklid(new BigInteger("6267"), new BigInteger("354")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("354"), new BigInteger("6267")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("661643"), new BigInteger("315")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("315"), new BigInteger("661643")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("661643"), new BigInteger("661643")));
        BitSet bitSet = BitSet.valueOf(new BigInteger("655360").toByteArray());
        for(int i = 0; i< bitSet.length();i++){
            System.out.print(bitSet.get(i));
        }
        System.out.print("\n");

        System.out.println(bitSet);
        System.out.println(bitSet.toString());*/
    }

    public static BigInteger getRandomBigInteger(BigInteger upperLimit){
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), new Random());
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }
}
