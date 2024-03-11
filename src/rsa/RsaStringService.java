package rsa;

import blockChiffre.FromDecimalBlockChiffre;
import blockChiffre.ToDecimalBlockChiffre;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public final class RsaStringService {

    public static String encrypt(final PublicKeyRsa key, final String message, int numberBase){
        int blockSize = (int) (key.n().bitLength() * (Math.log(2) / Math.log(numberBase)));
        // BlockChiffre toDecimal Encrypt
        List<BigInteger> encryptedBlockCipher = ToDecimalBlockChiffre.encrypt(message, numberBase, blockSize);
        // Für jeden Block einzeln RSA.encrypt aufrufen
        List<BigInteger> rsaEncryptedBlocks = new ArrayList<>();
        for (int i = 0; i < encryptedBlockCipher.size(); i++) {
            rsaEncryptedBlocks.add(RsaService.encrypt(key, encryptedBlockCipher.get(i)));
        }
        // BlockChiffre fromDecimal Encrypt
        return FromDecimalBlockChiffre.encrypt(rsaEncryptedBlocks, numberBase, blockSize);
    }

    public static String decrypt(final PrivateKeyRsa key, final String ciphertext, int numberBase) {
        int blockSize = (int) (key.n().bitLength() * (Math.log(2) / Math.log(numberBase)));
        // BlockChiffre fromDecimal Decrypt
        List<BigInteger> fromDecimalDecryptedBlocks = FromDecimalBlockChiffre.decrypt(ciphertext, numberBase, blockSize + 1);
        // Für jeden Block einzeln RSA.decrypt aufrufen
        List<BigInteger> rsaDecryptedCipherBlocks = new ArrayList<>();
        for (int i = 0; i < fromDecimalDecryptedBlocks.size(); i++) {
            rsaDecryptedCipherBlocks.add(RsaService.decrypt(key, fromDecimalDecryptedBlocks.get(i)));
        }
        // BlockChiffre toDecimal Decrypt
        return ToDecimalBlockChiffre.decrypt(rsaDecryptedCipherBlocks, numberBase);
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
    public static String sign(final PrivateKeyRsa key, final String message, int numberBase) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.n().bitLength() * (Math.log(2) / Math.log(numberBase)));
        String hashedMessage = hashAndConvertMessageToBigInteger(message).toString(16);
        List<BigInteger> encryptedBlockCipherBlocks = ToDecimalBlockChiffre.encrypt(hashedMessage, numberBase, blockSize);
        List<BigInteger> rsaEncryptedBlockCipherBlocks = new ArrayList<>();

        for (int i = 0; i < encryptedBlockCipherBlocks.size(); i++) {
            rsaEncryptedBlockCipherBlocks.add(RsaService.sign(key, encryptedBlockCipherBlocks.get(i)));
        }

        String signature = FromDecimalBlockChiffre.encrypt(rsaEncryptedBlockCipherBlocks, numberBase, blockSize + 1);

        return signature;
    }

    public static boolean verify(final PublicKeyRsa key, final String message, final String signature, int numberBase) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.n().bitLength() * (Math.log(2) / Math.log(numberBase)));
        String hashedMessage = hashAndConvertMessageToBigInteger(message).toString(16);
        List<BigInteger> encryptedMessageBlockCipherBlocks = ToDecimalBlockChiffre.encrypt(hashedMessage, numberBase, blockSize);
        List<BigInteger> decryptedBlockCipherSignature = FromDecimalBlockChiffre.decrypt(signature, numberBase, blockSize + 1);
        boolean signatureValid = false;
        for (int i = 0; i < encryptedMessageBlockCipherBlocks.size() ; i++) {
            if(RsaService.verify(key, decryptedBlockCipherSignature.get(i), encryptedMessageBlockCipherBlocks.get(i))){
               signatureValid = true;
            } else {
                signatureValid = false;
                break;
            }
        }

        return signatureValid;

    }
}
