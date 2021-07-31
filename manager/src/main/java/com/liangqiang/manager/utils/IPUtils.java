package com.liangqiang.manager.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        String ip = null;
        // 根据网卡取本机配置的IP
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            if (log.isDebugEnabled()) {
                log.debug("getLocalHost failed!", e);
            }
        }

        return ip;
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