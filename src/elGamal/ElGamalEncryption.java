package elGamal;

import FiniteFieldEllipticCurve.EllipticCurvePoint;
import FiniteFieldEllipticCurve.FiniteFieldEllipticCurve;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ElGamalEncryption {

    public CipherMessage encrypt(Message message, PublicKey publicKey) {
        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getModuleR();
        BigInteger k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, prime);

        while (k.equals(BigInteger.ZERO)){
            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, prime);
        }

        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);

        while (ky.getX().equals(BigInteger.ZERO) || ky.getY().equals(BigInteger.ZERO)) {
            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, prime);
            ky = publicKey.groupElement().multiply(k, ellipticCurve);
        }

        EllipticCurvePoint a = publicKey.generator().multiply(k, publicKey.ellipticCurve());

        return new CipherMessage(a, ky.getX().multiply(message.m1()).mod(prime), ky.getY().multiply(message.m2()).mod(prime));

    }

    public Message decrypt(CipherMessage cipherMessage, PrivateKey privateKey) {
        EllipticCurvePoint xa = cipherMessage.point().multiply( privateKey.secretMultiplierX(), privateKey.ellipticCurve() );
        BigInteger prime = privateKey.ellipticCurve().getModuleR();
        BigInteger c1 = MathMethods.modularInverse(xa.getX(), prime);
        BigInteger c2 =  MathMethods.modularInverse(xa.getY(), prime);
        BigInteger newM1 = cipherMessage.b1().multiply( c1 ).mod(prime);
        BigInteger newM2 = cipherMessage.b2().multiply( c2 ).mod(prime);

        return new Message(newM1, newM2);
    }
}

