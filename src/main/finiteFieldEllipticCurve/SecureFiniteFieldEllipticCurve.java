package main.finiteFieldEllipticCurve;

import main.elGamalMenezesVanstone.ElGamalMenezesVanstoneService;
import main.mathMethods.MathMethods;
import main.resource.Resource;

import java.math.BigInteger;

public class SecureFiniteFieldEllipticCurve {
    private final BigInteger a;
    private final BigInteger n;
    private BigInteger q;
    private FiniteFieldEllipticCurve safeEllipticCurve;

    public BigInteger getA() {
        return this.a;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public FiniteFieldEllipticCurve getSafeEllipticCurve() {
        return safeEllipticCurve;
    }

    /**
     * Generates a prime number that is congruent to 5 mod 8.
     *
     * @param bitLengthOfP        The bit length of the prime number to be generated.
     * @param millerRabinIterations The number of Miller-Rabin iterations for the primality test.
     * @param m                   The modulus used in the primality test.
     * @return A prime number that is congruent to 5 mod 8.
     */
    public BigInteger generatePrimeCongruentToFiveModEight(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p;
        while (true) {
            // Generate a unique prime using the ElGamalMenezesVanstoneService
            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, Resource.counter);
            p = adjustToFiveModEight(p, bitLengthOfP.intValue());

            // Test the primality using parallelMillerRabinTest
            if (MathMethods.parallelMillerRabinTest(p, millerRabinIterations, m, BigInteger.valueOf(Resource.counter.incrementAndGet()))) {
                break;
            }
        }
        return p;
    }

    /**
     * Adjusts the given number to be congruent to 5 mod 8.
     *
     * @param number The number to be adjusted.
     * @param bitLength The bit length of the number to be adjusted.
     * @return The adjusted number that is congruent to 5 mod 8.
     */
    private BigInteger adjustToFiveModEight(BigInteger number, int bitLength) {
        BigInteger modEight = number.mod(Resource.EIGHT);
        BigInteger adjustment = Resource.FIVE.subtract(modEight);
        number = number.add(adjustment);
        if (number.bitLength() > bitLength) {
            number = number.subtract(Resource.EIGHT);
        }
        return number;
    }

    /**
     * Skript S.78
     * Calculate q such that orderN = q*8
     * @param orderN order of the elliptic curve (group of points)
     * @return q
     */
    private BigInteger calculateQ(BigInteger orderN) {
        return orderN.divide(Resource.EIGHT);
    }

    /**
     * Skript Satz 4.1 S.76-78
     * Calculate a prime p congruent to 5 mod 8 and a prime q such that orderN = q*8
     * @param bitLengthOfP bitlength of prime to be generated
     * @param millerRabinIterations number of steps supposed to be done in millerRabin test
     * @param m module that is being used
     */
    private void calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p;
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, null);
        BigInteger orderN;
        BigInteger q;

        while (true) {
            p = generatePrimeCongruentToFiveModEight(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve.setP(p);

            //TODO: Is this ever going to be used?
            boolean pIsPrime = MathMethods.parallelMillerRabinTest(p, millerRabinIterations, m, BigInteger.valueOf(Resource.counter.incrementAndGet()));

            orderN = ellipticCurve.calculateOrder(n);

            if (orderN.equals(n.multiply(Resource.TWO)) || !orderN.mod(Resource.EIGHT).equals(Resource.ZERO)) {
                continue;
            }

            q = calculateQ(orderN);

            boolean qIsPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, m, BigInteger.valueOf(Resource.counter.incrementAndGet()));

            if (qIsPrime && orderN.equals(q.multiply(Resource.EIGHT))) {
                break;
            }
        }

        this.safeEllipticCurve = ellipticCurve;
        this.q = q;
        this.safeEllipticCurve.setQ(q);
    }

    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        this.a = n.multiply(n).negate();
        this.n = n;
        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);
    }

}
