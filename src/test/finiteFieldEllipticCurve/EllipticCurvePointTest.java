package test.finiteFieldEllipticCurve;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.finiteFieldEllipticCurve.FiniteFieldEcPoint;
import main.finiteFieldEllipticCurve.FiniteFieldEllipticCurve;
import main.finiteFieldEllipticCurve.InfinitePoint;
import main.resource.Resource;
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
    public void normalize() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(ONE, BigInteger.valueOf(13));
        EllipticCurvePoint point = new FiniteFieldEcPoint(Resource.FIVE, Resource.FOUR);
        assertTrue(ellipticCurve.isValidPoint(point));
    }

    /**
     * Test new point as result of addition of two points on the elliptic curve.
     * @expected: x of point equals big integer 6
     * @expected: y of point equals big integer 12
     * @expected: true if the point is on the elliptic curve, false otherwise
     */
    @Test
    public void add() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(Resource.FIVE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point1 = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(2));
        FiniteFieldEcPoint point2 = new FiniteFieldEcPoint(Resource.SEVEN, Resource.FIVE);
        EllipticCurvePoint newPoint = point1.add(point2, ellipticCurve);
        assertEquals(BigInteger.valueOf(6), newPoint.getX());
        assertEquals(BigInteger.valueOf(12), newPoint.getY());
        assertTrue(ellipticCurve.isValidPoint(newPoint));
    }

    @Test
    public void addInfinitePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(Resource.FIVE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point1 = new FiniteFieldEcPoint(BigInteger.valueOf(3), BigInteger.valueOf(2));
        EllipticCurvePoint point2 = new InfinitePoint();
        EllipticCurvePoint newPoint = point1.add(point2, ellipticCurve);
        assertEquals(point1, newPoint);
    }

    /**
     * Test double point for point (3,7) on the elliptic curve.
     * @expected: true if the point is on the elliptic curve, false otherwise
     * @expected: x of point equals big integer 15
     * @expected: y of point equals big integer 0
     */
    @Test
    public void doublePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(TWO, BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(BigInteger.valueOf(3), Resource.SEVEN);
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
    public void doublePointForZero() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(ONE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(ZERO, ZERO);
        ArithmeticException thrown = assertThrows(ArithmeticException.class, () -> point.doublePoint(ellipticCurve));
        assertTrue(thrown.getMessage().contains("No modular inverse exists for these parameters"));
    }

    @Test
    public void doublePointForYIsZero() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(ONE, BigInteger.valueOf(13));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(THREE, ZERO);
        EllipticCurvePoint doubledPoint = point.doublePoint(ellipticCurve);
        assertTrue(doubledPoint instanceof InfinitePoint);
    }

    /**
     * Test multiplication of a point on the elliptic curve.
     * @expected: new point x = 16, new point y = 12
     */
    @Test
    public void multiply() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(Resource.SEVEN, Resource.FIVE);
        EllipticCurvePoint newPoint = point.multiply(Resource.FOUR, ellipticCurve);
        assertEquals(BigInteger.valueOf(16), newPoint.getX());
        assertEquals(BigInteger.valueOf(12), newPoint.getY());
    }

    /**
     * Test multiplication of a point with zero.
     * @expected: the point is an instance of infinite point
     */
    @Test
    public void multiplyWithZero () {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(Resource.SEVEN, Resource.FIVE);
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(0), ellipticCurve);
        assertTrue(newPoint instanceof InfinitePoint);
    }

    @Test
    public void multiplyInfinitePoint () {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        InfinitePoint point = new InfinitePoint();
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
    public void reachInfinitePoint() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        FiniteFieldEcPoint point = new FiniteFieldEcPoint(Resource.SEVEN, Resource.FIVE);
        EllipticCurvePoint newPoint = point.multiply(BigInteger.valueOf(9), ellipticCurve);
        assertTrue(newPoint instanceof FiniteFieldEcPoint);
        assertFalse(newPoint instanceof InfinitePoint);
        newPoint = point.multiply(BigInteger.valueOf(10), ellipticCurve);
        assertTrue(newPoint instanceof InfinitePoint);
        assertFalse(newPoint instanceof FiniteFieldEcPoint);
    }

    @Test
    public void testInfinitePointResturnInstanceOfInfinitePoint() {
        InfinitePoint point = new InfinitePoint();
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        assertTrue(point.doublePoint(ellipticCurve) instanceof InfinitePoint);
    }

    @Test
    public void testInfinitePointErrorsXYGetters() {
        EllipticCurvePoint point = new InfinitePoint();
        assertThrows(RuntimeException.class, () -> point.getX());
        assertThrows(RuntimeException.class, () -> point.getY());
    }
}