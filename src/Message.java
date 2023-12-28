import java.math.BigInteger;

public class Message {
    public String message;
    public boolean isEncrypted;
    public boolean isSigned;
    String signature = null;
    BigInteger e;
    BigInteger n;
    public Message(String message, String signature, BigInteger e, BigInteger n, boolean isEncrypted, boolean isSigned) {
        this.message = message;
        this.signature = signature;
        this.e = e;
        this.n = n;
        this.isEncrypted = isEncrypted;
        this.isSigned = isSigned;
    }
    public Message(String t, BigInteger E, BigInteger N) {
        this.message = t;
        this.n = N;
        this.e = E;
    }
    public boolean isEncrypted() {
        return isEncrypted;
    }
    public boolean isSigned() {
        return isSigned;
    }
}
