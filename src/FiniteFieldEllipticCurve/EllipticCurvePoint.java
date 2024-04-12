package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;
import java.util.List;

public abstract class EllipticCurvePoint {
    BigInteger x;
    BigInteger y;

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
        if(this.y.equals(BigInteger.ZERO) && this.x.equals(BigInteger.ZERO)){
            throw new ArithmeticException("No modular inverse exists for these parameters");
        }
        BigInteger lambdaNumerator = MathMethods.alternativeQuickExponentiation(this.x, BigInteger.TWO, ellipticCurve.moduleR).multiply(BigInteger.valueOf(3)).add(ellipticCurve.a);
        BigInteger lambdaModInverseDenominator;
        if (this.y.equals(BigInteger.ZERO)){
            lambdaModInverseDenominator = BigInteger.ONE;
        } else {
           lambdaModInverseDenominator = MathMethods.modularInverse(this.y.multiply(BigInteger.TWO), ellipticCurve.moduleR);
        }
        BigInteger lambda = (lambdaNumerator.multiply(lambdaModInverseDenominator)).mod(ellipticCurve.moduleR);

        BigInteger newX = MathMethods.alternativeQuickExponentiation(lambda,BigInteger.TWO, ellipticCurve.moduleR).subtract(this.x.multiply(BigInteger.TWO));
        BigInteger newY =  lambda.multiply(this.x.subtract(newX)).subtract(this.y);
        EllipticCurvePoint newPoint = new FiniteFieldEcPoint(newX, newY);

        return newPoint.normalize(ellipticCurve);
    }
    public EllipticCurvePoint multiply (BigInteger scalarMultiplicator, FiniteFieldEllipticCurve ellipticCurve) {
        if(scalarMultiplicator.equals(BigInteger.ZERO)){

            EllipticCurvePoint lastPoint = this.doublePoint(ellipticCurve);
            EllipticCurvePoint newPoint = new FiniteFieldEcPoint(lastPoint.x, lastPoint.y);

            return newPoint.normalize(ellipticCurve);

        } else if (scalarMultiplicator.equals(BigInteger.ONE)) {

            return this;

        }
        else if (scalarMultiplicator.mod(BigInteger.TWO).equals(BigInteger.ONE)){

            return this.add(multiply( scalarMultiplicator.subtract(BigInteger.ONE), ellipticCurve), ellipticCurve);

        }
        else {

            return this.doublePoint(ellipticCurve).multiply(scalarMultiplicator.divide(BigInteger.TWO), ellipticCurve);

        }
    }

}
