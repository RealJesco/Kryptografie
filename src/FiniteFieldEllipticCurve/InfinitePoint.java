package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;

public class InfinitePoint extends EllipticCurvePoint {
    public InfinitePoint(BigInteger x, BigInteger y) {
        super(x, y);
    }

    //Neutrales Element
    public EllipticCurvePoint add (EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        return point2;
    }
    public InfinitePoint doublePoint (FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
    public EllipticCurvePoint multiply (int scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
}
