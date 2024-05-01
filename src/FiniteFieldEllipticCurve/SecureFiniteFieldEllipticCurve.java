package FiniteFieldEllipticCurve;

import elGamalMenezesVanstone.ElGamalMenezesVanstoneService;
import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SecureFiniteFieldEllipticCurve {
    private final BigInteger n;
    private BigInteger q;

    private FiniteFieldEllipticCurve safeEllipticCurve;
    AtomicInteger counter = new AtomicInteger(1);

    public BigInteger getN() {
        return this.n;
    }
    public  BigInteger getQ(){
        return this.q;
    }

    public FiniteFieldEllipticCurve getSafeEllipticCurve() {
        return safeEllipticCurve;
    }

    /**
     * Generate a prime congruent to 5 mod 8
     * @param bitLengthOfP
     * @param millerRabinIterations
     * @param m
     * @return p
     */
    private BigInteger generatePrimeCongruentToFiveModEight(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(bitLengthOfP.intValue(), random);
        BigInteger modEight = p.mod(Resource.EIGHT);
        p = p.add(Resource.FIVE.subtract(modEight));
        if (p.bitLength() > bitLengthOfP.intValue()) {
            p = p.subtract(Resource.EIGHT);
        }

        BigInteger eight = Resource.EIGHT;
        while (!MathMethods.parallelMillerRabinTest(p, millerRabinIterations, m, BigInteger.valueOf(counter.incrementAndGet()))) {
            p = p.add(eight);
            if (p.bitLength() > bitLengthOfP.intValue()) {
                p = new BigInteger(bitLengthOfP.intValue(), random);
                modEight = p.mod(eight);
                p = p.add(Resource.FIVE.subtract(modEight));
            }
        }
        return p;
    }

    /**
     * Calculate a prime p congruent to 5 mod 8
     * @param bitLengthOfP
     * @param millerRabinIterations
     * @param m
     * @return p
     */
    private BigInteger calculatePrimeMod8(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
        BigInteger pMod8 = p.mod(Resource.EIGHT);
        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, p);


        while (!pMod8.equals(Resource.FIVE) || !legendreSign.equals(Resource.ONE)){
//            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
            p = generatePrimeCongruentToFiveModEight(bitLengthOfP, millerRabinIterations, m);
            assert p.isProbablePrime(100);
            pMod8 = p.mod(Resource.EIGHT);
            legendreSign = MathMethods.verifyEulerCriterion(n, p);
        }

        assert legendreSign.equals(Resource.ONE);
        assert !(p.equals(n.multiply(Resource.TWO)));
        return p;
    }

    /**
     * Skript S.78
     * Calculate q such that orderN = q*8
     * @param orderN
     * @return q
     */
    private BigInteger calculateQ(BigInteger orderN){
        return orderN.divide(Resource.EIGHT);
    }

    /**
     * Skript Satz 4.1 S.76-78
     * Calculate a prime p congruent to 5 mod 8 and a prime q such that orderN = q*8
     * @param bitLengthOfP
     * @param millerRabinIterations
     * @param m
     */
    private void calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){

        BigInteger p;
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, null);
        BigInteger orderN;
        BigInteger q;

        while (true){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve.setP(p);
            boolean pIsPrime = MathMethods.parallelMillerRabinTest(p, millerRabinIterations, m, BigInteger.valueOf(counter.incrementAndGet()));

            if(!pIsPrime){
                continue;
            }


            orderN = ellipticCurve.calculateOrder(n);

            if(orderN.equals(n.multiply(Resource.TWO)) || !orderN.mod(Resource.EIGHT).equals(Resource.ZERO)){
                continue;
            }

            q = calculateQ(orderN);

            boolean qIsPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, m, BigInteger.valueOf(counter.incrementAndGet()));


            if(qIsPrime && orderN.equals(q.multiply(Resource.EIGHT))){
                break;
            }
        }

        assert q.multiply(Resource.EIGHT).equals(orderN);
        //Assert p is congruent to 5 mod 8
        assert p.mod(Resource.EIGHT).equals(Resource.FIVE);

        this.safeEllipticCurve = ellipticCurve;
        assert this.safeEllipticCurve.getP().equals(p);
        assert this.safeEllipticCurve.calculateOrder(n).equals(orderN);
        this.q = q;
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        assert n.compareTo(Resource.ZERO) > 0;
        this.n = n;
        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);
    }

    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m, FiniteFieldEllipticCurve ellipticCurve) {
        assert n.compareTo(Resource.ZERO) > 0;
        this.n = n;
        this.safeEllipticCurve = ellipticCurve;
    }

}
