import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathMethods {
    public static BigInteger alternativeQuickExponentiation(BigInteger base, BigInteger exp, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(mod); // Modulo operation, to ensure the base is within mod range

        while (!exp.equals(BigInteger.ZERO)) {
            // If the exponent is odd, multiply the result by base
            if (exp.and(BigInteger.ONE).equals(BigInteger.ONE)) {
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
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
        } else {
            BigInteger[] ee = extendedEuclidean(b, a.mod(b)); // b ist der Teiler (im Skript der erste Faktor); a.mod(b) ist der Rest
            BigInteger gcd = ee[0]; // im Skript der erste Faktor
            BigInteger x = ee[2]; // Rest
            BigInteger y = ee[1].subtract(a.divide(b).multiply(ee[2])); // im Skript der zweite Faktor
            return new BigInteger[] {gcd, x, y};
        }
    }

    public static BigInteger encrypt(BigInteger plaintext, BigInteger e, BigInteger n) {
        return plaintext.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {
        return ciphertext.modPow(d, n);
    }

    public static BigInteger getRandomBigInteger(BigInteger upperLimit){
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), new Random());
        } while (randomNumber.compareTo(upperLimit) >= 0);
        return randomNumber;
    }

    //Check if a number is prime using the Miller-Rabin primality test and returns true if it is probably prime and the probability
    public static boolean millerRabinTest(BigInteger possiblePrime, int numberOfTests){
        System.out.println("Testing number: " + possiblePrime.toString());
        //probability = 1 - (probabilityModifier)^numberOfTests
        //check if the number is even
        if(possiblePrime.mod(BigInteger.TWO).equals(BigInteger.ZERO) || possiblePrime.equals(BigInteger.ONE)){
            System.out.println("Number is even or 1");
            return false;
        }
        //find s and d so that possiblePrime-1 = 2^s * d
        BigInteger d = possiblePrime.subtract(BigInteger.ONE);
        int s = 0;
        while(d.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            d = d.divide(BigInteger.TWO);
            s++;
        }
        //repeat the test for the given certainty
        while(numberOfTests > 0){
            //pick a random number a between 2 and possiblePrime-2
            BigInteger a = getRandomBigInteger(possiblePrime.subtract(BigInteger.TWO)).add(BigInteger.TWO);
            //check if a^d mod possiblePrime = 1
            if(alternativeQuickExponentiation(a,d,possiblePrime).equals(BigInteger.ONE)){
                numberOfTests--;
                continue;
            }
            //check if a^(2^r * d) mod possiblePrime = -1 for 0 <= r <= s-1
            boolean isPrime = false;
            for(int r = 0; r < s; r++){
                if(alternativeQuickExponentiation(a,d.multiply(BigInteger.TWO.pow(r)),possiblePrime).equals(possiblePrime.subtract(BigInteger.ONE))){
                    isPrime = true;
                    break;
                }
            }
            if(isPrime){
                numberOfTests--;
                continue;
            }
            return false;
        }
        return true;
    }

    public static BigInteger prepareMessageForEncryption(List<Integer> message, int blockSize, int numberSystem){
        // Divide message into blocks of size blockSize
        List<List<Integer>> blocks = new ArrayList<>();

        for(int i = 0; i < message.size(); i++){
            if (i % blockSize == 0) {
                blocks.add(new ArrayList<>()); // Initializing each block
            }
            blocks.get(i / blockSize).add(message.get(i));
        }

        BigInteger encryptedMessage = BigInteger.valueOf(0);

        for(List<Integer> block : blocks) {
            BigInteger blockValue = BigInteger.ZERO;

            // For each block go through every character and convert it to a number
            // in the number system with respect to its index
            int exponent = blockSize - 1; // Start with the highest exponent
            for (Integer integer : block) {
                blockValue = blockValue.add(BigInteger.valueOf(integer).multiply(BigInteger.valueOf(numberSystem).pow(exponent)));
                exponent--; // Decrease the exponent for the next iteration
            }
            // Add the block value to the encryptedBlocks list
            encryptedMessage = encryptedMessage.add(blockValue);
        }

        return encryptedMessage;
    }

    public static List<Integer> prepareMessageForDecryption(BigInteger message, int blockSize, int numberSystem){
        List<Integer> decryptedMessage = new ArrayList<>();

        // Divide message into blocks of size blockSize
        List<BigInteger> blocks = new ArrayList<>();

        BigInteger numberSystemToThePowerOfBlockSize = BigInteger.valueOf(numberSystem).pow(blockSize);

        while (!message.equals(BigInteger.ZERO)) {
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

    public static List<Integer> convertTextToUniCode(String text){
        List<Integer> unicode = new ArrayList<>();
        for(int i = 0; i < text.length(); i++){
            unicode.add((int)text.charAt(i));
        }
        return unicode;
    }

    public static String convertUniCodeToText(List<Integer> unicode){
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < unicode.size(); i++){
            text.append((char)unicode.get(i).intValue());
        }
        return text.toString();
    }

    public static String rsaEncrypt(String message, BigInteger e, BigInteger n) {
        System.out.println("n: " + n);
        System.out.println("Message: " + message);
        // Step 1: Convert text to Unicode
        List<Integer> unicodeMessage = convertTextToUniCode(message);
        System.out.println("Unicode: " + unicodeMessage);

        // Step 2: Prepare message for encryption
        BigInteger numericMessage = prepareMessageForEncryption(unicodeMessage, 8, 55296);
        System.out.println("Numeric message: " + numericMessage);
        // Step 3: Encrypt the numeric representation
        BigInteger encryptedNumericMessage = encrypt(numericMessage, e, n);
        System.out.println("Encrypted numeric message: " + encryptedNumericMessage);
        // Step 4: Convert encrypted numeric message to UniCodeString
        String encryptedNumericMessageStr = convertUniCodeToText(prepareMessageForDecryption(encryptedNumericMessage, 9, 55296));
        System.out.println("Encrypted numeric message string: " + encryptedNumericMessageStr);
        return encryptedNumericMessageStr;
    }

//    public static List<Integer> getIndividualUnicodeValues(BigInteger numericEncryptedMessage) {
//        LinkedList<Integer> result = new LinkedList<>();
//        BigInteger unicodeLimit = BigInteger.valueOf(55296);
//
//        while (!numericEncryptedMessage.equals(BigInteger.ZERO)) {
//            result.addFirst(numericEncryptedMessage.mod(unicodeLimit).intValue());
//            numericEncryptedMessage = numericEncryptedMessage.divide(unicodeLimit);
//        }
//
//        return result;
//    }

    public static String rsaDecrypt(String encryptedNumericMessageStr, BigInteger d, BigInteger n) {
       // THIS METHOD DECRYPTS
        // Step 1: Convert encrypted numeric message to UniCodeString
        List<Integer> encryptedNumericMessage = convertTextToUniCode(encryptedNumericMessageStr);
        System.out.println("Encrypted numeric message string: " + encryptedNumericMessage);
        // Step 2: Prepare message for decryption
//        List<Integer> numericMessage = getIndividualUnicodeValues(encryptedNumericMessage);
//        System.out.println("Numeric message: " + numericMessage);

        return "";
    }
}
