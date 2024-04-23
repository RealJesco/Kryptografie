package elGamalMenezesVanstone;

import FiniteFieldEllipticCurve.FiniteFieldEllipticCurve;

import java.math.BigInteger;

public record PrivateKey ( FiniteFieldEllipticCurve ellipticCurve, BigInteger secretMultiplierX){

}
