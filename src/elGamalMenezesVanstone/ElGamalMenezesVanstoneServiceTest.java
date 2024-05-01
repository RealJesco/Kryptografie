package elGamalMenezesVanstone;

import FiniteFieldEllipticCurve.*;
import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalMenezesVanstoneServiceTest {

    /**
     * Test the encryption and decryption of a message with random values
     */
    @Test
    void encrypt() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        PrivateKey privateKey = new PrivateKey(ellipticCurve, BigInteger.valueOf(80));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        BigInteger q = ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(BigInteger.valueOf(8));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement, q);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, privateKey);

        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    /**
     * Testing successful generation of a key pair
     * Test the encryption and decryption of a message with random values
     * random values
     */
    @Test
    void testKeyGeneration() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
//        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    /**
     * Testing successful generation of a key pair
     * Test the encryption and decryption of a message with random values
     * random values
     */
    @Test
    void testKeyGenerationWithSecureEllipticCurve() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    /**
     * Testing successful generation of a key pair
     * Test the encryption and decryption of a message with random values
     * random values
     * test with actual string content but random string
     */
    @Test
    void fullTextCycle() {
        BigInteger bitLengthP = BigInteger.valueOf(512);
        double time = System.currentTimeMillis();
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(120500), 10, BigInteger.valueOf(13));
        System.out.println("passed time: " + (System.currentTimeMillis() - time));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);

//        String text = "Hello, World! This is a test of a really long text! do you like it? I hope so!";
        String text = "Ao0WDF!M57XkWm%ysCw1dUw0FoJ31tChJ1ajJ&NN2N2HuektYRJ703q20PYBjkGf4Shw0@GH42$Qpf!C6&UMU6uh94wyVuaQpEdJ\n" +
                "D*rQ@!ojFvUaH%T&FQ=#fAqSZV9pXZFqz9jHuCj&AuZned3&f#=HTFtcPp#ms&fvEyFNtaoMtzN4X2&g!oy84d#Rd@@nTbG9*aeW\n" +
                "Tx3@Dt5#=AVkS1aP#h@j+MPpF%=Y*s$39eYu=htZfsro0yJPwTjywSf7b8=T8O2evWQThMpemq7@GFjhYn*XZTaQA@PjGRKE%*XS\n" +
                "bH$KzS$%%=TYHbDuyHaU3EKj2emwN3QrBpPNtBvCGYp&nd*gza%vXcgyKSpCb4%E$xGtBrWetsudsMKU$@Sttg5&s4zZ@@DSYp%t\n" +
                "bo%1TCYHdh2rCh!J#qAAU2hBO7d6CaEotm7Ju!y1RQAPWzQG9K$FT#GJJCcQKBNUJzQw3w4Upo+Eov%P@V@v@E23OAm3m%kUuRvJ";

        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage = ElGamalMenezesVanstoneStringService.encrypt(keyPair.publicKey, text, 55296);
        String encryptedText = elGamalMenezesVanstoneCipherMessage.getCipherMessageString();

        String decryptedText = ElGamalMenezesVanstoneStringService.decrypt(keyPair.privateKey, elGamalMenezesVanstoneCipherMessage, 55296);

        assertEquals(text, decryptedText);
        assertNotEquals(text, encryptedText);
    }

    /**
     * Testing successful generation of a key pair
     * Test the encryption and decryption of a message with random values
     * random values
     * test with sign and verify
     */
    @Test
    void testSignAndVerifyWithText() throws NoSuchAlgorithmException {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);

        System.out.println(keyPair.publicKey.generator().multiply(keyPair.privateKey.secretMultiplierX(), keyPair.publicKey.ellipticCurve()));
        System.out.println(keyPair.publicKey.groupElement());

        System.out.println(keyPair);
        String message = "Hello, World! This is a test of a really long text! do you like it? I hope so!";

        String signature = ElGamalMenezesVanstoneStringService.sign(keyPair, message, 55296);

        System.out.println(signature);

        boolean verified = ElGamalMenezesVanstoneStringService.verify(keyPair.publicKey, message, signature, 55296);

        assertTrue(verified);

        message = message.substring(0, message.length() - 1);
        verified = ElGamalMenezesVanstoneStringService.verify(keyPair.publicKey, message, signature, 55296);
        assertFalse(verified);
    }

    /**
     * Testing successful generation of a key pair
     * Test the encryption and decryption of a message with random values
     * random values
     * test with sign and verify without content string
     */
    @Test
    void testSignAndVerify() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
        BigInteger message = BigInteger.valueOf(123456789);

        MenezesVanstoneSignature signature = ElGamalMenezesVanstoneService.sign(keyPair, message);

        assertTrue(signature.r().compareTo(BigInteger.ZERO) > 0);
        assertTrue(signature.s().compareTo(BigInteger.ZERO) > 0);
        boolean verified = ElGamalMenezesVanstoneService.verify(keyPair.publicKey, message, signature);

        assertTrue(verified);
    }

}