package main.finiteFieldEllipticCurve;

import main.mathMethods.MathMethods;
import main.resource.Resource;
import test.IgnoreCoverage;

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

    /**
     * Skript S. 62
     * @param ellipticCurve elliptic curve for which the point should be normalized
     * @return EllipticCurvePoint that is a valid point on the elliptic curve passes as parameter
     */
    public EllipticCurvePoint normalize(FiniteFieldEllipticCurve ellipticCurve) {
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(x.mod(ellipticCurve.p).abs(), y.mod(ellipticCurve.p).abs());
        if (ellipticCurve.isValidPoint(normalizedEcPoint)) {
            return normalizedEcPoint;
        } else {
            return new InfinitePoint();
        }
    }

    /**
     * Skript S. 62
     * @param point2 second point to add
     * @param ellipticCurve elliptic curve the new point should be on
     * @return new point that is the sum of the two points and is on the elliptic curve
     */
    public EllipticCurvePoint add(EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        if (point2 instanceof InfinitePoint) {
            return this;
        }
        if  (point2.x.equals(this.x) && point2.y.equals(this.y.negate())) {
            return new InfinitePoint();
        }
        if (point2.x.equals(this.x) && point2.y.equals(this.y)) {
            return this.doublePoint(ellipticCurve);
        }
        BigInteger lambdaNumerator = point2.getY().subtract(this.getY());
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean((point2.getX().subtract(this.getX())), ellipticCurve.p)[1];
        BigInteger lambda = lambdaNumerator.multiply(lambdaModInverseDenominator);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.p).subtract(this.getX()).subtract(point2.getX());
        BigInteger newY = lambda.multiply(this.getX().subtract(newX)).subtract(this.getY());

        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }

    /**
     * Skrip S. 62
     * @param ellipticCurve elliptic curve the point is on
     * @return new point that is the double of the current point and is on the elliptic curve
     */
    public EllipticCurvePoint doublePoint(FiniteFieldEllipticCurve ellipticCurve) {
        BigInteger y = this.getY();
        BigInteger x = this.getX();
        if (y.equals(Resource.ZERO) && x.equals(Resource.ZERO)) {
            throw new ArithmeticException("No modular inverse exists for these parameters");
        }
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(x, Resource.TWO, ellipticCurve.p).multiply(Resource.THREE).add(ellipticCurve.a);
        BigInteger lambdaModInverseDenominator;
        if (y.equals(Resource.ZERO)) {
            lambdaModInverseDenominator = Resource.ONE;
        } else {
            lambdaModInverseDenominator = MathMethods.modularInverse(y.multiply(Resource.TWO), ellipticCurve.p);
        }
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.p);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, Resource.TWO, ellipticCurve.p).subtract(x.multiply(Resource.TWO));
        BigInteger newY = lambda.multiply(x.subtract(newX)).subtract(y);
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }

    /**
     * Skript S. 63
     * @param scalarMultiplicator scalar multiplicator for the point
     * @param ellipticCurve elliptic curve the point is on
     * @return new point that is the scalar multiplicator times the current point and is on the elliptic curve
     */
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

    /**
     * @return string representation of the elliptic curve point with params x and y
     */
    @Override
    @IgnoreCoverage
    public String toString() {
        return "EllipticCurvePoint{" +
                "\n\tx = " + x +
                ", \n\ty = " + y +
                "}";
    }
}
