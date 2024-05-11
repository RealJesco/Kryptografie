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

    @Test
    public void testGenerateKandKy() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        BigInteger q = ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(BigInteger.valueOf(8));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement, q);
        BigInteger qSubtractONE = q.subtract(BigInteger.ONE);

        Pair<BigInteger, EllipticCurvePoint> result = ElGamalMenezesVanstoneService.generateKandKy(publicKey, qSubtractONE);

        assertNotNull(result.getKey());
        assertTrue(result.getKey().compareTo(BigInteger.ZERO) != 0);

        assertNotNull(result.getValue());
        assertFalse(result.getValue() instanceof InfinitePoint);
    }

    //TODO: Seems to loop indefinitely sometimes
    @Test
    void fullTextCycle() {
        BigInteger bitLengthP = BigInteger.valueOf(128);
        double time = System.currentTimeMillis();
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(120), 10, BigInteger.valueOf(13));
        System.out.println("passed time: " + (System.currentTimeMillis() - time));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
        System.out.println("Generation complete!");

        String text = "Ao0WDF!M57XkWm%ysCw1dUw0FoJ31tChJ1ajJ&NN2N2HuektYRJ703q20PYBjkGf4Shw0@GH42$Qpf!C6&UMU6uh94wyVuaQpEdJ\n";

        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage = ElGamalMenezesVanstoneStringService.encrypt(keyPair.getPublicKey(), text, 55296);
        String encryptedText = elGamalMenezesVanstoneCipherMessage.getCipherMessageString();

        String decryptedText = ElGamalMenezesVanstoneStringService.decrypt(keyPair.getPrivateKey(), elGamalMenezesVanstoneCipherMessage, 55296);

        assertEquals(text, decryptedText);
        assertNotEquals(text, encryptedText);
    }

    @Test
    void testSignAndVerify() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(32), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
        BigInteger message = BigInteger.valueOf(123456789);

        MenezesVanstoneSignature signature = ElGamalMenezesVanstoneService.sign(keyPair, message);

        assertTrue(signature.r().compareTo(BigInteger.ZERO) > 0);
        assertTrue(signature.s().compareTo(BigInteger.ZERO) > 0);
        boolean verified = ElGamalMenezesVanstoneService.verify(keyPair.getPublicKey(), message, signature);

        assertTrue(verified);
    }
}