package mathMethods;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathMethodsTest {

    //    TODO @Adham Die ersten beiden Fälle habe ich 1 zu 1 aus Main übernommen, brauchen wir die?
    @Test
    void alternativeQuickExponentiationFromMainOne() {
        BigInteger base = new BigInteger("5");
        BigInteger exp = new BigInteger("1");
        BigInteger mod = new BigInteger("1");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentiationFromMainTwo() {
        BigInteger base = new BigInteger("5345890").pow(50).pow(40);
        BigInteger exp = new BigInteger("561563").pow(50);
        BigInteger mod = new BigInteger("402").pow(453);

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentiationOne() {
        BigInteger base = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger exp = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentiationTwo() {
        BigInteger base = new BigInteger("9876543210987654321098765432109876543210");
        BigInteger exp = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger mod = new BigInteger("987654321098765432109875643");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentiationThree() {
        BigInteger base = new BigInteger("987654321098765445321098765432109876543");
        BigInteger exp = new BigInteger("987654321098765432109876543210468435124");
        BigInteger mod = new BigInteger("1928374651928374651928374651");

        BigInteger expectedResult = base.modPow(exp, mod); // Using Java's built-in modPow method as a reference
        BigInteger actualResult = MathMethods.alternativeQuickExponentiation(base, exp, mod);

        assertEquals(expectedResult, actualResult, "The alternativeQuickExponentiation method returned an incorrect result.");
    }

    @Test
    void alternativeQuickExponentiation_edgeCases() {
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
    void extendedEuclideanOne() {
        BigInteger a = new BigInteger("56");
        BigInteger b = new BigInteger("15");

        BigInteger[] expexted = new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(-4), BigInteger.valueOf(15)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, b), "The extendedEuclidean method returned an incorrect result.");
    }

    @Test
    void extendedEuclideanTwo() {
        BigInteger a = new BigInteger("123456789");
        BigInteger b = new BigInteger("9876543210");

        BigInteger[] expexted = new BigInteger[]{BigInteger.valueOf(9), BigInteger.valueOf(109739361), BigInteger.valueOf(-1371742)};

        assertArrayEquals(expexted, MathMethods.extendedEuclidean(a, b), "The extendedEuclidean method returned an incorrect result.");
    }

    @Test
    void extendedEuclideanFromMain() {
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
    void millerRabinOne() {
        BigInteger number = new BigInteger("12");
        assertFalse(MathMethods.millerRabinTest(number, 100));
    }

    @Test
    void millerRabinTwo() {
        BigInteger number = new BigInteger("13");
        assertTrue(MathMethods.millerRabinTest(number, 100));
    }

    @Test
    void millerRabinThree() {
        BigInteger number = new BigInteger("2147483249");
        assertTrue(MathMethods.millerRabinTest(number, 100));
    }

    @Test
    void millerRabinFour() {
        BigInteger number = new BigInteger("685082020225370353384144714523");
        assertTrue(MathMethods.millerRabinTest(number, 100));
    }

    @Test
    void millerRabinFive() {
        BigInteger number = new BigInteger("685082020225370353384144714529"); // = 7 * 21827453 * 19154510483 * 234083288753
        assertFalse(MathMethods.millerRabinTest(number, 100));
    }


    @Test
    void prepareForEncryptionOne() {
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
    void prepareForEncryptionTwo() {
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
    void prepareForEncryptionThree() {
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
    void encryptToDecryptAlternativeQuickExponentiation() {
        BigInteger message = new BigInteger("12345"); // Example message
    }

    @Test
    void testMessagePreparationAndEncryptionDecryptionCycle() {
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

}
