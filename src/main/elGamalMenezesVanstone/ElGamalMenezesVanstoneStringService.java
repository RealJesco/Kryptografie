package main.elGamalMenezesVanstone;

import main.elGamalMenezesVanstone.records.*;
import main.finiteFieldEllipticCurve.*;
import main.GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import main.blockChiffre.FromDecimalBlockChiffre;
import main.blockChiffre.ToDecimalBlockChiffre;
import main.encryption.StringEncryptionStrategy;
import main.resource.Resource;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ElGamalMenezesVanstoneStringService implements StringEncryptionStrategy {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static BigInteger hashAndConvertMessageToBigInteger(String message) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, hashbytes);
    }

    /**
     * Encrypts a message using the public key and a specific number base.
     *
     * @param key The public key to encrypt the message with.
     * @param message The message to be encrypted.
     * @param numberBase The number base to be used for the main.encryption.
     * @return The encrypted message as an ElGamalMenezesVanstoneMessage.
     */
    public static ElGamalMenezesVanstoneMessage encrypt(final PublicKey key, final String message, int numberBase, BigInteger m) {
        return encrypt(key, message, null, null, numberBase, m);
    }

    /**
     * Encrypts a message using the public key, a specific number base, and provided elliptic curve parameters.
     *
     * @param key The public key to encrypt the message with.
     * @param message The message to be encrypted.
     * @param k The scalar multiplication factor.
     * @param ky The elliptic curve point resulting from scalar multiplication.
     * @param numberBase The number base to be used for the main.encryption.
     * @return The encrypted message as an ElGamalMenezesVanstoneMessage.
     */
    public static ElGamalMenezesVanstoneMessage encrypt(final PublicKey key, final String message, BigInteger k, EllipticCurvePoint ky, int numberBase, BigInteger m) {
        FiniteFieldEllipticCurve ellipticCurve = key.ellipticCurve();
        int blockSize = calculateBlockSize(ellipticCurve.getP(), numberBase);
        List<BigInteger> blocks = ToDecimalBlockChiffre.encrypt(message, numberBase, blockSize);
        normalizeBlocks(blocks);

        List<CipherMessage> encryptedBlocks = parallelEncryptBlocks(blocks, key, k, ky, m);
        return constructElGamalMenezesVanstoneMessage(encryptedBlocks, numberBase, blockSize);
    }

    private static int calculateBlockSize(BigInteger prime, int numberBase) {
        return (int) (prime.bitLength() * (Math.log(2) / Math.log(numberBase)));
    }

    private static void normalizeBlocks(List<BigInteger> blocks) {
        if (blocks.size() % 2 != 0) {
            blocks.add(Resource.ZERO);
        }
    }

    private static List<CipherMessage> encryptBlocks(List<BigInteger> blocks, PublicKey key, BigInteger k, EllipticCurvePoint ky, BigInteger m) {
        List<CipherMessage> encryptedBlocks = new ArrayList<>();
        for (int i = 0; i < blocks.size(); i += 2) {
            Message clearMessage = new Message(blocks.get(i), blocks.get(i + 1));
            CipherMessage cipherMessage = (k == null || ky == null)
                    ? ElGamalMenezesVanstoneService.encrypt(clearMessage, key, m)
                    : ElGamalMenezesVanstoneService.encrypt(clearMessage, key, k, ky);
            encryptedBlocks.add(cipherMessage);
        }
        return encryptedBlocks;
    }

    public static List<CipherMessage> parallelEncryptBlocks(List<BigInteger> blocks, PublicKey key, BigInteger k, EllipticCurvePoint ky, BigInteger m) {
        int pairCount = blocks.size() / 2;

        return forkJoinPool.submit(() -> IntStream.range(0, pairCount).parallel().mapToObj(i -> {
            int index = i * 2;
            Message clearMessage = new Message(blocks.get(index), blocks.get(index + 1));
            return (k == null || ky == null)
                    ? ElGamalMenezesVanstoneService.encrypt(clearMessage, key, m)
                    : ElGamalMenezesVanstoneService.encrypt(clearMessage, key, k, ky);
        }).collect(Collectors.toList())).join();
    }



    private static ElGamalMenezesVanstoneMessage constructElGamalMenezesVanstoneMessage(List<CipherMessage> encryptedBlocks, int numberBase, int blockSize) {
        List<EllipticCurvePoint> points = new ArrayList<>();
        List<BigInteger> messageComponents = new ArrayList<>();

        for (CipherMessage encryptedBlock : encryptedBlocks) {
            points.add(encryptedBlock.point());
            messageComponents.add(encryptedBlock.b1());
            messageComponents.add(encryptedBlock.b2());
        }

        String cipherMessage = FromDecimalBlockChiffre.encrypt(messageComponents, numberBase, blockSize + 1);
        return new ElGamalMenezesVanstoneMessage(points, cipherMessage);
    }

    /**
     * @param key The private key to decrypt the message with
     * @param elGamalMenezesVanstoneCipherMessage The message to be decrypted
     * @param numberBase The number base to be used for the decryption (e.g. 55296)
     * @return The decrypted message
     */
    public static String decrypt(final PrivateKey key, final ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage, int numberBase) {
        FiniteFieldEllipticCurve ellipticCurve = key.ellipticCurve();
        int blockSize = (int) (ellipticCurve.getP().bitLength() * (Math.log(2) / Math.log(numberBase)));

        List<CipherMessage> receivedCipherMessagePoints = new ArrayList<CipherMessage>();
        List<BigInteger> receivedCipherMessageBs = FromDecimalBlockChiffre.decrypt(elGamalMenezesVanstoneCipherMessage.getCipherMessageString(), numberBase, blockSize + 1);

        if(receivedCipherMessageBs.size() % 2 != 0) {
            throw new IllegalArgumentException("The number of cipher points is not even, so the cipher message is either corrupted or not valid.");
        }


        for (int i = 0; i < receivedCipherMessageBs.size(); i+=2) {
            CipherMessage cipherMessage = new CipherMessage(elGamalMenezesVanstoneCipherMessage.getCipherMessagePoints().get(i/2), receivedCipherMessageBs.get(i), receivedCipherMessageBs.get(i+1));
            receivedCipherMessagePoints.add(cipherMessage);
        }



        List<BigInteger> decryptedText = parallelDecryptBlocks(receivedCipherMessagePoints, key);

        return ToDecimalBlockChiffre.decrypt(decryptedText, numberBase);
    }


    private static List<BigInteger> parallelDecryptBlocks(List<CipherMessage> cipherMessages, PrivateKey key) {
        List<Message> decryptedBlocks = forkJoinPool.submit(() -> cipherMessages.parallelStream()
                .map(cipherMessage -> ElGamalMenezesVanstoneService.decrypt(cipherMessage, key))
                .collect(Collectors.toList())).join();

        List<BigInteger> decryptedText = new ArrayList<>();

        for (Message decryptedBlock : decryptedBlocks) {
            decryptedText.add(decryptedBlock.m1());
            decryptedText.add(decryptedBlock.m2());
        }

        return decryptedText;
    }


    /**
     * @param key The key to sign the message with
     * @param message The message to be signed
     * @param numberBase The number base to be used for the signing (e.g. 55296)
     * @return The signature of the message
     * @throws NoSuchAlgorithmException if the SHA3-256 algorithm is not available/applicable
     */
    public static String sign(final KeyPair key, final String message, int numberBase, BigInteger m) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.publicKey.ellipticCurve().getP().bitLength() * (Math.log(2) / Math.log(numberBase)));
        BigInteger hashedMessage = hashAndConvertMessageToBigInteger(message);
        MenezesVanstoneSignature menezesVanstoneSignature = ElGamalMenezesVanstoneService.sign(key, hashedMessage, m);
        return FromDecimalBlockChiffre.encrypt(List.of(menezesVanstoneSignature.r(), menezesVanstoneSignature.s()), numberBase, blockSize + 1);
    }

    /**
     * @param key The key to verify the message with
     * @param message The message to be verified
     * @param signature The signature of the message
     * @param numberBase The number base to be used for the verification (e.g. 55296)
     * @return true if the signature is valid, false otherwise
     * @throws NoSuchAlgorithmException if the SHA3-256 algorithm is not available/applicable
     */
    public static boolean verify(final PublicKey key, final String message, final String signature, int numberBase) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.ellipticCurve().getP().bitLength() * (Math.log(2) / Math.log(numberBase)));
        BigInteger hashedMessage = hashAndConvertMessageToBigInteger(message);
        List<BigInteger> menezesVanstoneSignatureList = FromDecimalBlockChiffre.decrypt(signature, numberBase, blockSize + 1);
        if(menezesVanstoneSignatureList.size() != 2) {
            return false;
        }
        MenezesVanstoneSignature menezesVanstoneSignature = new MenezesVanstoneSignature(menezesVanstoneSignatureList.get(0), menezesVanstoneSignatureList.get(1));
        return ElGamalMenezesVanstoneService.verify(key, hashedMessage, menezesVanstoneSignature );
    }

    /**
     *
     * @param data the string to be encrypted
     * @param params the parameters for the main.encryption --> EncryptionContextParamBuilder
     * @return - The encrypted message as a ElGamalMenezesVanstoneMessage containing the cipher string and the specific point of the curve
     */
    @Override
    public Object encrypt(String data, Map<String, Object> params) {
        KeyPair key = (KeyPair) params.get("KeyPair");
        if(params.get("k") != null || params.get("ky") != null) {
            //System.out.println("Encrypting with k and ky: " + params.get("k") + " " + params.get("ky"));
            return encrypt(key.getPublicKey(), data, (BigInteger) params.get("k"), (EllipticCurvePoint) params.get("ky"), (int) params.get("numberBase"), (BigInteger) params.get("m"));
        } else {
            return encrypt(key.getPublicKey(), data, (int) params.get("numberBase"), (BigInteger) params.get("m"));
        }
    }

    /**
     *
     * @param data the string to be decrypted
     * @param params the parameters for the decryption --> EncryptionContextParamBuilder
     * @return - The decrypted message
     */
    @Override
    public String decrypt(String data, Map<String, Object> params) {
        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage = (ElGamalMenezesVanstoneMessage) params.get("elGamalMenezesVanstoneCipherMessage");
        assert elGamalMenezesVanstoneCipherMessage != null;
        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneMessage = new ElGamalMenezesVanstoneMessage(elGamalMenezesVanstoneCipherMessage.getCipherMessagePoints(), data);
        PrivateKey privateKey = (PrivateKey) params.get("PrivateKey");
        assert privateKey != null;
        return decrypt((PrivateKey) params.get("PrivateKey"), elGamalMenezesVanstoneMessage, (int) params.get("numberBase"));
    }

    /**
     *
     * @param data the string to be signed
     * @param params the parameters for the signing --> EncryptionContextParamBuilder
     * @return - The signature of the message
     */
    @Override
    public String sign(String data, Map<String, Object> params) {
        try {
            return sign((KeyPair) params.get("KeyPair"), data, (int) params.get("numberBase"), (BigInteger) params.get("m"));
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param data the string to be verified
     * @param signature the signature of the message
     * @param params the parameters for the verification --> EncryptionContextParamBuilder
     * @return - true if the signature is valid, false otherwise
     */
    @Override
    public boolean verify(String data, String signature, Map<String, Object> params) {
        try {
            return verify((PublicKey) params.get("PublicKey"), data, signature, (int) params.get("numberBase"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
