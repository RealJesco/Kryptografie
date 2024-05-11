package main.elGamalMenezesVanstone.records;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;

import java.math.BigInteger;

public record CipherMessage(EllipticCurvePoint point, BigInteger b1, BigInteger b2) {
}
