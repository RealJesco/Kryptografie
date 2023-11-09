package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import mathMethods.MathMethods;

import static mathMethods.MathMethods.generateRandomPrime;


public class RSA {

    private static int millerRabinSteps = 0;
    private static int blockSize = 0;
    private static int blockSizePlusOne = 0;
    private static int numberSystemBase = 0;
    private static BigInteger n;
    private static BigInteger phiN;
    private static BigInteger e;
    private static BigInteger d;
    private static BigInteger p;
    private static BigInteger q;
    private static BigInteger m;
    private static BigInteger a;
    private static BigInteger b;
    private static BigInteger countOfN = BigInteger.valueOf(1);
    private static int bitLengthN = 128;
    private static final SecureRandom random = new SecureRandom();
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger[] SMALL_PRIMES = {
            BigInteger.valueOf(2),
            BigInteger.valueOf(3),
            BigInteger.valueOf(5),
            BigInteger.valueOf(7),
            BigInteger.valueOf(11),
            BigInteger.valueOf(13),
            BigInteger.valueOf(17),
            BigInteger.valueOf(19),
            BigInteger.valueOf(23),
            BigInteger.valueOf(29),
            BigInteger.valueOf(31),
            BigInteger.valueOf(37),
            BigInteger.valueOf(41),
            BigInteger.valueOf(43),
            BigInteger.valueOf(47),
            BigInteger.valueOf(53),
            BigInteger.valueOf(59),
            BigInteger.valueOf(61),
            BigInteger.valueOf(67),
            BigInteger.valueOf(71),
            BigInteger.valueOf(73),
            BigInteger.valueOf(79),
            BigInteger.valueOf(83),
            BigInteger.valueOf(89),
            BigInteger.valueOf(97),
            BigInteger.valueOf(101),
            BigInteger.valueOf(103),
            BigInteger.valueOf(107),
            BigInteger.valueOf(109),
            BigInteger.valueOf(113),
            BigInteger.valueOf(127),
            BigInteger.valueOf(131),
            BigInteger.valueOf(137),
            BigInteger.valueOf(139),
            BigInteger.valueOf(149),
            BigInteger.valueOf(151),
            BigInteger.valueOf(157),
            BigInteger.valueOf(163),
            BigInteger.valueOf(167),
            BigInteger.valueOf(173),
            BigInteger.valueOf(179),
            BigInteger.valueOf(181),
            BigInteger.valueOf(191),
            BigInteger.valueOf(193),
            BigInteger.valueOf(197),
            BigInteger.valueOf(199),
            BigInteger.valueOf(211),
            BigInteger.valueOf(223),
            BigInteger.valueOf(227),
            BigInteger.valueOf(229),
            BigInteger.valueOf(233),
            BigInteger.valueOf(239),
            BigInteger.valueOf(241),
            BigInteger.valueOf(251),
            BigInteger.valueOf(257),
            BigInteger.valueOf(263),
            BigInteger.valueOf(269),
            BigInteger.valueOf(271),
            BigInteger.valueOf(277),
            BigInteger.valueOf(281),
            BigInteger.valueOf(283),
            BigInteger.valueOf(293),
            BigInteger.valueOf(307),
            BigInteger.valueOf(311),
            BigInteger.valueOf(313),
            BigInteger.valueOf(317),
            BigInteger.valueOf(331),
            BigInteger.valueOf(337),
            BigInteger.valueOf(347),
            BigInteger.valueOf(349),
            BigInteger.valueOf(353),
            BigInteger.valueOf(359),
            BigInteger.valueOf(367),
            BigInteger.valueOf(373),
            BigInteger.valueOf(379),
            BigInteger.valueOf(383),
            BigInteger.valueOf(389),
            BigInteger.valueOf(397),
            BigInteger.valueOf(401),
            BigInteger.valueOf(409),
            BigInteger.valueOf(419),
            BigInteger.valueOf(421),
            BigInteger.valueOf(431),
            BigInteger.valueOf(433),
            BigInteger.valueOf(439),
            BigInteger.valueOf(443),
            BigInteger.valueOf(449),
            BigInteger.valueOf(457),
            BigInteger.valueOf(461),
            BigInteger.valueOf(463),
            BigInteger.valueOf(467),
            BigInteger.valueOf(479),
            BigInteger.valueOf(487),
            BigInteger.valueOf(491),
            BigInteger.valueOf(499),
            BigInteger.valueOf(503),
            BigInteger.valueOf(509),
            BigInteger.valueOf(521),
            BigInteger.valueOf(523),
            BigInteger.valueOf(541),
            BigInteger.valueOf(547),
            BigInteger.valueOf(557),
            BigInteger.valueOf(563),
            BigInteger.valueOf(569),
            BigInteger.valueOf(571),
            BigInteger.valueOf(577),
            BigInteger.valueOf(587),
            BigInteger.valueOf(593),
            BigInteger.valueOf(599),
            BigInteger.valueOf(601),
            BigInteger.valueOf(607),
            BigInteger.valueOf(613),
            BigInteger.valueOf(617),
            BigInteger.valueOf(619),
            BigInteger.valueOf(631),
            BigInteger.valueOf(641),
            BigInteger.valueOf(643),
            BigInteger.valueOf(647),
            BigInteger.valueOf(653),
            BigInteger.valueOf(659),
            BigInteger.valueOf(661),
            BigInteger.valueOf(673),
            BigInteger.valueOf(677),
            BigInteger.valueOf(683),
            BigInteger.valueOf(691),
            BigInteger.valueOf(701),
            BigInteger.valueOf(709),
            BigInteger.valueOf(719),
            BigInteger.valueOf(727),
            BigInteger.valueOf(733),
            BigInteger.valueOf(739),
            BigInteger.valueOf(743),
            BigInteger.valueOf(751),
            BigInteger.valueOf(757),
            BigInteger.valueOf(761),
            BigInteger.valueOf(769),
            BigInteger.valueOf(773),
            BigInteger.valueOf(787),
            BigInteger.valueOf(797),
            BigInteger.valueOf(809),
            BigInteger.valueOf(811),
            BigInteger.valueOf(821),
            BigInteger.valueOf(823),
            BigInteger.valueOf(827),
            BigInteger.valueOf(829),
            BigInteger.valueOf(839),
            BigInteger.valueOf(853),
            BigInteger.valueOf(857),
            BigInteger.valueOf(859),
            BigInteger.valueOf(863),
            BigInteger.valueOf(877),
            BigInteger.valueOf(881),
            BigInteger.valueOf(883),
            BigInteger.valueOf(887),
            BigInteger.valueOf(907),
            BigInteger.valueOf(911),
            BigInteger.valueOf(919),
            BigInteger.valueOf(929),
            BigInteger.valueOf(937),
            BigInteger.valueOf(941),
            BigInteger.valueOf(947),
            BigInteger.valueOf(953),
            BigInteger.valueOf(967),
            BigInteger.valueOf(971),
            BigInteger.valueOf(977),
            BigInteger.valueOf(983),
            BigInteger.valueOf(991),
            BigInteger.valueOf(997)


    };

