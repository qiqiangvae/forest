package online.qiqiang.forest.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author qiqiang
 */
public class ShellUtils {
    public enum ShellType {
        bash, zsh
    }

    public static String execute(ShellType shellType, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        try {
            processBuilder.command(shellType.name(), "-c", command);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IoUtils.copy(inputStream, os);
            String result = new String(os.toByteArray(), StandardCharsets.UTF_8);
            System.out.println(result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
