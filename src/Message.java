import java.math.BigInteger;

public class Message {
    private final Communicator sender;

    private final Communicator receiver;
    final public String message;
    private final String clearMessage;
    public boolean isEncrypted;
    public boolean isSigned;
    String signature = null;
    final BigInteger e;
    final BigInteger n;
    public Message(String message, String signature, BigInteger e, BigInteger n, boolean isEncrypted, boolean isSigned, Communicator sender, Communicator receiver, String clearMessage){
        this.message = message;
        this.signature = signature;
        this.e = e;
        this.n = n;
        this.isEncrypted = isEncrypted;
        this.isSigned = isSigned;
        this.sender = sender;
        this.receiver = receiver;
        this.clearMessage = clearMessage;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }
    public Communicator getSender() {
        return sender;
    }
    public boolean isSigned() {
        return isSigned;
    }
    public Communicator getReceiver() {
        return receiver;
    }
    public String getClearMessage(Communicator sender) {
        if(sender == this.sender){
            return clearMessage;
        } else {
            return "You are not the sender of this message!";
        }
    }
}