    //    Constructor
    public RSA(int millerRabinSteps, int bitLengthN, int numberSystemBase, BigInteger m) {
        RSA.millerRabinSteps = millerRabinSteps;
        RSA.bitLengthN = bitLengthN;
        System.out.println("bitLengthN: " + bitLengthN);
        blockSize = (int)(bitLengthN * (Math.log(2) / Math.log(numberSystemBase))) + 1;
        System.out.println("blockSize: " + blockSize);
        blockSizePlusOne = blockSize;
        RSA.numberSystemBase = numberSystemBase;
        RSA.m = m;
    }

    public BigInteger getN(){
        return n;
    }
    public BigInteger getE(){
        return e;
    }
    public BigInteger getD(){
        return d;
    }
    public BigInteger getP(){
        return p;
    }
    public static BigInteger getCountOfN(){
        return countOfN;
    }
    public static BigInteger getQ(){
        return q;
    }
    public static BigInteger setN(BigInteger n){
        return RSA.n = n;
    }
    public static BigInteger setE(BigInteger e){
        return RSA.e = e;
    }
    public static BigInteger setD(BigInteger d){
        return RSA.d = d;
    }
    public static BigInteger setP(BigInteger p){
        return RSA.p = p;
    }
    public static BigInteger setQ(BigInteger q){
        return RSA.q = q;
    }
    public static BigInteger getM(){
        return m;
    }
    public static BigInteger getA(){
        return a;
    }
    public static BigInteger getB(){
        return b;
    }
    public static BigInteger getPhiN(){
        return phiN;
    }
    public static int getBitLengthN(){
        return bitLengthN;
    }
    public static int getMillerRabinSteps(){
        return millerRabinSteps;
    }
    public static void setBitLengthN(int bitLengthN){
        RSA.bitLengthN = bitLengthN;
    }
    public static void setMillerRabinSteps(int millerRabinSteps){
        RSA.millerRabinSteps = millerRabinSteps;
    }
    public static void setNumberSystemBase(int numberSystemBase){
        RSA.numberSystemBase = numberSystemBase;
    }
    public static void setBlockSize(int blockSize){
        RSA.blockSize = blockSize;
    }
    public static void setBlockSizePlusOne(int blockSizePlusOne){
        RSA.blockSizePlusOne = blockSizePlusOne;
    }
    public static void setM(BigInteger m){
        RSA.m = m;
    }
    public static void setCountOfN(BigInteger countOfN){
        RSA.countOfN = countOfN;
    }

