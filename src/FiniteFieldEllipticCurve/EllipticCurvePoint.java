package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;

public abstract class EllipticCurvePoint {
    private final BigInteger x;
    private final BigInteger y;

    public EllipticCurvePoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public EllipticCurvePoint normalize(FiniteFieldEllipticCurve ellipticCurve) {
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(x.mod(ellipticCurve.p).abs(), y.mod(ellipticCurve.p).abs());
        if (ellipticCurve.isValidPoint(normalizedEcPoint)) {
            return normalizedEcPoint;
        } else {
            return new InfinitePoint();
        }
    }

    public EllipticCurvePoint add(EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        if (point2 instanceof InfinitePoint) {
            return this;
        }
        BigInteger lambdaNumerator = point2.getY().subtract(this.getY());
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean((point2.getX().subtract(this.getX())), ellipticCurve.p)[1];
        BigInteger lambda = lambdaNumerator.multiply(lambdaModInverseDenominator);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.p).subtract(this.getX()).subtract(point2.getX());
        BigInteger newY = lambda.multiply(this.getX().subtract(newX)).subtract(this.getY());

        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }

    public EllipticCurvePoint doublePoint(FiniteFieldEllipticCurve ellipticCurve) {
        if (this.getY().equals(Resource.ZERO) && this.getX().equals(Resource.ZERO)) {
            throw new ArithmeticException("No modular inverse exists for these parameters");
        }
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(this.getX(), Resource.TWO, ellipticCurve.p).multiply(Resource.THREE).add(ellipticCurve.a);
        BigInteger lambdaModInverseDenominator;
        if (this.getY().equals(Resource.ZERO)) {
            lambdaModInverseDenominator = Resource.ONE;
        } else {
            lambdaModInverseDenominator = MathMethods.modularInverse(this.getY().multiply(Resource.TWO), ellipticCurve.p);
        }
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.p);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.p).subtract(this.getX().multiply(Resource.TWO));
        BigInteger newY = lambda.multiply(this.getX().subtract(newX)).subtract(this.getY());
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }

    public EllipticCurvePoint multiply(BigInteger scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
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
