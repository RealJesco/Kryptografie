package test.finiteFieldEllipticCurve;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
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

    /**
     * Test the calculation of the order of the elliptic curve.
     * @expected: the order of the elliptic curve is equal to the expected value if n for order calculation is 2
     * @expected: the order of the elliptic curve is equal to the expected value if n for order calculation is 1
     * @expected: the order of the elliptic curve is equal to the expected value if n for order calculation is 1 (different curve to 1)
     * @expected: the order of the elliptic curve is equal to the expected value if n for order calculation is 3
     * @expected: the order of the elliptic curve is equal to the expected value if n for order calculation is 2 (different curve)
     */
    @Test
    public void calculateOrder() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(17));
        BigInteger countOfElements = ellipticCurve.calculateOrder(TWO);
        assertEquals(16, countOfElements.intValue());

        FiniteFieldEllipticCurve ellipticCurve2 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(13));
        BigInteger countOfElements2 = ellipticCurve2.calculateOrder(ONE);
        assertEquals(8, countOfElements2.intValue());

        FiniteFieldEllipticCurve ellipticCurve3 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(17));
        BigInteger countOfElements3 = ellipticCurve3.calculateOrder(ONE);
        assertEquals(16, countOfElements3.intValue());

        FiniteFieldEllipticCurve ellipticCurve4 = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(13));
        BigInteger countOfElements4 = ellipticCurve4.calculateOrder(THREE);
        assertEquals(8, countOfElements4.intValue());

        FiniteFieldEllipticCurve ellipticCurve5 = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(509));
        BigInteger countOfElements5 = ellipticCurve5.calculateOrder(BigInteger.valueOf(2));
        assertEquals(500, countOfElements5.intValue());
    }
}