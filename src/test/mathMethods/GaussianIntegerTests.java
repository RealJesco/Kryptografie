package test.mathMethods;

import main.mathMethods.GaussianInteger;
import main.resource.Resource;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static main.resource.Resource.ZERO;

public class GaussianIntegerTests {
    @Test
    void testIsZeroTrue() {
        GaussianInteger a = new GaussianInteger(ZERO, ZERO);

        assertTrue(a.isZero());
    }

    @Test
    void testIsZeroOnlyRealZero() {
        GaussianInteger a = new GaussianInteger(ZERO, new BigInteger("7"));

        assertFalse(a.isZero());
    }

    @Test
    void testIsZeroOnlyImaginaryZero() {
        GaussianInteger a = new GaussianInteger(new BigInteger("6"), ZERO);

        assertFalse(a.isZero());
    }

    @Test
    void testIsZeroNeitherZero() {
        GaussianInteger a = new GaussianInteger(new BigInteger("42"), new BigInteger("27"));

        assertFalse(a.isZero());
    }

    @Test
    void testEqualsTrue() {
        GaussianInteger a = new GaussianInteger(new BigInteger("42"), new BigInteger("27"));
        GaussianInteger b = new GaussianInteger(new BigInteger("42"), new BigInteger("27"));

        assertTrue(a.equals(b));
    }

    @Test
    void testEqualsOnlyRealEquals() {
        GaussianInteger a = new GaussianInteger(new BigInteger("42"), new BigInteger("27"));
        GaussianInteger b = new GaussianInteger(new BigInteger("42"), new BigInteger("317"));

        assertFalse(a.equals(b));
    }

    @Test
    void testEqualsOnlyImaginaryEquals() {
        GaussianInteger a = new GaussianInteger(new BigInteger("93"), new BigInteger("27"));
        GaussianInteger b = new GaussianInteger(new BigInteger("42"), new BigInteger("27"));

        assertFalse(a.equals(b));
    }

    @Test
    void testEqualsNeitherEquals() {
        GaussianInteger a = new GaussianInteger(new BigInteger("217"), new BigInteger("27"));
        GaussianInteger b = new GaussianInteger(new BigInteger("42"), new BigInteger("97"));

        assertFalse(a.equals(b));
    }

    @Test
    void testIsSymmetricallyEqualToTrue() {
        GaussianInteger a = new GaussianInteger(new BigInteger("54"), new BigInteger("65"));
        GaussianInteger b = new GaussianInteger(new BigInteger("65"), new BigInteger("54"));

        assertTrue(a.isSymmetricallyEqualTo(b));
    }

