package test.rsa;

import main.rsa.KeyPairRsa;
import main.rsa.RsaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigInteger;

class RsaServiceTests {

    //TODO: Test generateUniquePrime, calculateE

    @Test
    public void generateKeyPair() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(87));
        Assertions.assertNotNull(keyPair);
        Assertions.assertEquals(keyPair.privateKeyRsa().n(), keyPair.publicKeyRsa().n());
        Assertions.assertNotEquals(keyPair.privateKeyRsa().d(), keyPair.publicKeyRsa().e());

        KeyPairRsa keyPair2 = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(87));
        Assertions.assertNotEquals(keyPair.privateKeyRsa().n(), keyPair2.privateKeyRsa().n());
    }

    @Test
    public void encrypt() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(87));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger encryptedMessage = RsaService.encrypt(keyPair.publicKeyRsa() ,message);
        Assertions.assertNotEquals(message, encryptedMessage);
    }

    @Test
    public void decrypt() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger encryptedMessage = RsaService.encrypt(keyPair.publicKeyRsa(), message);
        BigInteger decryptedMessage = RsaService.decrypt(keyPair.privateKeyRsa(), encryptedMessage);
        Assertions.assertEquals(message, decryptedMessage);
    }

    @Test
    public void sign() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger signedMessage = RsaService.sign(keyPair.privateKeyRsa(), message);
        Assertions.assertNotEquals(message, signedMessage);
    }

    @Test
    public void verify() {
        KeyPairRsa keyPair = RsaService.generateKeyPair(256, 100, BigInteger.valueOf(13));
        BigInteger message = BigInteger.valueOf(3213812);
        BigInteger signature = RsaService.sign(keyPair.privateKeyRsa(), message);
        Assertions.assertTrue(RsaService.verify(keyPair.publicKeyRsa(), signature, message));
    }
}
