package elGamal;

import FiniteFieldEllipticCurve.*;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyPair {

    PrivateKey privateKey;
    PublicKey publicKey;

    public KeyPair(){

    }

    public void generateKeyPair(FiniteFieldEllipticCurve ellipticCurve){

        BigInteger bitLengthOfP = BigInteger.valueOf(ellipticCurve.getModuleR().bitLength());
        //Generate g

        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        BigInteger x = MathMethods.randomElsner(new BigInteger(bitLengthOfP.bitLength(), random), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), BigInteger.ONE, bitLengthOfP);
        BigInteger r = x.pow(3).add(ellipticCurve.getA().multiply(x)).add(ellipticCurve.getB());

        BigInteger legendreSign = MathMethods.verifyEulerCriterion(r, ellipticCurve.getModuleR());

        while (legendreSign.equals(BigInteger.ONE.negate())){
            x = MathMethods.randomElsner(new BigInteger(bitLengthOfP.bitLength(), random), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), BigInteger.ONE, bitLengthOfP);
            r = x.pow(3).add(ellipticCurve.getA().multiply(x)).add(ellipticCurve.getB());
            legendreSign = MathMethods.verifyEulerCriterion(r, ellipticCurve.getModuleR());
        }

        BigInteger y = MathMethods.alternativeQuickExponentiation(r, (ellipticCurve.getModuleR().add(BigInteger.valueOf(3))).divide(BigInteger.valueOf(8)), ellipticCurve.getModuleR());

        EllipticCurvePoint generator = new FiniteFieldEcPoint(x,y);
        System.out.println(x + " " + y);

        //TODO Refactor this ASAP: Validate q*g == infinitePoint
        BigInteger a = ellipticCurve.getA();
        BigInteger q = ellipticCurve.calculateOrder(a.divide(a).negate()).divide(BigInteger.valueOf(8));
        EllipticCurvePoint qg = generator.multiply(q, ellipticCurve);
        System.out.println(qg.getX() + " " + qg.getY());

        while (qg instanceof InfinitePoint) {
            x = MathMethods.randomElsner(new BigInteger(bitLengthOfP.bitLength(), random), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), BigInteger.ONE, bitLengthOfP);
            r = x.pow(3).add(ellipticCurve.getA().multiply(x)).add(ellipticCurve.getB());
            legendreSign = MathMethods.verifyEulerCriterion(r, ellipticCurve.getModuleR());
            while (legendreSign.equals(BigInteger.ONE.negate())){
                x = MathMethods.randomElsner(new BigInteger(bitLengthOfP.bitLength(), random), new BigInteger(bitLengthOfP.bitLength(), randomRangePicker), BigInteger.ONE, bitLengthOfP);
                r = x.pow(3).add(ellipticCurve.getA().multiply(x)).add(ellipticCurve.getB());
                legendreSign = MathMethods.verifyEulerCriterion(r, ellipticCurve.getModuleR());
            }
            y = MathMethods.alternativeQuickExponentiation(r, (ellipticCurve.getModuleR().add(BigInteger.valueOf(3))).divide(BigInteger.valueOf(8)), ellipticCurve.getModuleR());
            generator = new FiniteFieldEcPoint(x,y);
            qg = generator.multiply(q, ellipticCurve);

        }
        assert !qg.getX().equals(BigInteger.ZERO) && !qg.getY().equals(BigInteger.ZERO);

        assert ellipticCurve.isValidPoint(generator);


        SecureRandom privateKeyRandomSeed = new SecureRandom();
        this.privateKey = new PrivateKey(ellipticCurve, new BigInteger(bitLengthOfP.bitLength(), privateKeyRandomSeed));
        System.out.println(generator.getX() + " " + generator.getY());
        this.publicKey = new PublicKey(ellipticCurve, generator, generator.multiply(privateKey.secretMultiplierX(), privateKey.ellipticCurve()));

    }
}
