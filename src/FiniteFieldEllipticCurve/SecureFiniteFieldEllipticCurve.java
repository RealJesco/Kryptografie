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

    private BigInteger generatePrimeCongruentToFiveModEight(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p = new BigInteger(bitLengthOfP.intValue(), new SecureRandom());
        // Adjust p to be congruent to 5 modulo 8
        BigInteger modEight = p.mod(BigInteger.valueOf(8));
        if (!modEight.equals(BigInteger.valueOf(5))) {
            p = p.add(BigInteger.valueOf(5).subtract(modEight));
            if (p.bitLength() > bitLengthOfP.intValue()) {
                p = p.subtract(BigInteger.valueOf(8));
            }
        }

        while (!MathMethods.millerRabinTest(p, millerRabinIterations, m, BigInteger.valueOf(counter.incrementAndGet()))) {
            p = p.add(BigInteger.valueOf(8));
            if (p.bitLength() > bitLengthOfP.intValue()) {
                p = new BigInteger(bitLengthOfP.intValue(), new SecureRandom()); // Reset if size exceeds
                p = p.add(BigInteger.valueOf(5).subtract(p.mod(BigInteger.valueOf(8))));
            }
        }
        return p;

    }

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
    private BigInteger calculateQ( BigInteger orderN){
        return orderN.divide(Resource.EIGHT);
    }
    private void calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){

        BigInteger p;
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, null);
        BigInteger orderN;
        BigInteger q;

        while (true){
            double time = System.currentTimeMillis();
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
