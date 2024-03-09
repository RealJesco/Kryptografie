//package rsa;
//
//import mathMethods.MathMethods;
//import org.junit.jupiter.api.Test;
//import java.math.BigInteger;
//import java.security.NoSuchAlgorithmException;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class RSATest {
//
//    @Test
//    public void encryptionAndDecryptionShouldReturnOriginalMessage() {
//        new RSA(1000, 512, 55926, BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String originalMessage = "Hello, World!0 und Mathematik ist spannend";
//        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
//        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
//        assertEquals(originalMessage, decryptedMessage);
//    }
//    @Test
//    public void encryptionAndDecryptionShouldReturnFalseIfMessageChanged() {
//        new RSA(40, 512, 55926, BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String originalMessage = "Hello, World!0 und Mathematik ist spannend";
//        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN()) + "䟭謦燩Ԕ㫫";
//        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
//        System.out.println("decryptedMessage" + decryptedMessage);
//        //assert not equals
//        assertNotEquals(originalMessage, decryptedMessage);
//    }
//
//    //Test characters outside of number system base
//    @Test
//    public void charactersOutsideOfNumberSystemBaseShouldThrowException() {
//        new RSA(40, 512, 55926, BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String originalMessage = "" + (char) 55927;
//        assertThrows(IllegalArgumentException.class, () -> RSA.encrypt(originalMessage, RSA.getD(), RSA.getN()));
//    }
//
//
//
//
//    @Test
//    public void encryptionAndDecryptionWithEmptyMessageShouldReturnEmptyString() {
//        RSA.setMillerRabinSteps(10);
//        RSA.setBitLengthN(256);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String originalMessage = "";
//        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
//        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
//        assertEquals(originalMessage, decryptedMessage);
//    }
//
//    @Test
//    public void signatureVerificationShouldReturnTrueForValidSignature() throws NoSuchAlgorithmException {
//        RSA.setMillerRabinSteps(10);
//        RSA.setBitLengthN(256);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String message = "Hello, World!";
//        String signature = RSA.sign(message);
//        assertTrue(RSA.verifySignature(message, signature));
//        assertFalse(RSA.verifySignature(message, signature.substring(5)));
//    }
//
//    @Test
//    public void signatureVerificationShouldReturnFalseForInvalidSignature() throws NoSuchAlgorithmException {
//        RSA.setMillerRabinSteps(10);
//        RSA.setBitLengthN(256);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String message = "Hello, World!";
//        String invalidSignature = RSA.sign(message) + "123";
//        assertFalse(RSA.verifySignature(message, invalidSignature));
//    }
//    //Test if non-hex presentation throws exception
//    @Test
//    public void signatureVerificationShouldThrowExceptionForNonHexPresentation() {
//        RSA.setMillerRabinSteps(10);
//        RSA.setBitLengthN(256);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String message = "Hello, World!";
//        String invalidSignature = "InvalidSignature";
//        assertThrows(IllegalArgumentException.class, () -> RSA.verifySignature(message, invalidSignature));
//    }
//    @Test
//    public void illegalMValueOne() {
//        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(100)));
//    }
//    @Test
//    public void illegalMValueTwo() {
//        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(4)));
//    }
//    @Test
//    public void illegalMValueThree() {
//        assertThrows(IllegalArgumentException.class, () -> RSA.setM(new BigInteger("9869600294464")));
//    }
//    @Test
//    public void illegalMValueFour() {
//        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(-23454545)));
//    }
//    @Test
//    public void illegalMValueFive() {
//        assertThrows(IllegalArgumentException.class, () -> RSA.setM(new BigInteger("-9869600294464")));
//    }
//    @Test
//    public void bitLengthOfNIsOddOne() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(844);
//        int wishedBitLength = 257;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    public void bitLengthOfNIsOddTwo() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(41);
//        int wishedBitLength = 317;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    public void bitLengthOfNIsOddThree() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(73);
//        int wishedBitLength = 1001;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    public void bitLengthOfNIsOddFour() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(1753);
//        int wishedBitLength = 755;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//
//    @Test
//    void bitLengthOfNIsEvenOne() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(845);
//        int wishedBitLength = 270;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    void bitLengthOfNIsEvenTwo() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(41);
//        int wishedBitLength = 4460;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    void bitLengthOfNIsEvenThree() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(73);
//        int wishedBitLength = 2306;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//    @Test
//    void bitLengthOfNIsEvenFour() {
//        int MillerRabinSteps = 10;
//        int NumberSystemBase = 55926;
//        BigInteger M = BigInteger.valueOf(1753);
//        int wishedBitLength = 890;
//        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
//        assertEquals(wishedBitLength, RSA.getN().bitLength());
//    }
//
//    @Test
//    void testCalculateBlockSize(){
//        RSA.setBitLengthN(2048);
//        RSA.setNumberSystemBase(55296);
//        assertEquals(129, RSA.getBlockSize());
//    }
//
//    //Speed tests
//    @Test
//    public void speedTestForEncryptionAndDecryption() {
//        new RSA(40, 512, 55926, BigInteger.valueOf(844));
////        RSA.setMillerRabinSteps(40);
////        RSA.setBitLengthN(1024);
////        RSA.setNumberSystemBase(55926);
////        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        System.out.println("p: " + RSA.getP());
//        System.out.println("q: " + RSA.getQ());
//        System.out.println("n2: " + RSA.getN());
//        System.out.println("e: " + RSA.getE());
//        System.out.println("d: " + RSA.getD());
//        String message = "Hello, World!";
//        long start = System.nanoTime();
//        String encryptedMessage = RSA.encrypt(message, RSA.getE(), RSA.getN());
//        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
//        System.out.println(decryptedMessage);
//        System.out.println("Time needed to encrypt and decrypt: " + (System.nanoTime()-start) / 1000000 + " ms");
//    }
//    @Test
//    public void speedTestForOnlyCalculatingP(){
//        RSA.setMillerRabinSteps(100);
//        RSA.setBitLengthN(512);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        long start = System.nanoTime();
//        RSA.generateP(BigInteger.valueOf(RSA.getBitLengthN()/2));
//        System.out.println("Time needed only to generate p: " + (System.nanoTime()-start) / 1000000 + " ms");
//    }
//
//    @Test
//    public void checkIfLengthIsMoreThan256BitsThrowsException() {
//        RSA.setMillerRabinSteps(10);
//        RSA.setBitLengthN(255);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
//        RSA.generateRSAKeys();
//        String message = "Hello, World!";
//        assertThrows(IllegalArgumentException.class, () -> RSA.sign(message));
//    }
//
//    @Test
//    public void test_calculateE_shouldBeCoprimeWithPhiN() {
//        BigInteger phiN = BigInteger.valueOf(120);
//        BigInteger commonFactor;
//
//        RSA.calculateE(phiN);
//        BigInteger e = RSA.getE();
//
//        commonFactor = phiN.gcd(e);
//        assertEquals(BigInteger.valueOf(1), commonFactor);
//    }
//
//    /**
//     * This test verifies that the calculateE() method from the RSA class is deterministic. That is, given the same inputs,
//     * the method should always produce the same output.
//     */
//    @Test
//    public void test_calculateE_shouldBeDeterministic() {
//        BigInteger phiN = BigInteger.valueOf(120);
//        BigInteger e1, e2;
//
//        RSA.calculateE(phiN);
//        e1 = RSA.getE();
//        RSA.calculateE(phiN);
//        e2 = RSA.getE();
//
//        assertEquals(e1, e2);
//    }
//
//
//    /**
//     * This test verifies that the calculateE() method from RSA class can handle large input sizes.
//     * We check this by providing a large phiN value and then verifying that the operation completes successfully.
//     */
//    @Test
//    public void testCalculateE_shouldHandleLargeInputs() {
//        BigInteger phiN = new BigInteger("9223372036854775807");
//
//        RSA.calculateE(phiN);
//        BigInteger e = RSA.getE();
//
//        assertNotEquals(0, e.compareTo(BigInteger.ZERO));
//    }
//
//    /**
//     * This test verifies that the calculateE() method from the RSA class fails when an unsuitable phiN is provided.
//     */
//    @Test
//    public void testCalculateE_shouldFailForUnsuitablePhiN() {
//        BigInteger phiN = BigInteger.valueOf(2);
//
//
//        assertThrows(IllegalArgumentException.class, () -> RSA.calculateE(phiN));
//    }
//}
