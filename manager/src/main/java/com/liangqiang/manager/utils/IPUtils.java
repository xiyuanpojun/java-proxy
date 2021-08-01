package com.liangqiang.manager.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IPUtils {

    public static final String LOCAL_HOST = "127.0.0.1";

    private static final String INVALID_IP = "0:0:0:0:0:0:0:1";

    public static String getIpAddress(HttpServletRequest request) {
        // Nginx的反向代理标志
        String ip = request.getHeader(HttpHeaderConst.X_FORWARDED_FOR);
        if (isValid(ip)) {
            // Apache的反向代理标志
            ip = request.getHeader(HttpHeaderConst.PROXY_CLIENT_IP);
        }
        if (isValid(ip)) {
            // WebLogic的反向代理标志
            ip = request.getHeader(HttpHeaderConst.WL_PROXY_CLIENT_IP);
        }
        if (isValid(ip)) {
            // 较少出现
            ip = request.getHeader(HttpHeaderConst.HTTP_CLIENT_IP);
        }
        if (isValid(ip)) {
            ip = request.getHeader(HttpHeaderConst.HTTP_X_FORWARDED_FOR);
        }
        if (isValid(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_HOST.equals(ip) || INVALID_IP.equals(ip)) {
                ip = getLocalHost();
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }

    public static String getLocalHost() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static String getPublicIp() {
        // 这里使用jsonip.com第三方接口获取本地IP
        String jsonip = "https://jsonip.com/";
        // 接口返回结果
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            // 使用HttpURLConnection网络请求第三方接口
            URL url = new URL(jsonip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        // 正则表达式，提取xxx.xxx.xxx.xxx，将IP地址从接口返回结果中提取出来
        String rexp = "(\\d{1,3}\\.){3}\\d{1,3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(result.toString());
        String res = "";
        while (mat.find()) {
            res = mat.group();
            break;
        }
        return res;
    }

    private static boolean isValid(String ip) {
        return StringUtils.isEmpty(ip) || HttpHeaderConst.UNKNOWN.equalsIgnoreCase(ip);
    }

    public interface HttpHeaderConst {

        String X_FORWARDED_FOR = "x-forwarded-for";

        String PROXY_CLIENT_IP = "Proxy-Client-IP";

        String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

        String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

        String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

        String X_REAL_IP = "x_real_ip";

        String JSON = "json";

        String UNKNOWN = "unknown";

        interface Cookie {

            String TRAVEL_AGGRE_COOKIE = "TRAVEL_AGGRE_COOKIE";

            String TRAVEL_AGGRE_TOKEN = "TRAVEL_AGGRE_TOKEN";

            String TRAVEL_AGGRE_SESSIONID = "TRAVEL_AGGRE_SESSIONID";

            String TRAVEL_AGGRE_SALE_COOKIE = "TRAVEL_AGGRE_SALE_COOKIE";

            String TRAVEL_AGGRE_SALE_TOKEN = "TRAVEL_AGGRE_SALE_TOKEN";

            String TRAVEL_AGGRE_SALE_SESSIONID = "TRAVEL_AGGRE_SALE_SESSIONID";

            String TRAVEL_AGGRE_SAAS_COOKIE = "TRAVEL_AGGRE_SAASE_COOKIE";

            String TRAVEL_AGGRE_SAAS_TOKEN = "TRAVEL_AGGRE_SAAS_TOKEN";

            String TRAVEL_AGGRE_SAAS_SESSIONID = "TRAVEL_AGGRE_SAAS_SESSIONID";
        }

        interface Authentication {

            String TIMESTAMP = "timestamp";

            String NONCE = "nonce";

            String ACCESS_KEY = "accessKey";

            String SIGN = "sign";

            String SOURCE = "source";

            String NEED_VERIFY_TOKEN = "needVerifyToken";
        }
    }
}
