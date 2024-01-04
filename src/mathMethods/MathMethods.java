package mathMethods;

import rsa.RSA;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static java.math.BigInteger.*;


/**
 * This class contains various mathematical methods used in the RSA encryption process.
 */
public class MathMethods {
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
    private static final BigInteger MINUSONE = BigInteger.valueOf(-1);
    private static final BigDecimal DECIMAL_ONE = BigDecimal.ONE;
    private static final BigDecimal DECIMAL_ZERO = BigDecimal.ZERO;

    /**
     * Calculates the alternative quick exponentiation of a base raised to the power of an exponent modulo a given modulus.
     *
     * @param base the base value
     * @param exp the exponent value
     * @param mod the modulus value
     * @return the result of the exponentiation
     * @throws IllegalArgumentException if the exponent is negative or if 0^0 mod 1 is encountered
     */
    public static BigInteger alternativeQuickExponentiation(BigInteger base, BigInteger exp, BigInteger mod) {
        if (exp.signum() < 0) throw new IllegalArgumentException("Exponent must be positive");
        if (exp.equals(ZERO) && mod.equals(ONE)) {
            System.out.println("0^0 mod 1 is not defined.");
            return ZERO;
        }

        BigInteger result = ONE;
        base = base.mod(mod);

        while (exp.signum() != 0) {
            if (exp.testBit(0)) { // Check if exponent is odd
                result = (result.multiply(base)).mod(mod);
            }
            exp = exp.shiftRight(1); // Halve the exponent
            if (exp.signum() != 0) {
                base = (base.multiply(base)).mod(mod);
            }
        }

        return result;
    }


