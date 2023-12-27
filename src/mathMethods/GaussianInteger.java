package mathMethods;

import java.math.BigInteger;

import static java.math.BigInteger.ZERO;

public class GaussianInteger {
//    public final BigInteger[] gaussianInteger;
    public BigInteger real;
    public BigInteger imaginary;

    public GaussianInteger(BigInteger real, BigInteger imaginary) {
        this.real = real;
        this.imaginary = imaginary;
//        this.gaussianInteger = new BigInteger[]{real, imaginary};
    }

    public boolean isZero() {
        return real.equals(BigInteger.ZERO) && imaginary.equals(BigInteger.ZERO);
    }
    public boolean equals(GaussianInteger b) {
        return this.real.equals(b.real) && this.imaginary.equals(b.imaginary);
    }
    public boolean isMultiple(GaussianInteger b) {
        // Avoid division by zero
        if (b.real.equals(BigInteger.ZERO) && b.imaginary.equals(BigInteger.ZERO)) {
            return false;
        }

        GaussianInteger quotient = this.divide(b);
        GaussianInteger product = quotient.multiply(b);

        // Check if multiplying the quotient by b results in a, without rounding
        return this.equals(product);
    }

    public GaussianInteger divide(GaussianInteger b) {
        BigInteger normB = b.real.pow(2).add(b.imaginary.pow(2));

        // Compute the real part of the quotient
        BigInteger realNumerator = this.real.multiply(b.real).add(this.imaginary.multiply(b.imaginary));
        BigInteger realPart = MathMethods.roundHalfUp(realNumerator.divide(normB));

        // Compute the imaginary part of the quotient
        BigInteger imagNumerator = this.imaginary.multiply(b.real).subtract(this.real.multiply(b.imaginary)); // Use subtraction here
        BigInteger imagPart = MathMethods.roundHalfUp(imagNumerator.divide(normB));

        return new GaussianInteger(realPart, imagPart);
    }


    public GaussianInteger multiply(GaussianInteger b) {
        BigInteger realPart = this.real.multiply(b.real).subtract(this.imaginary.multiply(b.imaginary));
        BigInteger imaginaryPart = this.real.multiply(b.imaginary).add(this.imaginary.multiply(b.real));
        return new GaussianInteger(realPart, imaginaryPart);
    }

    public GaussianInteger subtractGaussianInteger(GaussianInteger b) {
        BigInteger realPart = this.real.subtract(b.real);
        BigInteger imaginaryPart = this.imaginary.subtract(b.imaginary);
        return new GaussianInteger(realPart, imaginaryPart);
    }
    public GaussianInteger addGaussianIntegers(GaussianInteger b) {
        BigInteger realPart = this.real.add(b.real);
        BigInteger imaginaryPart = this.imaginary.add(b.imaginary);
        return new GaussianInteger(realPart, imaginaryPart);
    }

    public GaussianInteger normalizeGCD() {
        if (this.real.equals(BigInteger.ZERO) && this.imaginary.equals(BigInteger.ZERO)) {
            return new GaussianInteger(BigInteger.ZERO, BigInteger.ZERO);
        } else if (this.real.equals(BigInteger.ZERO)) {
            return new GaussianInteger(this.imaginary, ZERO);
        } else if (this.imaginary.equals(BigInteger.ZERO)) {
            return new GaussianInteger(this.real, ZERO);
        }
        return this;
    }

    @Override
    public String toString() {
        return real + " + " + imaginary + "i";
    }
}
