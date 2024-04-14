package elGamal;

import FiniteFieldEllipticCurve.*;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static mathMethods.MathMethods.generateRandomPrime;

public class ElGamalMenezesVanstoneService {
    public static BigInteger generateUniquePrime(BigInteger bitLength, int millerRabinSteps, BigInteger m, AtomicInteger counter){
        BigInteger possiblePrime;
        BigInteger lowerBound = TWO.pow(bitLength.intValue() - 1);
        BigInteger upperBound = TWO.pow(bitLength.intValue());
        possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps, counter);
        return possiblePrime;
    }
    public static CipherMessage encrypt(Message message, PublicKey publicKey) {
        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getModuleR();
        BigInteger q = ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(BigInteger.valueOf(8));


        BigInteger k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE,  q.subtract(ONE));


        while (k.equals(BigInteger.ZERO)){
            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, q.subtract(ONE));
        }

        EllipticCurvePoint ky = publicKey.groupElement().multiply(k, ellipticCurve);

        while (ky.getX().equals(BigInteger.ZERO) || ky.getY().equals(BigInteger.ZERO)) {
            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, prime);
            ky = publicKey.groupElement().multiply(k, ellipticCurve);
        }

        assert !(ky instanceof InfinitePoint);
        EllipticCurvePoint a = publicKey.generator().multiply(k, publicKey.ellipticCurve());

        return new CipherMessage(a, ky.getX().multiply(message.m1()).mod(prime), ky.getY().multiply(message.m2()).mod(prime));

    }

    public static Message decrypt(CipherMessage cipherMessage, PrivateKey privateKey) {
        EllipticCurvePoint xa = cipherMessage.point().multiply( privateKey.secretMultiplierX(), privateKey.ellipticCurve() );
        BigInteger prime = privateKey.ellipticCurve().getModuleR();
        BigInteger c1 = MathMethods.modularInverse(xa.getX(), prime);
        BigInteger c2 =  MathMethods.modularInverse(xa.getY(), prime);
        BigInteger newM1 = cipherMessage.b1().multiply( c1 ).mod(prime);
        BigInteger newM2 = cipherMessage.b2().multiply( c2 ).mod(prime);

        return new Message(newM1, newM2);
    }



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

        System.out.println("KeyPair " + keyPair.toString());

        SecureRandom random = new SecureRandom();
        SecureRandom randomRangePicker = new SecureRandom();
        FiniteFieldEllipticCurve ellipticCurve = keyPair.publicKey.ellipticCurve();
        BigInteger prime = ellipticCurve.getModuleR();
        System.out.println("prime " + prime);
        BigInteger q = keyPair.publicKey.order();
        System.out.println("q  in sign " + q);
        assert q.equals(ellipticCurve.calculateOrder(ellipticCurve.getA().divide(ellipticCurve.getA()).negate()).divide(BigInteger.valueOf(8)));
        BigInteger k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE,  q.subtract(ONE));



        FiniteFieldEcPoint testPoint = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(1));
        EllipticCurvePoint kg = keyPair.publicKey.generator().multiply(k, ellipticCurve);

//        while (kg.getX().equals(BigInteger.ZERO) || kg.getY().equals(BigInteger.ZERO)) {
//            k = MathMethods.randomElsner(new BigInteger(prime.bitLength(), random), new BigInteger(prime.bitLength(), randomRangePicker), BigInteger.ONE, q.subtract(ONE));
//            kg = keyPair.publicKey.generator().multiply(k, ellipticCurve);
//        }

        System.out.println("kg + " + kg);
//        kg = testPoint.multiply(BigInteger.valueOf(0), ellipticCurve);
        System.out.println(keyPair.publicKey.ellipticCurve().isValidPoint(kg));
        assert !(kg instanceof InfinitePoint);
        assert keyPair.publicKey.ellipticCurve().isValidPoint(kg);
        assert keyPair.publicKey.ellipticCurve().isValidPoint(keyPair.publicKey.generator());
        assert keyPair.publicKey.ellipticCurve().isValidPoint(keyPair.publicKey.groupElement());



        EllipticCurvePoint uv = kg;

        System.out.println("uv: " + uv);
        BigInteger r = uv.getX().mod(q);
        BigInteger h = message;
        BigInteger x = keyPair.privateKey.secretMultiplierX();
        BigInteger xr = x.multiply(r).mod(q);
        System.out.println("k: " + k);
        System.out.println("q: " + q);
        BigInteger kInverse = MathMethods.modularInverse(k, q);
        assert kInverse.equals(kInverse.mod(q));
        System.out.println("kInverse: " + kInverse);
        BigInteger hPlusXr = h.add(xr);
        System.out.println("h + xr: " + hPlusXr);
        BigInteger s = hPlusXr.multiply(kInverse).mod(q);
        //check if s is the correct value for calculating the signature
        BigInteger sInverse = MathMethods.modularInverse(s, q);
//        assert sInverse.equals(k);

        System.out.println("s mod q: " + s.mod(q));
        System.out.println("r in sign: " + r);
        System.out.println("s in sign: " + s);

        assert uv.getX().mod(q).equals(r);
        assert r.mod(q).equals(r);
        assert s.mod(q).equals(s);
        return new MenezesVanstoneSignature(r, s);

    }

    public static boolean verify(final PublicKey publicKey, final BigInteger message, final MenezesVanstoneSignature signature) {
        /**
         * Zur Verifikation pruft Bob die Signatur (rs) zu dem Klartext M wie folgt:
         *  Bob berechnet w = s 1 mod q.
         *  Bob berechnet u = h(M)w mod q.
         *  Bob berechnet v = rw mod q.
         *  Bob berechnet den Kurvenpunkt (uv) = uG + vP E(ZZp).
         *  Bob akzeptiert die Signatur, wenn r = u mod q gilt, andernfalls wird die Signatur abgelehnt.
         **/
        System.out.println("received hashed message: " + message);
        BigInteger q = publicKey.order();
        BigInteger h = message;
        BigInteger w = MathMethods.modularInverse(signature.s(), q);
        System.out.println("w mod q: " + w);
        BigInteger u1 = w.multiply(h).mod(q);
        BigInteger u2 = signature.r().multiply(w).mod(q);
        System.out.println("generator: " + publicKey.generator());
        System.out.println("groupElement: " + publicKey.groupElement());
        System.out.println("u1: " + u1);
        System.out.println("u2: " + u2);
        EllipticCurvePoint u1New = publicKey.generator().multiply(u1, publicKey.ellipticCurve());
        EllipticCurvePoint u2New = publicKey.groupElement().multiply(u2, publicKey.ellipticCurve());
        System.out.println("u1New: " + u1New);
        System.out.println("u2New: " + u2New);
        EllipticCurvePoint uv = u1New.add(u2New, publicKey.ellipticCurve());
        System.out.println(publicKey.ellipticCurve().isValidPoint(uv));

        System.out.println("u1New + u2New: " + uv);
        System.out.println("r in verify: " + signature.r());
        System.out.println("s in verify: " + signature.s() );
        System.out.println("uvX: " + uv.getX());
        System.out.println("uvY: " + uv.getY());
        System.out.println("q: " + q);
        BigInteger legendreSign = MathMethods.verifyEulerCriterion(uv.getY(), q);
        BigInteger uvXModQ = uv.getX().mod(q);
        BigInteger rModQ = signature.r().mod(q);
        System.out.println(rModQ);

        return uvXModQ.equals(rModQ);

    }


}

