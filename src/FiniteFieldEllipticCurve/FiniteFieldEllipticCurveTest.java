package FiniteFieldEllipticCurve;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the FiniteFieldEllipticCurve class.
 * This class specifically tests the calculateAllPoints method,
 * ensuring its correctness given a variety of inputs.
 */
public class FiniteFieldEllipticCurveTest {

    @Test
    void calculateAllPointsTest1() {
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3),  BigInteger.valueOf(11));

        List<EllipticCurvePoint> allPoints = ellipticCurve.calculateAllPoints();
        assertEquals(12, allPoints.size());
    }

    @Test
    void calculateAllPointsTest2() {
        BigInteger coefficientOfX = BigInteger.valueOf(3);
        BigInteger moduleR = BigInteger.valueOf(7);
        FiniteFieldEllipticCurve curve = new FiniteFieldEllipticCurve(coefficientOfX,  moduleR);

        List<EllipticCurvePoint> allPoints = curve.calculateAllPoints();
        assertFalse(allPoints.isEmpty());
    }

    @Test
    void calculateCountOfElements() {

        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(17));
        BigInteger countOfElements = ellipticCurve.calculateCountOfElements(BigInteger.TWO);
        assertEquals(16, countOfElements.intValue());

        FiniteFieldEllipticCurve ellipticCurve2 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(13));
        BigInteger countOfElements2 = ellipticCurve2.calculateCountOfElements(BigInteger.ONE);
        assertEquals(8, countOfElements2.intValue());

        FiniteFieldEllipticCurve ellipticCurve3 = new FiniteFieldEllipticCurve(BigInteger.valueOf(1), BigInteger.valueOf(17));
        BigInteger countOfElements3 = ellipticCurve3.calculateCountOfElements(BigInteger.ONE);
        assertEquals(16, countOfElements3.intValue());

        FiniteFieldEllipticCurve ellipticCurve4 = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(13));
        BigInteger countOfElements4 = ellipticCurve4.calculateCountOfElements(BigInteger.valueOf(3));
        assertEquals(8, countOfElements4.intValue());

        FiniteFieldEllipticCurve ellipticCurve5 = new FiniteFieldEllipticCurve(BigInteger.valueOf(2), BigInteger.valueOf(509));
        BigInteger countOfElements5 = ellipticCurve5.calculateCountOfElements(BigInteger.valueOf(2));
        assertEquals(500, countOfElements5.intValue());
    }

}