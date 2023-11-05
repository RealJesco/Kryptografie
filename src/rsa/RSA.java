package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import mathMethods.MathMethods;

import static mathMethods.MathMethods.getRandomBigInteger;

public class RSA {

    private static int millerRabinSteps = 0;
    private static int blockSize = 0;
    private static int blockSizePlusOne = 0;
    private static int numberSystemBase = 0;
    private static BigInteger n;
    private static BigInteger phiN;
    private static BigInteger e;
    private static BigInteger d;
    private static BigInteger p;
    private static BigInteger q;
    private static int bitLengthN = 128;
    private static final SecureRandom random = new SecureRandom();

    //    Constructor
    public RSA(int millerRabinSteps, int bitLengthN, int numberSystemBase) {
        this.millerRabinSteps = millerRabinSteps;
        this.bitLengthN = bitLengthN;
        System.out.println("bitLengthN: " + bitLengthN);
        this.blockSize = (int)(bitLengthN * (Math.log(2) / Math.log(55296)));
        System.out.println("blockSize: " + blockSize);
        this.blockSizePlusOne = blockSize + 1;
        this.numberSystemBase = numberSystemBase;
    }

    public BigInteger getN(){
        return n;
    }
    public BigInteger getE(){
        return e;
    }
    public BigInteger getD(){
        return d;
    }
    public BigInteger getP(){
        return p;
    }

    /**
     * Generates two prime numbers suitable for RSA encryption with bit length of 128 and calculates n and phi(n), as well as e and d.
     *
     * @return an array containing two prime numbers.
     */
    // Maybe split up into functions for better readability
    public static void generatePrimeNumbers() {
        int bitLengthPQ = bitLengthN / 2; // for p and q

        BigInteger lowerBound = BigInteger.ONE.shiftLeft(bitLengthPQ - 1);
        BigInteger upperBound = BigInteger.ONE.shiftLeft(bitLengthPQ);

        BigInteger p, q;

        do {
            p = generateRandomPrime(lowerBound, upperBound.subtract(BigInteger.ONE));
        } while (p.bitLength() != bitLengthPQ);

        do {
            q = generateRandomPrime(lowerBound, upperBound.subtract(BigInteger.ONE));
        } while (q.bitLength() != bitLengthPQ || p.equals(q));

        n = p.multiply(q);

//        System.out.println("Final p: " + p);
//        System.out.println("Final q: " + q);
//        System.out.println("n (p*q): " + n);

        phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Generate public exponent e
        e = getRandomBigInteger(phiN.subtract(BigInteger.ONE));
        while(e.compareTo(phiN) >= 0 || !MathMethods.parallelMillerRabinTest(e, millerRabinSteps) || !MathMethods.extendedEuclidean(e, phiN)[0].equals(BigInteger.ONE)){
         e = getRandomBigInteger(phiN);
       }

        // Generate private exponent d
        d = MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);

        // Outputs for verification
//        System.out.println("n: " + n);
//        System.out.println("phi: " + phiN);
//        System.out.println("e: " + e);
//        System.out.println("d: " + d);
    }
    /**
     * Generates a random prime number within specified limits and checks that it is within a certain range.
     *
     * @param upperBound  the upper bound for the generated prime number.
     * @return a prime number within the specified range.
     */
    private static BigInteger generateRandomPrime(BigInteger lowerBound, BigInteger upperBound) {
        BigInteger primeCandidate;
        SecureRandom random = new SecureRandom();

        while (true) {
            // Generate a random BigInteger within the range of lowerBound and upperBound
            int bitLength = upperBound.subtract(lowerBound).bitLength();
            BigInteger randomNumber = new BigInteger(bitLength, random);
            primeCandidate = lowerBound.add(randomNumber);

            // If the generated number is out of range, adjust it to be within the range
            if (primeCandidate.compareTo(upperBound) >= 0) {
                primeCandidate = primeCandidate.mod(upperBound.subtract(lowerBound)).add(lowerBound);
            }

            // If the primeCandidate is even, make it odd to increase the chance of being prime
            if (primeCandidate.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                primeCandidate = primeCandidate.add(BigInteger.ONE);
            }
            // Check if the primeCandidate is a prime number
            if (MathMethods.parallelMillerRabinTest(primeCandidate, millerRabinSteps)) {
                // If a prime number is found, break out of the loop
//                System.out.println("I AM BREAKING OUT OF THE LOOP");
                break;
            }
            // Otherwise, loop again and generate a new primeCandidate
        }
//        System.out.println("I AM RETURNING THE PRIME CANDIDATE");
        return primeCandidate;
    }



    /**
     * Generates a random BigInteger within the range of [0, upperLimit).
     *
     * @param upperLimit the upper limit for random number generation.
     * @return a random BigInteger within the specified range.
     */
    private static BigInteger getRandomBigInteger(BigInteger upperLimit) {
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), random);
        } while (randomNumber.compareTo(upperLimit) >= 0 || randomNumber.compareTo(BigInteger.ONE) <= 0);

        return randomNumber;
    }

    public static String encrypt(String message, BigInteger e, BigInteger n) {
        System.out.println("n: " + n);
        System.out.println("Message: " + message);
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = MathMethods.convertTextToUniCode(message);
        System.out.println("Unicode: " + unicodeMessage);
        /** TODO: @Adham: Use this as a test case for the prepareMessageForEncryption method
         List<Integer> testElsnerMessage = new ArrayList<>();
         testElsnerMessage.add(12);
         testElsnerMessage.add(0);
         testElsnerMessage.add(19);
         testElsnerMessage.add(7);
         testElsnerMessage.add(4);
         testElsnerMessage.add(12);
         testElsnerMessage.add(0);
         testElsnerMessage.add(19);
         System.out.println("Test Elsner: " + prepareMessageForEncryption(testElsnerMessage, 8, 47));
         **/
        // Step 2: Prepare message for encryption
        List<BigInteger> numericMessage = MathMethods.prepareMessageForEncryption(unicodeMessage, blockSize, 55296);
        System.out.println("Numeric message: " + numericMessage);
//        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = new ArrayList<>();
        for(BigInteger block : numericMessage) {
            encryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, e, n));
        }
        System.out.println("Encrypted numeric message: " + encryptedBlocks);
