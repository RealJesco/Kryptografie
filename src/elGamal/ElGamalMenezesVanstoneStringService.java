package elGamal;

import FiniteFieldEllipticCurve.*;
import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import blockChiffre.FromDecimalBlockChiffre;
import blockChiffre.ToDecimalBlockChiffre;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ElGamalMenezesVanstoneStringService {
    private static BigInteger hashAndConvertMessageToBigInteger(String message) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, hashbytes);
    }

    public static ElGamalMenezesVanstoneMessage encrypt(final PublicKey key, final String message, int numberBase, FiniteFieldEllipticCurve ellipticCurve) {
        int blockSize = (int) (ellipticCurve.getModuleR().bitLength() * (Math.log(2) / Math.log(55296)));

        List<BigInteger> blocks = ToDecimalBlockChiffre.encrypt(message, 55296, blockSize);

        if(blocks.size() % 2 != 0) {
            blocks.add(BigInteger.valueOf(0));
        }

        List<CipherMessage> encryptedBlocks = new ArrayList<CipherMessage>();
        for (int i = 0; i < blocks.size(); i+=2) {
            Message clearMessage = new Message(blocks.get(i), blocks.get(i+1));
            CipherMessage cipherMessage = ElGamalMenezesVanstoneService.encrypt(clearMessage, key);
            encryptedBlocks.add(cipherMessage);
        }

        List<EllipticCurvePoint>  sentCipherMessagePoints = new ArrayList<EllipticCurvePoint>();
        List<BigInteger> sentCipherMessageBs = new ArrayList<BigInteger>();

        for (int i = 0; i < encryptedBlocks.size(); i++) {
            sentCipherMessagePoints.add(encryptedBlocks.get(i).point());
            sentCipherMessageBs.add(encryptedBlocks.get(i).b1());
            sentCipherMessageBs.add(encryptedBlocks.get(i).b2());
        }

        String cipherMessage =  FromDecimalBlockChiffre.encrypt(sentCipherMessageBs, numberBase, blockSize + 1);


        return new ElGamalMenezesVanstoneMessage(sentCipherMessagePoints, cipherMessage);

    }

    public static String decrypt(final PrivateKey key, final ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneCipherMessage, int numberBase, FiniteFieldEllipticCurve ellipticCurve) {
        int blockSize = (int) (ellipticCurve.getModuleR().bitLength() * (Math.log(2) / Math.log(numberBase)));

        List<CipherMessage> receivedCipherMessagePoints = new ArrayList<CipherMessage>();
        List<BigInteger> receivedCipherMessageBs = FromDecimalBlockChiffre.decrypt(elGamalMenezesVanstoneCipherMessage.getCipherMessageString(), 55296, blockSize + 1);


        for (int i = 0; i < receivedCipherMessageBs.size(); i+=2) {
            CipherMessage cipherMessage = new CipherMessage(elGamalMenezesVanstoneCipherMessage.getCipherMessagePoints().get(i/2), receivedCipherMessageBs.get(i), receivedCipherMessageBs.get(i+1));
            receivedCipherMessagePoints.add(cipherMessage);
        }



        List<Message> decryptedBlocks = new ArrayList<Message>();
        for (int i = 0; i < receivedCipherMessagePoints.size(); i++) {
            Message decryptedMessage = ElGamalMenezesVanstoneService.decrypt(receivedCipherMessagePoints.get(i), key);
            decryptedBlocks.add(decryptedMessage);
        }


        List<BigInteger> decryptedText = new ArrayList<>();
        for (int i = 0; i < decryptedBlocks.size(); i++) {
            decryptedText.add(decryptedBlocks.get(i).m1());
            decryptedText.add(decryptedBlocks.get(i).m2());
        }


        return ToDecimalBlockChiffre.decrypt(decryptedText, numberBase);

    }

    public static String sign(final KeyPair key, final String message, int numberBase) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.publicKey.ellipticCurve().getModuleR().bitLength() * (Math.log(2) / Math.log(numberBase)));
        BigInteger hashedMessage = hashAndConvertMessageToBigInteger(message);
        MenezesVanstoneSignature menezesVanstoneSignature = ElGamalMenezesVanstoneService.sign(key, hashedMessage);
        return FromDecimalBlockChiffre.encrypt(List.of(menezesVanstoneSignature.r(), menezesVanstoneSignature.s()), 55296, blockSize + 1);
    }

    public static boolean verify(final PublicKey key, final String message, final String signature, int numberBase) throws NoSuchAlgorithmException {
        int blockSize = (int) (key.ellipticCurve().getModuleR().bitLength() * (Math.log(2) / Math.log(numberBase)));
        BigInteger hashedMessage = hashAndConvertMessageToBigInteger(message);
        List<BigInteger> menezesVanstoneSignatureList = FromDecimalBlockChiffre.decrypt(signature, 55296, blockSize + 1);
        MenezesVanstoneSignature menezesVanstoneSignature = new MenezesVanstoneSignature(menezesVanstoneSignatureList.get(0), menezesVanstoneSignatureList.get(1));
        return ElGamalMenezesVanstoneService.verify(key, hashedMessage, menezesVanstoneSignature );
    }
}
