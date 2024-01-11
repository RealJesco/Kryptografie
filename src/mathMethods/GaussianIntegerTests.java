package mathMethods;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;
import static mathMethods.MathMethods.extendedEuclideanInZi;
import static org.junit.jupiter.api.Assertions.*;

public class GaussianIntegerTests {

    @Test
    void testIsMultipleBIsZero() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(ZERO, ZERO);

        assertFalse(a.isMultiple(b));
    }

    @Test
    void isMultiple(){
        GaussianInteger a = new GaussianInteger(new BigInteger("2"), new BigInteger("2"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("4"));

        assertTrue(b.isMultiple(a));
    }
    @Test
    void isNotMultiple() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("1"));

        assertFalse(a.isMultiple(b));
    }
    @Test
    public void testNormalizeGCD_WithZeroRealPart() {
        GaussianInteger gaussianInteger = new GaussianInteger(ZERO, BigInteger.valueOf(5));

        GaussianInteger result = gaussianInteger.normalizeGCD();

        assertEquals(ZERO, result.imaginary);
        assertEquals(BigInteger.valueOf(5), result.real);
    }

    @Test
    void testAdd() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("1"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("7"), new BigInteger("6"));

        assertTrue(expected.equals(a.add(b)));
    }

    @Test
    void testDivide() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("2"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("2"), new BigInteger("0"));

        assertTrue(expected.equals(a.divide(b)));
    }

    @Test
    void testRoundHalfUp() {
        BigInteger number = new BigInteger("3");

        BigInteger expected = new BigInteger("3");

        assertEquals(expected, MathMethods.roundHalfUp(number));
    }

    @Test
    void testGaussianExtendedEuclideanInZi() {
        GaussianInteger a = new GaussianInteger(new BigInteger("5"), new BigInteger("4")); // 5 + 4i
        GaussianInteger b = new GaussianInteger(new BigInteger("7"), new BigInteger("3")); // 7 + 3i

        GaussianInteger gcd = extendedEuclideanInZi(a, b);
        System.out.println("GCD: " + gcd);

        // Assuming you know the expected real and imaginary parts of the GCD
        BigInteger expectedReal = new BigInteger("1"); // replace with the expected real part
        BigInteger expectedImag = new BigInteger("0"); // replace with the expected imaginary part

        assertEquals(expectedReal, gcd.real, "Real part of GCD is incorrect");
        assertEquals(expectedImag, gcd.imaginary, "Imaginary part of GCD is incorrect");
    }

    @Test
    void testGaussianExtendedEuclideanWithZeroInput() {
        GaussianInteger a = new GaussianInteger(ZERO, ZERO);
        GaussianInteger b = new GaussianInteger(new BigInteger("3"), new BigInteger("4")); // 3 + 4i

        GaussianInteger gcd = MathMethods.extendedEuclideanInZi(a, b);

        assertEquals(b.real, gcd.real, "Real part of GCD with zero input is incorrect");
        assertEquals(b.imaginary, gcd.imaginary, "Imaginary part of GCD with zero input is incorrect");
    }

    @Test
    void testGaussianExtendedEuclideanSymmetryOfValues() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), BigInteger.TWO); // 3 + 2i
        GaussianInteger b = new GaussianInteger(BigInteger.TWO, new BigInteger("3")); // 2 + 3i

        GaussianInteger gcdAB = extendedEuclideanInZi(a, b);
        GaussianInteger gcdBA = extendedEuclideanInZi(b, a);

        assertEquals(gcdAB.real, gcdBA.imaginary, "Real part of GCD should be symmetric");
        assertEquals(gcdAB.imaginary, gcdBA.real, "Imaginary part of GCD should be symmetric");
    }

    @Test
    void testGaussianExtendedEuclideanSymmetryOfResults() {
        GaussianInteger a = new GaussianInteger(new BigInteger("5"), new BigInteger("4"));
        GaussianInteger b = new GaussianInteger(new BigInteger("7"), new BigInteger("3"));

        GaussianInteger gcd_ab = MathMethods.extendedEuclideanInZi(a, b);
        GaussianInteger gcd_ba = MathMethods.extendedEuclideanInZi(b, a);

        assertEquals(gcd_ab.real, gcd_ba.real);
        assertEquals(gcd_ab.imaginary, gcd_ba.imaginary);
    }

    @Test
    void testGaussianExtendedEuclideanIdentity() {
        GaussianInteger a = new GaussianInteger(new BigInteger("1"), ZERO);
        GaussianInteger b = new GaussianInteger(new BigInteger("7"), new BigInteger("3"));

        GaussianInteger gcd = MathMethods.extendedEuclideanInZi(a, b);

        assertEquals(BigInteger.ONE, gcd.real);
        assertEquals(ZERO, gcd.imaginary);
    }
    @Test
    void testGaussianExtendedEuclideanMultiples() {
        GaussianInteger a = new GaussianInteger(new BigInteger("2"), new BigInteger("2"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("4"));

        GaussianInteger gcd = MathMethods.extendedEuclideanInZi(a, b);

        assertEquals(a.real, gcd.real);
        assertEquals(a.imaginary, gcd.imaginary);
    }

    @Test
    void testGaussianExtendedEuclideanPurelyRealAndImaginaryIntegers() {
        GaussianInteger a = new GaussianInteger(new BigInteger("5"), ZERO);
        GaussianInteger b = new GaussianInteger(new BigInteger("5"), ZERO);

        GaussianInteger gcd = extendedEuclideanInZi(a, b);

        assertEquals(BigInteger.valueOf(5), gcd.real);
        assertEquals(ZERO, gcd.imaginary);
    }
    @Test
    void testGaussianExtendedEuclideanCommonDivisorCases() {
        GaussianInteger a = new GaussianInteger(new BigInteger("4"), new BigInteger("2"));
        GaussianInteger b = new GaussianInteger(new BigInteger("6"), new BigInteger("3"));

        GaussianInteger gcd = MathMethods.extendedEuclideanInZi(a, b);

        assertEquals(new BigInteger("2"), gcd.real);
        assertEquals(BigInteger.ONE, gcd.imaginary);
    }

    @Test
    void testGaussianExtendedEuclideanWithPrimes() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), ZERO);
        GaussianInteger b = new GaussianInteger(new BigInteger("5"), ZERO);

        GaussianInteger gcd = MathMethods.extendedEuclideanInZi(a, b);

        assertEquals(BigInteger.ONE, gcd.real);
        assertEquals(ZERO, gcd.imaginary);
    }

    @Test
    void testNormalizeGCD() {
        GaussianInteger input = new GaussianInteger(ZERO, ZERO);

        GaussianInteger expected = new GaussianInteger(ZERO, ZERO);

        assertTrue(expected.equals(input.normalizeGCD()));
    }

}
