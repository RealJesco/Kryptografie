package mathMethods;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraTaskTests {
    @Test
    void testRepresentPrimeAsSumOfTwoSquaresOne() {
        BigInteger prime = new BigInteger("13");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(3), result.real);
        assertEquals(BigInteger.valueOf(2), result.imaginary);
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresTwo() {
        BigInteger prime = new BigInteger("17");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(4), result.real);
        assertEquals(BigInteger.valueOf(1), result.imaginary);
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresThree() {
        BigInteger prime = new BigInteger("29");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        System.out.println("p = " + prime + " -> " + result);
        assertEquals(BigInteger.valueOf(5), result.real);
        assertEquals(BigInteger.valueOf(2), result.imaginary);
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresNotPrime() {
        BigInteger notPrime = new BigInteger("625");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(notPrime);
        });

        String expectedMessage = "The number " + notPrime + " is not a prime number.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresNotInForm4NPlus1() {
        BigInteger notFourNPlusOne = new BigInteger("7");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(notFourNPlusOne);
        });

        String expectedMessage = "The number " + notFourNPlusOne + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresXIsAMultipleOfP() {
        BigInteger input = new BigInteger("7");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(input);
        });

        String expectedMessage = "The number " + input + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
