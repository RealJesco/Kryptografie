package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;
import resource.Resource;

import java.math.BigInteger;

public abstract class EllipticCurvePoint {
    private BigInteger x;
    private BigInteger y;

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
        BigInteger xAbs = x.mod(ellipticCurve.moduleR).abs();
        BigInteger yAbs = y.mod(ellipticCurve.moduleR).abs();
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(xAbs, yAbs);
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
        BigInteger point1Y = this.getY();
        BigInteger lambdaNumerator = point2.getY().subtract(point1Y);
        BigInteger point1X = this.getX();
        BigInteger point2X = point2.getX();
        BigInteger subtractX1 = point2X.subtract(point1X);
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean(subtractX1, ellipticCurve.moduleR)[1];
        BigInteger lambda = lambdaNumerator.multiply(lambdaModInverseDenominator);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.moduleR).subtract(point1X).subtract(point2X);
        BigInteger subtractNewX = point1X.subtract(newX);
        BigInteger newY = lambda.multiply(subtractNewX).subtract(point1Y);

        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }

    public EllipticCurvePoint doublePoint(FiniteFieldEllipticCurve ellipticCurve) {
        BigInteger x1 = this.getX();
        BigInteger y1 = this.getY();
        if (y1.equals(Resource.ZERO) && x1.equals(Resource.ZERO)) {
            throw new ArithmeticException("No modular inverse exists for these parameters");
        }
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(x1, Resource.TWO, ellipticCurve.moduleR).multiply(Resource.THREE).add(ellipticCurve.a);
        BigInteger lambdaModInverseDenominator;
        if (y1.equals(Resource.ZERO)) {
            lambdaModInverseDenominator = Resource.ONE;
        } else {
            BigInteger y1MultTWO = y1.multiply(Resource.TWO);
            lambdaModInverseDenominator = MathMethods.modularInverse(y1MultTWO, ellipticCurve.moduleR);
        }
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.moduleR);

        BigInteger multiply = x1.multiply(Resource.TWO);
        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.moduleR).subtract(multiply);
        BigInteger newY = lambda.multiply(x1.subtract(newX)).subtract(y1);
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