    @Test
    void testIsSymmetricallyEqualToOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("54"), new BigInteger("65"));
        GaussianInteger b = new GaussianInteger(new BigInteger("65"), new BigInteger("9"));

        assertFalse(a.isSymmetricallyEqualTo(b));
    }

    @Test
    void testIsSymmetricallyEqualToTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("54"), new BigInteger("65"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("54"));

        assertFalse(a.isSymmetricallyEqualTo(b));
    }

    @Test
    void testIsSymmetricallyEqualToThree() {
        GaussianInteger a = new GaussianInteger(new BigInteger("48"), new BigInteger("65"));
        GaussianInteger b = new GaussianInteger(new BigInteger("73"), new BigInteger("54"));

        assertFalse(a.isSymmetricallyEqualTo(b));
    }

    @Test
    void testAbsoluteOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("4"));

        BigInteger expected = new BigInteger("5");

        assertEquals(expected, a.absolute());
    }

    @Test
    void testAbsoluteTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("24652354"), new BigInteger("68435139"));

        BigInteger expected = new BigInteger("72739994");

        assertEquals(expected, a.absolute());
    }

    @Test
    void testAbsoluteSymmetrical() {
        GaussianInteger a = new GaussianInteger(new BigInteger("68435139"), new BigInteger("24652354"));

        BigInteger expected = new BigInteger("72739994");

        assertEquals(expected, a.absolute());
    }

    @Test
    void testIsMultipleBIsZero() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(ZERO, ZERO);

        assertFalse(a.isMultiple(b));
    }

    @Test
    void isMultiple(){
        GaussianInteger a = new GaussianInteger(new BigInteger("2"), new BigInteger("2"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("4"));

        assertTrue(b.isMultiple(a));
    }
    @Test
    void isNotMultiple() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("1"));

        assertFalse(a.isMultiple(b));
    }

    @Test
    void testDivideOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("2"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("2"), new BigInteger("0"));

        assertTrue(expected.equals(a.divide(b)));
    }

    @Test
    void testDivideTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8564"), new BigInteger("6854"));
        GaussianInteger b = new GaussianInteger(new BigInteger("7712"), new BigInteger("4646"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("1"), new BigInteger("0"));

        assertTrue(expected.equals(a.divide(b)));
    }

    @Test
    void testDivideThree() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("1"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));

        assertTrue(expected.equals(a.divide(b)));
    }

    @Test
    void testMultiplyOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("1"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));

        assertTrue(expected.equals(a.multiply(b)));
    }

    @Test
    void testMultiplyTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        assertTrue(expected.equals(a.multiply(b)));
    }

    @Test
    void testMultiplyThree() {
        GaussianInteger a = new GaussianInteger(new BigInteger("8"), new BigInteger("6"));
        GaussianInteger b = new GaussianInteger(new BigInteger("0"), new BigInteger("1"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("-6"), new BigInteger("8"));

        assertTrue(expected.equals(a.multiply(b)));
    }

    @Test
    void testMultiplyFour() {
        GaussianInteger a = new GaussianInteger(new BigInteger("87486"), new BigInteger("34566"));
        GaussianInteger b = new GaussianInteger(new BigInteger("98962"), new BigInteger("588464"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("-11683057092"), new BigInteger("54903081996"));

        assertTrue(expected.equals(a.multiply(b)));
    }

    @Test
    void testSubstractOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("87486"), new BigInteger("34566"));
        GaussianInteger b = new GaussianInteger(new BigInteger("98962"), new BigInteger("588464"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("-11476"), new BigInteger("-553898"));

        assertTrue(expected.equals(a.subtract(b)));
    }

    @Test
    void testSubstractTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("87486"), new BigInteger("34566"));
        GaussianInteger b = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("87486"), new BigInteger("34566"));

        assertTrue(expected.equals(a.subtract(b)));
    }

    @Test
    void testSubstractThree() {
        GaussianInteger a = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));
        GaussianInteger b = new GaussianInteger(new BigInteger("87486"), new BigInteger("34566"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("-87486"), new BigInteger("-34566"));

        assertTrue(expected.equals(a.subtract(b)));
    }

    @Test
    void testSubstractFour() {
        GaussianInteger a = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));
        GaussianInteger b = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        assertTrue(expected.equals(a.subtract(b)));
    }

    @Test
    void testAddOne() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("4"), new BigInteger("1"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("7"), new BigInteger("6"));

        assertTrue(expected.equals(a.add(b)));
    }

    @Test
    void testAddTwo() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("0"), new BigInteger("0"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));

        assertTrue(expected.equals(a.add(b)));
    }

    @Test
    void testAddThree() {
        GaussianInteger a = new GaussianInteger(new BigInteger("3"), new BigInteger("5"));
        GaussianInteger b = new GaussianInteger(new BigInteger("-4"), new BigInteger("-1"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("-1"), new BigInteger("4"));

        assertTrue(expected.equals(a.add(b)));
    }

    @Test
    void testAddFour() {
        GaussianInteger a = new GaussianInteger(new BigInteger("354584836"), new BigInteger("5364681"));
        GaussianInteger b = new GaussianInteger(new BigInteger("654516845"), new BigInteger("65484511"));

        GaussianInteger expected = new GaussianInteger(new BigInteger("1009101681"), new BigInteger("70849192"));

        assertTrue(expected.equals(a.add(b)));
    }

    @Test
    void testNormalizeGCD() {
        GaussianInteger input = new GaussianInteger(ZERO, ZERO);

        GaussianInteger expected = new GaussianInteger(ZERO, ZERO);

        assertTrue(expected.equals(input.normalizeGCD()));
    }

    @Test
    public void testNormalizeGCD_WithZeroRealPart() {
        GaussianInteger input = new GaussianInteger(ZERO, Resource.FIVE);

        GaussianInteger expected = new GaussianInteger(new BigInteger("5"), ZERO);

        assertTrue(expected.equals(input.normalizeGCD()));
    }

    @Test
    public void testNormalizeGCD_WithZeroImaginaryPart() {
        GaussianInteger input = new GaussianInteger(Resource.FIVE, ZERO);

        GaussianInteger expected = new GaussianInteger(new BigInteger("5"), ZERO);

        assertTrue(expected.equals(input.normalizeGCD()));
    }

    @Test
    public void testNormalizeGCD_Zero() {
        GaussianInteger input = new GaussianInteger(ZERO, ZERO);

        GaussianInteger expected = new GaussianInteger(ZERO, ZERO);

        assertTrue(expected.equals(input.normalizeGCD()));
    }

    @Test
    public void testToString() {
        GaussianInteger input = new GaussianInteger(BigInteger.valueOf(4), BigInteger.valueOf(2));

        String expected = "4 + 2i";

        assertEquals(expected, input.toString());
    }
}
