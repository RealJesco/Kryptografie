package test.rsa;

import main.rsa.KeyPairRsa;
import main.rsa.RsaService;
import main.rsa.RsaStringService;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class RsaStringServiceTest {
    @Test
    public void fullEncryptDecryptString() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        String message = "Mathematik is spannend!";
        String encryptedMessage = RsaStringService.encrypt(keyPair.publicKeyRsa(), message, 55296);
        assertNotEquals(message, encryptedMessage);
        String decryptedMessage = RsaStringService.decrypt(keyPair.privateKeyRsa(), encryptedMessage, 55296);
        assertEquals(message, decryptedMessage);
    }

    @Test
    public void sign() throws NoSuchAlgorithmException {
        KeyPairRsa keyPair = RsaService.generateKeyPair(512, 100, BigInteger.valueOf(13));
        String message = "Høllå die Walȡféè ㆕";
        String signature = RsaStringService.sign(keyPair.privateKeyRsa(), message, 55296);
        assertNotEquals(message, signature);
    }

    @Test
    public void verify() throws NoSuchAlgorithmException {
        KeyPairRsa keyPair = RsaService.generateKeyPair(512, 100, BigInteger.valueOf(13));
        String message = "Høllå die Walȡféè ㆕";
        String signature = RsaStringService.sign(keyPair.privateKeyRsa(), message, 55296);
        assertTrue(RsaStringService.verify(keyPair.publicKeyRsa(), message, signature, 55296));
        assertFalse(RsaStringService.verify(keyPair.publicKeyRsa(), message.concat(" Ewa"),signature, 55296));
        assertFalse(RsaStringService.verify(keyPair.publicKeyRsa(), message,signature, 55295));
        assertFalse(RsaStringService.verify(keyPair.publicKeyRsa(), message.substring(0, message.length() - 2),signature, 55295));
    }
}
