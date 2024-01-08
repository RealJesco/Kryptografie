import GUI.CommunicationPanel;
import GUI.Zusatzaufgabe;
import rsa.RSA;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        CommunicationPanel.getInstance();
        Zusatzaufgabe.getInstance();
    }

}
