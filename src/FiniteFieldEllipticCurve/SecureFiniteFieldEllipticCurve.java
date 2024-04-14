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
        BigInteger pMod8 = p.mod(BigInteger.valueOf(8));
        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, p);

        while (!pMod8.equals(BigInteger.valueOf(5)) || !legendreSign.equals(BigInteger.ONE)){
            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
            pMod8 = p.mod(BigInteger.valueOf(8));
            legendreSign = MathMethods.verifyEulerCriterion(n, p);
        }

        System.out.println(legendreSign.equals(BigInteger.ONE));
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
        BigInteger orderN = ellipticCurve.calculateOrder(n);
        BigInteger q = calculateQ(orderN);


        boolean isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));

        while (!isPrime || !orderN.divide(BigInteger.valueOf(8)).equals(q) || !(p.equals(n.multiply(BigInteger.TWO)))){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve = new FiniteFieldEllipticCurve(n, p);
            orderN = ellipticCurve.calculateOrder(n);
            q = calculateQ(orderN);
            isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));
        }

        System.out.println(orderN);
        System.out.println(q);
        assert q.multiply(BigInteger.valueOf(8)).equals(orderN);
        //Assert p is congruent to 5 mod 8
        assert p.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(5));

        this.safeEllipticCurve = ellipticCurve;
        assert this.safeEllipticCurve.getModuleR().equals(p);
        assert this.safeEllipticCurve.calculateOrder(n).equals(orderN);
        System.out.println("q + " + q);
        this.q = q;
        return p;
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;

        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);


//        this.safeEllipticCurve = new FiniteFieldEllipticCurve(n, p);



    }

    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m, FiniteFieldEllipticCurve ellipticCurve) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;
        this.safeEllipticCurve = ellipticCurve;
    }

}
