package encryption;

import FiniteFieldEllipticCurve.SecureFiniteFieldEllipticCurve;
import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import elGamal.ElGamalMenezesVanstoneStringService;
import elGamal.KeyPair;
import elGamal.PublicKey;
import org.junit.jupiter.api.Test;
import rsa.*;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class EncryptionStrategyTest {
    EncryptionContext context = new EncryptionContext();

    @Test
    void testEncryptElGamalMenezesVanstoneStringStrategy() {
        EncryptionContextParamBuilder builder = new EncryptionContextParamBuilder();


        context.setStrategy(new ElGamalMenezesVanstoneStringService());
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);
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

    @Test
    void testElGamalMenezesVanstoneSignStringStrategy() throws NoSuchAlgorithmException {

        context.setStrategy(new ElGamalMenezesVanstoneStringService());
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(secureFiniteFieldEllipticCurve);

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
    }

    @Test
    void testEncryptRsaStringStrategy(){
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
}
