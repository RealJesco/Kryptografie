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
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(13));
        BigInteger countOfElements = ellipticCurve.calculateCountOfElements();
        System.out.println(countOfElements.intValue());
        assertEquals(20, countOfElements.intValue());

        FiniteFieldEllipticCurve ellipticCurve2 = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(13));
        BigInteger countOfElements2 = ellipticCurve2.calculateCountOfElements();
        System.out.println(countOfElements2.intValue() == 8);

        FiniteFieldEllipticCurve ellipticCurve3 = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(17));
        BigInteger countOfElements3 = ellipticCurve3.calculateCountOfElements();
        System.out.println(countOfElements3.intValue() == 16);
    }

}