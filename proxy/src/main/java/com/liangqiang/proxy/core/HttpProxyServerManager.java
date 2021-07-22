package com.liangqiang.proxy.core;

import com.github.monkeywie.proxyee.proxy.ProxyConfig;
import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;
import com.github.monkeywie.proxyee.server.auth.BasicHttpProxyAuthenticationProvider;
import com.liangqiang.proxy.config.CusproxyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class HttpProxyServerManager {
    private static final Object startLock = new Object();
    private static Boolean started = Boolean.FALSE;

    private final CusproxyConfig cusproxyConfig;

    public boolean isStarted() {
        return started;
    }

    public void start() {
        new Thread(() -> {
            synchronized (startLock) {
                if (!started) {
                    init();
                    started = Boolean.TRUE;
                }
            }
        }).start();
    }

    private void init() {
        // curl -i -x 127.0.0.1:9999 -U utest:upwd 'http://lumtest.com/myip.json'

        HttpProxyServerConfig config = new HttpProxyServerConfig();
        config.setAuthenticationProvider(new BasicHttpProxyAuthenticationProvider() {
            @Override
            protected boolean authenticate(String usr, String pwd) {
                return cusproxyConfig.getUser().equals(usr) && cusproxyConfig.getPassword().equals(pwd);
            }
        });

        HttpProxyServer httpProxyServer = new HttpProxyServer()
                .serverConfig(config);

        if (cusproxyConfig.getProxyType() != null) {
            httpProxyServer.proxyConfig(new ProxyConfig(cusproxyConfig.getProxyType(), cusproxyConfig.getProxyHost(), cusproxyConfig.getProxyPort()));
        }

        httpProxyServer.start(cusproxyConfig.getPort());
    }
}
