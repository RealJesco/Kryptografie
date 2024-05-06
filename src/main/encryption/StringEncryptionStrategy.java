package main.encryption;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface StringEncryptionStrategy {
    Object encrypt(String data, Map<String, Object> params);
    String decrypt(String data, Map<String, Object> params);
    String sign(String data, Map<String, Object> params) throws NoSuchAlgorithmException;
    boolean verify(String data, String signature, Map<String, Object> params) throws NoSuchAlgorithmException;
}
