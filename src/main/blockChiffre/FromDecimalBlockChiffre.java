package main.blockChiffre;

import main.resource.Resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class FromDecimalBlockChiffre {
    public static String encrypt(final List<BigInteger> plaintext, final int numberBase, final int blockSize) {
        StringBuilder stringBuilder = new StringBuilder();
        var wrappedNumberBase = BigInteger.valueOf(numberBase);
        plaintext.stream().map(
                (BigInteger block) -> {
                    var string = unicodeGAdicDevelopment(block, wrappedNumberBase);

                    if (string.length() < blockSize) {
                        var diff = blockSize - string.length();
                        return "\u0000".repeat(diff) + string; // Padding vor dem Block ergänzt.
                    } else {
                        return string;
                    }
                }
        ).forEachOrdered(stringBuilder::append);
        return stringBuilder.toString();
    }

    public static List<BigInteger> decrypt(String plaintext, int numberBase, int blockSize) {
        List<String> stringChunks = new ArrayList<>();
        for (int i = 0; i < plaintext.length(); i += blockSize) {
            stringChunks.add(plaintext.substring(i, Math.min(i + blockSize, plaintext.length())));
        }

        List<List<Integer>> encodedCharsChunks = new ArrayList<>();
        for (String chunk : stringChunks) {
            List<Integer> chunkIntegers = new ArrayList<>();
            for (char c : chunk.toCharArray()) {
                chunkIntegers.add((int) c);
            }
            encodedCharsChunks.add(chunkIntegers);
        }

        List<BigInteger> result = new ArrayList<>();
        for (List<Integer> chunk : encodedCharsChunks) {
            BigInteger acc = Resource.ZERO;
            BigInteger accBase = Resource.ONE;
            for (int i = chunk.size() - 1; i >= 0; i--) {
                BigInteger product = accBase.multiply(BigInteger.valueOf(chunk.get(i)));
                acc = acc.add(product);
                accBase = accBase.multiply(BigInteger.valueOf(numberBase));
            }
            result.add(acc);
        }
        return result;
    }

    /**
     * Führt eine g-adische Entwicklung von einer Dezimalzahl zu einer Zielbasis aus.
     * Die Zielbasis wird dabei als höchstes Zeichen im Unicode-Zeichensatz interpretiert.
     */
    public static String unicodeGAdicDevelopment(final BigInteger source, final BigInteger targetBase) {
        var source_clone = source;
        var result = new StringBuilder();

        while (source_clone.signum() == 1) { // Laufen, während er > 0 ist
            var divideAndRemainder = source_clone.divideAndRemainder(targetBase);
            source_clone = divideAndRemainder[0];
            var remainder = divideAndRemainder[1];
            var character = remainder.intValueExact();
            result.append((char) character);
        }
        return result.reverse().toString();
    }
}
