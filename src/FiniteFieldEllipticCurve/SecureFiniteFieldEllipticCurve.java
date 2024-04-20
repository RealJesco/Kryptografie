package FiniteFieldEllipticCurve;

import elGamal.ElGamalMenezesVanstoneService;
import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class SecureFiniteFieldEllipticCurve {
    private BigInteger n;
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

    private BigInteger calculatePrimeMod8(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
        // random prime p. Use built in Java function
//        BigInteger p = BigInteger.probablePrime(bitLengthOfP.intValue(), new java.util.Random());
        BigInteger pMod8 = p.mod(Resource.EIGHT);
        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, p);

        while (!pMod8.equals(Resource.FIVE) || !legendreSign.equals(Resource.ONE)){
            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
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
    private BigInteger calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){

        BigInteger p;
        FiniteFieldEllipticCurve ellipticCurve;
        BigInteger orderN;
        BigInteger q;

        while (true){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve = new FiniteFieldEllipticCurve(n, p);
            boolean pIsPrime = MathMethods.parallelMillerRabinTest(p, millerRabinIterations, Resource.ONE_HUNDRED, BigInteger.valueOf(counter.incrementAndGet()));
            if(!pIsPrime){
                continue;
            }

            orderN = ellipticCurve.calculateOrder(n);
            q = calculateQ(orderN);

            boolean qIsPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, Resource.ONE_HUNDRED, BigInteger.valueOf(counter.incrementAndGet()));

            if(qIsPrime && orderN.equals(q.multiply(Resource.EIGHT))){
                break;
            }
        }

        assert q.multiply(Resource.EIGHT).equals(orderN);
        //Assert p is congruent to 5 mod 8
        assert p.mod(Resource.EIGHT).equals(Resource.FIVE);

        this.safeEllipticCurve = ellipticCurve;
        assert this.safeEllipticCurve.getModuleR().equals(p);
        assert this.safeEllipticCurve.calculateOrder(n).equals(orderN);
        this.q = q;
        return p;
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
