package mathMethods;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class MathMethods {
    static BigInteger ZERO = BigInteger.ZERO;
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger TWO = BigInteger.TWO;
    static BigInteger TEN = BigInteger.TEN;
    static BigInteger MINUSONE = ONE.negate();

    public static BigInteger alternativeQuickExponentiation(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger result = ONE;
        base = base.mod(mod); // Modulo operation, to ensure the base is within mod range

        while (!exp.equals(ZERO)) {
            // If the exponent is odd, multiply the result by base
            if (exp.and(ONE).equals(ONE)) {
                result = (result.multiply(base)).mod(mod);
            }

            // Square the base and halve the exponent for the next iteration
            base = (base.multiply(base)).mod(mod);
            exp = exp.shiftRight(1);
        }
        return result; // Return the accumulated result
    }

//    TODO: @Adham: Habe ich das so richtig kommentert (und verstanden)?
    public static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b) {
        if (b.equals(ZERO)) {
            return new BigInteger[] {a, ONE, ZERO};
        } else {
            BigInteger[] ee = extendedEuclidean(b, a.mod(b)); // b ist der Teiler (im Skript der erste Faktor); a.mod(b) ist der Rest
            BigInteger gcd = ee[0]; // im Skript der erste Faktor
            BigInteger x = ee[2]; // Rest
            BigInteger y = ee[1].subtract(a.divide(b).multiply(ee[2])); // im Skript der zweite Faktor
            return new BigInteger[] {gcd, x, y};
        }
    }
    public static BigInteger getRandomBigIntegerUpperLimit(BigInteger upperLimit, Random random){
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), random);
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }
    public static BigInteger getRandomBigInteger(int length, int m, Random random){
        if(length==0)return ZERO;
        int maxShift = length*100;
        MathContext context = new MathContext(maxShift+3*length+10);
        int a = random.nextInt(maxShift);
        return (BigDecimal.valueOf(m)).sqrt(context).multiply(BigDecimal.TEN.pow(Math.abs(a)), context).divideAndRemainder(BigDecimal.ONE, context)[1].multiply(BigDecimal.TEN.pow(length)).toBigInteger();
    }

    public static BigInteger getRandomPrimeBigInteger(int length, int m, int primechecks, Random random){
        if(length==0)return ZERO;
        int maxShift = length*100;
        MathContext context = new MathContext(maxShift+3*length);
        BigDecimal lengthDecimal = BigDecimal.TEN.pow(length);
        BigDecimal mRoot = BigDecimal.valueOf(m).sqrt(context).multiply(lengthDecimal);
        BigInteger prime;
        do{
            prime = mRoot.multiply(BigDecimal.TEN.pow(Math.abs(random.nextInt(maxShift))), context).divideAndRemainder(lengthDecimal, context)[1].toBigInteger();
        } while(!parallelMillerRabinTest(prime, primechecks, random));
        return prime;
    }
    //Check if a number is prime using the Miller-Rabin primality test and returns true if it is probably prime and the probability
    public static boolean millerRabinTest(BigInteger possiblePrime, int numberOfTests, Random random){
        possiblePrime = possiblePrime.abs();
        if (numberOfTests == 0 || possiblePrime.equals(TWO)) {
            return true;
        }
        if(possiblePrime.equals(ZERO) ||!possiblePrime.testBit(0) || possiblePrime.equals(ONE)){
            return false;
        }

        BigInteger possiblePrimeMinusOne = possiblePrime.add(MINUSONE);
        int s = possiblePrimeMinusOne.getLowestSetBit();
        BigInteger d = possiblePrime.shiftRight(s);
        BigInteger tempRange = possiblePrime.subtract(BigInteger.valueOf(4));
        while(numberOfTests-- > 0){
            BigInteger a = getRandomBigIntegerUpperLimit(tempRange, random).add(TWO); // 'a' is in the range [2, possiblePrime - 2]

            boolean isPrime = alternativeQuickExponentiation(a,d,possiblePrime).equals(ONE);
            int r = 0;
            while (!isPrime && r++<s){
                d = alternativeQuickExponentiation(d,TWO,possiblePrime);
                isPrime =d.equals(possiblePrimeMinusOne);
            }
            if(!isPrime){
                return false;
            }
        }
        return true;
    }
    // Parallel Miller-Rabin Test
    public static boolean parallelMillerRabinTest(BigInteger possiblePrime, int numberOfTests, Random random) {
        if (!possiblePrime.testBit(0)) return false;
        if (possiblePrime.equals(ONE)) return false;
        if (possiblePrime.equals(TWO)) return true;

        BigInteger possiblePrimeMinusOne = possiblePrime.add(MINUSONE);
        int s = possiblePrimeMinusOne.getLowestSetBit();
        BigInteger d = possiblePrime.add(MINUSONE);

        // ForkJoinPool can potentially be more efficient for certain tasks
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        BigInteger finalD = d;
        int finalS = s;
        BigInteger tempRange = possiblePrime.subtract(BigInteger.valueOf(4));
        List<Callable<Boolean>> tasks = IntStream.range(0, numberOfTests)
                .mapToObj(i -> (Callable<Boolean>) () -> {
                    BigInteger a = getRandomBigIntegerUpperLimit(tempRange,random).add(TWO);
                    BigInteger x = alternativeQuickExponentiation(a, finalD, possiblePrime);

                    if (x.equals(ONE) || x.equals(possiblePrime.add(MINUSONE))) {
                        return true;
                    }

                    for (int r = 0; r < finalS; r++) {
                        //x = x.modPow(BigInteger.TWO, possiblePrime);
                        x = alternativeQuickExponentiation(x, TWO, possiblePrime);
                        if (x.equals(BigInteger.ONE)) return false;
                        if (x.equals(possiblePrime.subtract(ONE))) return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        try {
            // Use the invokeAny method which returns the result of the fastest callable
            // and cancels all other tasks if one returns false
            return forkJoinPool.invokeAny(tasks);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            forkJoinPool.shutdown(); // Always remember to shutdown the pool
        }
    }

    public static List<BigInteger> prepareMessageForEncryption(List<Integer> message, int blockSize, int numberSystem){
        // Divide message into blocks of size blockSize
        List<List<Integer>> blocks = new ArrayList<>();

        for (int i = 0; i < message.size(); i++) {
            if (i % blockSize == 0) {
                blocks.add(new ArrayList<>()); // Initializing each block
            }
            blocks.get(i / blockSize).add(message.get(i));
        }

        List<BigInteger> encryptedBlocks = new ArrayList<>();

        for (List<Integer> block : blocks) {
            BigInteger blockValue = ZERO;

            // For each block go through every character and convert it to a number
            // in the number system with respect to its index
            int exponent = blockSize - 1; // Start with the highest exponent
            for (Integer integer : block) {
                blockValue = blockValue.add(BigInteger.valueOf(integer).multiply(BigInteger.valueOf(numberSystem).pow(exponent)));
                exponent--; // Decrease the exponent for the next iteration
            }
            // Add the block value to the encryptedBlocks list
            encryptedBlocks.add(blockValue);
        }

        return encryptedBlocks;
    }

    public static List<Integer> prepareMessageForDecryption(BigInteger message, int blockSize, int numberSystem){
        List<Integer> decryptedMessage = new ArrayList<>();

        // Divide message into blocks of size blockSize
        List<BigInteger> blocks = new ArrayList<>();

        BigInteger numberSystemToThePowerOfBlockSize = BigInteger.valueOf(numberSystem).pow(blockSize);

        while (!message.equals(ZERO)) {
            blocks.add(message.mod(numberSystemToThePowerOfBlockSize));
            message = message.divide(numberSystemToThePowerOfBlockSize);
        }

        for(BigInteger block : blocks) {
            // For each block go through every character and convert it to a number
            // in the number system with respect to its index
            for (int i = blockSize - 1; i >= 0; i--) {
                BigInteger numberSystemToThePowerOfI = BigInteger.valueOf(numberSystem).pow(i);
                BigInteger blockValue = block.divide(numberSystemToThePowerOfI);
                decryptedMessage.add(blockValue.intValue());
                block = block.subtract(blockValue.multiply(numberSystemToThePowerOfI));
            }
        }

        return decryptedMessage;
    }

    //TODO: Test diese kleinen Methoden
    public static List<Integer> convertTextToUniCode(String text){
        List<Integer> unicode = new ArrayList<>();
        for(int i = 0; i < text.length(); i++){
            unicode.add((int)text.charAt(i));
        }
        return unicode;
    }

    public static String convertUniCodeToText(List<Integer> unicode){
        StringBuilder text = new StringBuilder();
        for (Integer integer : unicode) {
            text.append((char) integer.intValue());
        }
        return text.toString();
    }


}
