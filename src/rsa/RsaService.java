package rsa;

import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static mathMethods.MathMethods.generateRandomPrime;

/**
 * Ein RSA Service für die Arbeit auf BigInteger.
 */
public final class RsaService {

    private static BigInteger generateUniquePrime(BigInteger bitLength, int millerRabinSteps, BigInteger m, AtomicInteger counter){
        BigInteger possiblePrime;
        int intBitLength = bitLength.intValue();
        BigInteger lowerBound = Resource.TWO.pow(intBitLength - 1);
        BigInteger upperBound = Resource.TWO.pow(intBitLength);
        possiblePrime = generateRandomPrime(m, lowerBound, upperBound, millerRabinSteps, counter);
        return possiblePrime;
    }

    private static BigInteger calculateE (BigInteger phiN, int millerRabinSteps, BigInteger m, AtomicInteger counter){
        if(phiN.compareTo(Resource.THREE) < 0){
            throw new IllegalArgumentException("phiN is too small to calculate e");
        }
        BigInteger e = BigInteger.valueOf(65537);
        if (e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(Resource.ONE)) {
            e = Resource.THREE;
            if (!e.gcd(phiN).equals(Resource.ONE)) {
                BigInteger upperBoundForE = phiN.subtract(Resource.ONE); // e must be less than phiN
                do {
//                    upperBoundForE = upperBoundForE.subtract(ONE);
                    e = generateRandomPrime(m, Resource.TWO, upperBoundForE, millerRabinSteps, counter);
                } while (!e.gcd(phiN).equals(Resource.ONE));
            }
        }
        return e;
    }

    public static KeyPairRsa generateKeyPair(int bitLength, int millerRabinSteps, BigInteger m) {

        AtomicInteger counter = new AtomicInteger(1);
        int bitLengthP = bitLength / 2;
        int bitLengthQ = bitLength / 2;
        BigInteger p;
        BigInteger q;
        BigInteger n;
        if(bitLength != bitLengthQ + bitLengthP){
            bitLengthQ++;
        }
        do{
            p = generateUniquePrime(BigInteger.valueOf(bitLengthP), millerRabinSteps, m, counter);
            q = generateUniquePrime(BigInteger.valueOf(bitLengthQ), millerRabinSteps, m, counter);
            n = p.multiply(q);
        } while(n.bitLength()!=bitLength);

        BigInteger qSubtractONE = q.subtract(Resource.ONE);
        BigInteger phiN = (p.subtract(Resource.ONE)).multiply(qSubtractONE);

        BigInteger e = calculateE(phiN, millerRabinSteps, m, counter);

        BigInteger d = MathMethods.extendedEuclidean(e, phiN)[1].mod(phiN);

        var privateKey = new PrivateKeyRsa(d, n);
        var publicKey = new PublicKeyRsa(e, n);

        return new KeyPairRsa(publicKey, privateKey);
    }

    public static BigInteger encrypt(final PublicKeyRsa key, final BigInteger message){
        return MathMethods.alternativeQuickExponentiation(message, key.e(), key.n());
    }

    public static BigInteger decrypt(final PrivateKeyRsa key, final BigInteger ciphertext) {
        return MathMethods.alternativeQuickExponentiation(ciphertext, key.d(), key.n());
    }

    public static BigInteger sign(final PrivateKeyRsa key, final BigInteger message) {
        return MathMethods.alternativeQuickExponentiation(message, key.d(), key.n());
    }

    public static boolean verify(final PublicKeyRsa key, final BigInteger signature,  final BigInteger message) {
        return message.equals(MathMethods.alternativeQuickExponentiation(signature, key.e(), key.n()));
    }
}
