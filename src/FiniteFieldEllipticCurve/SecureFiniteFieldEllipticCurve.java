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
    private BigInteger calculateQ(BigInteger p, BigInteger orderN){



        return orderN.divide(BigInteger.valueOf(8));
    }
    private BigInteger calculatePAndQ(BigInteger bitLengthOfP, int millerRabinIterations, BigInteger m){

        BigInteger p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(n, p);
        BigInteger orderN = ellipticCurve.calculateOrder(n);
        BigInteger q = calculateQ(p, orderN);


        boolean isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));


        while (!isPrime && !orderN.divide(BigInteger.valueOf(8)).equals(BigInteger.ZERO)){
            p = calculatePrimeMod8(bitLengthOfP, millerRabinIterations, m);
            ellipticCurve = new FiniteFieldEllipticCurve(n, p);
            orderN = ellipticCurve.calculateOrder(n);
            System.out.println("orderOfN + " + orderN);
            q = calculateQ(p, orderN);
            isPrime = MathMethods.parallelMillerRabinTest(q, millerRabinIterations, bitLengthOfP, BigInteger.valueOf(counter.incrementAndGet()));
        }

        System.out.println(orderN);
        System.out.println(q);
        assert q.multiply(BigInteger.valueOf(8)).equals(orderN);

        this.safeEllipticCurve = ellipticCurve;
        return p;
    }
    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;

        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);


//        this.safeEllipticCurve = new FiniteFieldEllipticCurve(n, p);



    }

    public SecureFiniteFieldEllipticCurve(BigInteger bitLengthOfP, BigInteger n, int millerRabinIterations, BigInteger m, BigInteger moduleR, BigInteger orderOfSubGroup) {
        assert n.compareTo(BigInteger.ZERO) > 0;
        this.n = n;

        calculatePAndQ(bitLengthOfP, millerRabinIterations, m);


//        this.safeEllipticCurve = new FiniteFieldEllipticCurve(n, p);



    }

}
