package mathMethods;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathMethodsTest {

    @Test
    void testAlternativeQuickExponentiationFromMainOne() {
        BigInteger base = new BigInteger("5");
        BigInteger exp = new BigInteger("1");
        BigInteger mod = new BigInteger("1");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationFromMainTwo() {
        BigInteger base = new BigInteger("5345890").pow(50).pow(40);
        BigInteger exp = new BigInteger("561563").pow(50);
        BigInteger mod = new BigInteger("402").pow(453);

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationOne() {
        BigInteger base = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger exp = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationTwo() {
        BigInteger base = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger exp = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger mod = new BigInteger("987654321098765432109875643");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationThree() {
        BigInteger base = new BigInteger("987654321098765445321098765432109876543");
        BigInteger exp = new BigInteger("987654321098765432109876543210468435124");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationZero(){
        BigInteger base = new BigInteger("1");
        BigInteger exp = new BigInteger("0");
        BigInteger mod = new BigInteger("1");
        System.out.println("base = " + base + " exp = " + exp + " mod = " + mod);
        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void testAlternativeQuickExponentiationNegativeExponent() {
        BigInteger base = new BigInteger("2");
        BigInteger exp = new BigInteger("-2");
        BigInteger mod = new BigInteger("5");

        assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.alternativeQuickExponentiation(base, exp, mod);
        }, "The alternativeQuickExponentiation method should throw an IllegalArgumentException when the exponent is negative.");
    }


    @Test
    void testAlternativeQuickExponentiation_edgeCases() {
        BigInteger base = BigInteger.ZERO;
        BigInteger exp = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        // Testing with a base of zero
        assertEquals(BigInteger.ZERO, MathMethods.alternativeQuickExponentiation(base, exp, mod), "Failed with a base of zero.");

        base = new BigInteger("1234567890123456789012345678901234567890");
        exp = BigInteger.ZERO;

        // Testing with an exponent of zero
        assertEquals(BigInteger.ONE, MathMethods.alternativeQuickExponentiation(base, exp, mod), "Failed with an exponent of zero.");

        base = BigInteger.ONE;
        exp = new BigInteger("9876543210987654321098765432109876543210");

        // Testing with a base of one
        assertEquals(BigInteger.ONE, MathMethods.alternativeQuickExponentiation(base, exp, mod), "Failed with a base of one.");
    }


    @Test
    void testExtendedEuclideanOne() {
        BigInteger a = new BigInteger("56");
        BigInteger b = new BigInteger("15");

        BigInteger[] expexted = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(-4), BigInteger.valueOf(15)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, b), "The extendedEuclidean method returned an incorrect result.");
    }

    @Test
    void testExtendedEuclideanTwo() {
        BigInteger a = new BigInteger("123456789");
        BigInteger b = new BigInteger("9876543210");

        BigInteger[] expexted = new BigInteger[]{BigInteger.valueOf(9), BigInteger.valueOf(109739361), BigInteger.valueOf(-1371742)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, b), "The extendedEuclidean method returned an incorrect result.");
    }

    @Test
    void testExtendedEuclideanFromMain() {
        BigInteger a = new BigInteger("315");
        BigInteger b = new BigInteger("661643");

        BigInteger[] expexted = new BigInteger[]{BigInteger.valueOf(315), BigInteger.valueOf(0), BigInteger.valueOf(1)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, a), "The extendedEuclidean method returned an incorrect result.");

        expexted = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(-319269), BigInteger.valueOf(152)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, b), "The extendedEuclidean method returned an incorrect result.");

        expexted = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(152), BigInteger.valueOf(-319269)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(b, a), "The extendedEuclidean method returned an incorrect result.");

        expexted = new BigInteger[]{BigInteger.valueOf(661643), BigInteger.valueOf(0), BigInteger.valueOf(1)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(b, b), "The extendedEuclidean method returned an incorrect result.");
    }

    @Test
    void testMillerRabinPossiblePrimeIsOne() {
        BigInteger number = new BigInteger("1");
        assertFalse(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinPossiblePrimeIsTwo() {
        BigInteger number = new BigInteger("2");
        assertTrue(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinOne() {
        BigInteger number = new BigInteger("12");
        assertFalse(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinTwo() {
        BigInteger number = new BigInteger("13");
        assertTrue(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinThree() {
        BigInteger number = new BigInteger("2147483249");
        assertTrue(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinFour() {
        BigInteger number = new BigInteger("685082020225370353384144714523");
        assertTrue(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testMillerRabinFive() {
        BigInteger number = new BigInteger("685082020225370353384144714529"); // = 7 * 21827453 * 19154510483 * 234083288753
        assertFalse(MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3)));
    }

    @Test
    void testParallelMillerRabinIsNotAPrime() {
        BigInteger number = new BigInteger("12");
        assertFalse(MathMethods.parallelMillerRabinTest(number, 100, BigInteger.valueOf(221), BigInteger.valueOf(3)));
    }

    @Test
    void testParallelMillerRabinIsAPrime() {
        BigInteger number = new BigInteger("13");
        assertTrue(MathMethods.parallelMillerRabinTest(number, 100, BigInteger.valueOf(844), BigInteger.valueOf(3)));
    }

    @Test
    void testPerformanceOfParallelMillerRabinVsMillerRabin() {
        BigInteger number = new BigInteger("343739715955308939545691391117987800132704566049496335135859248151793918149206619119756660724626712063086796201569784256452606629036433928130351694654329292086244629709724318410807122127054289983753814291813861654343316296116490321676945035304666147301022078109790068726697482080796040521994604710699");

        int warmupCount = 5; // Warm-up iterations
        int count = 5; // Number of test iterations
        long accumulatedMillerRabinTime = 0;
        long accumulatedParallelMillerRabinTime = 0;

        // Warm-up phase
        for (int i = 0; i < warmupCount; i++) {
            MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3));
        }

        for(int i = 0; i < count; i++) {
            long start = System.nanoTime();
            MathMethods.millerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3));
            long end = System.nanoTime();
            accumulatedMillerRabinTime += (end - start);
            start = System.nanoTime();
            MathMethods.parallelMillerRabinTest(number, 100, BigInteger.valueOf(2), BigInteger.valueOf(3));
            end = System.nanoTime();
            accumulatedParallelMillerRabinTime += (end - start);
        }
        //In ms
        System.out.println("Average time needed for Miller-Rabin: " + (accumulatedMillerRabinTime / count / 1000000) + " ms");
        System.out.println("Average time needed for Parallel Miller-Rabin: " + (accumulatedParallelMillerRabinTime / count / 1000000) + " ms");
        assertTrue(accumulatedParallelMillerRabinTime < accumulatedMillerRabinTime);
    }

@Test
    void testGeneratePrimeNumbersPerformance() {
    int warmupCount = 5; // Warm-up iterations
    int count = 50; // Number of test iterations
    long accumulatedTime = 0;

    // Warm-up phase
    for (int i = 0; i < warmupCount; i++) {
        MathMethods.generateRandomPrime(BigInteger.valueOf(95), BigInteger.valueOf(1), BigInteger.valueOf(100), 20);
    }

    for(int i = 0; i < count; i++) {
        long start = System.nanoTime();
        MathMethods.generateRandomPrime(BigInteger.valueOf(99), BigInteger.valueOf(1), BigInteger.valueOf(100), 20);
        long end = System.nanoTime();
        accumulatedTime += (end - start);
    }
    //In ms
    System.out.println("Average time needed for generating prime numbers: " + (accumulatedTime / count / 1000 ) + " ms");
    }

    @Test
    void testGeneratePrimeNumbers() {
        BigInteger m = new BigInteger("10");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("100");
        int millerRabinSteps = 20;

        BigInteger result = MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        System.out.println(result);
        //TODO: test countOfN from 0 to 100 and check if true, if not true print countOfN
    }

    @Test
    void testGeneratePrimeNumbersAGreaterThanB() {
        BigInteger m = new BigInteger("10");
        BigInteger a = new BigInteger("100");
        BigInteger b = new BigInteger("1");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The lower bound must be smaller than the upper bound";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGeneratePrimeNumbersASmallerThan0() {
        BigInteger m = new BigInteger("10");
        BigInteger a = new BigInteger("-1");
        BigInteger b = new BigInteger("100");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The lower bound must be greater than or equal to 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGeneratePrimeNumbersMSmallerThan0() {
        BigInteger m = new BigInteger("-10");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("100");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The random seed must be greater than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGeneratePrimeNumbersMIs0() {
        BigInteger m = new BigInteger("0");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("100");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The random seed must be greater than 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGeneratePrimeNumbersMAsBigAsRange() {
        BigInteger m = new BigInteger("10");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("10");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The random seed must be smaller than the range";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGeneratePrimeNumbersMLangerThanRange() {
        BigInteger m = new BigInteger("11");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("10");
        int millerRabinSteps = 20;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        });

        String expectedMessage = "The random seed must be smaller than the range";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testPrepareForEncryptionOne() {
        List<Integer> codeMessage = new ArrayList<>();
//        12 0 19 7 4 12 0 19
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);
        codeMessage.add(7);
        codeMessage.add(4);
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);

        BigInteger expected = new BigInteger("6083869600275");

        assertEquals(expected, MathMethods.prepareMessageForEncryption(codeMessage, 8, 47).get(0));
    }

    @Test
    void testPrepareForEncryptionTwo() {
        List<Integer> codeMessage = new ArrayList<>();
//        12 0 19 7 4 12 0 20
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);
        codeMessage.add(7);
        codeMessage.add(4);
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(20);

        BigInteger expected = new BigInteger("6083869600276");

        assertEquals(expected, MathMethods.prepareMessageForEncryption(codeMessage, 8, 47).get(0));
    }

    @Test
    void testPrepareForEncryptionThree() {
        List<Integer> codeMessage = new ArrayList<>();
//        12 0 19 7 4 12 1 19
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);
        codeMessage.add(7);
        codeMessage.add(4);
        codeMessage.add(12);
        codeMessage.add(1);
        codeMessage.add(19);

        BigInteger expected = new BigInteger("6083869600322");

        assertEquals(expected, MathMethods.prepareMessageForEncryption(codeMessage, 8, 47).get(0));
    }

    @Test
    void testEncryptToDecryptAlternativeQuickExponentiationOnlyWithNumbers() {
        BigInteger message = new BigInteger("12345"); // Example message
        BigInteger e = new BigInteger("18217281770421758450086481999749147637"); // public exponent
        BigInteger d = new BigInteger("69856630177376283805385594524728944213"); // private exponent
        BigInteger n = new BigInteger("152421106944440766760720109679329339863"); // modulus

        BigInteger encryptedMessage = MathMethods.alternativeQuickExponentiation(message, e, n);
        BigInteger decryptedMessage = MathMethods.alternativeQuickExponentiation(encryptedMessage, d, n);

        assertEquals(message, decryptedMessage, "The decrypted message should match the original message.");
    }

    /**
     * This test is for testing the whole encryption and decryption cycle.
     * The finished encrypt and decrypt methods do handle all the logic themselves, including padding and unpadding.
     * This test is for testing the methods that are used in the encrypt and decrypt methods.
     */
    @Test
    void testMessageBlockCipherCycle() {
        // Assuming we have a small RSA key pair for testing (not secure for real use)
        BigInteger e = new BigInteger("18217281770421758450086481999749147637"); // public exponent
        BigInteger d = new BigInteger("69856630177376283805385594524728944213"); // private exponent
        BigInteger n = new BigInteger("152421106944440766760720109679329339863"); // modulus

        String originalMessage = "Test RSA message!";
        List<Integer> unicodeMessage = MathMethods.convertTextToUniCode(originalMessage);

        // Preparing message for encryption
        List<BigInteger> preparedBlocks = MathMethods.prepareMessageForEncryption(unicodeMessage, 2, 256);
        List<BigInteger> encryptedBlocks = new ArrayList<>();
        for (BigInteger block : preparedBlocks) {
            encryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, e, n));
        }

        // Decrypting the message
        List<BigInteger> decryptedBlocks = new ArrayList<>();
        for (BigInteger block : encryptedBlocks) {
            decryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, d, n));
        }

        // Preparing decrypted blocks for reading as a message
        List<Integer> decryptedMessageCode = new ArrayList<>();
        for (BigInteger block : decryptedBlocks) {
            decryptedMessageCode.addAll(MathMethods.prepareMessageForDecryption(block, 2, 256));
        }

        String decryptedMessage = MathMethods.convertUniCodeToText(decryptedMessageCode);

        // Remove the padding
        decryptedMessage = decryptedMessage.substring(0, decryptedMessage.indexOf('\0'));
        // The decrypted message should match the original message
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void testPrepareMessageForEncryptionDifferentLengths() {
        List<Integer> shortMessage = Arrays.asList(65, 66); // 'AB'
        List<Integer> exactBlockMessage = Arrays.asList(65, 66, 67, 68); // 'ABCD'
        List<Integer> longMessage = Arrays.asList(65, 66, 67, 68, 69); // 'ABCDE'
        int blockSize = 4;
        int numberSystem = 256;

        // Testing short message
        List<BigInteger> shortMessageBlocks = MathMethods.prepareMessageForEncryption(shortMessage, blockSize, numberSystem);
        assertEquals(1, shortMessageBlocks.size(), "There should be 1 block for a short message.");

        // Testing exact block size message
        List<BigInteger> exactBlockMessageBlocks = MathMethods.prepareMessageForEncryption(exactBlockMessage, blockSize, numberSystem);
        assertEquals(1, exactBlockMessageBlocks.size(), "There should be 1 block for a message that exactly fits the block size.");

        // Testing long message
        List<BigInteger> longMessageBlocks = MathMethods.prepareMessageForEncryption(longMessage, blockSize, numberSystem);
        assertEquals(2, longMessageBlocks.size(), "There should be 2 blocks for a long message.");
    }

    @Test
    void testEncryptionDecryptionWithQuickExponentiation() {
        BigInteger message = new BigInteger("12345"); // Example message
        BigInteger e = new BigInteger("18217281770421758450086481999749147637"); // public exponent
        BigInteger d = new BigInteger("69856630177376283805385594524728944213"); // private exponent
        BigInteger n = new BigInteger("152421106944440766760720109679329339863"); // modulus

        // Encrypting with alternativeQuickExponentiation
        BigInteger encryptedMessage = MathMethods.alternativeQuickExponentiation(message, e, n);

        // Decrypting
        BigInteger decryptedMessage = MathMethods.alternativeQuickExponentiation(encryptedMessage, d, n);

        // The decrypted message should match the original message
        assertEquals(message,
                decryptedMessage,
                "The decrypted message should match the original message.");
    }

    @Test
    void testF() {
        BigInteger[] input = new BigInteger[2];
        input[0] = new BigInteger("0");
        input[1] = new BigInteger("0");

        BigInteger[] expected = new BigInteger[2];
        expected[0] = new BigInteger("0");
        expected[1] = new BigInteger("0");

        BigInteger[] result = MathMethods.f(input);

        assertTrue(expected[0].equals(result[0]), "The real part of the result is wrong.");
        assertTrue(expected[1].equals(result[1]), "The imaginary part of the result is wrong.");
    }

    @Test
    void testRandomElsner() {
        BigInteger m = new BigInteger("10");
        BigInteger n = new BigInteger("5");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("100");

        BigInteger result = MathMethods.randomElsner(m, n, a, b);

        assertTrue(result.compareTo(a) >= 0 && result.compareTo(b) <= 0, "The randomElsner method should return a BigInteger within the range [a, b].");
    }

    @Test
    void testIsCompositeAgainstSmallPrimesIsPrime() {
        BigInteger prime = new BigInteger("1201");

        assertFalse(MathMethods.isCompositeAgainstSmallPrimes(prime));
    }

    @Test
    void testIsCompositeAgainstSmallPrimesIsNotPrime() {
        BigInteger notPrime = new BigInteger("390625");

        assertTrue(MathMethods.isCompositeAgainstSmallPrimes(notPrime));
    }

    @Test
    void testGenerateRandomPrime() {
        BigInteger m = new BigInteger("10");
        BigInteger a = new BigInteger("1");
        BigInteger b = new BigInteger("100");
        int millerRabinSteps = 20;

        BigInteger result = MathMethods.generateRandomPrime(m, a, b, millerRabinSteps);
        System.out.println(result);
        //test countOfN from 0 to 100 and check if true, if not true print countOfN
//        for (int i = 100000; i < 100000000; i++) {
//            if (!MathMethods.millerRabinTest(result, millerRabinSteps, m, BigInteger.valueOf(i))) {
//                System.out.println(i);
//            }
//        }
        assertTrue(MathMethods.millerRabinTest(result, millerRabinSteps, m, BigInteger.valueOf(5)), "The generateRandomPrime method should return a probable prime number.");
    }

    @Test
    void testPrepareMessageForEncryption() {
        List<Integer> message = Arrays.asList(65, 66, 67, 68, 69);
        int blockSize = 4;
        int numberSystem = 256;

        List<BigInteger> result = MathMethods.prepareMessageForEncryption(message, blockSize, numberSystem);

        assertEquals(2, result.size(), "The prepareMessageForEncryption method should divide the message into blocks of the specified size.");
    }

    @Test
    void testPrepareMessageForDecryption() {
        BigInteger message = new BigInteger("1234567890");
        int blockSize = 4;
        int numberSystem = 256;

        List<Integer> result = MathMethods.prepareMessageForDecryption(message, blockSize, numberSystem);

        assertEquals(blockSize, result.size(), "The prepareMessageForDecryption method should divide the message into blocks of the specified size.");
    }

    @Test
    void testConvertTextToUniCode() {
        String text = "Test";

        List<Integer> result = MathMethods.convertTextToUniCode(text);

        assertEquals(Arrays.asList(84, 101, 115, 116), result, "The convertTextToUniCode method should correctly convert the text to Unicode.");
    }

    @Test
    void testConvertUniCodeToText() {
        List<Integer> unicode = Arrays.asList(84, 101, 115, 116); // 'Test'

        String result = MathMethods.convertUniCodeToText(unicode);

        assertEquals("Test", result, "The convertUniCodeToText method should correctly convert the Unicode to text.");
    }


}

