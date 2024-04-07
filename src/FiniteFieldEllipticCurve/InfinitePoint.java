package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;

public class InfinitePoint extends EllipticCurvePoint {
    public InfinitePoint(BigInteger x, BigInteger y) {
        super(x, y);
    }

    public InfinitePoint add (EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
    public InfinitePoint doublePoint (FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
    public InfinitePoint multiply (FiniteFieldEcPoint finiteFieldEcPoint, int scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
}
