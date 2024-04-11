package elGamal;

import FiniteFieldEllipticCurve.*;

public record PublicKey (FiniteFieldEllipticCurve ellipticCurve, EllipticCurvePoint generator, EllipticCurvePoint groupElement){
}
