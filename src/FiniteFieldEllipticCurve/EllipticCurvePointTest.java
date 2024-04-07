package FiniteFieldEllipticCurve;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;

import static org.junit.jupiter.api.Assertions.*;

class EllipticCurvePointTest {

    @Test
    void normalize() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));

    }

    @Test
    void add() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));
        FiniteFieldEcPoint point1 = new FiniteFieldEcPoint(BigInteger.TWO, BigInteger.valueOf(6));
        FiniteFieldEcPoint point2 = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(8));
        EllipticCurvePoint newPoint = point1.add(point2, ellipticCurve);
        System.out.println(newPoint.x + " " + newPoint.y);
        assertTrue(ellipticCurve.isValidPoint(newPoint));
    }

    @Test
    void doublePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.TWO, BigInteger.valueOf(6));
        EllipticCurvePoint doubledPoint = point.doublePoint(ellipticCurve);
        System.out.println(doubledPoint.x + " " + doubledPoint.y);
        assertTrue(ellipticCurve.isValidPoint(doubledPoint));
    }
    @Test
    void doublePointForZero() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.ZERO, BigInteger.ZERO);
        ArithmeticException thrown = assertThrows(ArithmeticException.class, () -> point.doublePoint(ellipticCurve));
        assertTrue(thrown.getMessage().contains("No modular inverse exists for these parameters"));
    }

    @Test
    void multiply() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.TWO, BigInteger.valueOf(6));
        EllipticCurvePoint newPoint = point.multiply(4, ellipticCurve);
        System.out.println(newPoint.x + " "  + newPoint.y);
        assertEquals(BigInteger.valueOf(4), newPoint.x);
        assertEquals(BigInteger.valueOf(4), newPoint.y);
    }
    @Test
    void reachInfinitePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.TWO, BigInteger.valueOf(6));
        EllipticCurvePoint newPoint = point.multiply(9, ellipticCurve);
        System.out.println(newPoint.x + " "  + newPoint.y);
        assertTrue(newPoint instanceof FiniteFieldEcPoint);
        assertFalse(newPoint instanceof InfinitePoint);
        newPoint = point.multiply(10, ellipticCurve);
        System.out.println(newPoint.x + " "  + newPoint.y);
        assertTrue(newPoint instanceof InfinitePoint);
        assertFalse(newPoint instanceof FiniteFieldEcPoint);
    }
}