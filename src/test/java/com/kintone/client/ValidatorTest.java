package com.kintone.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ValidatorTest {

    @Test
    public void testCheckContentType() {
        assertThatCode(() -> Validator.checkContentType("application/octet-stream"))
                .doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkContentType("text/plain")).doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkContentType("application/vnd.ms-powerpoint"))
                .doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkContentType("application/xslt+xml"))
                .doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkContentType("application/octet-stream; charset=utf-8"))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> Validator.checkContentType("text/xml\n"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkContentType("\r"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkContentType("\n"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkContentType("\t"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkContentType(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckFilename() {
        assertThatCode(() -> Validator.checkFilename("name")).doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkFilename("data.dat")).doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkFilename("s p a c e.txt")).doesNotThrowAnyException();
        assertThatCode(() -> Validator.checkFilename("マルチバイト文字.png")).doesNotThrowAnyException();

        assertThatThrownBy(() -> Validator.checkFilename("\nname"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename("name\n"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename("na\nme"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename("\n"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename("\r"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename("\b"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Validator.checkFilename(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
