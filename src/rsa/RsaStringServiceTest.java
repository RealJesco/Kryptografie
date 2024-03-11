package rsa;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class RsaStringServiceTest {

    @Test
    void fullEncryptDecryptString() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(87));
        String message = "Mathematik is spannend!";
        String encryptedMessage = RsaStringService.encrypt(keyPair.publicKeyRsa(), message, 55296);
        assertNotEquals(message, encryptedMessage);
        String decryptedMessage = RsaStringService.decrypt(keyPair.privateKeyRsa(), encryptedMessage, 55296);
        assertEquals(message, decryptedMessage);
    }

    @Test
    void sign() {
    }

    @Test
    void verify() {
    }
}