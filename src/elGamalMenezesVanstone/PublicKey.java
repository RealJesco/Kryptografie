package elGamalMenezesVanstone;

import FiniteFieldEllipticCurve.*;

import java.math.BigInteger;

public record PublicKey (FiniteFieldEllipticCurve ellipticCurve, EllipticCurvePoint generator, EllipticCurvePoint groupElement, BigInteger order){

}
