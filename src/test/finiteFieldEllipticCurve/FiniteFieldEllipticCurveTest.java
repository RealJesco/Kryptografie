package test.finiteFieldEllipticCurve;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.finiteFieldEllipticCurve.FiniteFieldEcPoint;
import main.finiteFieldEllipticCurve.FiniteFieldEllipticCurve;
import main.resource.Resource;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static main.resource.Resource.*;

/**
 * Test class for the main.FiniteFieldEllipticCurve class.
 * This class specifically tests the calculateAllPoints method,
 * ensuring its correctness given a variety of inputs.
 */
public class FiniteFieldEllipticCurveTest {

    //TODO: Test isValidPoint
    @Test
    public void isValidPointTestTrue() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(29),  BigInteger.valueOf(27));
        EllipticCurvePoint point = new FiniteFieldEcPoint(BigInteger.valueOf(20), BigInteger.valueOf(12));
        assertTrue(ellipticCurve.isValidPoint(point));
    }

    @Test
    public void isValidPointTestFalse() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(29),  BigInteger.valueOf(11));
        EllipticCurvePoint point = new FiniteFieldEcPoint(BigInteger.valueOf(23), BigInteger.valueOf(59));
        assertFalse(ellipticCurve.isValidPoint(point));
    }

    /**
     * Test the calculation of all points on the elliptic curve.
     * @expected: the list of points has the correct size
     */
    @Test
    public void calculateAllPointsTest1() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3),  BigInteger.valueOf(11));

        List<EllipticCurvePoint> allPoints = ellipticCurve.calculateAllPoints();
        assertEquals(12, allPoints.size());
    }

    /**
     * Test the calculation of all points on the elliptic curve.
     * @expected: false if the list of points is empty, true otherwise
     */
    @Test
    public void calculateAllPointsTest2() {
        BigInteger coefficientOfX = Resource.THREE;
        BigInteger moduleR = Resource.SEVEN;
        FiniteFieldEllipticCurve curve = new FiniteFieldEllipticCurve(coefficientOfX,  moduleR);

        List<EllipticCurvePoint> allPoints = curve.calculateAllPoints();
        assertFalse(allPoints.isEmpty());
    }

    @Test
    public void calculateOrder1() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(17));
        BigInteger countOfElements = ellipticCurve.calculateOrder(TWO);
        assertEquals(16, countOfElements.intValue());
    }

    @Test
    public void calculateOrder2() {
        FiniteFieldEllipticCurve ellipticCurve2 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(13));
        BigInteger countOfElements2 = ellipticCurve2.calculateOrder(ONE);
        assertEquals(8, countOfElements2.intValue());
    }

    @Test
    public void calculateOrder3() {
        FiniteFieldEllipticCurve ellipticCurve3 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(17));
        BigInteger countOfElements3 = ellipticCurve3.calculateOrder(ONE);
        assertEquals(16, countOfElements3.intValue());
    }

    @Test
    public void calculateOrder4() {
        FiniteFieldEllipticCurve ellipticCurve4 = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(13));
        BigInteger countOfElements4 = ellipticCurve4.calculateOrder(THREE);
        assertEquals(8, countOfElements4.intValue());
    }

    @Test
    public void calculateOrder5() {
        FiniteFieldEllipticCurve ellipticCurve5 = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(509));
        BigInteger countOfElements5 = ellipticCurve5.calculateOrder(BigInteger.valueOf(2));
        assertEquals(500, countOfElements5.intValue());
    }

    @Test
    public void calculateOrder6() {
        FiniteFieldEllipticCurve ellipticCurve5 = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(73));
        BigInteger countOfElements5 = ellipticCurve5.calculateOrder(BigInteger.valueOf(2));
        assertEquals(80, countOfElements5.intValue());
    }
}