package encryption;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class EncryptionContext {
    private StringEncryptionStrategy strategy;

    public EncryptionContext() {
    }

    public Object encrypt(String data, Map<String, Object> params) {
        return strategy.encrypt(data, params);
    }

    public String decrypt(String data, Map<String, Object> params) {
        return strategy.decrypt(data, params);
    }

    public String sign(String data, Map<String, Object> params) throws NoSuchAlgorithmException {
        return strategy.sign(data, params);
    }

    public boolean verify(String data, String signature, Map<String, Object> params) throws NoSuchAlgorithmException {
        return strategy.verify(data, signature, params);
    }

    public void setStrategy(StringEncryptionStrategy strategy) {
        this.strategy = strategy;
    }
    public StringEncryptionStrategy getStrategy() {
        return this.strategy;
    }
}
