package main.elGamalMenezesVanstone;

import main.elGamalMenezesVanstone.records.PrivateKey;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.finiteFieldEllipticCurve.*;
import main.mathMethods.MathMethods;
import main.resource.Resource;
import test.IgnoreCoverage;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {
    PrivateKey privateKey;
    PublicKey publicKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public EllipticCurvePoint calculateSignatureSuitableGeneratorPoint (FiniteFieldEllipticCurve ellipticCurve, BigInteger q, BigInteger m) {


        BigInteger prime = ellipticCurve.getP();
        BigInteger inverseTwo = MathMethods.modularInverse(Resource.TWO, prime);
        BigInteger yExponent = (prime).add(Resource.THREE).divide(Resource.EIGHT);
        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        BigInteger ellipticCurveA = ellipticCurve.getA();
        assert MathMethods.calculateEulerCriterion(prime, Resource.EIGHT).equals(Resource.ONE);
        assert MathMethods.calculateEulerCriterion(ellipticCurveA, prime).equals(Resource.ONE);
        assert MathMethods.calculateEulerCriterion(ellipticCurve.getB(), prime).equals(Resource.ONE);

        BigInteger ellipticCurveB = ellipticCurve.getB();
        EllipticCurvePoint generator;
        while ( true ) {
            BigInteger y;
            BigInteger x = MathMethods.randomElsner(m, BigInteger.valueOf(Resource.counter.incrementAndGet()), Resource.ONE, qSubtractONE);
            BigInteger r = x.pow(3).add(ellipticCurveA.multiply(x)).add(ellipticCurveB);
            BigInteger legendreSign = MathMethods.calculateEulerCriterion(r, prime);

            if (legendreSign.equals(Resource.ONE.negate())){
                continue;
            }

            BigInteger rExponent = prime.subtract(Resource.ONE).divide(Resource.FOUR);
            BigInteger rConditionMod = MathMethods.alternativeQuickExponentiation(r, rExponent, prime);


            if (rConditionMod.equals(Resource.ONE)) {
                y = MathMethods.alternativeQuickExponentiation(r, yExponent, prime);
            } else {
                y = MathMethods.alternativeQuickExponentiation(Resource.FOUR.multiply(r), yExponent, prime);
                y = y.multiply(inverseTwo).mod(prime);
            }

            generator = new FiniteFieldEcPoint(x,y);
            EllipticCurvePoint qg = generator.multiply(q, ellipticCurve);
            if (qg instanceof InfinitePoint) {
                //System.out.println("Generator: " + generator);

                return generator;
            }
        }
    }

    public void generateKeyPair(SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve, BigInteger m){
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        BigInteger q = secureFiniteFieldEllipticCurve.getQ();
        BigInteger bitLengthOfP = BigInteger.valueOf(ellipticCurve.getP().bitLength());
        assert MathMethods.calculateEulerCriterion(ellipticCurve.getP(), Resource.EIGHT).equals(Resource.ONE);


        EllipticCurvePoint generator = calculateSignatureSuitableGeneratorPoint(ellipticCurve, q, m);
        assert ellipticCurve.isValidPoint(generator);




        BigInteger secretMultiplierX  = MathMethods.randomElsner(m, BigInteger.valueOf(Resource.counter.incrementAndGet()), Resource.ONE, q.subtract(Resource.ONE));

        this.privateKey = new PrivateKey(ellipticCurve, secretMultiplierX);
        this.publicKey = new PublicKey(ellipticCurve, generator, generator.multiply(privateKey.secretMultiplierX(), privateKey.ellipticCurve()), q);
        assert ellipticCurve.isValidPoint(publicKey.groupElement());
        assert ellipticCurve.isValidPoint(publicKey.generator());
        assert privateKey.secretMultiplierX().compareTo(Resource.ZERO) > 0;
        assert privateKey.secretMultiplierX().compareTo(q) < 0;
        assert ellipticCurve.isValidPoint(generator);
        assert ellipticCurve.isValidPoint(publicKey.groupElement());
        assert ellipticCurve.isValidPoint(publicKey.generator());
        assert publicKey.order().equals(q);
        assert (publicKey.groupElement().multiply(q, ellipticCurve) instanceof InfinitePoint);
    }

    @Override
    @IgnoreCoverage
    public String toString() {
        return "KeyPair{" +
                "privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
