package test.encryption;

import main.finiteFieldEllipticCurve.SecureFiniteFieldEllipticCurve;
import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneStringService;
import main.elGamalMenezesVanstone.KeyPair;
import main.encryption.EncryptionContext;
import main.encryption.EncryptionContextParamBuilder;
import org.junit.jupiter.api.Test;
import main.rsa.*;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class EncryptionStrategyTest {
    EncryptionContext context = new EncryptionContext();

    /**
     * Test the main.encryption and decryption of a message
     * secure elliptic curve (128, 5, 100, 13)
     * key pair generation
     * message "Hello, World!"
     * number base 55296
     * @expected: param data equal to string after decryption
     */
    @Test
    void testEncryptElGamalMenezesVanstoneStringStrategy() {
        BigInteger m = BigInteger.valueOf(13);
        EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();

        context.setStrategy(new ElGamalMenezesVanstoneStringService());
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, m);
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve,m);
        builder.withElGamalMenezesVanstoneKeyPair(keyPair);
        builder.withNumberBase(55296);
        builder.setData("Hello, World!");

        Map<String, Object> encryptionParams = builder.build();
        ElGamalMenezesVanstoneMessage encryptedMessage = (ElGamalMenezesVanstoneMessage) context.encrypt(builder.getData(), encryptionParams);
        String encrypted = encryptedMessage.getCipherMessageString();
        System.out.println(encrypted);

        EncryptionContextParamBuilder decryptionBuilder = new EncryptionContextParamBuilder();
        decryptionBuilder.withElGamalMenezesVanstoneCipherMessage(encryptedMessage);
        decryptionBuilder.withNumberBase(55296);
        decryptionBuilder.withElGamalMenezesVanstonePrivateKey(keyPair.getPrivateKey());
        Map<String, Object> decryptionParams = decryptionBuilder.build();

        String decrypted = context.decrypt(encrypted, decryptionParams);
        System.out.println(decrypted);

        assert decrypted.equals(builder.getData());
    }

    /**
     * Test the main.encryption and decryption of a message
     * secure elliptic curve (128, 5, 100, 13)
     * key pair generation
     * message "Hello, World!"
     * number base 55296
     * @expected: verification of the signature is successful
     * @expected: verification of the signature is unsuccessful
     */
    @Test
    void testElGamalMenezesVanstoneSignStringStrategy() throws NoSuchAlgorithmException {
        BigInteger m = BigInteger.valueOf(13);
        context.setStrategy(new ElGamalMenezesVanstoneStringService());
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, m);
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve, m);

        EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();
        builder.withElGamalMenezesVanstoneKeyPair(keyPair);
        builder.withNumberBase(55296);
        builder.setData("Hello, World!");

        Map<String, Object> encryptionParams = builder.build();
        String message = builder.getData();
        String signature = (String) context.sign(message, encryptionParams);
        System.out.println(signature);

        EncryptionContextParamBuilder decryptionBuilder = new EncryptionContextParamBuilder();
        decryptionBuilder.withElGamalMenezesVanstonePublicKey(keyPair.getPublicKey());
        decryptionBuilder.withNumberBase(55296);
        decryptionBuilder.setData(message);
        decryptionBuilder.withElGamalMenezesVanstoneCipherMessage(signature);
        Map<String, Object> decryptionParams = decryptionBuilder.build();

        boolean verified = context.verify(message, signature, decryptionParams);
        System.out.println(verified);

        assert verified;

        //assert unequal signature

        String message2 = "Hello, World!";
        signature = signature + "e";
        boolean verified2 = context.verify(message2, signature, decryptionParams);
        System.out.println(verified2);
        assert !verified2;
    }

    /**
     * Test the main.encryption and decryption of a message (main.rsa strategy
     * key pair generation (2048, 100, 13)
     * message "Hello, World!"
     * number base 55296
     * @expected: message parameters are equal after decryption
     */
    @Test
    void testEncryptRsaStringStrategy() {
        KeyPairRsa keyPairRsa = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(13));

        EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();

        builder.withRsaKeyPair(keyPairRsa);
        builder.withNumberBase(55296);
        builder.setData("Hello, World!");

        Map<String, Object> encryptionParams = builder.build();

        context.setStrategy(new RsaStringService());

        String message = "Hello, World!";
        String encryptedMessage = (String) context.encrypt(message, encryptionParams);
        System.out.println(encryptedMessage);

        EncryptionContextParamBuilder decryptionBuilder = new EncryptionContextParamBuilder();
        decryptionBuilder.withRsaPrivateKey(keyPairRsa.privateKeyRsa());
        decryptionBuilder.withNumberBase(55296);
        decryptionBuilder.setData(encryptedMessage);
        Map<String, Object> decryptionParams = decryptionBuilder.build();

        String decrypted = (String) context.decrypt(encryptedMessage, decryptionParams);
        System.out.println(decrypted);

        assert decrypted.equals(message);

    }

    /**
     * Test the main.encryption and decryption of a message
     * key pair generation (2048, 100, 13) (main.rsa strategy)
     * message "Hello, World!"
     * number base 55296
     * @expected: verification of the signature is successful
     */
    @Test
    void testRsaSignStringStrategy() throws NoSuchAlgorithmException {
        EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();
        KeyPairRsa keyPairRsa = RsaService.generateKeyPair(2048, 100, BigInteger.valueOf(13));
        builder.withRsaKeyPair(keyPairRsa);
        builder.withNumberBase(55296);

        Map<String, Object> encryptionParams = builder.build();

        context.setStrategy(new RsaStringService());

        String message = "Hello, World!";
        String signature = (String) context.sign(message, encryptionParams);
        System.out.println(signature);

        EncryptionContextParamBuilder decryptionBuilder = new EncryptionContextParamBuilder();
        decryptionBuilder.withRsaPublicKey(keyPairRsa.publicKeyRsa());
        decryptionBuilder.withNumberBase(55296);
        decryptionBuilder.setData(message);
        Map<String, Object> decryptionParams = decryptionBuilder.build();

        boolean verified = context.verify(message, signature, decryptionParams);
        System.out.println(verified);

        assert verified;
    }

    //TODO: Test withK and withKy
}
