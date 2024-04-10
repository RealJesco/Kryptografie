package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;
import java.util.List;

abstract class EllipticCurvePoint {
    BigInteger x;
    BigInteger y;

    public EllipticCurvePoint (BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
    public EllipticCurvePoint normalize (FiniteFieldEllipticCurve ellipticCurve){
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(x.mod(ellipticCurve.moduleR).abs(), y.mod(ellipticCurve.moduleR).abs());
        if(ellipticCurve.isValidPoint(normalizedEcPoint)) {
            return normalizedEcPoint;
        } else {
            return new InfinitePoint(normalizedEcPoint.x, normalizedEcPoint.y);
        }
    }
    public EllipticCurvePoint add (EllipticCurvePoint point2, FiniteFieldEllipticCurve ellipticCurve) {
        BigInteger lambdaNumerator = point2.y.subtract(this.y);
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean((point2.x.subtract(this.x)), ellipticCurve.moduleR)[1];
        BigInteger lambda = lambdaNumerator.multiply(lambdaModInverseDenominator);
        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda, BigInteger.TWO, ellipticCurve.moduleR).subtract(this.x).subtract(point2.x);
        BigInteger newY = lambda.multiply(this.x.subtract(newX)).subtract(this.y);
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);
        return newPoint.normalize(ellipticCurve);
    }
    public EllipticCurvePoint doublePoint (FiniteFieldEllipticCurve ellipticCurve) {
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(this.x, BigInteger.TWO, ellipticCurve.moduleR).multiply(BigInteger.valueOf(3)).add(ellipticCurve.coefficientOfX);
        BigInteger lambdaModInverseDenominator = MathMethods.modularInverse(this.y.multiply(BigInteger.TWO), ellipticCurve.moduleR);
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.moduleR);
        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda,BigInteger.TWO, ellipticCurve.moduleR).subtract(this.x.multiply(BigInteger.TWO));
        BigInteger newY =  lambda.multiply(this.x.subtract(newX)).subtract(this.y);
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);
        return newPoint.normalize(ellipticCurve);
    }
    public EllipticCurvePoint multiply (int scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        if(scalarMultiplicator == 0){
            EllipticCurvePoint lastPoint = this.doublePoint(ellipticCurve);
            System.out.println(this.x + " " + this.y);
            System.out.println(lastPoint.x + " " + lastPoint.y);
            EllipticCurvePoint newPoint = new FiniteFieldEcPoint(lastPoint.x, lastPoint.y);
            return newPoint.normalize(ellipticCurve);
        } else if (scalarMultiplicator == 1) {
            return this;
        }
        else if (scalarMultiplicator % 2 == 1){
            return this.add(multiply( scalarMultiplicator - 1, ellipticCurve), ellipticCurve);
        }
        else {
            return this.doublePoint(ellipticCurve).multiply(scalarMultiplicator / 2, ellipticCurve);
        }
    }

}
