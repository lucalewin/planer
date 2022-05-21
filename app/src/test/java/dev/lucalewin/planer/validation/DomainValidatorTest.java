package dev.lucalewin.planer.validation;

import static org.junit.Assert.*;

import org.junit.Test;

public class DomainValidatorTest {

    @Test
    public void validatesURLStrings() {
        DomainValidator validator = new DomainValidator();

        assertTrue(validator.isValid("http://iserv.kgs-wiesmoor.de"));
        assertTrue(validator.isValid("https://iserv.kgs-wiesmoor.de"));
        assertTrue(validator.isValid("iserv.kgs-wiesmoor.de"));
        assertTrue(validator.isValid("google.com"));
        assertTrue(validator.isValid("www.google.com"));

        assertFalse(validator.isValid("ftp://google.com"));
        assertFalse(validator.isValid("google.com/iserv"));
    }

}