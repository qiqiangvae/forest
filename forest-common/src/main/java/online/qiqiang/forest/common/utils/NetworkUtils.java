package online.qiqiang.forest.common.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author qiqiang
 */
public class NetworkUtils {
    public static boolean available(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            return socket.isConnected();
        } catch (IOException ignored) {

        }
        return false;
    }

    /**
     * 解析带协议的地址
     *
     * @param address 如：<a href="http://127.0.0.1:8080">http://127.0.0.1:8080</a>
     * @return available
     */
    public static boolean available(String address) {
        try {
            URI uri = new URI(address);
            int port = uri.getPort();
            if (port == -1) {
                port = 80;
            }
            return available(uri.getHost(), port);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
