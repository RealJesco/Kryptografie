package main.mathMethods;

import main.resource.Resource;

import java.math.BigInteger;

public class GaussianInteger {
    public BigInteger real;
    public BigInteger imaginary;

    public GaussianInteger(BigInteger real, BigInteger imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public boolean isZero() {
        return real.equals(Resource.ZERO) && imaginary.equals(Resource.ZERO);
    }
    public boolean equals(GaussianInteger b) {
        return this.real.equals(b.real) && this.imaginary.equals(b.imaginary);
    }

    public boolean isSymmetricallyEqualTo(GaussianInteger b) {
        return this.real.equals(b.imaginary) && this.imaginary.equals(b.real);
    }

    public BigInteger absolute() {
        return real.multiply(real).add(imaginary.multiply(imaginary)).sqrt();
    }

    /**
     * Checks if this GaussianInteger is a multiple of the given GaussianInteger.
     *
     * @param b the GaussianInteger to check for multiple
     * @return true if this GaussianInteger is a multiple of the given GaussianInteger, false otherwise
     */
    public boolean isMultiple(GaussianInteger b) {

        if (b.real.equals(Resource.ZERO) && b.imaginary.equals(Resource.ZERO)) {
            return false;
        }

        GaussianInteger quotient = this.divide(b);
        GaussianInteger product = quotient.multiply(b);

        if(quotient.real.equals(Resource.ONE) && quotient.imaginary.equals(Resource.ONE)) {
            return false;
        }
        return this.equals(product);
    }

    public GaussianInteger divide(GaussianInteger b) {
        BigInteger normB = b.real.pow(2).add(b.imaginary.pow(2));

        BigInteger realNumerator = this.real.multiply(b.real).add(this.imaginary.multiply(b.imaginary));
        BigInteger realPart = MathMethods.roundHalfUp(realNumerator.divide(normB));

        BigInteger imagNumerator = this.imaginary.multiply(b.real).subtract(this.real.multiply(b.imaginary)); // Use subtraction here
        BigInteger imagPart = MathMethods.roundHalfUp(imagNumerator.divide(normB));

        return new GaussianInteger(realPart, imagPart);
    }


    public GaussianInteger multiply(GaussianInteger b) {
        BigInteger realPart = this.real.multiply(b.real).subtract(this.imaginary.multiply(b.imaginary));
        BigInteger imaginaryPart = this.real.multiply(b.imaginary).add(this.imaginary.multiply(b.real));
        return new GaussianInteger(realPart, imaginaryPart);
    }

    public GaussianInteger subtract(GaussianInteger b) {
        BigInteger realPart = this.real.subtract(b.real);
        BigInteger imaginaryPart = this.imaginary.subtract(b.imaginary);
        return new GaussianInteger(realPart, imaginaryPart);
    }
    public GaussianInteger add(GaussianInteger b) {
        BigInteger realPart = this.real.add(b.real);
        BigInteger imaginaryPart = this.imaginary.add(b.imaginary);
        return new GaussianInteger(realPart, imaginaryPart);
    }

    /**
     * Normalizes the GaussianInteger object by removing any common factors of the real and imaginary parts.
     * If both the real and imaginary parts are zero, it returns a new GaussianInteger object with zero values.
     * If the real part is zero, it returns a new GaussianInteger object with only the imaginary part.
     * If the imaginary part is zero, it returns a new GaussianInteger object with only the real part.
     * Otherwise, it returns the unchanged GaussianInteger object.
     *
     * @return the normalized GaussianInteger object
     */
    public GaussianInteger normalizeGCD() {
        if (this.real.equals(Resource.ZERO) && this.imaginary.equals(Resource.ZERO)) {
            return new GaussianInteger(Resource.ZERO, Resource.ZERO);
        } else if (this.real.equals(Resource.ZERO)) {
            return new GaussianInteger(this.imaginary, Resource.ZERO);
        } else if (this.imaginary.equals(Resource.ZERO)) {
            return new GaussianInteger(this.real, Resource.ZERO);
        }
        return this;
    }

    @Override
    public String toString() {
        return real + " + " + imaginary + "i";
    }
}
