package online.qiqiang.forest.common.utils.codec;

import online.qiqiang.forest.common.exception.ForestCodecException;
import online.qiqiang.forest.common.utils.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 工具类
 * todo 待实现
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Md5Utils {
    private static final String MD5 = "MD5";


    public static String md5(byte[] data) {
        try {
            return HexUtils.encodeHex2String(MessageDigest.getInstance(MD5).digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new ForestCodecException(e);
        }
    }

    public static String md5(String text) {
        return md5(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String md5(File file) {
        return md5(file, 0, -1);
    }

    public static String md5(File file, int skip, int size) {
        try {
            FileInputStream fis = new FileInputStream(file);
            if (size < 0) {
                long skipped = fis.skip(skip);
                if (skipped < skip) {
                    throw new IOException("Skipped only " + skipped + " bytes out of " + skip + " required");
                }
                return md5(IoUtils.copyToByteArray(fis));
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream(size);
                IoUtils.copyRange(fis, out, skip, skip + size);
                return md5(out.toByteArray());
            }
        } catch (IOException e) {
            throw new ForestCodecException(e);
        }
    }
}
