package main.rsa;

import java.math.BigInteger;

public record PrivateKeyRsa(BigInteger d, BigInteger n) {}
