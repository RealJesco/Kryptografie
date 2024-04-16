package FiniteFieldEllipticCurve;

import elGamal.ElGamalMenezesVanstoneService;
import mathMethods.MathMethods;

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
        BigInteger pMod8 = p.mod(BigInteger.valueOf(8));
        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, p);

        while (!pMod8.equals(BigInteger.valueOf(5)) || !legendreSign.equals(BigInteger.ONE)){
            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
            pMod8 = p.mod(BigInteger.valueOf(8));
            legendreSign = MathMethods.verifyEulerCriterion(n, p);
        }

        assert legendreSign.equals(BigInteger.ONE);
        assert !(p.equals(n.multiply(BigInteger.TWO)));
        return p;
    }
    private BigInteger calculateQ( BigInteger orderN){
        return orderN.divide(BigInteger.valueOf(8));
    }
    private BigInteger calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){

        BigInteger p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, p);
        BigInteger orderN;
        BigInteger q;


        while (true){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve.setP(p);
            boolean pIsPrime = MathMethods.parallelMillerRabinTest(p, millerRabinIterations, BigInteger.valueOf(100), BigInteger.valueOf(counter.incrementAndGet()));

            if(!pIsPrime){
                continue;
            }

            orderN = ellipticCurve.calculateOrder(n);

            if(orderN.equals(n.multiply(BigInteger.TWO)) || !orderN.mod(BigInteger.valueOf(8)).equals(BigInteger.ZERO)){
                continue;
            }

            q = calculateQ(orderN);

            boolean qIsPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, BigInteger.valueOf(100), BigInteger.valueOf(counter.incrementAndGet()));

            if(qIsPrime && orderN.equals(q.multiply(BigInteger.valueOf(8)))){
                break;
            }
        }

        assert q.multiply(BigInteger.valueOf(8)).equals(orderN);
        assert p.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(5));

        this.safeEllipticCurve = ellipticCurve;
        assert this.safeEllipticCurve.getP().equals(p);
        assert this.safeEllipticCurve.calculateOrder(n).equals(orderN);
        this.q = q;
        return p;
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;
        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);
    }

    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m, FiniteFieldEllipticCurve ellipticCurve) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;
        this.safeEllipticCurve = ellipticCurve;
    }

}
