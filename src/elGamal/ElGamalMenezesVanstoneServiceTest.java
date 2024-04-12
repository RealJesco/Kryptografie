package elGamal;

import FiniteFieldEllipticCurve.*;
import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import blockChiffre.FromDecimalBlockChiffre;
import blockChiffre.ToDecimalBlockChiffre;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalMenezesVanstoneServiceTest {

    @Test
    void encrypt() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        PrivateKey privateKey = new PrivateKey(ellipticCurve, BigInteger.valueOf(80));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, privateKey);

        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void testKeyGeneration() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

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

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }
    @Test
    void fullTextCycle() {
        //TODO This is how you can test the full cycle of encryption and decryption. This needs to be automated as a service.
        BigInteger bitLengthP = BigInteger.valueOf(128);
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

//        String text = "Hello, World! This is a test of a really long text! do you like it? I hope so!";
        String text = "Ao0WDF!M57XkWm%ysCw1dUw0FoJ31tChJ1ajJ&NN2N2HuektYRJ703q20PYBjkGf4Shw0@GH42$Qpf!C6&UMU6uh94wyVuaQpEdJ\n" +
                "D*rQ@!ojFvUaH%T&FQ=#fAqSZV9pXZFqz9jHuCj&AuZned3&f#=HTFtcPp#ms&fvEyFNtaoMtzN4X2&g!oy84d#Rd@@nTbG9*aeW\n" +
                "Tx3@Dt5#=AVkS1aP#h@j+MPpF%=Y*s$39eYu=htZfsro0yJPwTjywSf7b8=T8O2evWQThMpemq7@GFjhYn*XZTaQA@PjGRKE%*XS\n" +
                "bH$KzS$%%=TYHbDuyHaU3EKj2emwN3QrBpPNtBvCGYp&nd*gza%vXcgyKSpCb4%E$xGtBrWetsudsMKU$@Sttg5&s4zZ@@DSYp%t\n" +
                "bo%1TCYHdh2rCh!J#qAAU2hBO7d6CaEotm7Ju!y1RQAPWzQG9K$FT#GJJCcQKBNUJzQw3w4Upo+Eov%P@V@v@E23OAm3m%kUuRvJ";

        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage = ElGamalMenezesVanstoneStringService.encrypt(keyPair.publicKey, text, 55296, ellipticCurve);
        String encryptedText = elGamalMenezesVanstoneCipherMessage.getCipherMessageString();

        String decryptedText = ElGamalMenezesVanstoneStringService.decrypt(keyPair.privateKey, elGamalMenezesVanstoneCipherMessage, 55296, ellipticCurve);

        assertEquals(text, decryptedText);
        assertNotEquals(text, encryptedText);
    }
}