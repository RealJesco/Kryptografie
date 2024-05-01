package GUI.HelperClasses;

import FiniteFieldEllipticCurve.EllipticCurvePoint;

import java.util.List;

public class ElGamalMenezesVanstoneMessage {
    List<EllipticCurvePoint> cipherMessagePoints;

    String cipherMessageString;

    public ElGamalMenezesVanstoneMessage(List<EllipticCurvePoint> cipherMessagePoints, String cipherMessageString) {
        this.cipherMessagePoints = cipherMessagePoints;
        this.cipherMessageString = cipherMessageString;
    }

    public List<EllipticCurvePoint> getCipherMessagePoints() {
        return cipherMessagePoints;
    }

    public String getCipherMessageString() {
        return cipherMessageString;
    }

}
