package test.mathMethods;

import main.mathMethods.GaussianInteger;
import main.mathMethods.MathMethods;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraTaskTests {
    @Test
    void testRepresentPrimeAsSumOfTwoSquaresOne() {
        BigInteger prime = new BigInteger("13");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        assertEquals(BigInteger.valueOf(3), result.real);
        assertEquals(BigInteger.valueOf(2), result.imaginary);
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresTwo() {
        BigInteger prime = new BigInteger("17");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        assertEquals(BigInteger.valueOf(4), result.real);
        assertEquals(BigInteger.valueOf(1), result.imaginary);
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresThree() {
        BigInteger prime = new BigInteger("29");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
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

        String expectedMessage = "The prime number " + notFourNPlusOne + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testRepresentPrimeAsSumOfTwoSquaresXIsAMultipleOfP() {
        BigInteger input = new BigInteger("7");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(input);
        });

        String expectedMessage = "The prime number " + input + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExtendedEuclidean() {
        BigInteger a = BigInteger.TEN;
        BigInteger b = BigInteger.valueOf(6);
        BigInteger[] result = MathMethods.extendedEuclidean(a, b);
        assertEquals(BigInteger.valueOf(2), result[0]);
        assertEquals(BigInteger.valueOf(-1), result[1]);
        assertEquals(BigInteger.valueOf(2), result[2]);
    }

    @Test
    public void testModularInverse() {
        BigInteger a = BigInteger.valueOf(3);
        BigInteger b = BigInteger.valueOf(11);
        BigInteger result = MathMethods.modularInverse(a, b);
        assertEquals(BigInteger.valueOf(4), result);
    }

    @Test
    public void testVerifyEulerCriterion() {
        BigInteger c = BigInteger.valueOf(2);
        BigInteger p = BigInteger.valueOf(7);
        BigInteger result = MathMethods.verifyEulerCriterion(c, p);
        assertEquals(BigInteger.ONE, result);
    }

    @Test
    public void testF() {
        BigInteger[] z = {BigInteger.valueOf(3), BigInteger.valueOf(4)};
        BigInteger[] result = MathMethods.f(z);
        assertEquals(BigInteger.valueOf(3), result[0]);
        assertEquals(BigInteger.valueOf(4), result[1]);
    }

    @Test
    public void testGenerateRandomPrime() {
        BigInteger m = BigInteger.valueOf(2);
        BigInteger a = BigInteger.TEN;
        BigInteger b = BigInteger.valueOf(20);
        int millerRabinSteps = 10;
        AtomicInteger counter = new AtomicInteger(0);
        BigInteger result = MathMethods.generateRandomPrime(m, a, b, millerRabinSteps, counter);
        assertTrue(result.compareTo(a) >= 0 && result.compareTo(b) <= 0);
    }

    @Test
    public void testMillerRabinTest() {
        BigInteger possiblePrime = BigInteger.valueOf(17);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = BigInteger.ZERO;
        boolean result = MathMethods.millerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertTrue(result);
    }

    @Test
    public void testPrepareMessageForEncryption() {
        List<Integer> message = List.of(1, 2, 3, 4, 5);
        int blockSize = 2;
        int numberSystem = 10;
        List<BigInteger> result = MathMethods.prepareMessageForEncryption(message, blockSize, numberSystem);
        //TODO: We're padding with a 0 here -> Is that intentional?
        List<BigInteger> expected = List.of(BigInteger.valueOf(12), BigInteger.valueOf(34), BigInteger.valueOf(50));
        assertEquals(expected, result);
    }

    @Test
    public void testPrepareMessageForDecryption() {
        BigInteger message= BigInteger.valueOf(12345);
        int blockSize = 2;
        int numberSystem = 10;
        List<Integer> result = MathMethods.prepareMessageForDecryption(message, blockSize, numberSystem);
        //TODO: Padding with 0 again?
        List<Integer> expected = List.of(4, 5, 2, 3, 0, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testConvertTextToUniCode() {
        String text = "Hello";
        List<Integer> result = MathMethods.convertTextToUniCode(text);
        List<Integer> expected = List.of(72, 101, 108, 108, 111);
        assertEquals(expected, result);
    }

    @Test
    public void testConvertUniCodeToText() {
        List<Integer> unicode = List.of(72, 101, 108, 108, 111);
        String result = MathMethods.convertUniCodeToText(unicode);
        String expected = "Hello";
        assertEquals(expected, result);
    }
}