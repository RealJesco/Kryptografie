package blockChiffre;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockCipherTest {

    @Test
    void encrypt() {
        int bitLength = 256;
        int numberSystemBase = 55296;
        int blockSize = (int) (bitLength * (Math.log(2) / Math.log(numberSystemBase)));
        String message = "Mathematik ist cool!";
        List<BigInteger> encryptedDecimalText = ToDecimalBlockChiffre.encrypt(message, numberSystemBase, blockSize);
        System.out.println(encryptedDecimalText);
        assertNotNull(encryptedDecimalText);
    }

    @Test
    void decryptToDecimal() {
        int bitLength = 512;
        int numberSystemBase = 55296;
        int blockSize = (int) (bitLength * (Math.log(2) / Math.log(numberSystemBase)));
        String message = "Da苉 ist eine Testnachricht";
        List<BigInteger> encryptedDecimalText = ToDecimalBlockChiffre.encrypt(message, numberSystemBase, blockSize);
        String decryptedDecimalText = ToDecimalBlockChiffre.decrypt(encryptedDecimalText, numberSystemBase);

        assertEquals(message, decryptedDecimalText);
    }

    @Test
    void decryptFromDecimal() {
        int bitLength = 512;
        int numberSystemBase = 55296;
        int blockSize = (int) (bitLength * (Math.log(2) / Math.log(numberSystemBase)));
        List<BigInteger> expectedDecryptedDecimal = new ArrayList<>();
        expectedDecryptedDecimal.add(new BigInteger("107492014297546449612193802144047136"));
        expectedDecryptedDecimal.add(new BigInteger("159656113899559548508775364389320819"));
        expectedDecryptedDecimal.add(new BigInteger("183367115080887221772378868133959779"));
        expectedDecryptedDecimal.add(new BigInteger("5750900"));
        String encryptedBlockchiffreText = FromDecimalBlockChiffre.encrypt(expectedDecryptedDecimal, numberSystemBase, blockSize);
        List<BigInteger> decryptedBlockcipher = FromDecimalBlockChiffre.decrypt(encryptedBlockchiffreText, numberSystemBase, blockSize);
        assertEquals(expectedDecryptedDecimal, decryptedBlockcipher);
    }

    @Test
    void decryptFromDecimalFail() {
        int bitLength = 512;
        int numberSystemBase = 55296;
        int blockSize = (int) (bitLength * (Math.log(2) / Math.log(numberSystemBase)));
        List<BigInteger> expectedDecryptedDecimal = new ArrayList<>();
        expectedDecryptedDecimal.add(new BigInteger("107492014297546449612193802144047136"));
        expectedDecryptedDecimal.add(new BigInteger("159656113899559548508775364389320819"));
        expectedDecryptedDecimal.add(new BigInteger("183367115080887221772378868133959779"));
        expectedDecryptedDecimal.add(new BigInteger("5750900"));
        String encryptedBlockchiffreText = FromDecimalBlockChiffre.encrypt(expectedDecryptedDecimal, numberSystemBase, blockSize);
        encryptedBlockchiffreText = encryptedBlockchiffreText.concat("test");
        List<BigInteger> decryptedBlockcipher = FromDecimalBlockChiffre.decrypt(encryptedBlockchiffreText, numberSystemBase, blockSize);
        assertNotEquals(expectedDecryptedDecimal.size(), decryptedBlockcipher.size());

        for (int i = 0; i < expectedDecryptedDecimal.size() - 1; i++) {
            assertEquals(expectedDecryptedDecimal.get(i), decryptedBlockcipher.get(i));
        }

        assertNotEquals(expectedDecryptedDecimal, decryptedBlockcipher);
    }

}