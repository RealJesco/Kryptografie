import java.math.BigInteger;

public class Message {
    String signature = null;
    BigInteger e;
    BigInteger n;
    String text;
    public Message(String t, String s, BigInteger E, BigInteger N) {
        this.signature = s;
        this.text = t;
        this.n = N;
        this.e = E;
    }
    public Message(String t, BigInteger E, BigInteger N) {
        this.text = t;
        this.n = N;
        this.e = E;
    }
}
