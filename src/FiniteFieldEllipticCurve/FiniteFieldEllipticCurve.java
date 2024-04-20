package FiniteFieldEllipticCurve;

import mathMethods.GaussianInteger;
import mathMethods.MathMethods;
import resource.Resource;

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
        this.b = Resource.ZERO;
        this.moduleR = moduleR;
    }


    public boolean isValidPoint(EllipticCurvePoint ellipticCurvePoint){
        BigInteger inputYSquared = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getY(), Resource.TWO, moduleR));
        BigInteger valueToCheck = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getX(), Resource.THREE, moduleR).add(a.multiply(ellipticCurvePoint.getX()))).mod(moduleR);
        return inputYSquared.equals(valueToCheck);
    }

    public List<EllipticCurvePoint> calculateAllPoints() {
        List<EllipticCurvePoint> calculatedPoints = new ArrayList<EllipticCurvePoint>();
        for (BigInteger i = Resource.ZERO; i.compareTo(moduleR) < 0; i = i.add(Resource.ONE)) {
            BigInteger z = ((i.pow(3)).add(this.a.multiply(i)).add(this.b)).mod(moduleR);
            for (BigInteger j = Resource.ZERO; j.compareTo(moduleR) < 0; j = j.add(Resource.ONE)) {
                BigInteger ySquared = j.pow(2);
                BigInteger ySquaredModuleR = ySquared.mod(moduleR);
                BigInteger possibleQuadraticRest = moduleR.subtract(ySquaredModuleR);
                BigInteger squaredY = possibleQuadraticRest.pow(2);
                BigInteger sqrtOfY = squaredY.sqrt();
                if(ySquaredModuleR.equals(z) && (sqrtOfY.equals(possibleQuadraticRest) && !squaredY.equals(Resource.ONE))){
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
        if(!quadraticDivisors.real.mod(Resource.TWO).equals(Resource.ZERO)){
           y = quadraticDivisors.real;
           x = quadraticDivisors.imaginary;
        } else {
            y = quadraticDivisors.imaginary;
            x = quadraticDivisors.real;
        }

        BigInteger legendreSign = MathMethods.verifyEulerCriterion(n, this.moduleR);

        BigInteger realPartSign = y.mod(Resource.TWO).equals(Resource.ZERO) ? Resource.ONE : Resource.ONE.negate();
//        System.out.println("legendreSign + " + legendreSign);
//        System.out.println("realPartSign " + realPartSign);
        //TODO Test if this is correct for all cases
        if(legendreSign.equals(realPartSign)){
            return this.moduleR.add(Resource.ONE).subtract(Resource.TWO.multiply(y).multiply(legendreSign.negate()));
        } else {
            return this.moduleR.add(Resource.ONE).subtract(Resource.TWO.multiply(y).multiply(legendreSign));
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
