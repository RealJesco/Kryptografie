package main.elGamalMenezesVanstone;

import main.elGamalMenezesVanstone.records.*;
import main.finiteFieldEllipticCurve.*;
import main.mathMethods.MathMethods;
import org.apache.commons.math3.util.Pair;
import main.resource.Resource;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static main.mathMethods.MathMethods.generateRandomPrime;

public class ElGamalMenezesVanstoneService {
    /**
     * @param bitLength bit length of the prime
     * @param millerRabinSteps number of miller rabin steps
     * @param m random number seed
     * @param counter counter
     * @return unique prime
     */
    public static BigInteger generateUniquePrime(BigInteger bitLength, int millerRabinSteps, BigInteger m, AtomicInteger counter) {
        BigInteger possiblePrime;
        BigInteger lowerBound = Resource.FIVE;
        BigInteger upperBound = Resource.TWO.pow(bitLength.intValue());
        possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps, counter);
        return possiblePrime;
    }

    /**
     * Skript S.69-70
     * @param publicKey public key
     * @return random number k and public key generator point multiplied by k
     */
    public static Pair<BigInteger, EllipticCurvePoint> generateKandKy(PublicKey publicKey, BigInteger m) {
        BigInteger qSubtractOne = publicKey.q().subtract(Resource.ONE);
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getP();
        BigInteger k;

        do {
            k = MathMethods.randomElsner(m, BigInteger.valueOf(Resource.counter.incrementAndGet()), Resource.ONE, qSubtractOne);
        } while (k.equals(Resource.ZERO));

        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);

        while (ky instanceof InfinitePoint || ky.getX().equals(Resource.ZERO) || ky.getY().equals(Resource.ZERO)) {
            k = MathMethods.randomElsner(m, BigInteger.valueOf(Resource.counter.incrementAndGet()), Resource.ONE, qSubtractOne);
            ky = publicKey.groupElement().multiply(k, ellipticCurve);
        }
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
     * Skript S. 71-72 Algorithm 3.3
     * @param message  message to be encrypted
     * @param publicKey public key
     * @return cipher message
     */
    public static CipherMessage encrypt(Message message, PublicKey publicKey, BigInteger m) {
        BigInteger q = publicKey.q();
        Pair<BigInteger, EllipticCurvePoint> kAndKy = generateKandKy(publicKey, m);
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
    public static MenezesVanstoneSignature sign(final KeyPair keyPair, final BigInteger message, BigInteger m) {
        FiniteFieldEllipticCurve ellipticCurve = keyPair.publicKey.ellipticCurve();
        BigInteger q = keyPair.publicKey.q();
        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        BigInteger k = MathMethods.randomElsner(m, BigInteger.valueOf(Resource.counter.incrementAndGet()), Resource.ONE, qSubtractONE);

        EllipticCurvePoint kg = keyPair.publicKey.generator().multiply(k, ellipticCurve);

        BigInteger r = kg.getX().mod(q);
        BigInteger x = keyPair.privateKey.secretMultiplierX();
        BigInteger xr = x.multiply(r).mod(q);
        BigInteger kInverse = MathMethods.modularInverse(k, q);
        BigInteger hPlusXr = message.add(xr);
        BigInteger s = hPlusXr.multiply(kInverse).mod(q);

        return new MenezesVanstoneSignature(r, s);
    }

    /**
     * Skript S. 73-75 Algorithm 3.5
     * @param publicKey public key
     * @param message  message
     * @param signature signature
     * @return true if the signature is valid, false otherwise
     */
    public static boolean verify(final PublicKey publicKey, final BigInteger message, final MenezesVanstoneSignature signature) {
        BigInteger q = publicKey.q();
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