    /**
     * Calculates the extended Euclidean algorithm for two BigIntegers.
     *
     * @param a the first BigInteger
     * @param b the second BigInteger
     * @return an array containing three BigIntegers: [gcd, x, y], where gcd is the greatest common divisor of a and b, and x and y are Bezout's coefficients
     */
    public static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b) {
        if (b.equals(ZERO)) {
            return new BigInteger[]{a, ONE, ZERO};
        } else {
            BigInteger[] ee = extendedEuclidean(b, a.mod(b)); // b ist der Teiler (im Skript der erste Faktor); a.mod(b) ist der Rest
            BigInteger gcd = ee[0]; // im Skript der erste Faktor
            BigInteger x = ee[2]; // Rest
            BigInteger y = ee[1].subtract(a.divide(b).multiply(ee[2])); // im Skript der zweite Faktor
            return new BigInteger[]{gcd, x, y};
        }
    }


    /**
     * Generates a random BigInteger satisfying Euler's criterion for a given prime number.
     *
     * @param p the prime number
     * @return a random BigInteger satisfying Euler's criterion
     */
    public static BigInteger eulerCriterion(BigInteger p) {
        SecureRandom rand = new SecureRandom();
        BigInteger z;
        do {
            z = new BigInteger(p.bitLength(), rand).mod(p);
        } while (alternativeQuickExponentiation(z, p.subtract(ONE).divide(TWO), p).equals(ONE));

        return z;
    }


    /**
     * Represents the given prime number as a sum of two squares.
     *
     * @param p the prime number to be represented
     * @return a GaussianInteger representing the prime number as a sum of two squares
     * @throws IllegalArgumentException if the given number is not a prime number or cannot be represented as a sum of two squares
     * @throws IllegalStateException    if an error occurs during the calculation
     */
    public static GaussianInteger representPrimeAsSumOfSquares(BigInteger p) throws IllegalArgumentException {
        //Check if p is a prime number
        if (!p.isProbablePrime(100)) {
            throw new IllegalArgumentException("The number " + p + " is not a prime number.");
        }
        // Check if p is of the form 4n + 1
        if (!p.mod(BigInteger.valueOf(4)).equals(ONE)) {
            throw new IllegalArgumentException("The number " + p + " cannot be represented as a sum of two squares.");
        }

        //Use the extended Euclidean algorithm in Z[i] to find a Gaussian integer x such that x^2 = -1 mod p
        GaussianInteger x = extendedEuclideanInZi(new GaussianInteger(ZERO, ONE), new GaussianInteger(p, ZERO));
        // Check if x is a multiple of p
        if (x.isMultiple(new GaussianInteger(p, ZERO))) {
            throw new IllegalArgumentException("The prime number " + p + " cannot be represented as a sum of two squares.");
        }

        BigInteger randomZ = eulerCriterion(p);
        //Check if randomZ is a squared non-residue mod p
        while (alternativeQuickExponentiation(randomZ, p.subtract(ONE).divide(TWO), p).equals(ONE) || randomZ.compareTo(ONE) <= 0) {
            //If equal to or smaller than 1, it is not a squared non-residue mod p
            randomZ = eulerCriterion(p);
        }

        BigInteger four = BigInteger.valueOf(4);
        BigInteger randomW = alternativeQuickExponentiation(randomZ, p.subtract(ONE).divide(four), p);
        GaussianInteger w_i = new GaussianInteger(randomW, ONE);
        GaussianInteger p_0 = new GaussianInteger(p, ZERO);

        GaussianInteger result = extendedEuclideanInZi(w_i, p_0);
        //Make sure that results are positive by multiplying with -1 if necessary
        if (result.real.compareTo(ZERO) < 0) {
            result.real = result.real.multiply(MINUSONE);
        }
        if (result.imaginary.compareTo(ZERO) < 0) {
            result.imaginary = result.imaginary.multiply(MINUSONE);
        }
        //Check if the found values are correct
        BigInteger c = result.real;
        BigInteger d = result.imaginary;
        if (p.equals(c.multiply(c).add(d.multiply(d)))) {
            return new GaussianInteger(c, d);
        } else {
            throw new IllegalStateException("Etwas ist schief gelaufen bei der Berechnung des ggT.");
        }
    }



    /**
     * Calculates the extended Euclidean algorithm in Z[i].
     *
     * @param a the first GaussianInteger
     * @param b the second GaussianInteger
     * @return the greatest common divisor of a and b in Z[i]
     */
    public static GaussianInteger extendedEuclideanInZi(GaussianInteger a, GaussianInteger b) {
        GaussianInteger gk_minus1 = b;
        GaussianInteger gk = a;
        GaussianInteger ck, gk_plus1;

        while (!gk.isZero()) {
            ck = gk_minus1.divide(gk);
            gk_plus1 = gk_minus1.subtract(gk.multiply(ck));


            if (gk.equals(gk_plus1) || gk.isSymmetricallyEqualTo(gk_plus1)) {
                break;
            }

            gk_minus1 = gk;
            gk = gk_plus1;
        }
        return gk_minus1.normalizeGCD();
    }


    /**
     * Returns the rounded value of the given array of BigIntegers.
     *
     * @param z an array of BigIntegers [realPart, imaginaryPart]
     * @return an array of BigIntegers [roundedRealPart, roundedImaginaryPart]
     */
    // Function f for the extended Euclidean algorithm in Z[i]
    public static BigInteger[] f(BigInteger[] z) {

        BigInteger realPart = roundHalfUp(z[0]);
        BigInteger imaginaryPart = roundHalfUp(z[1]);
        return new BigInteger[]{realPart, imaginaryPart};
    }

    /**
     * Rounds a BigInteger to the nearest integer using the round half up method.
     *
     * @param number the BigInteger to be rounded
     * @return the rounded BigInteger
     */
    // Rounds a BigInteger to the nearest integer
    public static BigInteger roundHalfUp(BigInteger number) {
        // The rounding logic needs to handle negative numbers differently than positive numbers
        BigInteger halfDivisor = new BigInteger("5");
        BigInteger scaledNumber = number.multiply(TEN);

        if (number.compareTo(ZERO) < 0) {
            // For negative numbers, subtract 5 before dividing by 10
            return scaledNumber.subtract(halfDivisor).divide(TEN);
        } else {
            // For positive numbers, add 5 before dividing by 10
            return scaledNumber.add(halfDivisor).divide(TEN);
        }
    }


    private static final MathContext context = new MathContext(10);
    private static BigInteger randomElsnerA = BigInteger.ZERO;
    private static BigInteger randomElsnerB = BigInteger.ZERO;
    private static BigInteger randomElsnerM = BigInteger.ZERO;
    private static BigDecimal randomElsnerDecimalA = BigDecimal.ZERO;
    private static BigDecimal randomElsnerDecimalB = BigDecimal.ZERO;
    private static BigDecimal randomElsnerDecimalM = BigDecimal.ZERO;
    private static BigDecimal range = BigDecimal.ZERO;
    /**
     * Generates a random BigInteger within the range [a, b] using Elsner's algorithm.
     * The precision was chosen to be 10 decimal places for the sake of performance.
     * This precision only affects the decimal part of the number, not the binary bit length.
     * The binary bit length will still be the proper length and the numbers don't suddenly have trailing zeros.
     * TODO Add a test that proves it. So far it has only been tested manually.
     * @param m the random seed (must be greater than 0 and non-square)
     * @param n the range of randomness
     * @param a the lower bound inclusive
     * @param b the upper bound inclusive
     * @return a random BigInteger within the range [a, b]
     */
    public static BigInteger randomElsner(BigInteger m, BigInteger n, BigInteger a, BigInteger b) {
        if(!randomElsnerM.equals(m) || !randomElsnerA.equals(a) || !randomElsnerB.equals(b)) {
            randomElsnerDecimalM = new BigDecimal(m);
            randomElsnerDecimalA = new BigDecimal(a);
            randomElsnerDecimalB = new BigDecimal(b);
            randomElsnerM = m;
            randomElsnerA = a;
            randomElsnerB = b;
            range = randomElsnerDecimalB.subtract(randomElsnerDecimalA).add(DECIMAL_ONE);
        }
//        BigDecimal decimalM = new BigDecimal(m);
        BigDecimal decimalN = new BigDecimal(n);
//        BigDecimal decimalA = new BigDecimal(a);
//        BigDecimal decimalB = new BigDecimal(b);

//        BigDecimal range = decimalB.subtract(decimalA).add(DECIMAL_ONE);
//        BigDecimal mathContextRange = range.add(decimalN);

//         int decadicLogarithm = mathContextRange.precision() - mathContextRange.scale();
//         MathContext context = new MathContext(decadicLogarithm);

        // In this context, decimalM is always positive, and sqrt is always positive, so the remainder is always positive
        // So this condition will never be true. We can comment/remove this condition
//         if (randomElsnerDecimalM.sqrt(context).remainder(DECIMAL_ONE).equals(DECIMAL_ZERO)) {
//             System.out.println("It happens");
//             randomElsnerDecimalM = randomElsnerDecimalM.add(DECIMAL_ONE);
//         }

        BigDecimal randomSeededNumber = decimalN.multiply(randomElsnerDecimalM.sqrt(context)).remainder(DECIMAL_ONE);
        BigDecimal randomSeedNumberOffset = randomSeededNumber.multiply(range);
        return a.add(randomSeedNumberOffset.toBigInteger());
    }

    private static boolean isCompositeAgainstSmallPrimes(BigInteger primeCandidate) {
        return Arrays.stream(SMALL_PRIMES).parallel().anyMatch(smallPrime ->
                primeCandidate.mod(smallPrime).equals(ZERO) || primeCandidate.equals(smallPrime)
        );
    }


    /**
     * Generates a random prime number within a specified range using the Elsner algorithm and the Miller-Rabin primality test.
     *
     * @param m                the random seed
     * @param a                the lower bound (inclusive)
     * @param b                the upper bound (inclusive)
     * @param millerRabinSteps the number of Miller-Rabin steps to perform for primality testing
     * @return a random prime number within the range [a, b]
     * @throws IllegalArgumentException if the input values are invalid
     */
    public static BigInteger generateRandomPrime(BigInteger m, BigInteger a, BigInteger b, int millerRabinSteps) {
        //Check that input values are valid
        if (a.compareTo(b) > 0) {
            throw new IllegalArgumentException("The lower bound must be smaller than the upper bound");
        }
        if (a.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("The lower bound must be greater than or equal to 0");
        }
        if (b.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("The upper bound must be greater than or equal to 0");
        }
        if (m.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("The random seed must be greater than or equal to 0");
        }
        if (m.compareTo(b.subtract(a)) > 0) {
            System.out.println("m: " + m);
            System.out.println("b: " + b);
            System.out.println("a: " + a);
            System.out.println("b - a: " + b.subtract(a));
            throw new IllegalArgumentException("The random seed must be smaller than the range");
        }
        if (m.equals(ZERO)) {
            throw new IllegalArgumentException("The random seed must be greater than 0");
        }


        BigInteger primeCandidate;
        BigInteger copyOfCountOfN = RSA.getCountOfN();
        while (true) {
            // Generate a random odd BigInteger within the range
            primeCandidate = randomElsner(m, copyOfCountOfN, a, b).setBit(0); // Ensure it's odd
            // Fast check against small primes
            boolean isComposite = false;
            for (BigInteger smallPrime : SMALL_PRIMES) {
                if (primeCandidate.equals(smallPrime)) {
                    return primeCandidate; // Prime is found
                } else if (primeCandidate.mod(smallPrime).equals(ZERO)) {
                    isComposite = true;
                    break;
                }
            }
            if (isComposite) {
                copyOfCountOfN = copyOfCountOfN.add(ONE);
                continue; // Skip to the next candidate
            }
            // Perform the expensive primality check
            if (parallelMillerRabinTest(primeCandidate, millerRabinSteps, m, copyOfCountOfN)) {
                break; // Prime is found
            }
            copyOfCountOfN = copyOfCountOfN.add(ONE);
            // Otherwise, loop again and generate a new primeCandidate
        }
        RSA.setCountOfN(copyOfCountOfN.add(ONE));
        return primeCandidate;
    }


    /**
     * Checks if a number is prime using the Miller-Rabin primality test and returns true if it is probably prime and the probability
     *
     * @param possiblePrime  the number to be tested for primality
     * @param numberOfTests  the number of Miller-Rabin tests to perform
     * @param m              the random seed
     * @param countOfN       a value to increase the randomness of the tests
     * @return true if the number is probably prime, false otherwise
     */
    //Check if a number is prime using the Miller-Rabin primality test and returns true if it is probably prime and the probability
    public static boolean millerRabinTest(BigInteger possiblePrime, int numberOfTests, BigInteger m, BigInteger countOfN) {

        if (possiblePrime.equals(ONE)) {
            return false;
        }
        if (possiblePrime.equals(TWO)) {
            return true;
        }
        if (!possiblePrime.testBit(0)) {
            return false;
        }

        BigInteger d = possiblePrime.subtract(ONE);
        BigInteger possiblePrimeMinusTwo = possiblePrime.subtract(TWO);

        int s = 0;
        while (!d.testBit(0)) {
            d = d.shiftRight(1);
            s++;
        }
        for (int i = 0; i < numberOfTests; i++) {
            BigInteger modifiedCountOfN = countOfN.add(BigInteger.valueOf(i));
            BigInteger a = randomElsner(m, modifiedCountOfN, TWO, possiblePrimeMinusTwo);

            BigInteger x = alternativeQuickExponentiation(a, d, possiblePrime);

            if (x.equals(ONE) || x.equals(possiblePrime.subtract(ONE))) {
                continue;
            }

            int r;
            for (r = 1; r < s; r++) {
                x = alternativeQuickExponentiation(x, TWO, possiblePrime);
                if (x.equals(ONE)) {
                    return false;
                }
                if (x.equals(possiblePrime.subtract(ONE))) {
                    break;
                }
            }

            if (r == s) { // None of the steps made x equal to possiblePrime-1
                return false;
            }
        }

        return true;
    }

    /**
     * Performs the Parallel Miller-Rabin Test to check if a number is probably prime.
     *
     * @param possiblePrime the number to be tested for primality
     * @param numberOfTests the number of Miller-Rabin tests to perform
     * @param m             the random seed
     * @param countOfN      a value to increase the randomness of the tests
     * @return true if the number is probably prime, false otherwise
     *
     */
    public static boolean parallelMillerRabinTest(BigInteger possiblePrime, int numberOfTests, BigInteger m, BigInteger countOfN) {
        if (possiblePrime.equals(TWO)) return true;
        if (!possiblePrime.testBit(0)) return false;
        if (possiblePrime.equals(ONE)) return false;

        int s = possiblePrime.subtract(ONE).getLowestSetBit();
        BigInteger finalD = possiblePrime.subtract(ONE).shiftRight(s);
        // ForkJoinPool can potentially be more efficient for certain tasks
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        BigInteger possiblePrimeMinusOne = possiblePrime.subtract(ONE);
        BigInteger possiblePrimeMinusTwo = possiblePrime.subtract(TWO);
        List<Callable<Boolean>> tasks = IntStream.range(0, numberOfTests)
                .mapToObj(i -> (Callable<Boolean>) () -> {
                    BigInteger modifiedCountOfN = countOfN.add(BigInteger.valueOf(i));
                    BigInteger a = randomElsner(m, modifiedCountOfN, TWO, possiblePrimeMinusTwo);
                    BigInteger x = alternativeQuickExponentiation(a, finalD, possiblePrime);

                    if (x.equals(ONE) || x.equals(possiblePrimeMinusOne)) {
                        return true;
                    }

                    for (int r = 0; r < s; r++) {
                        x = alternativeQuickExponentiation(x, TWO, possiblePrime);
                        if (x.equals(possiblePrimeMinusOne)) return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        try {
            return forkJoinPool.invokeAny(tasks);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            forkJoinPool.shutdown();
        }
    }

    /**
     * This is the block cipher encryption method.
     * It takes a message and encrypts it using the RSA algorithm.
     * It has been renamed to encryptMessage to avoid confusion, since it is not strictly speaking a pure block cipher encryption method.
     * It is used to encrypt the message before it is converted to a list of integers, which is then encrypted using the block cipher.
     * @param message the message to be encrypted
     * @param blockSize the block size to be used for the conversion
     * @param numberSystem the number system to be used for the conversion
     * @return encryptedBlocks
     */
    public static List<BigInteger> prepareMessageForEncryption(List<Integer> message, int blockSize, int numberSystem) {

        List<List<Integer>> blocks = new ArrayList<>();

        for (int i = 0; i < message.size(); i++) {
            if (i % blockSize == 0) {
                blocks.add(new ArrayList<>());
            }
            blocks.get(i / blockSize).add(message.get(i));
        }

        List<BigInteger> encryptedBlocks = new ArrayList<>();

        for (List<Integer> block : blocks) {
            BigInteger blockValue = ZERO;


            int exponent = blockSize - 1;
            for (Integer integer : block) {
                blockValue = blockValue.add(BigInteger.valueOf(integer).multiply(BigInteger.valueOf(numberSystem).pow(exponent)));
                exponent--;
            }

            encryptedBlocks.add(blockValue);
        }

        return encryptedBlocks;
    }

    /**
     * This is the block cipher decryption method.
     * It also has been renamed to decryptMessage to avoid confusion, since it is not strictly speaking a pure block cipher decryption method.
     * It is used to decrypt the message after it is converted to a list of integers, which is then decrypted using the block cipher.
     * @param message the message to be decrypted
     * @param blockSize the block size to be used for the conversion
     * @param numberSystem the number system to be used for the conversion
     * @return decryptedMessage
     */
    public static List<Integer> prepareMessageForDecryption(BigInteger message, int blockSize, int numberSystem) {
        List<Integer> decryptedMessage = new ArrayList<>();

        List<BigInteger> blocks = new ArrayList<>();

        BigInteger numberSystemToThePowerOfBlockSize = BigInteger.valueOf(numberSystem).pow(blockSize);

        while (!message.equals(ZERO)) {
            blocks.add(message.mod(numberSystemToThePowerOfBlockSize));
            message = message.divide(numberSystemToThePowerOfBlockSize);
        }

        for (BigInteger block : blocks) {
            for (int i = blockSize - 1; i >= 0; i--) {
                BigInteger numberSystemToThePowerOfI = BigInteger.valueOf(numberSystem).pow(i);
                BigInteger blockValue = block.divide(numberSystemToThePowerOfI);
                decryptedMessage.add(blockValue.intValue());
                block = block.subtract(blockValue.multiply(numberSystemToThePowerOfI));
            }
        }

        return decryptedMessage;
    }

    /**
     * Converts a given text into a list of unicode values.
     *
     * @param text the text to be converted
     * @return a list of unicode values representing the characters in the text
     */
    public static List<Integer> convertTextToUniCode(String text) {
        List<Integer> unicode = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            unicode.add((int) text.charAt(i));
        }
        return unicode;
    }

    /**
     * Converts a list of Unicode values representing characters into a corresponding text.
     *
     * @param unicode the list of Unicode values representing characters
     * @return the text represented by the Unicode values
     */
    public static String convertUniCodeToText(List<Integer> unicode) {
        StringBuilder text = new StringBuilder();
        for (Integer integer : unicode) {
            text.append((char) integer.intValue());
        }
        return text.toString();
    }


}
