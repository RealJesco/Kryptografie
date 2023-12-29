package rsa;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import mathMethods.MathMethods;

import static mathMethods.MathMethods.generateRandomPrime;


public class RSA {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.TWO;
    private static final BigInteger ZERO = BigInteger.ZERO;
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
    private static BigInteger m;
    private static BigInteger a;
    private static BigInteger b;
    private static BigInteger countOfN = BigInteger.valueOf(1);
    private static int bitLengthN = 128;


    //    Constructor
    public RSA(int millerRabinSteps, int bitLengthN, int numberSystemBase, BigInteger m) {
        RSA.millerRabinSteps = millerRabinSteps;
        RSA.bitLengthN = bitLengthN;
        calculateAndSetBlockSizes(bitLengthN, numberSystemBase);
        RSA.numberSystemBase = numberSystemBase;
        RSA.m = m;
    }
    private static int calculateBlockSize(int bitLengthN, int numberSystemBase){
        return (int)(bitLengthN * (Math.log(2) / Math.log(numberSystemBase)));
    }
    private static void calculateAndSetBlockSizes(int bitLengthN, int numberSystemBase){
        blockSize = calculateBlockSize(bitLengthN, numberSystemBase);
        blockSizePlusOne = blockSize + 1;
    }
    public static BigInteger getN(){
        return n;
    }
    public static BigInteger getE(){
        return e;
    }
    public static BigInteger getD(){
        return d;
    }
    public static BigInteger getP(){
        return p;
    }
    public static BigInteger getCountOfN(){
        return countOfN;
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
    public static BigInteger getM(){
        return m;
    }
    public static BigInteger getA(){
        return a;
    }
    public static BigInteger getB(){
        return b;
    }
    public static BigInteger getPhiN(){
        return phiN;
    }
    public static int getBitLengthN(){
        return bitLengthN;
    }
    public static int getMillerRabinSteps(){
        return millerRabinSteps;
    }
    public static void setBitLengthN(int bitLengthN){
        RSA.bitLengthN = bitLengthN;
        //change blockSize
        calculateAndSetBlockSizes(bitLengthN, numberSystemBase);
    }
    public static void setMillerRabinSteps(int millerRabinSteps){
        RSA.millerRabinSteps = millerRabinSteps;
    }
    public static void setNumberSystemBase(int numberSystemBase){
        RSA.numberSystemBase = numberSystemBase;
        calculateAndSetBlockSizes(bitLengthN, numberSystemBase);
    }
    public static void setBlockSize(int blockSize){
        RSA.blockSize = blockSize;
    }
    public static void setBlockSizePlusOne(int blockSizePlusOne){
        RSA.blockSizePlusOne = blockSizePlusOne;
    }
    public static void setM(BigInteger m){
        RSA.m = m;
    }
    public static void setCountOfN(BigInteger countOfN){
        RSA.countOfN = countOfN;
    }

    public static void calculateN(BigInteger p, BigInteger q){
        n = p.multiply(q);
    }
    public static void calculatePhiN(BigInteger p, BigInteger q){
        phiN = (p.subtract(ONE)).multiply(q.subtract(ONE));
    }
    // Initially try to use 65537 as the public exponent e
    public static void calculateE(BigInteger phiN){
        e = BigInteger.valueOf(65537);
        if (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(ONE)) {
            e = BigInteger.valueOf(3);
            if (!e.gcd(phiN).equals(ONE)) {
                BigInteger upperBoundForE = phiN.subtract(ONE); // e must be less than phiN
                do {
                    e = generateRandomPrime(m, TWO, upperBoundForE, millerRabinSteps);
                } while (!e.gcd(phiN).equals(ONE));
            }
        }
    }

    public static void calculateD(BigInteger e, BigInteger phiN){
        d = MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);
    }
    public static void calculateP(BigInteger bitLengthP){
        BigInteger possibleP;
        a = TWO.pow(bitLengthP.intValue() - 1);
        b = TWO.pow(bitLengthP.intValue());
        do {
            possibleP = generateRandomPrime(m,a, b, millerRabinSteps);
        } while (possibleP.equals(q));
        p = possibleP;
    }
    public static void calculateQ(BigInteger bitLengthQ){
        BigInteger possibleQ;
        a = TWO.pow(bitLengthQ.intValue() -1);
        b = TWO.pow(bitLengthQ.intValue());
        do {
            possibleQ = generateRandomPrime(m,a, b, millerRabinSteps);
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
        calculateN(p, q);
        calculatePhiN(p, q);
        calculateE(phiN);
        calculateD(e, phiN);
    }

    public static String encrypt(String message, BigInteger e, BigInteger n) {
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(message);
        //Unicode values should not be equal or larger than numberSystemBase
        for(int i = 0; i < unicodeMessage.size(); i++){
            if(unicodeMessage.get(i) >= numberSystemBase){
                throw new IllegalArgumentException("Unicode value is equal or larger than numberSystemBase");
            }
        }
        // Step 2: Prepare message for encryption (Block cipher)
        List<BigInteger> numericMessage = prepareMessageForEncryption(unicodeMessage);
        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = encryptNumericBlocks(numericMessage, e, n);
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
        while (!block.equals(ZERO)) {
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
        // Step 2: Create encrypted blocks from Unicode
        List<List<BigInteger>> encryptedBlocks = createEncryptedBlocksFromUnicode(unicodeMessage);
        // Step 3: Convert the encrypted blocks to BigInteger format
        List<BigInteger> encryptedNumericMessages = convertBlocksToBigIntegers(encryptedBlocks);
        // Step 4: Decrypt the numeric representation
        List<BigInteger> numericMessage = decryptNumericMessages(encryptedNumericMessages, d, n);
        // Step 5: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = convertNumericMessageToIntegers(numericMessage);
        // Step 6: Convert the list of integers to a string
        String decryptedMessageStr = convertIntegersToText(decryptedMessage);
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
                    block.add(ZERO);
                }
            }
            encryptedBlocks.add(block);
        }
        return encryptedBlocks;
    }

    private static List<BigInteger> convertBlocksToBigIntegers(List<List<BigInteger>> encryptedBlocks) {
        List<BigInteger> encryptedNumericMessages = new ArrayList<>();
        for (List<BigInteger> encryptedBlock : encryptedBlocks) {
            BigInteger sum = ZERO;
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
     private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String sign(String message) throws NoSuchAlgorithmException {
        //check if N is larger than 256 bit
        if(n.bitLength() < 256){
            throw new IllegalArgumentException("N is smaller than 256 bit");
        }
        // Hash the message using SHA3-256
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                message.getBytes(StandardCharsets.UTF_8));

        // Convert the hashed bytes to a BigInteger
        BigInteger hashInteger = new BigInteger(1, hashbytes);

        // Encrypt the hash directly using RSA private key
        BigInteger encryptedHash = MathMethods.alternativeQuickExponentiation(hashInteger, d, n);

        // Convert the encrypted hash to a hex string (or you can use another format as needed)
        return encryptedHash.toString(16); // Using base 16 for hex representation
    }
    public static boolean verifySignature(String message, String signature) throws NoSuchAlgorithmException {
        //If signature is not in hex format, throw exception
        if(!signature.matches("-?[0-9a-fA-F]+")){
            throw new IllegalArgumentException("Signature is not in hex format");
        }

        // Hash the message using SHA3-256
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                message.getBytes(StandardCharsets.UTF_8));

        // Convert the hashed bytes to a BigInteger
        BigInteger hashInteger = new BigInteger(1, hashbytes);

        // Convert the signature to a BigInteger
        BigInteger signatureInteger = new BigInteger(signature, 16);

        // Decrypt the signature using the public key
        BigInteger decryptedSignature = MathMethods.alternativeQuickExponentiation(signatureInteger, e, n);

        // Compare the decrypted signature with the hash of the message
        return decryptedSignature.equals(hashInteger);
    }
}
