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
        FiniteFieldEllipticCurve ellipticCurve = new FiniteFieldEllipticCurve(BigInteger.valueOf(3), BigInteger.valueOf(9), BigInteger.valueOf(11));

        List<EllipticCurvePoint> allPoints = ellipticCurve.calculateAllPoints();
        assertEquals(10, allPoints.size());
    }

    @Test
    void calculateAllPointsTest2() {
        BigInteger coefficientOfX = BigInteger.valueOf(3);
        BigInteger b = BigInteger.valueOf(4);
        BigInteger moduleR = BigInteger.valueOf(7);
        FiniteFieldEllipticCurve curve = new FiniteFieldEllipticCurve(coefficientOfX, b, moduleR);

        List<EllipticCurvePoint> allPoints = curve.calculateAllPoints();
        assertFalse(allPoints.isEmpty());
    }

    //TODO Prüfen ob singuläre elliptische Kurven notwendig sind
    @Test
    void calculateAllPointsTest3() {
        BigInteger coefficientOfX = BigInteger.valueOf(5);
        BigInteger b = BigInteger.valueOf(6);
        BigInteger moduleR = BigInteger.valueOf(3);
        FiniteFieldEllipticCurve curve = new FiniteFieldEllipticCurve(coefficientOfX, b, moduleR);

        List<EllipticCurvePoint> allPoints = curve.calculateAllPoints();
        assertTrue(allPoints.isEmpty());
    }
}