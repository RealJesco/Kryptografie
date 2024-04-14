package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;
import java.util.List;

public abstract class EllipticCurvePoint {
    private BigInteger x;
    private BigInteger y;

    public EllipticCurvePoint (BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public EllipticCurvePoint normalize (FiniteFieldEllipticCurve ellipticCurve){
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(x.mod(ellipticCurve.moduleR).abs(), y.mod(ellipticCurve.moduleR).abs());
        if(ellipticCurve.isValidPoint(normalizedEcPoint)) {
            return normalizedEcPoint;
        } else {
            return new InfinitePoint();
        }
    }
    public EllipticCurvePoint add (EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        if (point2 instanceof InfinitePoint) {
            return this;
        }
        BigInteger lambdaNumerator = point2.getY().subtract(this.getY());
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean((point2.getX().subtract(this.getX())), ellipticCurve.moduleR)[1];
        BigInteger lambda = lambdaNumerator.multiply(lambdaModInverseDenominator);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, BigInteger.TWO, ellipticCurve.moduleR).subtract(this.getX()).subtract(point2.getX());
        BigInteger newY = lambda.multiply(this.getX().subtract(newX)).subtract(this.getY());

        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }
    public EllipticCurvePoint doublePoint (FiniteFieldEllipticCurve ellipticCurve) {
        if(this.getY().equals(BigInteger.ZERO) && this.getX().equals(BigInteger.ZERO)){
            throw new ArithmeticException("No modular inverse exists for these parameters");
        }
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(this.getX(), BigInteger.TWO, ellipticCurve.moduleR).multiply(BigInteger.valueOf(3)).add(ellipticCurve.a);
        BigInteger lambdaModInverseDenominator;
        if (this.getY().equals(BigInteger.ZERO)){
            lambdaModInverseDenominator = BigInteger.ONE;
        } else {
           lambdaModInverseDenominator = MathMethods.modularInverse(this.getY().multiply(BigInteger.TWO), ellipticCurve.moduleR);
        }
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.moduleR);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda,BigInteger.TWO, ellipticCurve.moduleR).subtract(this.getX().multiply(BigInteger.TWO));
        BigInteger newY =  lambda.multiply(this.getX().subtract(newX)).subtract(this.getY());
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }
    public EllipticCurvePoint multiply (BigInteger scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        EllipticCurvePoint result = new InfinitePoint();
        EllipticCurvePoint currentPoint = this;
        for (int i = 0; i < scalarMultiplicator.bitLength(); i++) {
            if (scalarMultiplicator.testBit(i)) {
                result = result.add(currentPoint, ellipticCurve);
            }
            currentPoint = currentPoint.doublePoint(ellipticCurve);
        }
        return result;
    }

    @Override
    public String toString() {
        return "EllipticCurvePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
