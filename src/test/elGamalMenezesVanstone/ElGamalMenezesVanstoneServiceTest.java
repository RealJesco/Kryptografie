package test.elGamalMenezesVanstone;

import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneService;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.elGamalMenezesVanstone.KeyPair;
import main.elGamalMenezesVanstone.records.MenezesVanstoneSignature;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.finiteFieldEllipticCurve.*;
import main.resource.Resource;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ElGamalMenezesVanstoneServiceTest {
    @Test
    public void testGenerateUniquePrime() {
        BigInteger bitLength = BigInteger.valueOf(16);
        int millerRabinSteps = 5;
        BigInteger m = BigInteger.valueOf(123456);
        AtomicInteger counter = new AtomicInteger(0);

        BigInteger uniquePrime = ElGamalMenezesVanstoneService.generateUniquePrime(bitLength, millerRabinSteps, m, counter);

        BigInteger lowerBound = Resource.FIVE;
        BigInteger upperBound = BigInteger.valueOf(2).pow(bitLength.intValue()).subtract(BigInteger.ONE);
        assertTrue(uniquePrime.compareTo(lowerBound) >= 0 && uniquePrime.compareTo(upperBound) <= 0);
    }

    /**
     * test for the generation of k and ky
     * k is a random number in the range of 1 to q-1
     * ky is the group element multiplied by k
     * @expected: k is not null
     * @expected: k is not zero
     * @expected: ky is not null
     * @expected: ky is not an infinite point
     */
    @Test
    public void testGenerateKandKy() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 10, BigInteger.valueOf(13));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, BigInteger.valueOf(13));
        EllipticCurvePoint generator = keyPair.getPublicKey().generator();
        EllipticCurvePoint groupElement = keyPair.getPublicKey().groupElement();
        BigInteger q = keyPair.getPublicKey().order();
        PublicKey publicKey = keyPair.getPublicKey();

        Pair<BigInteger, EllipticCurvePoint> result = ElGamalMenezesVanstoneService.generateKandKy(publicKey);

        assertNotNull(result.getKey());
        assertTrue(result.getKey().compareTo(BigInteger.ZERO) != 0);

        assertNotNull(result.getValue());
        assertFalse(result.getValue() instanceof InfinitePoint);
    }

    /**
     * test for entire encryption and decryption cycle
     * @expected: decrypted text is equal to the original text
     * @expected: encrypted text is not equal to the original text
     */

    @Test
    void fullTextCycle() {
        BigInteger m = BigInteger.valueOf(13);
        BigInteger bitLengthP = BigInteger.valueOf(128);
        double time = System.currentTimeMillis();
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(120), 10, m);
        System.out.println("passed time: " + (System.currentTimeMillis() - time));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);
        System.out.println("Generation complete!");

        String text = "Ao0WDF!M57XkWm%ysCw1dUw0FoJ31tChJ1ajJ&NN2N2HuektYRJ703q20PYBjkGf4Shw0@GH42$Qpf!C6&UMU6uh94wyVuaQpEdJ\n";

        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage = ElGamalMenezesVanstoneStringService.encrypt(keyPair.getPublicKey(), text, 55296);
        String encryptedText = elGamalMenezesVanstoneCipherMessage.getCipherMessageString();

        String decryptedText = ElGamalMenezesVanstoneStringService.decrypt(keyPair.getPrivateKey(), elGamalMenezesVanstoneCipherMessage, 55296);

        assertEquals(text, decryptedText);
        assertNotEquals(text, encryptedText);
    }

    /**
     * test for the signing and verifying of a message
     * @expected: signature r is greater than 0
     * @expected: signature s is greater than 0
     * @expected: the signature is verified
     */
    @Test
    void testSignAndVerify() {
        BigInteger m = BigInteger.valueOf(13);
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100,m);
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);
        BigInteger message = BigInteger.valueOf(123456789);

        MenezesVanstoneSignature signature = ElGamalMenezesVanstoneService.sign(keyPair, message);

        assertTrue(signature.r().compareTo(BigInteger.ZERO) > 0);
        assertTrue(signature.s().compareTo(BigInteger.ZERO) > 0);
        boolean verified = ElGamalMenezesVanstoneService.verify(keyPair.getPublicKey(), message, signature);

        assertTrue(verified);
    }
}
