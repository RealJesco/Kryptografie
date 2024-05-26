package test.finiteFieldEllipticCurve;

import main.finiteFieldEllipticCurve.FiniteFieldEllipticCurve;
import main.finiteFieldEllipticCurve.SecureFiniteFieldEllipticCurve;
import main.resource.Resource;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SecureFiniteFieldEllipticCurveTest {
    /**
     * Test the creation of a secure finite field elliptic curve.
     * @expected: the value of a is equal to the expected value
     */
    @Test
    public void testSecureFiniteFieldEllipticCurve() {
        BigInteger bitLengthOfP = BigInteger.valueOf(128);
        BigInteger n = Resource.SEVEN;
        int millerRabinIterations = 5;
        BigInteger m = BigInteger.valueOf(2);
        SecureFiniteFieldEllipticCurve curve = new SecureFiniteFieldEllipticCurve(bitLengthOfP, n, millerRabinIterations, m);
        assertTrue(curve.getA().equals(n.multiply(n).negate()));
    }
}
