package elGamal;

import FiniteFieldEllipticCurve.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalEncryptionTest {

    @Test
    void encrypt() {
        ElGamalEncryption elGamalEncryption = new ElGamalEncryption();
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        PrivateKey privateKey = new PrivateKey(ellipticCurve, BigInteger.valueOf(80));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = elGamalEncryption.encrypt(message, publicKey);

        Message decryptedMessage = elGamalEncryption.decrypt(cipherMessage, privateKey);

        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void decrypt() {
    }
}