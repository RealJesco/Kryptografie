package main.GUI.HelperClasses;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;

import java.math.BigInteger;
import java.util.List;

public class ElGamalMenezesVanstoneMessage {
    BigInteger k;
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

    public BigInteger getK() {
        return k;
    }

    public void setK(BigInteger k) {
        this.k = k;
    }

}
