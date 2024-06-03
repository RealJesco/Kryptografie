package main.finiteFieldEllipticCurve;

import main.mathMethods.GaussianInteger;
import main.mathMethods.MathMethods;
import main.resource.Resource;
import test.IgnoreCoverage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FiniteFieldEllipticCurve {
    BigInteger a;
    BigInteger b;
    BigInteger p;
    BigInteger q;

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public BigInteger getP() {
        return p;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public FiniteFieldEllipticCurve(BigInteger n, BigInteger p) {
        this.a = n.multiply(n).negate();
        this.b = Resource.ZERO;
        this.p = p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    /**
     * Skript S.54 Def. 3.1
     * checks if 4a^3 + 27b^2 != 0
     * @param ellipticCurvePoint elliptic curve in question
     * @return true if the point is on the elliptic curve
     */
    public boolean isValidPoint(EllipticCurvePoint ellipticCurvePoint) {
        BigInteger inputYSquared = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getY(), Resource.TWO, p));
        BigInteger valueToCheck = (MathMethods.alternativeQuickExponentiation(ellipticCurvePoint.getX(), Resource.THREE, p).add(a.multiply(ellipticCurvePoint.getX()))).mod(p);
        return inputYSquared.equals(valueToCheck);
    }

    /**
     * Skript S.28 & S. 59
     * calculates all points represented by quadratic rest
     * @return list of all points on the elliptic curve
     */
    public List<EllipticCurvePoint> calculateAllPoints() {
        List<EllipticCurvePoint> calculatedPoints = new ArrayList<EllipticCurvePoint>();
        for (BigInteger i = Resource.ZERO; i.compareTo(p) < 0; i = i.add(Resource.ONE)) {
            BigInteger z = ((i.pow(3)).add(this.a.multiply(i)).add(this.b)).mod(p);
            for (BigInteger j = Resource.ZERO; j.compareTo(p) < 0; j = j.add(Resource.ONE)) {
                BigInteger ySquared = j.pow(2);
                BigInteger ySquaredModuleR = ySquared.mod(p);
                BigInteger possibleQuadraticRest = p.subtract(ySquaredModuleR);
                BigInteger squaredY = possibleQuadraticRest.pow(2);
                BigInteger sqrtOfY = squaredY.sqrt();
                if (ySquaredModuleR.equals(z) && (sqrtOfY.equals(possibleQuadraticRest) && !squaredY.equals(Resource.ONE))) {
                    calculatedPoints.add(new FiniteFieldEcPoint(i, j));
                }
            }
        }
        calculatedPoints.add(new InfinitePoint());
        return calculatedPoints;
    }

    /**
     * Skript S.61 - 63
     * @param n element in question
     * @return biginteger representing the order of the elements of the elliptic curve
     */
    public BigInteger calculateOrder(BigInteger n) {
        GaussianInteger quadraticDivisors = MathMethods.representPrimeAsSumOfSquares(this.p);
        BigInteger y;
        BigInteger x;
        if (quadraticDivisors.real.mod(Resource.TWO).equals(Resource.ZERO)) {
            y = quadraticDivisors.imaginary;
            x = quadraticDivisors.real;
        } else {
            y = quadraticDivisors.real;
            x = quadraticDivisors.imaginary;
        }

        BigInteger h;
        if (x.mod(Resource.FOUR).equals(Resource.ZERO)) {
            if (y.mod(Resource.FOUR).equals(Resource.THREE)) {
                h = Resource.TWO.negate().multiply(y);
            } else {
                h = Resource.TWO.multiply(y);
            }
        } else {
            if (y.mod(Resource.FOUR).equals(Resource.THREE)) {
                h = Resource.TWO.multiply(y);
            } else {
                h = Resource.TWO.negate().multiply(y);
            }
        }

        BigInteger legendreSign = MathMethods.calculateEulerCriterion(n, this.p);
        if (legendreSign.equals(Resource.ONE.negate())) {
            h = h.negate();
        }

        return this.p.add(Resource.ONE).subtract(h);
    }

    @Override
    @IgnoreCoverage
    public String toString() {
        return "main.FiniteFieldEllipticCurve{" +
                "\n\ta = " + a +
                ", \n\tb = " + b +
                ", \n\tmodule prime = " + p +
                '}';
    }
}
