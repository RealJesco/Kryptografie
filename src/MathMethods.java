import java.math.BigInteger;

public class MathMethods {

    public static BigInteger alternativeQuickExponentation(BigInteger base, BigInteger exp, BigInteger mod) {
        if (exp.equals(BigInteger.ONE)) return base.mod(mod);

        // When the exponent is even
        if (exp.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
            return alternativeQuickExponentation(base.multiply(base).mod(mod), exp.shiftRight(1), mod);
        }

        // When the exponent is odd
        return base.multiply(alternativeQuickExponentation(base, exp.subtract(BigInteger.ONE), mod)).mod(mod);
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
