package FiniteFieldEllipticCurve;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;

import static org.junit.jupiter.api.Assertions.*;

class EllipticCurvePointTest {

    @Test
    void normalize() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.valueOf(13));
        EllipticCurvePoint point = new FiniteFieldEcPoint(BigInteger.valueOf(5), BigInteger.valueOf(4));
        assertTrue(ellipticCurve.isValidPoint(point));
    }

    @Test
    void add() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point1 = new FiniteFieldEcPoint(BigInteger.TWO, BigInteger.valueOf(6));
        FiniteFieldEcPoint point2 = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(8));
        EllipticCurvePoint newPoint = point1.add(point2, ellipticCurve);
        assertTrue(ellipticCurve.isValidPoint(newPoint));
    }

    @Test
    void doublePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.TWO, BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(7));
        EllipticCurvePoint doubledPoint = point.doublePoint(ellipticCurve);
        assertTrue(ellipticCurve.isValidPoint(doubledPoint));
        assertEquals(BigInteger.valueOf(15), doubledPoint.x);
        assertEquals(BigInteger.valueOf(0), doubledPoint.y);
    }
    @Test
    void doublePointForZero() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.ONE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.ZERO, BigInteger.ZERO);
        ArithmeticException thrown = assertThrows(ArithmeticException.class, () -> point.doublePoint(ellipticCurve));
        assertTrue(thrown.getMessage().contains("No modular inverse exists for these parameters"));
    }

    @Test
    void multiply() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(5));
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(4), ellipticCurve);
        assertEquals(BigInteger.valueOf(16), newPoint.x);
        assertEquals(BigInteger.valueOf(12), newPoint.y);
    }
    @Test
    void reachInfinitePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(5));
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(9), ellipticCurve);
        assertTrue(newPoint instanceof FiniteFieldEcPoint);
        assertFalse(newPoint instanceof InfinitePoint);
        newPoint = point.multiply(BigInteger.valueOf(10), ellipticCurve);
        assertTrue(newPoint instanceof InfinitePoint);
        assertFalse(newPoint instanceof FiniteFieldEcPoint);
    }
}