package test.finiteFieldEllipticCurve;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.finiteFieldEllipticCurve.FiniteFieldEcPoint;
import main.finiteFieldEllipticCurve.FiniteFieldEllipticCurve;
import main.finiteFieldEllipticCurve.InfinitePoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static main.resource.Resource.*;

class EllipticCurvePointTest {

    /**
     * Test if the point is on the elliptic curve.
     * @expected: true if the point is on the elliptic curve, false otherwise
     */
    @Test
    void normalize() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(ONE, BigInteger.valueOf(13));
        EllipticCurvePoint point = new FiniteFieldEcPoint(BigInteger.valueOf(5), BigInteger.valueOf(4));
        assertTrue(ellipticCurve.isValidPoint(point));
    }

    /**
     * Test new point as result of addition of two points on the elliptic curve.
     * @expected: x of point equals big integer 6
     * @expected: y of point equals big integer 12
     * @expected: true if the point is on the elliptic curve, false otherwise
     */
    @Test
    void add() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(5), BigInteger.valueOf(13));
        FiniteFieldEcPoint point1 = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(2));
        FiniteFieldEcPoint point2 = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(5));
        EllipticCurvePoint newPoint = point1.add(point2, ellipticCurve);
        assertEquals(BigInteger.valueOf(6), newPoint.getX());
        assertEquals(BigInteger.valueOf(12), newPoint.getY());
        assertTrue(ellipticCurve.isValidPoint(newPoint));
    }

    /**
     * Test double point for point (3,7) on the elliptic curve.
     * @expected: true if the point is on the elliptic curve, false otherwise
     * @expected: x of point equals big integer 15
     * @expected: y of point equals big integer 0
     */
    @Test
    void doublePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(TWO, BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(7));
        EllipticCurvePoint doubledPoint = point.doublePoint(ellipticCurve);
        assertTrue(ellipticCurve.isValidPoint(doubledPoint));
        assertEquals(BigInteger.valueOf(15), doubledPoint.getX());
        assertEquals(BigInteger.valueOf(0), doubledPoint.getY());
    }

    /**
     * Test double point for point (0,0).
     * @expected: no modular inverse exists for these parameters
     */
    @Test
    void doublePointForZero() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(ONE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(ZERO, ZERO);
        ArithmeticException thrown = assertThrows(ArithmeticException.class, () -> point.doublePoint(ellipticCurve));
        assertTrue(thrown.getMessage().contains("No modular inverse exists for these parameters"));
    }

    /**
     * Test multiplication of a point on the elliptic curve.
     * @expected: new point x = 16, new point y = 12
     */
    @Test
    void multiply() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(5));
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(4), ellipticCurve);
        assertEquals(BigInteger.valueOf(16), newPoint.getX());
        assertEquals(BigInteger.valueOf(12), newPoint.getY());
    }

    /**
     * Test multiplication of a point with zero.
     * @expected: the point is an instance of infinite point
     */
    @Test
    void multiplyWithZero () {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(7), BigInteger.valueOf(5));
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(0), ellipticCurve);
        assertTrue(newPoint instanceof InfinitePoint);
    }

    /**
     * Test the normalization of a point on the elliptic curve.
     * @expected: the point is an finite field elliptic curve point
     * @expected: the point is not an instance of infinite point
     * @expected: the new point is instance of infinite point
     * @expected: the point is not instance of finite field elliptic curve point
     */
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