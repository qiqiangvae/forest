package org.nature.forest.common.utils.codec;

import org.junit.Test;

import static org.junit.Assert.*;

public class Base64UtilsTest {

    @Test
    public void encode() {
        System.out.println(Base64Utils.encode("forest"));
    }

    @Test
    public void decode() {
        System.out.println(Base64Utils.decode("Zm9yZXN0"));
    }
}