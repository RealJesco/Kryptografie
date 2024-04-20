package elGamal;

import FiniteFieldEllipticCurve.*;
import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static mathMethods.MathMethods.generateRandomPrime;

public class ElGamalMenezesVanstoneService {
    public static BigInteger generateUniquePrime(BigInteger bitLength, int millerRabinSteps, BigInteger m, AtomicInteger counter) {
        BigInteger possiblePrime;
        int exponent = bitLength.intValue();
        BigInteger lowerBound = Resource.TWO.pow(exponent - 1);
        BigInteger upperBound = Resource.TWO.pow(exponent);
        possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps, counter);
        return possiblePrime;
    }

    /**
     * @param message message to be encrypted
     * @param publicKey public key
     * @return cipher message
     */
    public static CipherMessage encrypt(Message message, PublicKey publicKey) {
        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getModuleR();
        BigInteger a1 = ellipticCurve.getA();
        BigInteger q = ellipticCurve.calculateOrder(a1.divide(a1).negate()).divide(Resource.EIGHT);

        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        int primeBitLength = prime.bitLength();
        BigInteger k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);

        while (k.equals(Resource.ZERO)) {
            k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, qSubtractONE);
        }

        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);

        BigInteger yOfKY = ky.getY();
        BigInteger xOfKY = ky.getX();
        while (xOfKY.equals(Resource.ZERO) || yOfKY.equals(Resource.ZERO)) {
            k = MathMethods.randomElsner(new BigInteger(primeBitLength, random), new BigInteger(primeBitLength, randomRangePicker), Resource.ONE, prime);
            ky = publicKey.groupElement().multiply(k, ellipticCurve);
        }

        assert !(ky instanceof InfinitePoint);
        EllipticCurvePoint a = publicKey.generator().multiply(k, ellipticCurve);

        return new CipherMessage(a, xOfKY.multiply(message.m1()).mod(prime), yOfKY.multiply(message.m2()).mod(prime));
    }

    /**
     * @param cipherMessage
     * @param privateKey
     * @return
     */
    public static Message decrypt(CipherMessage cipherMessage, PrivateKey privateKey) {
        EllipticCurvePoint xa = cipherMessage.point().multiply(privateKey.secretMultiplierX(), privateKey.ellipticCurve());
        BigInteger prime = privateKey.ellipticCurve().getModuleR();
        BigInteger c1 = MathMethods.modularInverse(xa.getX(), prime);
        BigInteger c2 = MathMethods.modularInverse(xa.getY(), prime);
        BigInteger newM1 = cipherMessage.b1().multiply(c1).mod(prime);
        BigInteger newM2 = cipherMessage.b2().multiply(c2).mod(prime);

        return new Message(newM1, newM2);
    }

    /**
     * @param keyPair
     * @param message
     * @return
     */
    //sign and verify methods
    public static MenezesVanstoneSignature sign(final KeyPair keyPair, final BigInteger message) {
/**
 * Zur Signierung fuhrt Alice die folgenden Schritte aus:
 *  Alice wahlt eine zufallige Zahl k ZZq mit k= 0.
 *  Alice berechnet den Kurvenpunkt (uv) = kg E(ZZp). Hiermit wiederum bestimmt
 *  sie die Zahl r u mod q.
 *  Alice berechnet k 1 mod q.
 *  Alice berechnet s = (h(M) + xr)k 1 mod q mit der Hashfunktion h = SHA-1 14, wobei M ein Tupel aus zwei Zahlen ist.
 *  Im Falle r = 0 oder s = 0 bricht Alice das Verfahren ab und wiederholt den Proze
 *  mit der Wahl einer neuen Zufallszahl k. Anderenfalls ubermittelt sie Bob den Klartext
 *  M sowie die zugehorige Signatur (rs).
 *
 **/

        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = keyPair.publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getModuleR();
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

        EllipticCurvePoint uv = kg;

        BigInteger r = uv.getX().mod(q);
        BigInteger h = message;
        BigInteger x = keyPair.privateKey.secretMultiplierX();
        BigInteger xr = x.multiply(r).mod(q);
        BigInteger kInverse = MathMethods.modularInverse(k, q);
        assert kInverse.equals(kInverse.mod(q));
        BigInteger hPlusXr = h.add(xr);
        BigInteger s = hPlusXr.multiply(kInverse).mod(q);
        BigInteger sInverse = MathMethods.modularInverse(s, q);

        assert uv.getX().mod(q).equals(r);
        assert r.mod(q).equals(r);
        assert s.mod(q).equals(s);
        return new MenezesVanstoneSignature(r, s);
    }

    /**
     * @param publicKey
     * @param message
     * @param signature
     * @return
     */
    public static boolean verify(final PublicKey publicKey, final BigInteger message, final MenezesVanstoneSignature signature) {
        /**
         * Zur Verifikation pruft Bob die Signatur (rs) zu dem Klartext M wie folgt:
         *  Bob berechnet w = s 1 mod q.
         *  Bob berechnet u = h(M)w mod q.
         *  Bob berechnet v = rw mod q.
         *  Bob berechnet den Kurvenpunkt (uv) = uG + vP E(ZZp).
         *  Bob akzeptiert die Signatur, wenn r = u mod q gilt, andernfalls wird die Signatur abgelehnt.
         **/
        BigInteger q = publicKey.order();
        BigInteger h = message;
        BigInteger w = MathMethods.modularInverse(signature.s(), q);
        BigInteger u1 = w.multiply(h).mod(q);
        BigInteger u2 = signature.r().multiply(w).mod(q);
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        EllipticCurvePoint u1New = publicKey.generator().multiply(u1, ellipticCurve);
        EllipticCurvePoint u2New = publicKey.groupElement().multiply(u2, ellipticCurve);
        EllipticCurvePoint uv = u1New.add(u2New, ellipticCurve);

        BigInteger uvXModQ = uv.getX().mod(q);
        BigInteger rModQ = signature.r().mod(q);

        return uvXModQ.equals(rModQ);
    }
}

