package main.blockChiffre;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public final class ToDecimalBlockChiffre {

    public static List<BigInteger> encrypt(final String plaintext, final int numberBase, final int blockSize) {
        //Die beiden Methoden sind identisch
        return FromDecimalBlockChiffre.decrypt(plaintext, numberBase, blockSize);
    }

    public static String decrypt(final List<BigInteger> ciphertext, final int numberBase) {
        return ciphertext
                .stream()
                .map((BigInteger block) -> FromDecimalBlockChiffre.unicode_g_adic_development(block, BigInteger.valueOf(numberBase)))
                .collect(Collectors.joining(""));
    }
}
