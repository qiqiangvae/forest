package online.qiqiang.forest.common.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 操作系统工具类
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class OsUtils {

    private static final String OS_NAME_CONSTANT = "os.name";
    private static final String LINE_SEPARATOR_CONSTANT = "line.separator";
    private static final String FILE_SEPARATOR_CONSTANT = "file.separator";
    /**
     * user's current working directory
     */
    private static final String CURRENT_PATH = "user.dir";

    private static final String OS_WINDOWS = "WINDOWS";
    private static final String OS_LINUX = "LINUX";
    private static final String OS_MAC = "MAC OS";
    private static String localIP = null;
    private static final String COLON = ":";


    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }


    /**
     * get current process's ID
     *
     * @return get current process's ID
     */
    public static int getCurrentProcessId() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMxBean.getName().split("@")[0]);
    }

    public static String getCurrentPath() {
        return OsUtils.getProperty(OsUtils.CURRENT_PATH);
    }

    public static String getStandardPath() {
        File directory = new File("");
        try {
            return directory.getCanonicalPath();
        } catch (Exception ignored) {
        }

        return null;
    }

    public static String getAbsolutionPath() {
        File directory = new File("");
        try {
            return directory.getAbsolutePath();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * get system's properties
     */
    private static final Properties PROPERTIES = System.getProperties();

    /**
     * get OS name
     */
    public static final String OS_NAME = OsUtils.getProperty(OS_NAME_CONSTANT);

    private static final String LINE_SEPARATOR = OsUtils.getProperty(LINE_SEPARATOR_CONSTANT);

    private static final String FILE_SEPARATOR = OsUtils.getProperty(FILE_SEPARATOR_CONSTANT);

    private static String getProperty(final String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }

    public static String getSystemOsName() {
        return OsUtils.getProperty(OsUtils.OS_NAME_CONSTANT);
    }

    public static String getHostName() throws SocketException, UnknownHostException {
        String osName = OsUtils.getSystemOsName();

        try {
            String osNameTmp = osName.toUpperCase();
            InetAddress iNet;

            if (osNameTmp.startsWith(OsUtils.OS_WINDOWS)) {
                iNet = OsUtils.getWindowsLocalAddress();
            } else if (osNameTmp.startsWith(OsUtils.OS_LINUX)) {
                iNet = OsUtils.getUnixLocalAddress();
            } else if (osNameTmp.startsWith(OsUtils.OS_MAC)) {
                iNet = OsUtils.getMacLocalAddress();
            } else {
                iNet = OsUtils.getUnixLocalAddress();
            }
            if (iNet == null) {
                throw new UnknownHostException("local IP is unknown!");
            }

            return iNet.getHostName();
        } catch (SocketException e) {
            throw new SocketException("error to get host IP " + e.getMessage());
        } catch (UnknownHostException e) {
            throw new UnknownHostException("unknown error when get host IP");
        }
    }


    /**
     * get local IP in the format of String
     */
    public static String getStringIp() {
        if (null != localIP) {
            return localIP;
        }
        try {
            String hostname = getHostName();
            localIP = InetAddress.getByName(hostname).getHostAddress();
        } catch (Exception ignore) {
        }
        return localIP;
    }

    /**
     * get local IP in the format of Long
     *
     * @return long
     */
    public static long getLongIp() {
        String stringIp = OsUtils.getStringIp();
        if (stringIp == null || stringIp.length() == 0) {
            return -1;
        }
        byte[] intIp;
        if (stringIp.contains(COLON)) {
            intIp = OsUtils.ipv62Int(stringIp);
        } else {
            intIp = OsUtils.ipv42Int(stringIp);
        }
        BigInteger bigInteger = new BigInteger(intIp);
        return bigInteger.longValue();
    }

    private static InetAddress getWindowsLocalAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    private static InetAddress getUnixLocalAddress() throws SocketException {
        InetAddress iNet;
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
                        && inetAddress.isSiteLocalAddress()) {
                    iNet = inetAddress;
                    return iNet;
                }
            }
        }

        return null;
    }

    private static InetAddress getMacLocalAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (Exception ignored) {
        }
        return null;
    }

    private static byte[] ipv62Int(final String stringIp) {
        byte[] ret = new byte[17];
        ret[0] = 0x00;
        int ib = 16;
        //ipv4 composited flag
        boolean compositedFlag = false;
        String tmpStringIp = stringIp;

        //erase the ":"
        if (tmpStringIp.startsWith(COLON)) {
            tmpStringIp = tmpStringIp.substring(1);
        }

        String[] groups = tmpStringIp.split(COLON);
        for (int ig = groups.length - 1; ig > -1; ig--) {
            if (groups[ig].contains(".")) {
                ret = OsUtils.ipv42Int(groups[ig]);
                compositedFlag = true;
            } else if ("".equals(groups[ig])) {
                //出现零长度压缩，计算缺少的数组
                int zlg = 9 - (groups.length + (compositedFlag ? 1 : 0));
                while (zlg-- > 0) {
                    ret[ib--] = 0;
                    ret[ib--] = 0;
                }
            } else {
                int temp = Integer.parseInt(groups[ig], 16);
                ret[ib--] = (byte) temp;
                ret[ib--] = (byte) (temp >> 8);
            }
        }

        return ret;
    }

    private static byte[] ipv42Int(final String stringIp) {
        //find the position of "." in the IP string
        int pos1 = stringIp.indexOf(".");
        int pos2 = stringIp.indexOf(".", pos1 + 1);
        int pos3 = stringIp.indexOf(".", pos2 + 1);

        byte[] ret = new byte[5];
        ret[0] = 0;
        ret[1] = (byte) Integer.parseInt(stringIp.substring(0, pos1));
        ret[2] = (byte) Integer.parseInt(stringIp.substring(pos1 + 1, pos2));
        ret[3] = (byte) Integer.parseInt(stringIp.substring(pos2 + 1, pos3));
        ret[4] = (byte) Integer.parseInt(stringIp.substring(pos3 + 1));

        return ret;
    }
}
