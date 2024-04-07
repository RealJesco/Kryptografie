package FiniteFieldEllipticCurve;

import mathMethods.MathMethods;

import java.math.BigInteger;

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
}
