package rsa;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    @Test
    public void encryptionAndDecryptionShouldReturnOriginalMessage() {
        new RSA(1000, 512, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "Mathematik ist spannend";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }
    @Test
    public void encryptionAndDecryptionShouldReturnFalseIfMessageChanged() {
        new RSA(40, 512, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "Hello, World!0";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN()) + "䟭謦燩Ԕ㫫";
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        //assert not equals
        assertNotEquals(originalMessage, decryptedMessage);
    }

    //Test characters outside of number system base
    @Test
    public void charactersOutsideOfNumberSystemBaseShouldThrowException() {
        new RSA(40, 512, 55926, BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "" + (char) 55927;
        assertThrows(IllegalArgumentException.class, () -> RSA.encrypt(originalMessage, RSA.getD(), RSA.getN()));
    }




    @Test
    public void encryptionAndDecryptionWithEmptyMessageShouldReturnEmptyString() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String originalMessage = "";
        String encryptedMessage = RSA.encrypt(originalMessage, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    public void signatureVerificationShouldReturnTrueForValidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String signature = RSA.sign(message);
        assertTrue(RSA.verifySignature(message, signature));
    }

    @Test
    public void signatureVerificationShouldReturnFalseForInvalidSignature() throws NoSuchAlgorithmException {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String invalidSignature = message + "a";
        // Convert to hex presentation
        invalidSignature = new BigInteger(invalidSignature.getBytes()).toString(16);
        assertFalse(RSA.verifySignature(message, invalidSignature));
    }
    //Test if non-hex presentation throws exception
    @Test
    public void signatureVerificationShouldThrowExceptionForNonHexPresentation() {
        RSA.setMillerRabinSteps(10);
        RSA.setBitLengthN(256);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        String message = "Hello, World!";
        String invalidSignature = "InvalidSignature";
        assertThrows(IllegalArgumentException.class, () -> RSA.verifySignature(message, invalidSignature));
    }
    @Test
    public void illegalMValueOne() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(100)));
    }
    @Test
    public void illegalMValueTwo() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(4)));
    }
    @Test
    public void illegalMValueThree() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(new BigInteger("9869600294464")));
    }
    @Test
    public void illegalMValueFour() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(BigInteger.valueOf(-23454545)));
    }
    @Test
    public void illegalMValueFive() {
        assertThrows(IllegalArgumentException.class, () -> RSA.setM(new BigInteger("-9869600294464")));
    }
    @Test
    public void bitLengthOfNIsOddOne() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(844);
        int wishedBitLength = 257;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);

        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    public void bitLengthOfNIsOddTwo() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(41);
        int wishedBitLength = 317;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);

        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    public void bitLengthOfNIsOddThree() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(73);
        int wishedBitLength = 1001;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);

        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    public void bitLengthOfNIsOddFour() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(1753);
        int wishedBitLength = 755;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);

        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }

    @Test
    void bitLengthOfNIsEvenOne() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(845);
        int wishedBitLength = 270;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    void bitLengthOfNIsEvenTwo() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(41);
        int wishedBitLength = 4460;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    void bitLengthOfNIsEvenThree() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(73);
        int wishedBitLength = 2306;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }
    @Test
    void bitLengthOfNIsEvenFour() {
        int MillerRabinSteps = 10;
        int NumberSystemBase = 55926;
        BigInteger M = BigInteger.valueOf(1753);
        int wishedBitLength = 890;
        RSA.generateRSAKeys(M, MillerRabinSteps, NumberSystemBase, wishedBitLength);
        assertEquals(wishedBitLength, RSA.getN().bitLength());
    }

    @Test
    void testCalculateBlockSize(){
        RSA.setBitLengthN(2048);
        RSA.setNumberSystemBase(55296);
        assertEquals(129, RSA.getBlockSize());
    }

    //Speed tests
    @Test
    public void speedTestForEncryptionAndDecryption() {
        new RSA(40, 512, 55926, BigInteger.valueOf(844));
//        RSA.setMillerRabinSteps(40);
//        RSA.setBitLengthN(1024);
//        RSA.setNumberSystemBase(55926);
//        RSA.setM(BigInteger.valueOf(844));
        RSA.generateRSAKeys();
        System.out.println("p: " + RSA.getP());
        System.out.println("q: " + RSA.getQ());
        System.out.println("n2: " + RSA.getN());
        System.out.println("e: " + RSA.getE());
        System.out.println("d: " + RSA.getD());
        String message = "Hello, World!";
        long start = System.nanoTime();
        String encryptedMessage = RSA.encrypt(message, RSA.getE(), RSA.getN());
        String decryptedMessage = RSA.decrypt(encryptedMessage, RSA.getD(), RSA.getN());
        System.out.println(decryptedMessage);
        System.out.println("Time needed to encrypt and decrypt: " + (System.nanoTime()-start) / 1000000 + " ms");
    }
    @Test
    public void speedTestForOnlyCalculatingP(){
        RSA.setMillerRabinSteps(100);
        RSA.setBitLengthN(512);
        RSA.setNumberSystemBase(55926);
        RSA.setM(BigInteger.valueOf(844));
        long start = System.nanoTime();
        RSA.generateP(BigInteger.valueOf(RSA.getBitLengthN()/2));
        System.out.println("Time needed only to generate p: " + (System.nanoTime()-start) / 1000000 + " ms");
    }
}
