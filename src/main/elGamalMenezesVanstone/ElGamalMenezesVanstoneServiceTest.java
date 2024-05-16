package main.elGamalMenezesVanstone;

//TODO: Klasse entfernen, wenn die Tests im test-package fertig sind!

import main.elGamalMenezesVanstone.records.CipherMessage;
import main.elGamalMenezesVanstone.records.Message;
import main.elGamalMenezesVanstone.records.PrivateKey;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.finiteFieldEllipticCurve.*;
import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalMenezesVanstoneServiceTest {

    /**
     * elliptic curve with prime p = 5 and a = 821
     * private key with secret multiplier x = 80
     * generator point with x = 115 and y = 253
     * group element with x = 575 and y = 481
     * q = order of the elliptic curve
     * public key with elliptic curve, generator, group element and q
     * message with m1 = 3 and m2 = 8
     * @expected: the decrypted message m1 equals the original message m1
     * @expected: the decrypted message m2 is equal to the original message m2
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
     * secure elliptic curve
     * key pair generation
     * message of big integer tuple (3,8)
     * @expected: the encrypted message part b1 is NOT equal to the original message part m1
     * @expected: the encrypted message part b2 is not equal to the original message part m2
     * @expected: the decrypted message m1 equals the original message m1
     * @expected: the decrypted message m2 is not equal to the original message m2
     */
    @Test
    void testKeyGeneration() {
        BigInteger m = BigInteger.valueOf(13);
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
//        main.FiniteFieldEllipticCurve ellipticCurve = new main.FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    /**
     * secure elliptic curve
     * key pair generation
     * message of big integer tuple (3,8)
     * @expected: the encrypted message part b1 is NOT equal to the original message part m1
     * @expected: the encrypted message part b2 is not equal to the original message part m2
     * @expected: the decrypted message m1 equals the original message m1
     * @expected: the decrypted message m2 is not equal to the original message m2
     */
    @Test
    void testKeyGenerationWithSecureEllipticCurve() {
        BigInteger m = BigInteger.valueOf(13);
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    /**
     * bitLength: bitlength of prime for main.encryption -> 128
     * secure elliptic curve -> safe elliptic curve
     * key pair generation
     * message string with random content
     * @expected: the decrypted message equals the original message
     * @expected: the encrypted message is not equal to the original message
     */
    @Test
    void fullTextCycle() {
        BigInteger m = BigInteger.valueOf(13);
        BigInteger bitLengthP = BigInteger.valueOf(128);
        double time = System.currentTimeMillis();
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(120500), 10, m);
        System.out.println("passed time: " + (System.currentTimeMillis() - time));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);

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

    //TODO: Does this run very long or indefinitely?
//    /**
//     * secure elliptic curve
//     * key pair generation
//     * message string -> "Hello, World! This is a test of a really long text! do you like it? I hope so!"
//     * test with sign and verify
//     * @expected: the signature can be verified
//     * @expected: the signature can not be verified after changing the message
//     */
//    @Test
//    void testSignAndVerifyWithText() throws NoSuchAlgorithmException {
//        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
//        KeyPair keyPair = new KeyPair();
//        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
//
//        System.out.println(keyPair.publicKey.generator().multiply(keyPair.privateKey.secretMultiplierX(), keyPair.publicKey.ellipticCurve()));
//        System.out.println(keyPair.publicKey.groupElement());
//
//        System.out.println(keyPair);
//        String message = "Hello, World! This is a test of a really long text! do you like it? I hope so!";
//
//        String signature = ElGamalMenezesVanstoneStringService.sign(keyPair, message, 55296);
//
//        System.out.println(signature);
//
//        boolean verified = ElGamalMenezesVanstoneStringService.verify(keyPair.publicKey, message, signature, 55296);
//
//        assertTrue(verified);
//
//        message = message.substring(0, message.length() - 1);
//        verified = ElGamalMenezesVanstoneStringService.verify(keyPair.publicKey, message, signature, 55296);
//        assertFalse(verified);
//    }

    //TODO: Does this run very long or indefinitely?
//    /**
//     * secure elliptic curve
//     * key pair generation
//     * message in form of a big integer
//     * @expected: signature parameter r > 0
//     * @expected: signature parameter s > 0
//     * @expected: the signature can be verified
//     */
//    @Test
//    void testSignAndVerify() {
//        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
//        KeyPair keyPair = new KeyPair();
//        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
//        BigInteger message = BigInteger.valueOf(123456789);
//
//        MenezesVanstoneSignature signature = ElGamalMenezesVanstoneService.sign(keyPair, message);
//
//        assertTrue(signature.r().compareTo(BigInteger.ZERO) > 0);
//        assertTrue(signature.s().compareTo(BigInteger.ZERO) > 0);
//        boolean verified = ElGamalMenezesVanstoneService.verify(keyPair.publicKey, message, signature);
//
//        assertTrue(verified);
//    }
}