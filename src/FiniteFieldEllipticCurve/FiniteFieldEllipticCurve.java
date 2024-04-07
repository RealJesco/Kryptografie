package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FiniteFieldEllipticCurve {
//    int ySquared;
//    int xCubed;
    BigInteger coefficientOfX;
//    int x;
    BigInteger b;
    BigInteger moduleR;

    public FiniteFieldEllipticCurve(BigInteger coefficientOfX,  BigInteger b, BigInteger moduleR) {
//        this.ySquared = (int) Math.pow(x, 2);
//        this.xCubed = (int) Math.pow(x, 3);
        this.coefficientOfX = coefficientOfX;
//        this.x = x;
        this.b = b;
        this.moduleR = moduleR;
    }

    public boolean isValidPoint(EllipticCurvePoint ellipticCurvePoint){
        BigInteger inputYSquared = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.y, BigInteger.TWO, moduleR));
        BigInteger valueToCheck = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.x, BigInteger.valueOf(3), moduleR).add(coefficientOfX.multiply(ellipticCurvePoint.x.add(b)))).mod(moduleR);
        return inputYSquared.equals(valueToCheck);
    }

    public boolean noCubicSolutionsPossible(){
        BigInteger a = MathMethods.alternativeQuickExponentiation(BigInteger.valueOf(4).multiply(this.coefficientOfX), BigInteger.valueOf(3), BigInteger.ONE);
        BigInteger b = MathMethods.alternativeQuickExponentiation(BigInteger.valueOf(4).multiply(this.b), BigInteger.valueOf(2), BigInteger.ONE);
        return !a.add(b).mod(moduleR).equals(BigInteger.ZERO);
    }
    public List<EllipticCurvePoint> calculateAllPoints(){
        List<EllipticCurvePoint> calculatedPoints = new ArrayList<EllipticCurvePoint>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(moduleR) < 0; i = i.add(BigInteger.ONE)) {
            BigInteger z = ((i.pow(3)).add(this.coefficientOfX.multiply(i)).add(this.b)).mod(moduleR);
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
        return calculatedPoints;
    }
}