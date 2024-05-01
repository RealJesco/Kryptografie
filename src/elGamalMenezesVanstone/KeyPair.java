package elGamalMenezesVanstone;

import FiniteFieldEllipticCurve.*;
import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {

    PrivateKey privateKey;
    PublicKey publicKey;

    public KeyPair(){

    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public EllipticCurvePoint calculateSignatureSuitableGeneratorPoint (FiniteFieldEllipticCurve ellipticCurve, BigInteger q) {

        SecureRandom randomRangePicker = new SecureRandom();

        BigInteger bitLengthOfP = BigInteger.valueOf(ellipticCurve.getP().bitLength());

        BigInteger prime = ellipticCurve.getP();
        EllipticCurvePoint generator;

        while ( true ) {
            BigInteger y;
            BigInteger x = MathMethods.randomElsner(BigInteger.valueOf(13), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), BigInteger.ONE, q.subtract(Resource.ONE));
            BigInteger r = x.pow(3).add(ellipticCurve.getA().multiply(x)).add(ellipticCurve.getB());
            BigInteger legendreSign = MathMethods.verifyEulerCriterion(r, prime);

            if (legendreSign.equals(BigInteger.ONE.negate())){
                continue;
            }

            BigInteger rExponent = prime.subtract(Resource.ONE).divide(Resource.FOUR);
            BigInteger rConditionMod = MathMethods.alternativeQuickExponentiation(r, rExponent, prime);

            BigInteger yExponent = (prime).add(Resource.THREE).divide(Resource.EIGHT);

            if (rConditionMod.equals(Resource.ONE)) {
                y = MathMethods.alternativeQuickExponentiation(r, yExponent, prime);
            } else {
                BigInteger inverseTwo = MathMethods.modularInverse(Resource.TWO, prime);
                y = MathMethods.alternativeQuickExponentiation(Resource.FOUR.multiply(r), yExponent, prime);
                y = y.multiply(inverseTwo).mod(prime);
            }

            generator = new FiniteFieldEcPoint(x,y);
            EllipticCurvePoint qg = generator.multiply(q, ellipticCurve);
            if (qg instanceof InfinitePoint) {
                return generator;
            }
        }
    }

    public void generateKeyPair(SecureFiniteFieldEllipticCurve secureFiniteFieldEllipticCurve){
        FiniteFieldEllipticCurve ellipticCurve = secureFiniteFieldEllipticCurve.getSafeEllipticCurve();
        BigInteger q = secureFiniteFieldEllipticCurve.getQ();
        BigInteger bitLengthOfP = BigInteger.valueOf(ellipticCurve.getP().bitLength());
        assert MathMethods.verifyEulerCriterion(ellipticCurve.getP(), Resource.EIGHT).equals(Resource.ONE);

        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();

        EllipticCurvePoint generator = calculateSignatureSuitableGeneratorPoint(ellipticCurve, q);
        assert ellipticCurve.isValidPoint(generator);

        //TODO Refactor this ASAP: Validate q*g == infinitePoint



        //TODO groupElement needs to be not an instance of InfinitePoint. Refactoring needed
        BigInteger secretMultiplierX  = MathMethods.randomElsner(new BigInteger(bitLengthOfP.bitLength(), random), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), Resource.ONE, q.subtract(Resource.ONE));

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
        assert ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(Resource.EIGHT).equals(q);
    }

    @Override
    public String toString() {
        return "KeyPair{" +
                "privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
