package rsa;

import java.math.BigInteger;

public final class RsaStringService {

    public static String encrypt(final PublicKeyRsa key, final String message){
        throw new Error("TODO");
        // BlockChiffre toDecimal Encrypt

        // Für jeden Block einzeln RSA.encrypt aufrufen

        // BlockChiffre fromDecimal Encrypt
    }

    public static String decrypt(final PrivateKeyRsa key, final String ciphertext) {
        throw new Error("TODO");
        // BlockChiffre fromDecimal Decrypt

        // Für jeden Block einzeln RSA.decrypt aufrufen

        // BlockChiffre toDecimal Decrypt
    }

    public static String sign(final PrivateKeyRsa key, final String message) {
        throw new Error("TODO");
        // Message mit SHA-256 hashen

        // BlockChiffre toDecimal Encrypt auf hash anwenden

        // Für jeden Block einzeln RSA.sign aufrufen

        // BlockChiffre fromDecimal Encrypt
    }

    public static boolean verify(final PublicKeyRsa key, final String message, final String signature) {
        throw new Error("TODO");
        // Message mit SHA-256 hashen

        // BlockChiffre toDecimal Encrypt auf hash anwenden

        // BlockChiffre fromDecimal Decrypt auf Signatur anwenden

        // Anschließend beide Listen Elementweise mit RSA.verify prüfen und ergebnis ver-unden
    }
}
