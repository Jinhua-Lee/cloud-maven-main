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

    /**
     * 解析URI中/分隔的第一个字符串，作为访问的服务名
     * @param uri URI
     * @return 服务名
     */
    public static String getUriServiceName(String uri) {
        if (uri == null || uri.isEmpty()) {
            return null;
        }
        int first = uri.indexOf("/");
        int last = uri.lastIndexOf("/");

        // 能解析出serviceName的uri
        // 1. 必须以 / 开头
        // 2. / 数量必须多于1个
        if (first != 0 || first == last) {
            return null;
        }
        // 3. service长度不为0

        int second = uri.substring(1).indexOf("/") + 1;
        if (second == 0) {
            return null;
        }
        String serviceName = uri.substring(1, second);
        if (serviceName.trim().isEmpty()) {
            return null;
        }
        return serviceName;
    }
}
