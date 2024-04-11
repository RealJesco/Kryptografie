package FiniteFieldEllipticCurve;

import elGamal.ElGamalService;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.SecureRandom;
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
        BigInteger p = ElGamalService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
        BigInteger pMod8 = p.mod(BigInteger.valueOf(8));

        while (!pMod8.equals(BigInteger.valueOf(5))){
            p = ElGamalService.generateUniquePrime(bitLengthOfP, millerRabinIterations, m, counter);
            pMod8 = p.mod(BigInteger.valueOf(8));
        }
        return p;
    }
    private BigInteger calculateQ(BigInteger p){


        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, p);

        BigInteger orderN = ellipticCurve.calculateOrder(n);
        return orderN.divide(BigInteger.valueOf(8));
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        //TODO assert n > 0
        this.n = n;
        BigInteger p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);

        while (MathMethods.parallelMillerRabinTest(calculateQ(p),millerRabinIterations , bitLengthOfP,BigInteger.valueOf(counter.incrementAndGet()))){
            BigInteger pMod8 = p.mod(BigInteger.valueOf(8));

            while (!pMod8.equals(BigInteger.valueOf(5))){
                p = ElGamalService.generateUniquePrime(bitLengthOfP, millerRabinIterations, BigInteger.ONE, counter);
            }
        }

        this.safeEllipticCurve = new FiniteFieldEllipticCurve(n, p);

        //TODO assert and recalculate if p divides 2*n (2n mod p !=0)

    }
}
