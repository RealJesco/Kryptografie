package FiniteFieldEllipticCurve;

import mathMethods.GaussianInteger;
import mathMethods.MathMethods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FiniteFieldEllipticCurve {
    BigInteger a;
    BigInteger b;
    BigInteger moduleR;

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getModuleR() {
        return moduleR;
    }

    public FiniteFieldEllipticCurve(BigInteger n, BigInteger moduleR) {
        this.a = n.multiply(n).negate();
        this.b = BigInteger.ZERO;
        this.moduleR = moduleR;
    }


    public boolean isValidPoint(EllipticCurvePoint ellipticCurvePoint){
        BigInteger inputYSquared = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getY(), BigInteger.TWO, moduleR));
        BigInteger valueToCheck = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getX(), BigInteger.valueOf(3), moduleR).add(a.multiply(ellipticCurvePoint.getX()))).mod(moduleR);
        return inputYSquared.equals(valueToCheck);
    }

    public List<EllipticCurvePoint> calculateAllPoints() {
        List<EllipticCurvePoint> calculatedPoints = new ArrayList<EllipticCurvePoint>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(moduleR) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger z = ((i.pow(3)).add(this.a.multiply(i)).add(this.b)).mod(moduleR);
            for (BigInteger j = BigInteger.ZERO; j.compareTo(moduleR) < 0; j = j.add(BigInteger.ONE)) {
                BigInteger ySquared = j.pow(2);
                BigInteger ySquaredModuleR = ySquared.mod(moduleR);
                BigInteger possibleQuadraticRest = moduleR.subtract(ySquaredModuleR);
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
        GaussianInteger quadraticDivisors = MathMethods.representPrimeAsSumOfSquares(this.moduleR);
        BigInteger y;
        BigInteger x;
        if(!quadraticDivisors.real.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
           y = quadraticDivisors.real;
           x = quadraticDivisors.imaginary;
        } else {
            y = quadraticDivisors.imaginary;
            x = quadraticDivisors.real;
        }

        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, this.moduleR);

        BigInteger realPartSign = y.mod(BigInteger.TWO).equals(BigInteger.ZERO) ? BigInteger.ONE : BigInteger.ONE.negate();
//        System.out.println("legendreSign + " + legendreSign);
//        System.out.println("realPartSign " + realPartSign);
        //TODO Test if this is correct for all cases
        if(legendreSign.equals(realPartSign)){
            return this.moduleR.add(BigInteger.ONE).subtract(BigInteger.TWO.multiply(y).multiply(legendreSign.negate()));
        } else {
            return this.moduleR.add(BigInteger.ONE).subtract(BigInteger.TWO.multiply(y).multiply(legendreSign));
        }
    }

    @Override
    public String toString() {
        return "FiniteFieldEllipticCurve{" +
                "a=" + a +
                ", b=" + b +
                ", moduleR=" + moduleR +
                '}';
    }
}
