package main.resource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public final class Resource {
    public static final BigInteger ZERO = BigInteger.ZERO;
    public static final BigInteger ONE = BigInteger.ONE;
    public static final BigInteger TWO = BigInteger.TWO;
    public static final BigInteger THREE = BigInteger.valueOf(3);
    public static final BigInteger FOUR = BigInteger.valueOf(4);
    public static final BigInteger FIVE = BigInteger.valueOf(5);
    public static final BigInteger SEVEN = BigInteger.valueOf(7);
    public static final BigInteger EIGHT = BigInteger.valueOf(8);
    public static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);

    public static final BigDecimal DECIMAL_ZERO = BigDecimal.ZERO;
    public static final BigDecimal DECIMAL_ONE = BigDecimal.ONE;
    public static final BigDecimal DECIMAL_TWO = BigDecimal.valueOf(2);

    public static AtomicInteger counter = new AtomicInteger(1);

}
