import java.math.BigInteger;

public class MathMethods {

    public static BigInteger alternativeQuickExponentation(BigInteger num, BigInteger exp, BigInteger mod){
        BigInteger returnValue = null;
        if(exp.equals(BigInteger.ONE)){
            returnValue = num;
        } else if(exp.equals(BigInteger.TWO)){
            returnValue = (num.multiply(num));
        } else if(exp.mod(new BigInteger("2")).equals(new BigInteger("0"))){
            BigInteger i = alternativeQuickExponentation(num, exp.divide(BigInteger.TWO),mod);
            returnValue = i.multiply(i);
        } else {
            BigInteger i = alternativeQuickExponentation(num, (exp.subtract(BigInteger.ONE)).divide(BigInteger.TWO),mod);
            returnValue = num.multiply(i.multiply(i));
        }
        return returnValue.mod(mod);
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
