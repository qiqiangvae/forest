package org.qiqiang.forest.common.utils.codec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64 工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Base64Utils {


    public static byte[] encode2bytes(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static byte[] encode2bytes(String text) {
        return encode2bytes(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        return new String(encode2bytes(data), StandardCharsets.UTF_8);
    }

    public static String encode(String text) {
        return new String(encode2bytes(text), StandardCharsets.UTF_8);
    }

    public static byte[] decode2bytes(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static byte[] decode2bytes(String text) {
        return decode2bytes(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(byte[] data) {
        return new String(decode2bytes(data), StandardCharsets.UTF_8);
    }

    public static String decode(String text) {
        return new String(decode2bytes(text), StandardCharsets.UTF_8);
    }
}
