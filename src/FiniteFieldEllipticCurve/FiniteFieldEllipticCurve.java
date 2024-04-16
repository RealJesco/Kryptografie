package FiniteFieldEllipticCurve;

import mathMethods.GaussianInteger;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FiniteFieldEllipticCurve {
    BigInteger a;
    BigInteger b;
    BigInteger p;

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public FiniteFieldEllipticCurve(BigInteger n, BigInteger p) {
        this.a = n.multiply(n).negate();
        this.b = BigInteger.ZERO;
        this.p = p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }



    public boolean isValidPoint(EllipticCurvePoint ellipticCurvePoint){
        BigInteger inputYSquared = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getY(), BigInteger.TWO, p));
        BigInteger valueToCheck = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getX(), BigInteger.valueOf(3), p).add(a.multiply(ellipticCurvePoint.getX()))).mod(p);
        return inputYSquared.equals(valueToCheck);
    }

    public List<EllipticCurvePoint> calculateAllPoints() {
        List<EllipticCurvePoint> calculatedPoints = new ArrayList<EllipticCurvePoint>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger z = ((i.pow(3)).add(this.a.multiply(i)).add(this.b)).mod(p);
            for (BigInteger j = BigInteger.ZERO; j.compareTo(p) < 0; j = j.add(BigInteger.ONE)) {
                BigInteger ySquared = j.pow(2);
                BigInteger ySquaredModuleR = ySquared.mod(p);
                BigInteger possibleQuadraticRest = p.subtract(ySquaredModuleR);
                BigInteger squaredY = possibleQuadraticRest.pow(2);
                BigInteger sqrtOfY = squaredY.sqrt();
                if(ySquaredModuleR.equals(z) && (sqrtOfY.equals(possibleQuadraticRest) && !squaredY.equals(BigInteger.ONE))){
                    calculatedPoints.add(new FiniteFieldEcPoint(i,j));
                }
            }
        }
        calculatedPoints.add(new InfinitePoint());
        return calculatedPoints;
    }
    public BigInteger calculateOrder(BigInteger n){
        GaussianInteger quadraticDivisors = MathMethods.representPrimeAsSumOfSquares(this.p);
        BigInteger y;
        BigInteger x;
        if(!quadraticDivisors.real.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
           y = quadraticDivisors.real;
           x = quadraticDivisors.imaginary;
        } else {
            y = quadraticDivisors.imaginary;
            x = quadraticDivisors.real;
        }

        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, this.p);

        BigInteger realPartSign = y.mod(BigInteger.TWO).equals(BigInteger.ZERO) ? BigInteger.ONE : BigInteger.ONE.negate();
//        System.out.println("legendreSign + " + legendreSign);
//        System.out.println("realPartSign " + realPartSign);
        //TODO Test if this is correct for all cases
        BigInteger commonCalculation = BigInteger.TWO.multiply(y).multiply(legendreSign);
        if(legendreSign.equals(realPartSign)){
            commonCalculation = commonCalculation.negate();
        }
        return this.p.add(BigInteger.ONE).subtract(commonCalculation);
    }

    @Override
    public String toString() {
        return "FiniteFieldEllipticCurve{" +
                "a=" + a +
                ", b=" + b +
                ", moduleR=" + p +
                '}';
    }
}
