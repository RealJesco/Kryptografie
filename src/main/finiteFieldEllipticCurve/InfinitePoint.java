package main.finiteFieldEllipticCurve;

import java.math.BigInteger;

public class InfinitePoint extends EllipticCurvePoint {
    public InfinitePoint() {
        super(null, null);
    }

    //Neutrales Element
    public EllipticCurvePoint add(EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        return point2;
    }
    public EllipticCurvePoint doublePoint(FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
    public EllipticCurvePoint multiply(int scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        return this;
    }
    @Override
    public BigInteger getX(){
        throw new RuntimeException("Infinite Point has no x coordinate");
    }

    @Override
    public BigInteger getY(){
        throw new RuntimeException("Infinite Point has no y coordinate");
    }
}
