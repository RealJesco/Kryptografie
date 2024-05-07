package main.rsa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import main.mathMethods.MathMethods;
import main.resource.Resource;


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
    private static BigInteger m;
    private static BigInteger a;
    private static BigInteger b;
    private static BigInteger countOfN = BigInteger.valueOf(1);
    private static int bitLengthN = 128;


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
    public static int getBitLengthN(){
        return bitLengthN;
    }
    public static int getBlockSize(){
        return blockSize;
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

    public static void setM(BigInteger m){
        if (m.compareTo(Resource.ZERO) < 0) {
            throw new IllegalArgumentException("Negative Numbers aren't allowed here");
        }
        BigDecimal sqrt = new BigDecimal(m).sqrt(new MathContext(1000));
        if(sqrt.compareTo(sqrt.divideAndRemainder(Resource.DECIMAL_ONE)[0]) == 0){
            throw new IllegalArgumentException("Cubic-numbers aren't allowed here");
        }
        RSA.m = m;
    }

    //TODO: This method is only used in tests, isn't it?
    /**
     * Generates RSA keys.
     * <p>
     * This method generates RSA keys by calculating the values of p, q, n, phiN, e, and d
     * based on the given bit length and number system base.
     * </p>
     *
     * @param m                 the value of m
     * @param millerRabinSteps  the number of steps to perform in the Miller-Rabin primality test
     * @param numberSystemBase  the base of the number system
     * @param bitLengthN        the bit length of n
     */
    public static void generateRSAKeys(BigInteger m, int millerRabinSteps, int numberSystemBase, int bitLengthN) {
        setM(m);
        setMillerRabinSteps(millerRabinSteps);
        setNumberSystemBase(numberSystemBase);
        setBitLengthN(bitLengthN);
    }

    /**
     * Performs block cipher main.encryption on the given message using the provided main.encryption key.
     *
     * @param message the message to be encrypted
     * @param e the main.encryption key exponent
     * @param n the main.encryption key modulo
     * @return the encrypted message as a string representation
     * @throws IllegalArgumentException if any Unicode value in the message is equal to or larger than the numberSystemBase
     */
    private static String blockCipherEncrypt(String message, BigInteger e, BigInteger n){
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(message);
        //Unicode values should not be equal or larger than numberSystemBase
        for(int i = 0; i < unicodeMessage.size(); i++){
            if(unicodeMessage.get(i) >= numberSystemBase){
                throw new IllegalArgumentException("Unicode value is equal or larger than numberSystemBase");
            }
        }
        // Step 2: Prepare message for main.encryption (Block cipher)
        List<BigInteger> numericMessage = prepareMessageForEncryption(unicodeMessage);
        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = rsaEncryptNumericMessage(numericMessage, e, n);
        // Step 4: Process the encrypted blocks into a string representation
        return processEncryptedBlocksToString(encryptedBlocks);
    }

    /**
     * Encrypts a numeric message using RSA main.encryption.
     *
     * @param numericMessage the numeric message to be encrypted as a List of BigIntegers
     * @param e the public exponent used for main.encryption as a BigInteger
     * @param n the modulus used for main.encryption as a BigInteger
     * @return the encrypted message as a List of BigIntegers
     */
    private static List<BigInteger> rsaEncryptNumericMessage(List<BigInteger> numericMessage, BigInteger e, BigInteger n){
     return rsaEncryptNumericBlocks(numericMessage, e, n);
    }

    /**
     * Encrypts a list of numeric blocks using RSA main.encryption.
     *
     * @param numericMessage the list of numeric blocks to be encrypted
     * @param e              the public exponent value
     * @param n              the modulus value
     * @return the encrypted list of numeric blocks
     */
    private static List<BigInteger> rsaEncryptNumericBlocks (List<BigInteger> numericMessage, BigInteger e, BigInteger n){
        return encryptNumericBlocks(numericMessage, e, n);
    }
    /**
     * Encrypts a given message using the RSA algorithm.
     *
     * @param message The message to be encrypted.
     * @param e       The public exponent.
     * @param n       The modulus.
     * @return The encrypted message.
     */
    public static String encrypt(String message, BigInteger e, BigInteger n) {
        return blockCipherEncrypt(message, e, n);
    }

    /**
     * Converts a given message to a list of Unicode values.
     *
     * @param message The message to be converted.
     * @return A list of Unicode values representing the given message.
     */
    private static List<Integer> convertTextToUnicode(String message) {
        return MathMethods.convertTextToUniCode(message);
    }

    /**
     * Prepares a message for main.encryption by converting a list of Unicode values to a list of BigIntegers.
     *
     * @param unicodeMessage The list of Unicode values representing the message.
     * @return A list of BigIntegers representing the prepared message for main.encryption.
     */
    private static List<BigInteger> prepareMessageForEncryption(List<Integer> unicodeMessage) {
        return MathMethods.prepareMessageForEncryption(unicodeMessage, blockSize, numberSystemBase);
    }

    /**
     * Encrypts a given list of numeric blocks using the RSA algorithm.
     *
     * @param numericMessage The list of numeric blocks to be encrypted.
     * @param e              The public exponent.
     * @param n              The modulus.
     * @return A list of encrypted numeric blocks.
     */
    private static List<BigInteger> encryptNumericBlocks(List<BigInteger> numericMessage, BigInteger e, BigInteger n) {
        List<BigInteger> encryptedBlocks = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            encryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, e, n));
        }
        return encryptedBlocks;
    }

    /**
     * Processes the encrypted blocks into a string representation.
     *
     * @param encryptedBlocks The list of encrypted blocks to be processed.
     * @return The encrypted blocks as a string.
     */
    private static String processEncryptedBlocksToString(List<BigInteger> encryptedBlocks) {
        StringBuilder encryptedNumericMessageStr = new StringBuilder();
        for (BigInteger block : encryptedBlocks) {
            encryptedNumericMessageStr.append(convertBlockToString(block));
        }

        return encryptedNumericMessageStr.toString();
    }

    /**
     * Converts a single encrypted block into a string representation.
     *
     * @param block The encrypted block to be converted.
     * @return The string representation of the encrypted block.
     */
    private static String convertBlockToString(BigInteger block) {
        List<Integer> tempList = convertBlockToNumberList(block);
        return MathMethods.convertUniCodeToText(tempList);
    }

    /**
     * Converts a single encrypted block into a list of numbers.
     *
     * @param block The encrypted block to be converted.
     * @return A list of numbers representing the encrypted block.
     */
    private static List<Integer> convertBlockToNumberList(BigInteger block) {
        List<Integer> numberList = new ArrayList<>();
        while (!block.equals(Resource.ZERO)) {
            BigInteger bigIntNumberSystemBase = BigInteger.valueOf(numberSystemBase);
            numberList.add(0, block.mod(bigIntNumberSystemBase).intValue());
            block = block.divide(bigIntNumberSystemBase);
        }
        return numberList;
    }

    private static List<String> splitStringIntoBlocks(String message){
        List<String> blocks = new ArrayList<>();
        for(int i = 0; i < message.length(); i += blockSizePlusOne){
            blocks.add(message.substring(i, Math.min(message.length(), i + blockSizePlusOne)));
        }
        return blocks;
    }

    /**
     * Decrypts a numeric message using block cipher main.encryption algorithm.
     *
     * @param encryptedNumericMessageStr the encrypted numeric message in string format
     * @param d the private key parameter
     * @param n the modulus parameter
     * @return the decrypted message as a string
     */
    public static String blockCipherDecrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n){
        //Split into blocks
        List<String> splitBlocks = splitStringIntoBlocks(encryptedNumericMessageStr);

        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = new ArrayList<>();

        for(String block : splitBlocks){
            unicodeMessage.addAll(convertTextToUnicode(block));
        }


        //fill with leading zeros if necessary for the block size
        // Step 2: Create encrypted blocks from Unicode
        List<List<BigInteger>> encryptedBlocks = createEncryptedBlocksFromUnicode(unicodeMessage);
        // Step 3: Convert the encrypted blocks to BigInteger format
        List<BigInteger> encryptedNumericMessages = convertBlocksToBigIntegers(encryptedBlocks);
        // Step 4: Decrypt the numeric representation
        List<BigInteger> numericMessage = decryptNumericMessages(encryptedNumericMessages, d, n);
        // Step 5: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = convertNumericMessageToIntegers(numericMessage);
//        List<Integer> depaddedMessage = removePadding(decryptedMessage);
        // Step 6: Convert the list of integers to a string
        // Step 7: Remove padding from the decrypted string
        String paddedString = convertIntegersToText(decryptedMessage);
        return removePadding(paddedString);
    }

    /**
     * Decrypts a given encrypted numeric message using the RSA algorithm.
     *
     * @param encryptedNumericMessageStr The encrypted message to be decrypted.
     * @param d                          The private exponent.
     * @param n                          The modulus.
     * @return The decrypted message.
     */
    public static String decrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n) {
        return blockCipherDecrypt(encryptedNumericMessageStr, d, n);
    }


    /**
     * Creates encrypted blocks from a list of Unicode values.
     *
     * @param unicodeMessage The list of Unicode values representing the message.
     * @return A list of encrypted blocks, where each block is a list of BigIntegers.
     */
    private static List<List<BigInteger>> createEncryptedBlocksFromUnicode(List<Integer> unicodeMessage) {
        List<List<BigInteger>> encryptedBlocks = new ArrayList<>();
        for (int i = 0; i < unicodeMessage.size(); i += blockSizePlusOne) {
            List<BigInteger> block = new ArrayList<>();
            for (int j = 0; j < blockSizePlusOne; j++) {
                if (i + j < unicodeMessage.size()) {
                    block.add(BigInteger.valueOf(unicodeMessage.get(i + j)));
                }
            }

            encryptedBlocks.add(block);
        }
        return encryptedBlocks;
    }

    /**
     * Converts a given list of encrypted blocks to a list of BigIntegers.
     *
     * @param encryptedBlocks The list of encrypted blocks to be converted.
     * @return A list of BigIntegers representing the encrypted blocks.
     */
    private static List<BigInteger> convertBlocksToBigIntegers(List<List<BigInteger>> encryptedBlocks) {
        List<BigInteger> encryptedNumericMessages = new ArrayList<>();
        for (List<BigInteger> encryptedBlock : encryptedBlocks) {
            BigInteger sum = Resource.ZERO;
            for (int j = 0; j < encryptedBlock.size(); j++) {
                BigInteger temp = encryptedBlock.get(encryptedBlock.size() - j - 1).multiply(BigInteger.valueOf(numberSystemBase).pow(j));
                sum = sum.add(temp);
            }
            encryptedNumericMessages.add(sum);
        }
        return encryptedNumericMessages;
    }

    /**
     * Decrypts a given list of encrypted numeric messages using the RSA algorithm.
     *
     * @param encryptedNumericMessages The list of encrypted numeric messages to be decrypted.
     * @param d                        The private exponent.
     * @param n                        The modulus.
     * @return The list of decrypted numeric messages.
     */
    private static List<BigInteger> decryptNumericMessages(List<BigInteger> encryptedNumericMessages, BigInteger d, BigInteger n) {
        List<BigInteger> numericMessage = new ArrayList<>();
        for (BigInteger block : encryptedNumericMessages) {
            numericMessage.add(MathMethods.alternativeQuickExponentiation(block, d, n));
        }
        return numericMessage;
    }

    /**
     * Converts a given numeric message to a list of integers.
     *
     * @param numericMessage The numeric message to be converted.
     * @return A list of integers representing the numeric message.
     */
    private static List<Integer> convertNumericMessageToIntegers(List<BigInteger> numericMessage) {
        List<Integer> decryptedMessage = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            decryptedMessage.addAll(MathMethods.prepareMessageForDecryption(block, blockSize, numberSystemBase));
        }
        return decryptedMessage;
    }

    /**
     * Converts a list of integers representing a decrypted message to text.
     *
     * @param decryptedMessage The list of integers representing the decrypted message.
     * @return The text representation of the decrypted message.
     */
    private static String convertIntegersToText(List<Integer> decryptedMessage) {
        return MathMethods.convertUniCodeToText(decryptedMessage);
    }


    private static String removePadding(String decryptedMessageStr) {
        return decryptedMessageStr.replace("\u0000", "");
    }

    /**
     * Sign the given message using the provided private exponent and modulus.
     *
     * @param message The message to be signed.
     * @param d       The private exponent.
     * @param n       The modulus.
     * @return A hexadecimal string representation of the signed message.
     * @throws NoSuchAlgorithmException If hashing algorithm is not available.
     */
    public static String sign(String message, BigInteger d, BigInteger n) throws NoSuchAlgorithmException {
        checkIfLengthIsMoreThan256Bits(n);
        BigInteger hashedBigInteger = hashAndConvertMessageToBigInteger(message);
        BigInteger encryptedHash = MathMethods.alternativeQuickExponentiation(hashedBigInteger, d, n);

        return encryptedHash.toString(16);
    }

    /**
     * Calculates the sign of a given message using the class fields d and n.
     *
     * @param message the message to sign
     * @return the signature of the given message
     * @throws NoSuchAlgorithmException if the required algorithm is not supported
     */
    public static String sign(String message) throws NoSuchAlgorithmException {
        return sign(message, d, n);
    }

    /**
     * Checks if the length of the given BigInteger number is more than 256 bits.
     * If the length is less than 256 bits, an IllegalArgumentException is thrown with a corresponding error message.
     *
     * @param number the BigInteger number to be checked
     * @throws IllegalArgumentException if the length of the number is less than 256 bits
     */
    private static void checkIfLengthIsMoreThan256Bits(BigInteger number) {
        if (number.bitLength() < 256) {
            throw new IllegalArgumentException("N is smaller than 256 bit");
        }
    }

    /**
     * Hashes the given message using SHA3-256 and converts it to a BigInteger.
     *
     * @param message The message to be hashed and converted.
     * @return The hashed message as a BigInteger.
     * @throws NoSuchAlgorithmException if the SHA3-256 algorithm is not available.
     */
    private static BigInteger hashAndConvertMessageToBigInteger(String message) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, hashbytes);
    }

    /**
     * Verifies the signature of a message using SHA3-256 hash algorithm.
     *
     * @param message   the message to be verified
     * @param signature the signature to be verified, must be in hex format
     * @param e         The public exponent of the key (optional)
     * @param n         The modulus of the key (optional)
     * @return true if the signature is valid for the message, false otherwise
     * @throws NoSuchAlgorithmException if the SHA3-256 algorithm is not available
     * @throws IllegalArgumentException if the signature is not in hex format
     */
    public static boolean verifySignature(String message, String signature, BigInteger e, BigInteger n) throws NoSuchAlgorithmException {
        validateSignatureHexFormat(signature);

        BigInteger hashInteger = getMessageSha3Hash(message);

        BigInteger signatureInteger = new BigInteger(signature, 16);

        BigInteger decryptedSignature = MathMethods.alternativeQuickExponentiation(signatureInteger, e, n);

        return decryptedSignature.equals(hashInteger);
    }

    /**
     * Verifies the signature of a given message using the default public exponent and modulus.
     *
     * @param message   the message to verify signature for
     * @param signature the signature to verify
     * @return true if the signature is valid, false otherwise
     * @throws NoSuchAlgorithmException if the algorithm used for signature verification is not available
     */
    public static boolean verifySignature(String message, String signature) throws NoSuchAlgorithmException {
        BigInteger defaultExponent = e;
        BigInteger defaultModulus = n;
        return verifySignature(message, signature, defaultExponent, defaultModulus);
    }

    /**
     * Validates if the given signature string is in hex format.
     * Throws an IllegalArgumentException if the signature is not in hex format.
     *
     * @param signature the signature string to be validated
     * @throws IllegalArgumentException if the signature is not in hex format
     */
    private static void validateSignatureHexFormat(String signature) {
        if (!signature.matches("-?[0-9a-fA-F]+")) {
            throw new IllegalArgumentException("Signature is not in hex format");
        }
    }

    /**
     * Computes the SHA3-256 hash of the provided message.
     *
     * @param message the input message as a string
     * @return the SHA3-256 hash of the message as a BigInteger
     * @throws NoSuchAlgorithmException if the SHA3-256 algorithm is not available
     */
    private static BigInteger getMessageSha3Hash(String message) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));
        return new BigInteger(1, hashbytes);
    }

}
