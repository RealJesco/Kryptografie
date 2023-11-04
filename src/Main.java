import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        CommunicationPanel.getInstance();

        System.out.println(MathMethods.alternativeQuickExponentiation(new BigInteger("5"), new BigInteger("1"), new BigInteger("11")).toString());
        System.out.print("\n");
        Boolean noMistake = true;
        for(int i = 0; i<1; i++){
            BigInteger n1 = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000"));
            BigInteger n2 = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000"));
//            noMistake = noMistake && MathMethods.expandedEuklid(n1,n2).equals(n1.gcd(n2));
            long start = System.nanoTime();
            MathMethods.extendedEuclidean(n1,n2);
            long end = System.nanoTime();
            System.out.println(end-start);
            BigInteger a = new BigInteger("56");
            BigInteger b = new BigInteger("15");
            BigInteger[] result = MathMethods.extendedEuclidean(a,b);
            System.out.println(result[0].toString());
            System.out.println(result[1].toString());
            System.out.println(result[2].toString());
        }
        System.out.println(noMistake);

        noMistake = true;
        for(int i = 0; i<1; i++){
//            BigInteger n = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
//            BigInteger exp = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
//            BigInteger mod = getRandomBigInteger(new BigInteger("5000000000000000000000000000000000000000000000"));
            BigInteger n = new BigInteger("5345890").pow(50).pow(40);
            BigInteger exp = new BigInteger("561563").pow(50);
            BigInteger mod = new BigInteger("402").pow(453);
//            noMistake = noMistake && MathMethods.alternativeQuickExponentation(n,exp,mod).equals(n.modPow(exp,mod));
            long start = System.nanoTime();
            MathMethods.alternativeQuickExponentiation(n,exp,mod);
            long end = System.nanoTime();
            System.out.println(end-start);
        }
        System.out.println(noMistake);


        BigInteger num1 = new BigInteger("12");
        System.out.println("Miller rabin for value: " + num1 + " "+ MathMethods.millerRabinTest(num1, 3));
        BigInteger n1 = new BigInteger("13");
        System.out.println("Miller rabin for value: " + n1 + " " + MathMethods.millerRabinTest(n1, 3));
        /*
        System.out.println(MathMethods.expandedEuklid(new BigInteger("6267"), new BigInteger("354")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("354"), new BigInteger("6267")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("661643"), new BigInteger("315")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("315"), new BigInteger("661643")));
        System.out.println(MathMethods.expandedEuklid(new BigInteger("661643"), new BigInteger("661643")));
        BitSet bitSet = BitSet.valueOf(new BigInteger("655360").toByteArray());
        for(int i = 0; i< bitSet.length();i++){
            System.out.print(bitSet.get(i));
        }
        System.out.print("\n");

        System.out.println(bitSet);
        System.out.println(bitSet.toString());*/

        //call blockCipherEncrypt
        // list of parameters: BigInteger n, BigInteger e, BigInteger d, BigInteger message
        List<Integer> codeMessage = new ArrayList<>();
//        12 0 19 7 4 12 0 19
//        codeMessage.add(new BigInteger("12"));
//        codeMessage.add(new BigInteger("0"));
//        codeMessage.add(new BigInteger("19"));
//        codeMessage.add(new BigInteger("7"));
//        codeMessage.add(new BigInteger("4"));
//        codeMessage.add(new BigInteger("12"));
//        codeMessage.add(new BigInteger("0"));
//        codeMessage.add(new BigInteger("19"));
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);
        codeMessage.add(7);
        codeMessage.add(4);
        codeMessage.add(12);
        codeMessage.add(0);
        codeMessage.add(19);
        List<BigInteger> result = MathMethods.prepareMessageForEncryption(codeMessage, 8, 47);
        System.out.println(result);


//        TODO @Mattis implement tests for the following logic. Encryption and decryption works, but needs tests. It is missing proper function implementation, block cipher and signature.
//        TODO @Jesco this is how you will roughly implement the RSA encryption for the masks. You can use this as a reference. It is WIP!!! Subject to change.
//        Create RSA encryption using MathMethods
        BigInteger upperLimit = new BigInteger("2").pow(64);
        BigInteger possibleP = getRandomBigInteger(upperLimit);
        BigInteger possibleQ = getRandomBigInteger(upperLimit);
        while(!MathMethods.millerRabinTest(possibleP, 3)){
            possibleP = getRandomBigInteger(upperLimit);
        }
        while(!MathMethods.millerRabinTest(possibleQ, 3)){
            possibleQ = getRandomBigInteger(upperLimit);
        }
        BigInteger n = possibleP.multiply(possibleQ);
//        n should be bigger than 55296^7
        while(n.compareTo(new BigInteger("55296").pow(7)) < 0){
            possibleP = getRandomBigInteger(upperLimit);
            possibleQ = getRandomBigInteger(upperLimit);
            while(!MathMethods.millerRabinTest(possibleP, 3)){
                possibleP = getRandomBigInteger(upperLimit);
            }
            while(!MathMethods.millerRabinTest(possibleQ, 3)){
                possibleQ = getRandomBigInteger(upperLimit);
            }
            n = possibleP.multiply(possibleQ);
        }
        System.out.println("n: " + n);
        BigInteger phi = possibleP.subtract(BigInteger.ONE).multiply(possibleQ.subtract(BigInteger.ONE));
//        get e from interval 1<e<phi(n). Use 65537 if that is smaller than phi(n). If that is too big use 3, else getRandomBigInteger that is prime and unequal phi(n)
        BigInteger e = BigInteger.valueOf(65537);
        System.out.println("phi: " + phi);
        e = getRandomBigInteger(phi.subtract(BigInteger.ONE));
//        e should be prime and smaller than phi and not have a common divisor with phi
        while(e.compareTo(phi) >= 0 || !MathMethods.millerRabinTest(e, 3) || !MathMethods.extendedEuclidean(e, phi)[0].equals(BigInteger.ONE)){
            e = getRandomBigInteger(phi);
        }
//        if(e > phi){
//            e = BigInteger.valueOf(3);
//        }
        BigInteger d = MathMethods.extendedEuclidean(e, phi)[1];
        if(d.compareTo(BigInteger.ZERO) < 0){
            d = d.multiply(BigInteger.valueOf(-1));
        }

        // Example: Encrypting and Decrypting a message
        BigInteger message = new BigInteger("12345"); // Example message
        System.out.println("e: " + e);
        System.out.println("d: " + d);
        // Encryption
        System.out.println("Message: " + message);
//        BigInteger encryptedMessage = MathMethods.encrypt(message, e, n);
        BigInteger encryptedMessage = MathMethods.alternativeQuickExponentiation(message, e, n);
        System.out.println("Encrypted Message: " + encryptedMessage);

        // Decryption
//        BigInteger decryptedMessage = MathMethods.decrypt(encryptedMessage, d, n);
        BigInteger decryptedMessage = MathMethods.alternativeQuickExponentiation(encryptedMessage, d, n);
        System.out.println("Decrypted Message: " + decryptedMessage);
//        n = new BigInteger("791569306435939");
//        e = new BigInteger("15485863");
//        RSAencrypt
        String encryptedMessageMathemat = MathMethods.rsaEncrypt("Mathematik", e, n);
//        RSAdecrypt
        MathMethods.rsaDecrypt(encryptedMessageMathemat, d, n);
    }

    public static BigInteger getRandomBigInteger(BigInteger upperLimit){
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), new Random());
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }
}
