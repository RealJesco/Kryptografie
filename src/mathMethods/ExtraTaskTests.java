package mathMethods;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtraTaskTests {
    @Test
    void representPrimeAsSumOfTwoSquaresOne() {
        BigInteger prime = new BigInteger("13");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(3), result.real);
        assertEquals(BigInteger.valueOf(2), result.imaginary);
    }

    @Test
    void representPrimeAsSumOfTwoSquaresTwo() {
        BigInteger prime = new BigInteger("17");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(4), result.real);
        assertEquals(BigInteger.valueOf(1), result.imaginary);
    }

    @Test
    void representPrimeAsSumOfTwoSquaresThree() {
        BigInteger prime = new BigInteger("29");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(5), result.real);
        assertEquals(BigInteger.valueOf(2), result.imaginary);
    }
}
