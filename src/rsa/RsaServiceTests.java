package rsa;

import mathMethods.MathMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.security.PublicKey;

class RsaServiceTests {

    @Test
    void generateKeyPair() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(87));
        Assertions.assertNotNull(keyPair);
        Assertions.assertEquals(keyPair.privateKeyRsa().n(), keyPair.publicKeyRsa().n());
        Assertions.assertNotEquals(keyPair.privateKeyRsa().d(), keyPair.publicKeyRsa().e());
    }

    @Test
    void encrypt() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(87));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger encryptedMessage = RsaService.encrypt(keyPair.publicKeyRsa() ,message);
        Assertions.assertNotEquals(message, encryptedMessage);
    }

    @Test
    void decrypt() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger encryptedMessage = RsaService.encrypt(keyPair.publicKeyRsa(), message);
        BigInteger decryptedMessage = RsaService.decrypt(keyPair.privateKeyRsa(), encryptedMessage);
        Assertions.assertEquals(message, decryptedMessage);
    }

    @Test
    void sign() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger signedMessage = RsaService.sign(keyPair.privateKeyRsa(), message);
        Assertions.assertNotEquals(message, signedMessage);
    }

    @Test
    void verify() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger signature = RsaService.sign(keyPair.privateKeyRsa(), message);
        Assertions.assertTrue(RsaService.verify(keyPair.publicKeyRsa(), message, signature));
    }
}