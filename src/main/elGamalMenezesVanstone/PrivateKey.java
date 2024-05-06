package main.elGamalMenezesVanstone;

import main.finiteFieldEllipticCurve.FiniteFieldEllipticCurve;

import java.math.BigInteger;

public record PrivateKey ( FiniteFieldEllipticCurve ellipticCurve, BigInteger secretMultiplierX){

}
