package rsa;

import java.math.BigInteger;

public record PublicKeyRsa(BigInteger e, BigInteger n) {}