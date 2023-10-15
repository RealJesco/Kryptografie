import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathMethodsTest {

    @Test
    void alternativeQuickExponentation() {
        BigInteger base = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger exp = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentation_edgeCases() {
        BigInteger base = BigInteger.ZERO;
        BigInteger exp = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        // Testing with a base of zero
        assertEquals(BigInteger.ZERO, MathMethods.alternativeQuickExponentation(base, exp, mod), "Failed with a base of zero.");

        base = new BigInteger("1234567890123456789012345678901234567890");
        exp = BigInteger.ZERO;

        // Testing with an exponent of zero
        assertEquals(BigInteger.ONE, MathMethods.alternativeQuickExponentation(base, exp, mod), "Failed with an exponent of zero.");

        base = BigInteger.ONE;
        exp = new BigInteger("9876543210987654321098765432109876543210");

        // Testing with a base of one
        assertEquals(BigInteger.ONE, MathMethods.alternativeQuickExponentation(base, exp, mod), "Failed with a base of one.");
    }
}
