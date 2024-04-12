package elGamal;

import FiniteFieldEllipticCurve.*;
import GUI.HelperClasses.ElGamalMenezesVanstoneMessage;
import blockChiffre.FromDecimalBlockChiffre;
import blockChiffre.ToDecimalBlockChiffre;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalServiceTest {

    @Test
    void encrypt() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        PrivateKey privateKey = new PrivateKey(ellipticCurve, BigInteger.valueOf(80));
        EllipticCurvePoint generator = new FiniteFieldEcPoint(BigInteger.valueOf(115), BigInteger.valueOf(253));
        EllipticCurvePoint groupElement = new FiniteFieldEcPoint(BigInteger.valueOf(575), BigInteger.valueOf(481));
        PublicKey publicKey = new PublicKey(ellipticCurve, generator, groupElement);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, privateKey);

        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void testKeyGeneration() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(821));
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }

    @Test
    void testKeyGenerationWithSecureEllipticCurve() {
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(BigInteger.valueOf(128), BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        Message message = new Message(BigInteger.valueOf(3), BigInteger.valueOf(8));

        CipherMessage cipherMessage = ElGamalService.encrypt(message, keyPair.publicKey);

        Message decryptedMessage = ElGamalService.decrypt(cipherMessage, keyPair.privateKey);

        assertNotEquals(message.m1(), cipherMessage.b1());
        assertNotEquals(message.m2(), cipherMessage.b2());
        assertEquals(message.m1(), decryptedMessage.m1());
        assertEquals(message.m2(), decryptedMessage.m2());
    }
    @Test
    void fullTextCycle() {
        //TODO This is how you can test the full cycle of encryption and decryption. This needs to be automated as a service.
        BigInteger bitLengthP = BigInteger.valueOf(128);
        SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve = new SecureFiniteFieldEllipticCurve(bitLengthP, BigInteger.valueOf(5), 100, BigInteger.valueOf(13));
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        KeyPair keyPair = new KeyPair();
        keyPair.generateKeyPair(ellipticCurve);

        int blockSize = (int) (ellipticCurve.getModuleR().bitLength() * (Math.log(2) / Math.log(55296)));
//        System.out.println(blockSize);
//        String text = "Hello, World! This is a test of a really long text! do you like it? I hope so!";
        String text = "Ao0WDF!M57XkWm%ysCw1dUw0FoJ31tChJ1ajJ&NN2N2HuektYRJ703q20PYBjkGf4Shw0@GH42$Qpf!C6&UMU6uh94wyVuaQpEdJ\n" +
                "D*rQ@!ojFvUaH%T&FQ=#fAqSZV9pXZFqz9jHuCj&AuZned3&f#=HTFtcPp#ms&fvEyFNtaoMtzN4X2&g!oy84d#Rd@@nTbG9*aeW\n" +
                "Tx3@Dt5#=AVkS1aP#h@j+MPpF%=Y*s$39eYu=htZfsro0yJPwTjywSf7b8=T8O2evWQThMpemq7@GFjhYn*XZTaQA@PjGRKE%*XS\n" +
                "bH$KzS$%%=TYHbDuyHaU3EKj2emwN3QrBpPNtBvCGYp&nd*gza%vXcgyKSpCb4%E$xGtBrWetsudsMKU$@Sttg5&s4zZ@@DSYp%t\n" +
                "bo%1TCYHdh2rCh!J#qAAU2hBO7d6CaEotm7Ju!y1RQAPWzQG9K$FT#GJJCcQKBNUJzQw3w4Upo+Eov%P@V@v@E23OAm3m%kUuRvJ";
        List<BigInteger> blocks = ToDecimalBlockChiffre.encrypt(text, 55296, blockSize);
//        System.out.println(blocks.size());

        if(blocks.size() % 2 != 0) {
            blocks.add(BigInteger.valueOf(0));
        }

        List<CipherMessage> encryptedBlocks = new ArrayList<CipherMessage>();
        for (int i = 0; i < blocks.size(); i+=2) {
//            System.out.println(i + " " + blocks.size());
            Message message = new Message(blocks.get(i), blocks.get(i+1));
            CipherMessage cipherMessage = ElGamalService.encrypt(message, keyPair.publicKey);
            encryptedBlocks.add(cipherMessage);
        }

        System.out.println("Encrypted: " + encryptedBlocks.size());
//        System.out.println(encryptedBlocks);

        //-- This is how the message should probably be wrapped --

        List<EllipticCurvePoint>  sentCipherMessagePoints = new ArrayList<EllipticCurvePoint>();
        List<BigInteger> sentCipherMessageBs = new ArrayList<BigInteger>();

        for (int i = 0; i < encryptedBlocks.size(); i++) {
            sentCipherMessagePoints.add(encryptedBlocks.get(i).point());
            sentCipherMessageBs.add(encryptedBlocks.get(i).b1());
            sentCipherMessageBs.add(encryptedBlocks.get(i).b2());
        }

        System.out.println("Sent: " + sentCipherMessageBs.size());

        System.out.println("Sent list of Bs: " + sentCipherMessageBs);

        String cipherString = FromDecimalBlockChiffre.encrypt(sentCipherMessageBs, 55296, blockSize + 1);

        System.out.println(cipherString);

        System.out.println("Sent amount of points: " + sentCipherMessagePoints.size());

        ElGamalMenezesVanstoneMessage elGamalMenezesVanstoneMessage = new ElGamalMenezesVanstoneMessage(sentCipherMessagePoints, cipherString);


        List<CipherMessage> receivedCipherMessagePoints = new ArrayList<CipherMessage>();
        List<BigInteger> receivedCipherMessageBs = FromDecimalBlockChiffre.decrypt(elGamalMenezesVanstoneMessage.getCipherMessageString(), 55296, blockSize + 1);
        System.out.println("Received: " + receivedCipherMessageBs.size());
        System.out.println("Received amount of points: " + elGamalMenezesVanstoneMessage.getCipherMessagePoints().size());
        System.out.println("Received list of Bs: " + receivedCipherMessageBs);


       for (int i = 0; i < receivedCipherMessageBs.size(); i+=2) {
                CipherMessage cipherMessage = new CipherMessage(sentCipherMessagePoints.get(i/2), receivedCipherMessageBs.get(i), receivedCipherMessageBs.get(i+1));
                receivedCipherMessagePoints.add(cipherMessage);
        }



        List<Message> decryptedBlocks = new ArrayList<Message>();
        for (int i = 0; i < receivedCipherMessagePoints.size(); i++) {
            Message decryptedMessage = ElGamalService.decrypt(receivedCipherMessagePoints.get(i), keyPair.privateKey);
            decryptedBlocks.add(decryptedMessage);
        }


        List<BigInteger> decryptedText = new ArrayList<>();
        for (int i = 0; i < decryptedBlocks.size(); i++) {
            decryptedText.add(decryptedBlocks.get(i).m1());
            decryptedText.add(decryptedBlocks.get(i).m2());
        }


        String decryptedString = ToDecimalBlockChiffre.decrypt(decryptedText, 55296);


        assertEquals(text, decryptedString);

    }
}