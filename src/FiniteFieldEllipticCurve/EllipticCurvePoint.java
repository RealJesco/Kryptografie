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
    public EllipticCurvePoint normalize (BigInteger x, BigInteger y, FiniteFieldEllipticCurve ellipticCurve){
        EllipticCurvePoint normalizedEcPoint = new FiniteFieldEcPoint(x.mod(ellipticCurve.moduleR).abs(), y.mod(ellipticCurve.moduleR).abs());
        if(ellipticCurve.isValidPoint(normalizedEcPoint)){
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
        return normalize(newX, newY, ellipticCurve);
    }
    public EllipticCurvePoint doublePoint (FiniteFieldEllipticCurve ellipticCurve) {
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(this.x, BigInteger.TWO, ellipticCurve.moduleR).multiply(BigInteger.valueOf(3)).add(ellipticCurve.coefficientOfX);
        BigInteger lambdaModInverseDenominator = MathMethods.extendedEuclidean(this.y.multiply(BigInteger.TWO), ellipticCurve.moduleR)[1];
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.moduleR);
        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda,BigInteger.TWO, ellipticCurve.moduleR).subtract(this.x.multiply(BigInteger.TWO));
        BigInteger newY =  lambda.multiply(this.x.subtract(newX)).subtract(this.y);
        return normalize(newX, newY, ellipticCurve);
    }
    public EllipticCurvePoint multiply (EllipticCurvePoint ecPoint, int scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        if(scalarMultiplicator == 0){
            return normalize(ecPoint.x, ecPoint.y, ellipticCurve);
        } else if (scalarMultiplicator == 1) {
            return ecPoint;
        }
        else if (scalarMultiplicator % 2 == 1){
            return ecPoint.add(multiply(ecPoint, scalarMultiplicator - 1, ellipticCurve), ellipticCurve);
        }
        else {
            return multiply(ecPoint.doublePoint(ellipticCurve), scalarMultiplicator / 2, ellipticCurve);
        }
    }

}
