package rsa;

import mathMethods.MathMethods;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.math.BigInteger.TWO;
import static mathMethods.MathMethods.generateRandomPrime;

public class MethodenFromRSA {
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static BigInteger a;
    private static BigInteger b;
    public static BigInteger calculateE(BigInteger phiN, BigInteger m, int millerRabinSteps){
        BigInteger e = BigInteger.valueOf(65537);
        if (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(ONE)) {
            e = BigInteger.valueOf(3);
            if (!e.gcd(phiN).equals(ONE)) {
                BigInteger upperBoundForE = phiN.subtract(ONE); // e must be less than phiN
                do {
                    e = generateRandomPrime(m, TWO, upperBoundForE, millerRabinSteps);
                } while (!e.gcd(phiN).equals(ONE));
            }
        }
        return e;
    }
    private static List<Integer> convertTextToUnicode(String message) {
        // Implementation of text to Unicode conversion
        return MathMethods.convertTextToUniCode(message);
    }
    public static String sign(String message, BigInteger d, BigInteger n) throws NoSuchAlgorithmException {
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
    public static String encrypt(String message, BigInteger e, BigInteger n, int numberSystemBase, int blockSize) {
        // Step 1: Convert text to Unicode
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        List<Integer> unicodeMessage = convertTextToUnicode(message);
        //Unicode values should not be equal or larger than numberSystemBase
        for(int i = 0; i < unicodeMessage.size(); i++){
            if(unicodeMessage.get(i) >= numberSystemBase){
                throw new IllegalArgumentException("Unicode value is equal or larger than numberSystemBase");
            }
        }
        // Step 2: Prepare message for encryption (Block cipher)
        List<BigInteger> numericMessage = prepareMessageForEncryption(unicodeMessage, numberSystemBase, blockSize);
        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = encryptNumericBlocks(numericMessage, e, n);
        // Step 4: Process the encrypted blocks into a string representation

        return processEncryptedBlocksToString(encryptedBlocks, numberSystemBase, blockSize);
    }

    private static List<BigInteger> prepareMessageForEncryption(List<Integer> unicodeMessage, int numberSystemBase, int blockSize) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
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

    public static boolean verifySignature(String message, String signature, BigInteger e, BigInteger n) throws NoSuchAlgorithmException {
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

    private static String processEncryptedBlocksToString(List<BigInteger> encryptedBlocks, int numberSystemBase, int blockSize) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        // Implementation of processing encrypted blocks into string
        StringBuilder encryptedNumericMessageStr = new StringBuilder();
        for (BigInteger block : encryptedBlocks) {
            encryptedNumericMessageStr.append(convertBlockToString(block, numberSystemBase, blockSize));
        }
        return encryptedNumericMessageStr.toString();
    }
    private static List<Integer> convertBlockToNumberList(BigInteger block, int numberSystemBase, int blockSize) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        // Convert a single encrypted block into a list of numbers
        List<Integer> numberList = new ArrayList<>();
        int count = 0;
        while (!block.equals(ZERO)) {
            numberList.add(0, block.mod(BigInteger.valueOf(numberSystemBase)).intValue());
            block = block.divide(BigInteger.valueOf(numberSystemBase));
            count++;
        }
        int blockSizePlusOne = blockSize + 1;
        while (count < blockSizePlusOne) {
            numberList.add(0, 0);
            count++;
        }
        return numberList;
    }
    private static String convertBlockToString(BigInteger block, int numberSystemBase, int blockSize) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        // Convert a single encrypted block into a string representation
        List<Integer> tempList = convertBlockToNumberList(block, numberSystemBase, blockSize);
        return MathMethods.convertUniCodeToText(tempList);
    }
    public static BigInteger calculateD(BigInteger e, BigInteger phiN){
        return MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);
    }

    public static BigInteger calculatePrimeByBitLength(BigInteger primeBitLength, BigInteger m, int millerRabinSteps){
        return calculatePrimeByBitLength(primeBitLength, m, millerRabinSteps, ONE);
    }

    public static BigInteger calculatePrimeByBitLength(BigInteger primeBitLength,BigInteger m,  int millerRabinSteps, BigInteger shouldBeDifferentTo) {
        if (primeBitLength.bitLength() > 31) {
            throw new IllegalArgumentException("Bit length is too large to handle");
        }

        // Convert BigInteger bit length to int, assuming it's within int range
        int bitLength = primeBitLength.intValue();

        BigInteger a = TWO.pow(bitLength - 1);
        BigInteger b = TWO.pow(bitLength);

        // Parallel prime generation
        return IntStream.range(0, Runtime.getRuntime().availableProcessors())
                .parallel()
                .mapToObj(i -> generateRandomPrime(m, a, b, millerRabinSteps))
                .filter(possiblePrime -> !possiblePrime.equals(shouldBeDifferentTo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prime not found"));
    }

    public static String decrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n, int blockSize, int numberSystemBase) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(encryptedNumericMessageStr);
        // Step 2: Create encrypted blocks from Unicode
        List<List<BigInteger>> encryptedBlocks = createEncryptedBlocksFromUnicode(unicodeMessage, blockSize);
        // Step 3: Convert the encrypted blocks to BigInteger format
        List<BigInteger> encryptedNumericMessages = convertBlocksToBigIntegers(encryptedBlocks, numberSystemBase);
        // Step 4: Decrypt the numeric representation
        List<BigInteger> numericMessage = decryptNumericMessages(encryptedNumericMessages, d, n);
        // Step 5: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = convertNumericMessageToIntegers(numericMessage, blockSize, numberSystemBase);
        // Step 6: Convert the list of integers to a string
        String decryptedMessageStr = convertIntegersToText(decryptedMessage);
        // Step 7: Remove padding from the decrypted string
        return removePadding(decryptedMessageStr);
    }

    private static List<Integer> convertNumericMessageToIntegers(List<BigInteger> numericMessage, int blockSize, int numberSystemBase) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        List<Integer> decryptedMessage = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            decryptedMessage.addAll(MathMethods.prepareMessageForDecryption(block, blockSize, numberSystemBase));
        }
        return decryptedMessage;
    }

    private static List<BigInteger> decryptNumericMessages(List<BigInteger> encryptedNumericMessages, BigInteger d, BigInteger n) {
        List<BigInteger> numericMessage = new ArrayList<>();
        for (BigInteger block : encryptedNumericMessages) {
            numericMessage.add(MathMethods.alternativeQuickExponentiation(block, d, n));
        }
        return numericMessage;
    }

    private static String convertIntegersToText(List<Integer> decryptedMessage) {
        return MathMethods.convertUniCodeToText(decryptedMessage);
    }

    private static String removePadding(String decryptedMessageStr) {
        int paddingIndex = decryptedMessageStr.indexOf(0);
        return paddingIndex >= 0 ? decryptedMessageStr.substring(0, paddingIndex) : decryptedMessageStr;
    }

    private static List<BigInteger> convertBlocksToBigIntegers(List<List<BigInteger>> encryptedBlocks, int numberSystemBase) {
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

    private static List<List<BigInteger>> createEncryptedBlocksFromUnicode(List<Integer> unicodeMessage, int blockSize) {
        if(blockSize<=0) throw new IllegalArgumentException("Blocksize can't be lower than 1");
        List<List<BigInteger>> encryptedBlocks = new ArrayList<>();
        int blockSizePlusOne = blockSize + 1;
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
}