    public static void calculateN(BigInteger p, BigInteger q){
        System.out.println("pppp: " + p);
        System.out.println("qqqq: " + q);
        n = p.multiply(q);
        System.out.println("nnn: " + n);
    }
    public static void calculatePhiN(BigInteger p, BigInteger q){
        phiN = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }
    // Initially try to use 65537 as the public exponent e
    public static void calculateE(BigInteger phiN){
        e = BigInteger.valueOf(65537);
        if (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(BigInteger.ONE)) {
            e = BigInteger.valueOf(3);
            if (!e.gcd(phiN).equals(BigInteger.ONE)) {
                BigInteger lowerBoundForE = BigInteger.TWO; // e must be greater than 1
                BigInteger upperBoundForE = phiN.subtract(BigInteger.ONE); // e must be less than phiN
                do {
                    e = generateRandomPrime(m, lowerBoundForE, upperBoundForE, millerRabinSteps);
                } while (!e.gcd(phiN).equals(BigInteger.ONE));
            }
        }
    }

    public static void calculateD(BigInteger e, BigInteger phiN){
        d = MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);
    }
    public static void calculateP(BigInteger bitLengthP){
        BigInteger possibleP;
        a = TWO.pow(bitLengthP.intValue() - 1);
        b = TWO.pow(bitLengthP.intValue());
        System.out.println("a: " + a);
        do {
            possibleP = generateRandomPrime(m,a, b, millerRabinSteps);
        } while (possibleP.equals(q));
        p = possibleP;
    }
    public static void calculateQ(BigInteger bitLengthQ){
        BigInteger possibleQ;
        a = TWO.pow(bitLengthQ.intValue() -1);
        b = TWO.pow(bitLengthQ.intValue());
        do {
            possibleQ = generateRandomPrime(m,a, b, millerRabinSteps);
        } while (possibleQ.equals(p));
        q = possibleQ;
    }

    /**
     * Generates two prime numbers suitable for RSA encryption with bit length of 128 and calculates n and phi(n), as well as e and d.
     *
     */
    // Maybe split up into functions for better readability
    public static void generatePrimeNumbers() {
        // Calculate bit length of p and q
        int bitLengthPQ = bitLengthN / 2;
        System.out.println("bitLengthPQ: " + bitLengthPQ);
        long startTime = System.nanoTime();
        calculateP(BigInteger.valueOf(bitLengthPQ));
        calculateQ(BigInteger.valueOf(bitLengthPQ));
        long endTime = System.nanoTime();
        System.out.println("Time for prime generation: " + (endTime - startTime) / 1000000 + " ms");
        calculateN(p, q);
        calculatePhiN(p, q);
        calculateE(phiN);
        calculateD(e, phiN);
    }

    public static String encrypt(String message, BigInteger e, BigInteger n) {
        System.out.println("n: " + n);
        System.out.println("Input message: " + message);

        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(message);
        System.out.println("Unicode message: " + unicodeMessage);
        // Step 2: Prepare message for encryption (Block cipher)
        List<BigInteger> numericMessage = prepareMessageForEncryption(unicodeMessage);
        System.out.println("Numeric message: " + numericMessage);
        // Step 3: Encrypt the numeric representation
        List<BigInteger> encryptedBlocks = encryptNumericBlocks(numericMessage, e, n);
        System.out.println("Encrypted blocks: " + encryptedBlocks);
        // Step 4: Process the encrypted blocks into a string representation

        return processEncryptedBlocksToString(encryptedBlocks);
    }

    private static List<Integer> convertTextToUnicode(String message) {
        // Implementation of text to Unicode conversion
        return MathMethods.convertTextToUniCode(message);
    }

    private static List<BigInteger> prepareMessageForEncryption(List<Integer> unicodeMessage) {
        // Implementation of preparing message for encryption
        return MathMethods.prepareMessageForEncryption(unicodeMessage, blockSize, numberSystemBase);
    }

    private static List<BigInteger> encryptNumericBlocks(List<BigInteger> numericMessage, BigInteger e, BigInteger n) {
        // Implementation of encrypting numeric blocks
        List<BigInteger> encryptedBlocks = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            encryptedBlocks.add(MathMethods.alternativeQuickExponentiation(block, e, n));
        }
        return encryptedBlocks;
    }

    private static String processEncryptedBlocksToString(List<BigInteger> encryptedBlocks) {
        // Implementation of processing encrypted blocks into string
        StringBuilder encryptedNumericMessageStr = new StringBuilder();
        for (BigInteger block : encryptedBlocks) {
            encryptedNumericMessageStr.append(convertBlockToString(block));
        }
        return encryptedNumericMessageStr.toString();
    }

    private static String convertBlockToString(BigInteger block) {
        // Convert a single encrypted block into a string representation
        List<Integer> tempList = convertBlockToNumberList(block);
        return MathMethods.convertUniCodeToText(tempList);
    }

    private static List<Integer> convertBlockToNumberList(BigInteger block) {
        // Convert a single encrypted block into a list of numbers
        List<Integer> numberList = new ArrayList<>();
        int count = 0;
        while (!block.equals(BigInteger.ZERO)) {
            numberList.add(0, block.mod(BigInteger.valueOf(numberSystemBase)).intValue());
            block = block.divide(BigInteger.valueOf(numberSystemBase));
            count++;
        }
        while (count < blockSizePlusOne) {
            numberList.add(0, 0);
            count++;
        }
        return numberList;
    }
    public static String decrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n) {
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUnicode(encryptedNumericMessageStr);
        System.out.println("Decrypting Unicode to encrypted blocks: " + unicodeMessage);
        // Step 2: Create encrypted blocks from Unicode
        List<List<BigInteger>> encryptedBlocks = createEncryptedBlocksFromUnicode(unicodeMessage);
        System.out.println("Encrypted blocks: " + encryptedBlocks);
        // Step 3: Convert the encrypted blocks to BigInteger format
        List<BigInteger> encryptedNumericMessages = convertBlocksToBigIntegers(encryptedBlocks);
        System.out.println("Encrypted numeric messages: " + encryptedNumericMessages);
        // Step 4: Decrypt the numeric representation
        List<BigInteger> numericMessage = decryptNumericMessages(encryptedNumericMessages, d, n);
        System.out.println("Numeric message: " + numericMessage);
        // Step 5: Convert the numeric message to a list of integers
        List<Integer> decryptedMessage = convertNumericMessageToIntegers(numericMessage);
        System.out.println("Decrypted message in Unicode integer values: " + decryptedMessage);
        // Step 6: Convert the list of integers to a string
        String decryptedMessageStr = convertIntegersToText(decryptedMessage);
        System.out.println("Decrypted message string: " + decryptedMessageStr);
        // Step 7: Remove padding from the decrypted string
        return removePadding(decryptedMessageStr);
    }


    private static List<List<BigInteger>> createEncryptedBlocksFromUnicode(List<Integer> unicodeMessage) {
        List<List<BigInteger>> encryptedBlocks = new ArrayList<>();
        for (int i = 0; i < unicodeMessage.size(); i += blockSizePlusOne) {
            List<BigInteger> block = new ArrayList<>();
            for (int j = 0; j < blockSizePlusOne; j++) {
                if (i + j < unicodeMessage.size()) {
                    block.add(BigInteger.valueOf(unicodeMessage.get(i + j)));
                } else {
                    block.add(BigInteger.ZERO);
                }
            }
            encryptedBlocks.add(block);
        }
        return encryptedBlocks;
    }

    private static List<BigInteger> convertBlocksToBigIntegers(List<List<BigInteger>> encryptedBlocks) {
        List<BigInteger> encryptedNumericMessages = new ArrayList<>();
        for (List<BigInteger> encryptedBlock : encryptedBlocks) {
            BigInteger sum = BigInteger.ZERO;
            for (int j = 0; j < encryptedBlock.size(); j++) {
                BigInteger temp = encryptedBlock.get(encryptedBlock.size() - j - 1).multiply(BigInteger.valueOf(numberSystemBase).pow(j));
                sum = sum.add(temp);
            }
            encryptedNumericMessages.add(sum);
        }
        return encryptedNumericMessages;
    }

    private static List<BigInteger> decryptNumericMessages(List<BigInteger> encryptedNumericMessages, BigInteger d, BigInteger n) {
        List<BigInteger> numericMessage = new ArrayList<>();
        for (BigInteger block : encryptedNumericMessages) {
            numericMessage.add(MathMethods.alternativeQuickExponentiation(block, d, n));
        }
        return numericMessage;
    }

    private static List<Integer> convertNumericMessageToIntegers(List<BigInteger> numericMessage) {
        List<Integer> decryptedMessage = new ArrayList<>();
        for (BigInteger block : numericMessage) {
            decryptedMessage.addAll(MathMethods.prepareMessageForDecryption(block, blockSize, numberSystemBase));
        }
        return decryptedMessage;
    }

    private static String convertIntegersToText(List<Integer> decryptedMessage) {
        return MathMethods.convertUniCodeToText(decryptedMessage);
    }

    private static String removePadding(String decryptedMessageStr) {
        int paddingIndex = decryptedMessageStr.indexOf(0);
        return paddingIndex >= 0 ? decryptedMessageStr.substring(0, paddingIndex) : decryptedMessageStr;
    }
}
