package encryption;

import elGamal.KeyPair;
import elGamal.PrivateKey;
import elGamal.PublicKey;
import rsa.KeyPairRsa;
import rsa.PrivateKeyRsa;
import rsa.PublicKeyRsa;

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
