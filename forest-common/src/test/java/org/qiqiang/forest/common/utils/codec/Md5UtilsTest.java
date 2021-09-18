package org.nature.forest.common.utils.codec;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class Md5UtilsTest {

    @Test
    public void md5Test() {
        System.out.println(Md5Utils.md5("forest"));
    }

    @Test
    public void fileMd5Test() {
        File file = new File(System.getProperty("user.dir") + "/pom.xml");
        System.out.println(Md5Utils.md5(file));
        System.out.println(Md5Utils.md5(file, 10, 100000));
    }
}