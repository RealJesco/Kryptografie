package elGamal;

import FiniteFieldEllipticCurve.EllipticCurvePoint;

import java.math.BigInteger;

public record CipherMessage(EllipticCurvePoint point, BigInteger b1, BigInteger b2) {
}
