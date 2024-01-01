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
    public static int getBlockSize(){
        return blockSize;
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
        double sqrt = Math.sqrt(m.intValue());
        if(sqrt == Math.floor(sqrt)){
            throw new IllegalArgumentException("Cubic-numbers aren't allowed here");
        }
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

    /**
     * This method is used to generate and set the prime number 'P'
     * of the RSA Key pair
     *
     * @param halfBitLength The half bit length of N.
     */
    public static void generateP(BigInteger halfBitLength){
        p = generateUniquePrime(halfBitLength);
    }

    /**
     * This method is used to generate and set the prime number 'Q'
     * of the RSA Key pairs
     *
     * @param halfBitLength The half bit length of N.
     */
    public static void generateQ(BigInteger halfBitLength){
        q = generateUniquePrime(halfBitLength);
    }

    /**
     * This private helper method helps to generate a unique prime number
     * for the RSA Key pair.
     *
     * It generates a random prime number in the range of the provided bit length.
     * If the generated prime number is equal to the provided BigInteger to compare with,
     * the process will be repeated until a different prime number is generated.
     * It no longer needs to check for equality with the other prime number, since a
     * global counter is used to ensure that the displacement in the seed of randomElsner can never be the same at runtime.
     *
     * @param bitLength The bit length of the prime number to generate.
     * @return BigInteger A prime number
     */
    private static BigInteger generateUniquePrime(BigInteger bitLength) {
        BigInteger possiblePrime;
        BigInteger lowerBound = TWO.pow(bitLength.intValue() - 1);
        BigInteger upperBound = TWO.pow(bitLength.intValue());
            possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps);
        return possiblePrime;
    }


    /**
     * Generates RSA keys.
     * <p>
     * This method generates RSA keys by calculating the values of p, q, n, phiN, e, and d
     * based on the given bit length and number system base.
     * </p>
     */
    private static void keyGenerator(){
        // Calculate bit length of p and q
        int bitLengthPQ = bitLengthN / 2;
        do{
            generateP(BigInteger.valueOf(bitLengthPQ));
            if(bitLengthN % 2 == 1){
                generateQ(BigInteger.valueOf(bitLengthPQ+1));
            } else {
                generateQ(BigInteger.valueOf(bitLengthPQ));
            }
            calculateN(p, q);
        } while(n.bitLength()!=bitLengthN);
        calculatePhiN(p, q);
        calculateE(phiN);
        calculateD(e, phiN);
    }



    /**
     * Generates RSA keys.
     *
     * This method generates RSA keys by calculating the values of p, q, n, phiN, e, and d
     * based on the given bit length and number system base.
     */
    public static void generateRSAKeys() {
      keyGenerator();
    }

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
        keyGenerator();
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
     * Prepares a message for encryption by converting a list of Unicode values to a list of BigIntegers.
     *
     * @param unicodeMessage The list of Unicode values representing the message.
     * @return A list of BigIntegers representing the prepared message for encryption.
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
        // Implementation of encrypting numeric blocks
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
        // Implementation of processing encrypted blocks into string
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
        // Convert a single encrypted block into a string representation
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

    /**
     * Decrypts a given encrypted numeric message using the RSA algorithm.
     *
     * @param encryptedNumericMessageStr The encrypted message to be decrypted.
     * @param d                          The private exponent.
     * @param n                          The modulus.
     * @return The decrypted message.
     */
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
                } else {
                    block.add(ZERO);
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
            BigInteger sum = ZERO;
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

    /**
     * Removes padding from a decrypted message.
     *
     * @param decryptedMessageStr The decrypted message string.
     * @return The decrypted message without padding.
     */
    private static String removePadding(String decryptedMessageStr) {
        int paddingIndex = decryptedMessageStr.indexOf(0);
        return paddingIndex >= 0 ? decryptedMessageStr.substring(0, paddingIndex) : decryptedMessageStr;
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

        // Convert the encrypted hash to a hex string (or you can use another format as needed)
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
        // This should refer to class fields or constants, but I'm going to skip this part
        // since the full context isn't available
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
        // Hash the message using SHA3-256
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        // Convert the hashed bytes to a BigInteger
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
    // For situations where no public exponent and modulus parameters exist
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
