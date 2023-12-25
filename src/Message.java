import java.math.BigInteger;

public class Message {
    public String originalText;
    public String encryptedText;
    String signature = null;
    BigInteger e;
    BigInteger n;
    public Message(String originalText, String encryptedText, String signature, BigInteger e, BigInteger n) {
        this.originalText = originalText;
        this.encryptedText = encryptedText;
        this.signature = signature;
        this.e = e;
        this.n = n;
    }
    public Message(String t, BigInteger E, BigInteger N) {
        this.originalText = t;
        this.n = N;
        this.e = E;
    }
}
