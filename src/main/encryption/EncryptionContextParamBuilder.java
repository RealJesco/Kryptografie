package main.encryption;

import main.finiteFieldEllipticCurve.EllipticCurvePoint;
import main.elGamalMenezesVanstone.KeyPair;
import main.elGamalMenezesVanstone.records.PrivateKey;
import main.elGamalMenezesVanstone.records.PublicKey;
import main.rsa.KeyPairRsa;
import main.rsa.PrivateKeyRsa;
import main.rsa.PublicKeyRsa;

import java.math.BigInteger;
import java.util.Map;

public class EncryptionContextParamBuilder {
    private String data;
    private Map<String, Object> params = new java.util.HashMap<>();

    public EncryptionContextParamBuilder setData(String data) {
        this.data = data;
        return this;
    }

    public EncryptionContextParamBuilder setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public EncryptionContextParamBuilder withElGamalMenezesVanstonePublicKey(PublicKey publicKey) {
        params.put("PublicKey", publicKey);
        return this;
    }

    public EncryptionContextParamBuilder withElGamalMenezesVanstonePrivateKey(PrivateKey privateKey) {
        params.put("PrivateKey", privateKey);
        return this;
    }

    public EncryptionContextParamBuilder withNumberBase(int numberBase) {
        params.put("numberBase", numberBase);
        return this;
    }

    public EncryptionContextParamBuilder withElGamalMenezesVanstoneKeyPair(KeyPair keyPair) {
        params.put("KeyPair", keyPair);
        params.put("PublicKey", keyPair.getPublicKey());
        params.put("PrivateKey", keyPair.getPrivateKey());
        return this;
    }

    public EncryptionContextParamBuilder withElGamalMenezesVanstoneCipherMessage(Object elGamalMenezesVanstoneCipherMessage) {
        params.put("elGamalMenezesVanstoneCipherMessage", elGamalMenezesVanstoneCipherMessage);
        return this;
    }

    public EncryptionContextParamBuilder withRsaKeyPair(KeyPairRsa keyPair) {
        params.put("KeyPair", keyPair);
        params.put("PublicKey", keyPair.publicKeyRsa());
        params.put("PrivateKey", keyPair.privateKeyRsa());
        return this;
    }

    public EncryptionContextParamBuilder withRsaPublicKey(PublicKeyRsa publicKey) {
        params.put("PublicKey", publicKey);
        return this;
    }

    public EncryptionContextParamBuilder withRsaPrivateKey(PrivateKeyRsa privateKey) {
        params.put("PrivateKey", privateKey);
        return this;
    }

    public EncryptionContextParamBuilder withK(BigInteger k) {
        params.put("k", k);
        return this;
    }

    public EncryptionContextParamBuilder withKy(EllipticCurvePoint ky) {
        params.put("ky", ky);
        return this;
    }

    public Map<String, Object> build() {
        params.put("data", data);
        return params;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "EncryptionContextParamBuilder{" +
                "data='" + data + '\'' +
                ", params=" + params +
                '}';
    }
}
