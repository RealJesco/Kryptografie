package rsa;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    @Test
    public void encryptionAndDecryptionShouldReturnOriginalMessage() {
        RSA rsa = new RSA(40, 1024, 200, BigInteger.valueOf(100));
        RSA.generatePrimeNumbers();
        String originalMessage = "Mathematik ist spannend";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void encryptionAndDecryptionWithEmptyMessageShouldReturnEmptyString() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(10);
        RSA.setM(BigInteger.valueOf(100));
        RSA.generatePrimeNumbers();
        String originalMessage = "";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void signatureVerificationShouldReturnTrueForValidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(200);
        RSA.setM(BigInteger.valueOf(100));
        RSA.generatePrimeNumbers();
        String message = "Hello, World!";
        String signature = RSA.sign(message);
        assertTrue(RSA.verifySignature(message, signature));
    }

    @Test
    public void signatureVerificationShouldReturnFalseForInvalidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(10);
        RSA.setM(BigInteger.valueOf(100));
        RSA.generatePrimeNumbers();
        String message = "Hello, World!";
        String invalidSignature = "InvalidSignature";
        // Convert to hex presentation
        invalidSignature = new BigInteger(invalidSignature.getBytes()).toString(16);
        assertFalse(RSA.verifySignature(message, invalidSignature));
    }
    //Test if non-hex presentation throws exception
    @Test
    public void signatureVerificationShouldThrowExceptionForNonHexPresentation() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(10);
        RSA.setM(BigInteger.valueOf(100));
        RSA.generatePrimeNumbers();
        String message = "Hello, World!";
        String invalidSignature = "InvalidSignature";
        assertThrows(IllegalArgumentException.class, () -> RSA.verifySignature(message, invalidSignature));
    }
}
