package main.elGamalMenezesVanstone;

import main.finiteFieldEllipticCurve.*;

import java.math.BigInteger;

public record PublicKey (FiniteFieldEllipticCurve ellipticCurve, EllipticCurvePoint generator, EllipticCurvePoint groupElement, BigInteger order){

}
