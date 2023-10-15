import java.math.BigInteger;

public class MathMethods {

    public static BigInteger alternativeQuickExponentation(BigInteger base, BigInteger exp, BigInteger mod) {
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
    public static BigInteger expandedEuklid(BigInteger n1, BigInteger n2){
        /*if(n1.compareTo(n2) == -1){
            BigInteger temp = n1;
            n1 = n2;
            n2 = temp;
        }*/
        BigInteger result = n1.subtract(n2);
        while(!result.equals(BigInteger.ZERO)){
            result = n1.mod(n2);
            n1 = n2;
            n2 = result;
        }
        return n1;
    }
}
