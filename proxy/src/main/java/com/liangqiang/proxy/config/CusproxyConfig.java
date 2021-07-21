package com.liangqiang.proxy.config;

import com.github.monkeywie.proxyee.proxy.ProxyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "cusproxy")
public class CusproxyConfig {
    private Integer port;
    private ProxyType proxyType;
    private String proxyHost;
    private Integer proxyPort;
    private ProxyTypeEnum type;
    private String user;
    private String password;

    public enum ProxyTypeEnum {
        GENERAL("GENERAL", "普通代理"),
        MIDDLEMAN("MIDDLEMAN", "中间人代理");

        private String code;
        private String msg;

        ProxyTypeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
