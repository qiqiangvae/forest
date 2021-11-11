package online.qiqiang.forest.common.utils.codec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64 工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Base64Utils {

    private static Charset charset = StandardCharsets.UTF_8;

    public static void setCharset(Charset charset) {
        Base64Utils.charset = charset;
    }

    public static byte[] encode2bytes(byte[] data) {
        return Base64.getEncoder().encode(data);
    }

    public static byte[] encode2bytes(String text) {
        return encode2bytes(text.getBytes(charset));
    }

    public static String encode(byte[] data) {
        return new String(encode2bytes(data), charset);
    }

    public static String encode(String text) {
        return new String(encode2bytes(text), charset);
    }

    public static byte[] decode2bytes(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

    public static byte[] decode2bytes(String text) {
        return decode2bytes(text.getBytes(charset));
    }

    public static String decode(byte[] data) {
        return new String(decode2bytes(data), charset);
    }

    public static String decode(String text) {
        return new String(decode2bytes(text), charset);
    }
}
