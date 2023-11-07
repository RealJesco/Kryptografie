package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import mathMethods.MathMethods;


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
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger[] SMALL_PRIMES = {
            BigInteger.valueOf(2),
            BigInteger.valueOf(3),
            BigInteger.valueOf(5),
            BigInteger.valueOf(7),
            BigInteger.valueOf(11),
            BigInteger.valueOf(13),
            BigInteger.valueOf(17),
            BigInteger.valueOf(19),
            BigInteger.valueOf(23),
            BigInteger.valueOf(29),
            BigInteger.valueOf(31),
            BigInteger.valueOf(37),
            BigInteger.valueOf(41),
            BigInteger.valueOf(43),
            BigInteger.valueOf(47),
            BigInteger.valueOf(53),
            BigInteger.valueOf(59),
            BigInteger.valueOf(61),
            BigInteger.valueOf(67),
            BigInteger.valueOf(71),
            BigInteger.valueOf(73),
            BigInteger.valueOf(79),
            BigInteger.valueOf(83),
            BigInteger.valueOf(89),
            BigInteger.valueOf(97),
            BigInteger.valueOf(101),
            BigInteger.valueOf(103),
            BigInteger.valueOf(107),
            BigInteger.valueOf(109),
            BigInteger.valueOf(113),
            BigInteger.valueOf(127),
            BigInteger.valueOf(131),
            BigInteger.valueOf(137),
            BigInteger.valueOf(139),


    };

    //    Constructor
    public RSA(int millerRabinSteps, int bitLengthN, int numberSystemBase) {
        RSA.millerRabinSteps = millerRabinSteps;
        RSA.bitLengthN = bitLengthN;
        System.out.println("bitLengthN: " + bitLengthN);
        blockSize = (int)(bitLengthN * (Math.log(2) / Math.log(numberSystemBase)));
        System.out.println("blockSize: " + blockSize);
        blockSizePlusOne = blockSize + 1;
        RSA.numberSystemBase = numberSystemBase;
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
    public static BigInteger getQ(){
        return q;
    }
    public static BigInteger setN(BigInteger n){
        return RSA.n = n;
    }
    public static BigInteger setE(BigInteger e){
        return RSA.e = e;
    }
    public static BigInteger setD(BigInteger d){
        return RSA.d = d;
    }
    public static BigInteger setP(BigInteger p){
        return RSA.p = p;
    }
    public static BigInteger setQ(BigInteger q){
        return RSA.q = q;
    }
    public static void calculateN(BigInteger p, BigInteger q){
        n = p.multiply(q);
    }
    public static void calculatePhiN(BigInteger p, BigInteger q){
        phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }
    // Initially try to use 65537 as the public exponent e
    public static void calculateE(BigInteger phiN){
        e = BigInteger.valueOf(65537);
        if (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(BigInteger.ONE)) {
            e = BigInteger.valueOf(3);
            if (!e.gcd(phiN).equals(BigInteger.ONE)) {
                BigInteger lowerBoundForE = BigInteger.TWO; // e must be greater than 1
                BigInteger upperBoundForE = phiN.subtract(BigInteger.ONE); // e must be less than phiN
                do {
                    e = generateRandomPrime(lowerBoundForE, upperBoundForE);
                } while (!e.gcd(phiN).equals(BigInteger.ONE));
            }
        }
    }

    public static void calculateD(BigInteger e, BigInteger phiN){
        d = MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);
    }
    public static void calculateP(BigInteger bitLengthPQ){
        BigInteger lowerBound = BigInteger.ONE.shiftLeft(bitLengthPQ.intValue() - 1);
        BigInteger upperBound = BigInteger.ONE.shiftLeft(bitLengthPQ.intValue()).subtract(BigInteger.ONE);
        BigInteger possibleP;
        do {
            possibleP = generateRandomPrime(lowerBound, upperBound);
        } while (possibleP.equals(q));
        p = possibleP;
    }
    public static void calculateQ(BigInteger bitLengthPQ){
        BigInteger lowerBound = BigInteger.ONE.shiftLeft(bitLengthPQ.intValue() - 1);
        BigInteger upperBound = BigInteger.ONE.shiftLeft(bitLengthPQ.intValue()).subtract(BigInteger.ONE);
        BigInteger possibleQ;
        do {
            possibleQ = generateRandomPrime(lowerBound, upperBound);
        } while (possibleQ.equals(p));
        q = possibleQ;
    }

    /**
     * Generates two prime numbers suitable for RSA encryption with bit length of 128 and calculates n and phi(n), as well as e and d.
     *
     */
    // Maybe split up into functions for better readability
    public static void generatePrimeNumbers() {
        // Calculate bit length of p and q
        int bitLengthPQ = bitLengthN / 2;
        long startTime = System.nanoTime();
        calculateP(BigInteger.valueOf(bitLengthPQ));
        calculateQ(BigInteger.valueOf(bitLengthPQ));
        long endTime = System.nanoTime();
        System.out.println("Time for prime generation: " + (endTime - startTime) / 1000000 + " ms");
        calculateN(p, q);
        calculatePhiN(p, q);
        calculateE(phiN);
        calculateD(e, phiN);
    }
    /**
     * Generates a random prime number within specified limits and checks that it is within a certain range.
     *
     * @param upperBound  the upper bound for the generated prime number.
     * @return a prime number within the specified range.
     */


    private static BigInteger generateRandomPrime(BigInteger lowerBound, BigInteger upperBound) {
        SecureRandom random = new SecureRandom();
        int bitLength = upperBound.subtract(lowerBound).bitLength();
        BigInteger primeCandidate;

        while (true) {
            // Generate a random odd BigInteger within the range
            BigInteger randomNumber = new BigInteger(bitLength, random).setBit(0);
            primeCandidate = lowerBound.add(randomNumber);

            // If the generated number is out of range, retry
            if (primeCandidate.compareTo(upperBound) >= 0) {
                continue;
            }

            // Fast check against small primes
            boolean divisible = false;
            for (BigInteger smallPrime : SMALL_PRIMES) {
                if (primeCandidate.mod(smallPrime).equals(BigInteger.ZERO)) {
                    divisible = true;
                    break;
                }
            }
            if (divisible) {
                continue;
            }

            // If primeCandidate is even (it can only be even if it's equal to the lower bound), make it odd
            if (primeCandidate.mod(TWO).equals(BigInteger.ZERO)) {
                primeCandidate = primeCandidate.add(BigInteger.ONE);
            }

            // Expensive primality check
            if (MathMethods.parallelMillerRabinTest(primeCandidate, millerRabinSteps)) {
                break; // Prime is found
            }
            // Otherwise, loop again and generate a new primeCandidate
        }
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
        System.out.println("Input message: " + message);

        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(message);
        System.out.println("Unicode message: " + unicodeMessage);
        // Step 2: Prepare message for encryption (Block cipher)
        List<BigInteger> numericMessage = prepareMessageForEncryption(unicodeMessage);
        System.out.println("Numeric message: " + numericMessage);
        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = encryptNumericBlocks(numericMessage, e, n);
        System.out.println("Encrypted blocks: " + encryptedBlocks);
        // Step 4: Process the encrypted blocks into a string representation

        return processEncryptedBlocksToString(encryptedBlocks);
    }

    private static List<Integer> convertTextToUnicode(String message) {
        // Implementation of text to Unicode conversion
        return MathMethods.convertTextToUniCode(message);
    }

    private static List<BigInteger> prepareMessageForEncryption(List<Integer> unicodeMessage) {
        // Implementation of preparing message for encryption
        return MathMethods.prepareMessageForEncryption(unicodeMessage, blockSize, numberSystemBase);
    }

    private static List<BigInteger> encryptNumericBlocks(List<BigInteger> numericMessage, BigInteger e, BigInteger n) {
        // Implementation of encrypting numeric blocks
        List<BigInteger> encryptedBlocks = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            encryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, e, n));
        }
        return encryptedBlocks;
    }

    private static String processEncryptedBlocksToString(List<BigInteger> encryptedBlocks) {
        // Implementation of processing encrypted blocks into string
        StringBuilder encryptedNumericMessageStr = new StringBuilder();
        for (BigInteger block : encryptedBlocks) {
            encryptedNumericMessageStr.append(convertBlockToString(block));
        }
        return encryptedNumericMessageStr.toString();
    }

    private static String convertBlockToString(BigInteger block) {
        // Convert a single encrypted block into a string representation
        List<Integer> tempList = convertBlockToNumberList(block);
        return MathMethods.convertUniCodeToText(tempList);
    }

    private static List<Integer> convertBlockToNumberList(BigInteger block) {
        // Convert a single encrypted block into a list of numbers
        List<Integer> numberList = new ArrayList<>();
        int count = 0;
        while (!block.equals(BigInteger.ZERO)) {
            numberList.add(0, block.mod(BigInteger.valueOf(numberSystemBase)).intValue());
            block = block.divide(BigInteger.valueOf(numberSystemBase));
            count++;
        }
        while (count < blockSizePlusOne) {
            numberList.add(0, 0);
            count++;
        }
        return numberList;
    }
    public static String decrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n) {
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(encryptedNumericMessageStr);
        System.out.println("Decrypting Unicode to encrypted blocks: " + unicodeMessage);
        // Step 2: Create encrypted blocks from Unicode
        List<List<BigInteger>> encryptedBlocks = createEncryptedBlocksFromUnicode(unicodeMessage);
        System.out.println("Encrypted blocks: " + encryptedBlocks);
        // Step 3: Convert the encrypted blocks to BigInteger format
        List<BigInteger> encryptedNumericMessages = convertBlocksToBigIntegers(encryptedBlocks);
        System.out.println("Encrypted numeric messages: " + encryptedNumericMessages);
        // Step 4: Decrypt the numeric representation
        List<BigInteger> numericMessage = decryptNumericMessages(encryptedNumericMessages, d, n);
        System.out.println("Numeric message: " + numericMessage);
        // Step 5: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = convertNumericMessageToIntegers(numericMessage);
        System.out.println("Decrypted message in Unicode integer values: " + decryptedMessage);
        // Step 6: Convert the list of integers to a string
        String decryptedMessageStr = convertIntegersToText(decryptedMessage);
        System.out.println("Decrypted message string: " + decryptedMessageStr);
        // Step 7: Remove padding from the decrypted string
        return removePadding(decryptedMessageStr);
    }


    private static List<List<BigInteger>> createEncryptedBlocksFromUnicode(List<Integer> unicodeMessage) {
        List<List<BigInteger>> encryptedBlocks = new ArrayList<>();
        for (int i = 0; i < unicodeMessage.size(); i += blockSizePlusOne) {
            List<BigInteger> block = new ArrayList<>();
            for (int j = 0; j < blockSizePlusOne; j++) {
                if (i + j < unicodeMessage.size()) {
                    block.add(BigInteger.valueOf(unicodeMessage.get(i + j)));
                } else {
                    block.add(BigInteger.ZERO);
                }
            }
            encryptedBlocks.add(block);
        }
        return encryptedBlocks;
    }

    private static List<BigInteger> convertBlocksToBigIntegers(List<List<BigInteger>> encryptedBlocks) {
        List<BigInteger> encryptedNumericMessages = new ArrayList<>();
        for (List<BigInteger> encryptedBlock : encryptedBlocks) {
            BigInteger sum = BigInteger.ZERO;
            for (int j = 0; j < encryptedBlock.size(); j++) {
                BigInteger temp = encryptedBlock.get(encryptedBlock.size() - j - 1).multiply(BigInteger.valueOf(numberSystemBase).pow(j));
                sum = sum.add(temp);
            }
            encryptedNumericMessages.add(sum);
        }
        return encryptedNumericMessages;
    }

    private static List<BigInteger> decryptNumericMessages(List<BigInteger> encryptedNumericMessages, BigInteger d, BigInteger n) {
        List<BigInteger> numericMessage = new ArrayList<>();
        for (BigInteger block : encryptedNumericMessages) {
            numericMessage.add(MathMethods.alternativeQuickExponentiation(block, d, n));
        }
        return numericMessage;
    }

    private static List<Integer> convertNumericMessageToIntegers(List<BigInteger> numericMessage) {
        List<Integer> decryptedMessage = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            decryptedMessage.addAll(MathMethods.prepareMessageForDecryption(block, blockSize, numberSystemBase));
        }
        return decryptedMessage;
    }

    private static String convertIntegersToText(List<Integer> decryptedMessage) {
        return MathMethods.convertUniCodeToText(decryptedMessage);
    }

    private static String removePadding(String decryptedMessageStr) {
        int paddingIndex = decryptedMessageStr.indexOf(0);
        return paddingIndex >= 0 ? decryptedMessageStr.substring(0, paddingIndex) : decryptedMessageStr;
    }
}
