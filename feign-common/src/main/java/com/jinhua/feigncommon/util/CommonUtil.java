package com.jinhua.feigncommon.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p> 工具类
 * @author ZX
 * @date 2020/4/14 14:44
 */
public class CommonUtil {

    private static final Integer IP_MIN_LENGTH = 7;

    private static final Integer IP_MAX_LENGTH = 15;

    public static boolean isIpAddress(String addr) {
        if(addr.length() < IP_MIN_LENGTH || addr.length() > IP_MAX_LENGTH) {
            return false;
        }
        String regex = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }

    /**
     * 判断是否是形如 http://ip:port 这种格式的url。
     * @param url url
     * @return 是否校验通过
     */
    public static boolean isUrl(String url) {
        try {
            if (url.length() < 16 || url.length() > 28) {
                return false;
            }
            String prefix = url.substring(0, 7);
            if (!"http://".equals(prefix)) {
                return false;
            }
            String suffix = url.substring(7);
            int index = suffix.indexOf(":");
            if (index <= 0) {
                return false;
            }
            String ip = suffix.substring(0, index);
            if (!isIpAddress(ip)){
                return false;
            }
            String portString = suffix.substring(index + 1);
            int port = Integer.parseInt(portString);
            if (port > 65535 || port < 1024){
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
