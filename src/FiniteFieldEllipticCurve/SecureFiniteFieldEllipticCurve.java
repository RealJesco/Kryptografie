package FiniteFieldEllipticCurve;

import elGamal.ElGamalMenezesVanstoneService;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class SecureFiniteFieldEllipticCurve {
    private BigInteger n;

    private FiniteFieldEllipticCurve safeEllipticCurve;
    AtomicInteger counter = new AtomicInteger(1);

    public BigInteger getN() {
        return n;
    }

    public FiniteFieldEllipticCurve getSafeEllipticCurve() {
        return safeEllipticCurve;
    }

    private BigInteger calculatePrimeMod8(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m) {
        BigInteger p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
        BigInteger pMod8 = p.mod(BigInteger.valueOf(8));

        while (!pMod8.equals(BigInteger.valueOf(5))){
            p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
            pMod8 = p.mod(BigInteger.valueOf(8));
        }
        return p;
    }
    private BigInteger calculateQ(BigInteger p){


        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, p);

        BigInteger orderN = ellipticCurve.calculateOrder(n);
        return orderN.divide(BigInteger.valueOf(8));
    }
    private BigInteger calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){
        BigInteger p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
        BigInteger q = calculateQ(p);
        boolean isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));
        while (!isPrime){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            q = calculateQ(p);
            isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));
            BigInteger pMod8 = p.mod(BigInteger.valueOf(8));

            while (!pMod8.equals(BigInteger.valueOf(5))){
                p = ElGamalMenezesVanstoneService.generateUniquePrime(bitLengthOfP, millerRabinIterations, BigInteger.ONE, counter);
            }
        }
        return p;
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;

        BigInteger p = calculatePAndQ(bitLengthOfP, millerRabinIterations, m);

        while (p.mod(n).equals(BigInteger.ZERO)){
            p = calculatePAndQ(bitLengthOfP, millerRabinIterations, m);
        }

        this.safeEllipticCurve = new FiniteFieldEllipticCurve(n, p);



    }
}
