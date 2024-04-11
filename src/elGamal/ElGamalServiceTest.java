package elGamal;

import FiniteFieldEllipticCurve.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalServiceTest {

    @Test
    void encrypt() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        PrivateKey privateKey = new PrivateKey(ellipticCurve, BigInteger.valueOf(80));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, privateKey);

        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void testKeyGeneration() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void testKeyGenerationWithSecureEllipticCurve() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }
    @Test
    void decrypt() {
    }
}