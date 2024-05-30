package test.mathMethods;

import main.mathMethods.GaussianInteger;
import main.mathMethods.MathMethods;
import main.resource.Resource;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class MathMethodsTest {
    @Test
    public void testAlternativeQuickExponentiation() {
        BigInteger base = BigInteger.valueOf(42);
        BigInteger exponent = BigInteger.valueOf(13);
        BigInteger mod = BigInteger.valueOf(23);
        BigInteger expected = BigInteger.valueOf(7);
        assertEquals(expected, MathMethods.alternativeQuickExponentiation(base, exponent, mod));
    }

    @Test
    public void testAlternativeQuickExponentiationNegativeExponent() {
        BigInteger base = BigInteger.valueOf(42);
        BigInteger exponent = BigInteger.valueOf(-13);
        BigInteger mod = BigInteger.valueOf(23);
        assertThrows(IllegalArgumentException.class, () -> MathMethods.alternativeQuickExponentiation(base, exponent, mod));
    }

    @Test
    public void testAlternativeQuickExponentiationExponentZero() {
        BigInteger base = BigInteger.valueOf(42);
        BigInteger exponent = Resource.ZERO;
        BigInteger mod = BigInteger.valueOf(23);
        BigInteger expected = Resource.ONE;
        assertEquals(expected, MathMethods.alternativeQuickExponentiation(base, exponent, mod));
    }

    @Test
    public void testAlternativeQuickExponentiationExponentZeroModOne() {
        BigInteger base = BigInteger.valueOf(42);
        BigInteger exponent = Resource.ZERO;
        BigInteger mod = Resource.ONE;
        BigInteger expected = Resource.ZERO;
        assertEquals(expected, MathMethods.alternativeQuickExponentiation(base, exponent, mod));
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
        BigInteger a = Resource.THREE;
        BigInteger b = BigInteger.valueOf(11);
        BigInteger result = MathMethods.modularInverse(a, b);
        assertEquals(Resource.FOUR, result);
    }

    @Test
    public void testModularInverseThrows() {
        BigInteger a = Resource.THREE;
        BigInteger b = BigInteger.valueOf(12);
        assertThrows(ArithmeticException.class, () -> MathMethods.modularInverse(a, b));
    }

    @Test
    public void testEulerCriterionInteger() {
        BigInteger p = BigInteger.valueOf(23);
        BigInteger result = MathMethods.eulerCriterionInteger(p);
        assertFalse(MathMethods.alternativeQuickExponentiation(result, p.subtract(Resource.ONE).divide(Resource.TWO), p).equals(Resource.ONE));
    }

    @Test
    public void testCalculateEulerCriterion() {
        BigInteger c = BigInteger.valueOf(2);
        BigInteger p = Resource.SEVEN;
        BigInteger result = MathMethods.calculateEulerCriterion(c, p);
        assertEquals(Resource.ONE, result);
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresOne() {
        BigInteger prime = new BigInteger("13");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        assertEquals(Resource.THREE, result.real);
        assertEquals(Resource.TWO, result.imaginary);
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresTwo() {
        BigInteger prime = new BigInteger("17");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        assertEquals(Resource.FOUR, result.real);
        assertEquals(Resource.ONE, result.imaginary);
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresThree() {
        BigInteger prime = new BigInteger("29");

        GaussianInteger result = MathMethods.representPrimeAsSumOfSquares(prime);
        assertEquals(Resource.FIVE, result.real);
        assertEquals(Resource.TWO, result.imaginary);
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresNotPrime() {
        BigInteger notPrime = new BigInteger("625");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(notPrime);
        });

        String expectedMessage = "The number " + notPrime + " is not a prime number.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresNotInForm4NPlus1() {
        BigInteger notFourNPlusOne = new BigInteger("7");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(notFourNPlusOne);
        });

        String expectedMessage = "The prime number " + notFourNPlusOne + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRepresentPrimeAsSumOfTwoSquaresXIsAMultipleOfP() {
        BigInteger input = new BigInteger("7");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.representPrimeAsSumOfSquares(input);
        });

        String expectedMessage = "The prime number " + input + " cannot be represented as a sum of two squares.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExtendedEuclideanInZiThrows() {
        GaussianInteger b = new GaussianInteger(Resource.ONE, Resource.TWO);
        GaussianInteger a = new GaussianInteger(Resource.THREE, Resource.FOUR);
        assertThrows(IllegalArgumentException.class, () -> MathMethods.extendedEuclideanInZi(a, b));
    }

    @Test
    public void testF() {
        BigInteger[] z = {Resource.THREE, Resource.FOUR};
        BigInteger[] result = MathMethods.f(z);
        assertEquals(Resource.THREE, result[0]);
        assertEquals(Resource.FOUR, result[1]);
    }

    @Test
    public void testRoundHalfUp() {
        BigInteger number = BigInteger.valueOf(3);
        BigInteger expected = BigInteger.valueOf(3);
        assertEquals(expected, MathMethods.roundHalfUp(number));
    }

    //TODO: Test randomElsner?
    @Test
    public void testRandomElsnerIs6() {
        BigInteger m = BigInteger.valueOf(2);
        BigInteger n = BigInteger.valueOf(3);
        BigInteger a = BigInteger.valueOf(2);
        BigInteger b = BigInteger.valueOf(20);
        BigInteger expected = BigInteger.valueOf(6);
        BigInteger result = MathMethods.randomElsner(m, n, a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testRandomElsner() {
        BigInteger m = BigInteger.valueOf(13);
        BigInteger n = BigInteger.valueOf(213);
        BigInteger a = BigInteger.valueOf(47);
        BigInteger b = BigInteger.valueOf(63);
        BigInteger expected = BigInteger.valueOf(6);
        BigInteger result = MathMethods.randomElsner(m, n, a, b);
        assertNotEquals(expected, result);
    }

    //TODO: Test isCompositeAgainstSmallPrimes?
    @Test
    public void testIsCompositeAgainstSmallPrimesTrue() {
        BigInteger primeCandidate = BigInteger.valueOf(7983);
        assertTrue(MathMethods.isCompositeAgainstSmallPrimes(primeCandidate));
    }

    @Test
    public void testIsCompositeAgainstSmallPrimesFalse() {
        BigInteger primeCandidate = BigInteger.valueOf(7927);
        assertFalse(MathMethods.isCompositeAgainstSmallPrimes(primeCandidate));
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
    public void testGenerateRandomPrimeThrows1() {
        BigInteger m = BigInteger.valueOf(2);
        BigInteger a = BigInteger.valueOf(20);
        BigInteger b = BigInteger.TEN;
        int millerRabinSteps = 10;
        AtomicInteger counter = new AtomicInteger(0);
        assertThrows(IllegalArgumentException.class, () -> MathMethods.generateRandomPrime(m, a, b, millerRabinSteps, counter));
    }

    @Test
    public void testGenerateRandomPrimeThrows2() {
        BigInteger m = BigInteger.valueOf(2);
        BigInteger a = BigInteger.TEN.negate();
        BigInteger b = BigInteger.valueOf(20);
        int millerRabinSteps = 10;
        AtomicInteger counter = new AtomicInteger(0);
        assertThrows(IllegalArgumentException.class, () -> MathMethods.generateRandomPrime(m, a, b, millerRabinSteps, counter));
    }

    @Test
    public void testGenerateRandomPrimeThrows3() {
        BigInteger m = BigInteger.valueOf(2).negate();
        BigInteger a = BigInteger.TEN;
        BigInteger b = BigInteger.valueOf(20);
        int millerRabinSteps = 10;
        AtomicInteger counter = new AtomicInteger(0);
        assertThrows(IllegalArgumentException.class, () -> MathMethods.generateRandomPrime(m, a, b, millerRabinSteps, counter));
    }

    @Test
    public void testMillerRabinTest() {
        BigInteger possiblePrime = BigInteger.valueOf(17);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.millerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertTrue(result);
    }

    @Test
    public void testMillerRabinTestOne() {
        BigInteger possiblePrime = BigInteger.valueOf(1);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.millerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertFalse(result);
    }

    @Test
    public void testMillerRabinTestTwo() {
        BigInteger possiblePrime = BigInteger.valueOf(2);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.millerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertTrue(result);
    }

    @Test
    public void testMillerRabinTestEven() {
        BigInteger possiblePrime = BigInteger.valueOf(16);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.millerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertFalse(result);
    }

    //TODO: Test parallelMillerRabinTest
    @Test
    public  void testParallelMillerRabinTest() {
        BigInteger possiblePrime = BigInteger.valueOf(17);
        int numberOfTests = 5;
        BigInteger m = BigInteger.valueOf(2);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.parallelMillerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertTrue(result);
    }
    @Test
    public  void testParallelMillerRabinTestFalse() {
        BigInteger possiblePrime = BigInteger.valueOf(296);
        int numberOfTests = 10;
        BigInteger m = BigInteger.valueOf(9);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.parallelMillerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertFalse(result);
    }

    @Test
    public  void testParallelMillerRabinTestBigger() {
        BigInteger possiblePrime = BigInteger.valueOf(1307);
        int numberOfTests = 25;
        BigInteger m = BigInteger.valueOf(16);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.parallelMillerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertTrue(result);
    }

    @Test
    public  void testParallelMillerRabinTestBiggerFalse() {
        BigInteger possiblePrime = BigInteger.valueOf(1024);
        int numberOfTests = 25;
        BigInteger m = BigInteger.valueOf(16);
        BigInteger countOfN = Resource.ZERO;
        boolean result = MathMethods.parallelMillerRabinTest(possiblePrime, numberOfTests, m, countOfN);
        assertFalse(result);
    }

    @Test
    public void testPrepareMessageForEncryption() {
        List<Integer> message = List.of(1, 2, 3, 4, 5);
        int blockSize = 2;
        int numberSystem = 10;
        List<BigInteger> result = MathMethods.prepareMessageForEncryption(message, blockSize, numberSystem);
        List<BigInteger> expected = List.of(BigInteger.valueOf(12), BigInteger.valueOf(34), BigInteger.valueOf(50));
        assertEquals(expected, result);
    }

    @Test
    public void testPrepareMessageForDecryption() {
        BigInteger message= BigInteger.valueOf(12345);
        int blockSize = 2;
        int numberSystem = 10;
        List<Integer> result = MathMethods.prepareMessageForDecryption(message, blockSize, numberSystem);
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