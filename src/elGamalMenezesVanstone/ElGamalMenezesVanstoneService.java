package elGamalMenezesVanstone;

import FiniteFieldEllipticCurve.*;
import mathMethods.MathMethods;
import org.apache.commons.math3.util.Pair;
import resource.Resource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static mathMethods.MathMethods.generateRandomPrime;

public class ElGamalMenezesVanstoneService {
    public static BigInteger generateUniquePrime(BigInteger bitLength, int millerRabinSteps, BigInteger m, AtomicInteger counter) {
        BigInteger possiblePrime;
        BigInteger lowerBound = Resource.FIVE;
        BigInteger upperBound = Resource.TWO.pow(bitLength.intValue());
        possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps, counter);
        return possiblePrime;
    }

//    /**
//     * @param message   message to be encrypted
//     * @param publicKey public key
//     * @return cipher message
//     */
//    public static CipherMessage encrypt(Message message, PublicKey publicKey) {
//        SecureRandom random = new SecureRandom();
//        SecureRandom randomRangePicker = new SecureRandom();
//        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
//        BigInteger prime = ellipticCurve.getP();
//        BigInteger q = ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(Resource.EIGHT);
//        int primeBitLength = prime.bitLength();
//        BigInteger qSubtractONE = q.subtract(Resource.ONE);
//
//        BigInteger k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
//
//        while (k.equals(Resource.ZERO)) {
//            k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
//        }
//
//        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);
//
//        while (ky.getX().equals(Resource.ZERO) || ky.getY().equals(Resource.ZERO)) {
//            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
//            ky = publicKey.groupElement().multiply(k, ellipticCurve);
//        }
//
//        assert !(ky instanceof InfinitePoint);
//        EllipticCurvePoint a = publicKey.generator().multiply(k, publicKey.ellipticCurve());
//
//        return new CipherMessage(a, ky.getX().multiply(message.m1()).mod(prime), ky.getY().multiply(message.m2()).mod(prime));
//    }

    /**
     * @param publicKey public key
     * @param qSubtractONE order of the elliptic curve subtracted by one
     * @return random number k and public key generator point multiplied by k
     */
    private static Pair<BigInteger, EllipticCurvePoint> generateKandKy(PublicKey publicKey, BigInteger qSubtractONE) {
        SecureRandom random = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        SecureRandom randomRangePicker = new SecureRandom();
        BigInteger prime = ellipticCurve.getP();
        int primeBitLength = prime.bitLength();
        BigInteger k;

        do {
            k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
        } while (k.equals(Resource.ZERO));

        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);

        while (ky.getX().equals(Resource.ZERO) || ky.getY().equals(Resource.ZERO)) {
            k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
            ky = publicKey.groupElement().multiply(k, ellipticCurve);
        }
        assert !(ky instanceof InfinitePoint);
        return new Pair<>(k, ky);
    }

    /**
     * Skript S. 71-72 Algorithm 3.3
     * @param message  message (as biginteger tuple) to be encrypted
     * @param publicKey public key
     * @param k random number
     * @param ky public key generator point multiplied by k
     * @return cipher message
     */
    public static CipherMessage encrypt(Message message, PublicKey publicKey, BigInteger k, EllipticCurvePoint ky) {
        EllipticCurvePoint a = publicKey.generator().multiply(k, publicKey.ellipticCurve());
        BigInteger prime = publicKey.ellipticCurve().getP();
        return new CipherMessage(a, ky.getX().multiply(message.m1()).mod(prime), ky.getY().multiply(message.m2()).mod(prime));
    }

    /**
     * @param message  message to be encrypted
     * @param publicKey public key
     * @return cipher message
     */
    public static CipherMessage encrypt(Message message, PublicKey publicKey) {
        BigInteger q = publicKey.order();
        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        Pair<BigInteger, EllipticCurvePoint> kAndKy = generateKandKy(publicKey, qSubtractONE);
        return encrypt(message, publicKey, kAndKy.getKey(), kAndKy.getValue());
    }

    /**
     * Skript S. 72 Algorithm 3.3
     * @param cipherMessage cipher message
     * @param privateKey   private key
     * @return  decrypted message
     */
    public static Message decrypt(CipherMessage cipherMessage, PrivateKey privateKey) {
        EllipticCurvePoint xa = cipherMessage.point().multiply(privateKey.secretMultiplierX(), privateKey.ellipticCurve());
        BigInteger prime = privateKey.ellipticCurve().getP();
        BigInteger c1 = MathMethods.modularInverse(xa.getX(), prime);
        BigInteger c2 = MathMethods.modularInverse(xa.getY(), prime);
        BigInteger newM1 = cipherMessage.b1().multiply(c1).mod(prime);
        BigInteger newM2 = cipherMessage.b2().multiply(c2).mod(prime);

        return new Message(newM1, newM2);
    }

    /**
     * Skript S. 73-75 Algorithm 3.5
     * @param keyPair key pair
     * @param message message
     * @return signature
     */
    //sign and verify methods
    public static MenezesVanstoneSignature sign(final KeyPair keyPair, final BigInteger message) {

        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = keyPair.publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getP();
        BigInteger q = keyPair.publicKey.order();
        BigInteger a = ellipticCurve.getA();
        assert q.equals(ellipticCurve.calculateOrder(a.divide(a).negate()).divide(Resource.EIGHT));
        int primeBitLength = prime.bitLength();
        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        BigInteger k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);

        EllipticCurvePoint kg = keyPair.publicKey.generator().multiply(k, ellipticCurve);

        assert !(kg instanceof InfinitePoint);
        assert keyPair.publicKey.ellipticCurve().isValidPoint(kg);
        assert keyPair.publicKey.ellipticCurve().isValidPoint(keyPair.publicKey.generator());
        assert keyPair.publicKey.ellipticCurve().isValidPoint(keyPair.publicKey.groupElement());


        BigInteger r = kg.getX().mod(q);
        BigInteger x = keyPair.privateKey.secretMultiplierX();
        BigInteger xr = x.multiply(r).mod(q);
        BigInteger kInverse = MathMethods.modularInverse(k, q);
        assert kInverse.equals(kInverse.mod(q));
        BigInteger hPlusXr = message.add(xr);
        BigInteger s = hPlusXr.multiply(kInverse).mod(q);

        assert kg.getX().mod(q).equals(r);
        assert r.mod(q).equals(r);
        assert s.mod(q).equals(s);
        return new MenezesVanstoneSignature(r, s);
    }

    /**
     * @param publicKey public key
     * @param message  message
     * @param signature signature
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verify(final PublicKey publicKey, final BigInteger message, final MenezesVanstoneSignature signature) {
        BigInteger q = publicKey.order();
        BigInteger w = MathMethods.modularInverse(signature.s(), q);
        BigInteger u1 = w.multiply(message).mod(q);
        BigInteger u2 = signature.r().multiply(w).mod(q);
        EllipticCurvePoint u1New = publicKey.generator().multiply(u1, publicKey.ellipticCurve());
        EllipticCurvePoint u2New = publicKey.groupElement().multiply(u2, publicKey.ellipticCurve());
        EllipticCurvePoint uv = u1New.add(u2New, publicKey.ellipticCurve());

        BigInteger uvXModQ = uv.getX().mod(q);
        BigInteger rModQ = signature.r().mod(q);

        return uvXModQ.equals(rModQ);
    }
}

