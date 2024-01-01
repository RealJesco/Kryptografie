package rsa;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    @Test
    public void encryptionAndDecryptionShouldReturnOriginalMessage() {
        RSA rsa = new RSA(1000, 512, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "Mathematik ist spannend";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }
    //Test again
    @Test
    public void encryptionAndDecryptionShouldReturnOriginalMessageAgain() {
        RSA rsa = new RSA(40, 512, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "Hello, World!";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void encryptionAndDecryptionWithEmptyMessageShouldReturnEmptyString() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void signatureVerificationShouldReturnTrueForValidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String signature = RSA.sign(message);
        assertTrue(RSA.verifySignature(message, signature));
    }

    @Test
    public void signatureVerificationShouldReturnFalseForInvalidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String invalidSignature = "InvalidSignature";
        // Convert to hex presentation
        invalidSignature = new BigInteger(invalidSignature.getBytes()).toString(16);
        assertFalse(RSA.verifySignature(message, invalidSignature));
    }
    //Test if non-hex presentation throws exception
    @Test
    public void signatureVerificationShouldThrowExceptionForNonHexPresentation() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String invalidSignature = "InvalidSignature";
        assertThrows(IllegalArgumentException.class, () -> RSA.verifySignature(message, invalidSignature));
    }
    @Test
    public void illegalMValue() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(100)));
    }
    @Test
    public void bitLengthOfNIsOdd() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(257);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();

        assertEquals(RSA.getBitLengthN(), RSA.getN().bitLength());
    }

    //Speed tests
    @Test
    public void speedTestForEncryptionAndDecryption() {
        RSA rsa = new RSA(40, 512, 55926, BigInteger.valueOf(844));
//        RSA.setMillerRabinSteps(40);
//        RSA.setBitLengthN(1024);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        System.out.println("p: " + RSA.getP());
        System.out.println("q: " + RSA.getQ());
        System.out.println("n2: " + RSA.getN());
        System.out.println("e: " + RSA.getE());
        System.out.println("d: " + RSA.getD());
        String message = "Hello, World!";
        long start = System.nanoTime();
        String encryptedMessage = RSA.encrypt(message, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        System.out.println(decryptedMessage);
        System.out.println("Time needed to encrypt and decrypt: " + (System.nanoTime()-start) / 1000000 + " ms");
    }
    @Test
    public void speedTestForOnlyCalculatingP(){
        RSA.setMillerRabinSteps(100);
        RSA.setBitLengthN(512);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        long start = System.nanoTime();
        RSA.generateP(BigInteger.valueOf(RSA.getBitLengthN()/2));
        System.out.println("Time needed only to generate p: " + (System.nanoTime()-start) / 1000000 + " ms");
    }
}
