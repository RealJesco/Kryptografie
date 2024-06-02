package main.GUI.HelperClasses;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class ElGamalMenezesVanstoneMessage {
    LocalDateTime time;
    BigInteger k;
    List<EllipticCurvePoint> cipherMessagePoints;

    String cipherMessageString;
    String signature;

    public ElGamalMenezesVanstoneMessage(List<EllipticCurvePoint> cipherMessagePoints, String cipherMessageString) {
        this.cipherMessagePoints = cipherMessagePoints;
        this.cipherMessageString = cipherMessageString;
        this.time = java.time.LocalDateTime.now();
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
    public LocalDateTime getTime() {
        return time;
    }

    public void setK(BigInteger k) {
        this.k = k;
    }

    public void addSignature(String sign) {
        signature = sign;
    }
    public String getSignature() {
        return signature;
    }
}