//        Divide with remainder by 55296 to get the Unicode values
        List<List<Integer>> encryptedNumericMessageList = new ArrayList<>();
        for(BigInteger block : encryptedBlocks) {
            List<Integer> tempList = new ArrayList<>();

            int count = 0;
            while (!block.equals(BigInteger.ZERO)) {
                tempList.add(0, block.mod(BigInteger.valueOf(55296)).intValue());
                block = block.divide(BigInteger.valueOf(55296));
                count++;
            }
//            While loop is necessary to get the correct number of Unicode values
            while (count < blockSizePlusOne) {
//                Add 0s to the beginning of the list
                tempList.add(0, 0);
                count++;
            }
            System.out.println("Count: " + count);
            encryptedNumericMessageList.add(tempList);
        }
        System.out.println("Encrypted numeric message list: " + encryptedNumericMessageList);
//        Make UniCodeString from the list
        StringBuilder encryptedNumericMessageStr = new StringBuilder();
//        For each list, get the unicodevalues and add them to the string
        for (List<Integer> encryptedNumericMessage : encryptedNumericMessageList) {
            encryptedNumericMessageStr.append(MathMethods.convertUniCodeToText(encryptedNumericMessage));
        }

        System.out.println("Encrypted numeric message string: " + encryptedNumericMessageStr);

        return encryptedNumericMessageStr.toString();
    }
    public static String decrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n) {
        // THIS METHOD DECRYPTS
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = MathMethods.convertTextToUniCode(encryptedNumericMessageStr);
        System.out.println("Unicode: " + unicodeMessage);
        List<List<BigInteger>> encryptedBlocks = new ArrayList<>();
        for(int i = 0; i < unicodeMessage.size(); i+=blockSizePlusOne){
            List<BigInteger> block = new ArrayList<>();
            for(int j = 0; j < blockSizePlusOne; j++){
                if (i + j < unicodeMessage.size()) {
                    block.add(BigInteger.valueOf(unicodeMessage.get(i + j)));
                } else {
                    // If there's padding needed, add it here, otherwise break
                    block.add(BigInteger.ZERO); // Assuming padding with zeros is acceptable
                    // break; // If you don't want to pad and instead just process what's available
                }
            }
            encryptedBlocks.add(block);
        }
        System.out.println("Encrypted blocks: " + encryptedBlocks);
//        Step 2: Convert the encrypted blocks to the correct format
        List<BigInteger> encryptedNumericMessages = new ArrayList<>();
        for (List<BigInteger> encryptedBlock : encryptedBlocks) {
            BigInteger sum = BigInteger.ZERO;
            for (int j = 0; j < encryptedBlock.size(); j++) {
//                System.out.println("Encrypted block: " + encryptedBlock.get(encryptedBlock.size() - j - 1));
                BigInteger temp = encryptedBlock.get(encryptedBlock.size() - j - 1).multiply(BigInteger.valueOf(55296).pow(j));
//                System.out.println(encryptedBlock.get(encryptedBlock.size() - j - 1) + " * " + BigInteger.valueOf(55296).pow(j) + " = " + temp);
                sum = sum.add(temp);
            }
            encryptedNumericMessages.add(sum);
            System.out.println("Sum: " + sum);
        }
//        Encrypted blocks are now in the correct format
//Step 3: Decrypt the numeric representation
        List<BigInteger> numericMessage = new ArrayList<>();
//        Decrypt every encryptedNumericMessages block
        for(BigInteger block : encryptedNumericMessages) {
            numericMessage.add(MathMethods.alternativeQuickExponentiation(block, d, n));
//            System.out.println("Block: " + block);
        }
        System.out.println("Numeric message: " + numericMessage);
//        Step 4: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = new ArrayList<>();
//      prepareMessageForDecryption(numericMessage.get(0), 8, 55296) for every block
        for (BigInteger block : numericMessage) {
            decryptedMessage.addAll(MathMethods.prepareMessageForDecryption(block, blockSize, 55296));
        }
        System.out.println("Decrypted message: " + decryptedMessage);
//        Convert the list of integers to a string
        String decryptedMessageStr = MathMethods.convertUniCodeToText(decryptedMessage);
        System.out.println("Decrypted message string: " + decryptedMessageStr);
//        Remove the padding from the string
        decryptedMessageStr = decryptedMessageStr.substring(0, decryptedMessageStr.indexOf(0));
        System.out.println("Decrypted message string without padding: " + decryptedMessageStr);

        return decryptedMessageStr;
    }
}
