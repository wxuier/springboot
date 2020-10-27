package com.libra.mimipay.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.logging.Logger;

public class DeviceUUID {
    private static final String UUID_FILE_NAME = "mimipay_uuid.txt";
    private static final Logger logger = Logger.getLogger(DeviceUUID.class.getName());
    private static String uuid = null;

    public static String getUUID(Context context) {
        if (uuid == null) {
            uuid = SdcardFileUtils.read(UUID_FILE_NAME);
            if (uuid == null) {
                StringBuffer s = new StringBuffer();
                s.append("uuid");
                try {
                    s.append(getMac(context));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uuid = getMD5(s.toString(), false);
                SdcardFileUtils.write(UUID_FILE_NAME, uuid);
            }
        }
        return uuid;
    }

    public static String getMac(Context context) {
        if (VERSION.SDK_INT < 23) {
            return getLocalMacAddressFromWifiInfo(context);
        }
        if (VERSION.SDK_INT < 24 && VERSION.SDK_INT >= 23) {
            return getMacAddress(context);
        }
        if (VERSION.SDK_INT < 24) {
            return "02:00:00:00:00:00";
        }
        if (!TextUtils.isEmpty(getMacAddress())) {
            return getMacAddress();
        }
        if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
            return getMachineHardwareAddress();
        }
        return getLocalMacAddressFromBusybox();
    }

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public static String getMacAddress(Context context) {
        if (VERSION.SDK_INT < 23) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }
        String str = "";
        String macSerial = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address").getInputStream()));
            while (true) {
                if (str == null) {
                    break;
                }
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->NetInfoManager", "getMacAddress:" + ex.toString());
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("----->NetInfoManager", "getMacAddress:" + e.toString());
            }
        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            try {
                return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (Exception e) {
                Log.e("----->NetInfoManager", "getMacAddress0:" + e.toString());
            }
        }
        return "";
    }

    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Log.e("----->NetInfoManager", "isAccessWifiStateAuthorized:access wifi state is enabled");
        return true;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    public static String getMacAddress() {
        try {
            byte[] b = NetworkInterface.getByInetAddress(getLocalInetAddress()).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 255);
                if (str.length() == 1) {
                    str = 0 + str;
                }
                buffer.append(str);
            }
            return buffer.toString().toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                Enumeration<InetAddress> en_ip = ((NetworkInterface) en_netInterface.nextElement()).getInetAddresses();
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress) en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        break;
                    }
                    ip = null;
                }
                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    private static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (true) {
                    if (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 121 */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
        if (r1 == null) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getMachineHardwareAddress() {
        /*
            r3 = 0
            java.util.Enumeration r3 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch:{ SocketException -> 0x000b }
        L_0x0005:
            r1 = 0
            r2 = 0
            if (r3 != 0) goto L_0x0014
            r4 = 0
        L_0x000a:
            return r4
        L_0x000b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0005
        L_0x0010:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0014:
            boolean r4 = r3.hasMoreElements()
            if (r4 == 0) goto L_0x002a
            java.lang.Object r2 = r3.nextElement()
            java.net.NetworkInterface r2 = (java.net.NetworkInterface) r2
            byte[] r4 = r2.getHardwareAddress()     // Catch:{ SocketException -> 0x0010 }
            java.lang.String r1 = bytesToString(r4)     // Catch:{ SocketException -> 0x0010 }
            if (r1 == 0) goto L_0x0014
        L_0x002a:
            r4 = r1
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.libra.mimipay.common.DeviceUUID.getMachineHardwareAddress():java.lang.String");
    }

    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", new Object[]{Byte.valueOf(b)}));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    public static String getLocalMacAddressFromBusybox() {
        String str = "";
        String str2 = "";
        String result = callCmd("busybox ifconfig", "HWaddr");
        if (result == null) {
            return "网络异常";
        }
        if (result.length() <= 0 || !result.contains("HWaddr")) {
            return result;
        }
        return result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
    }

    private static String callCmd(String cmd, String filter) {
        String line;
        String result = "";
        String str = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
            while (true) {
                line = br.readLine();
                if (line != null && !line.contains(filter)) {
                    result = result + line;
                }
                else{
                    break;
                }
            }
            return line;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            return bytesToHex(MessageDigest.getInstance("MD5").digest(message.getBytes()), upperCase);
        } catch (Exception e) {
            e.printStackTrace();
            return md5str;
        }
    }

    public static String bytesToHex(byte[] bArray, boolean upperCase) {
        StringBuffer sb = new StringBuffer(bArray.length);
        for (byte b : bArray) {
            String sTemp = Integer.toHexString(b & 255);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            if (upperCase) {
                sb.append(sTemp.toUpperCase());
            } else {
                sb.append(sTemp);
            }
        }
        return sb.toString();
    }
}
