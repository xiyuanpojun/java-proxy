package com.liangqiang.proxy.core;

import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptInitializer;
import com.github.monkeywie.proxyee.intercept.HttpProxyInterceptPipeline;
import com.github.monkeywie.proxyee.intercept.common.FullResponseIntercept;
import com.github.monkeywie.proxyee.proxy.ProxyConfig;
import com.github.monkeywie.proxyee.proxy.ProxyType;
import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;
import com.github.monkeywie.proxyee.server.auth.BasicHttpProxyAuthenticationProvider;
import com.github.monkeywie.proxyee.util.HttpUtil;
import com.liangqiang.proxy.config.CusproxyConfig;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

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
                boolean match = cusproxyConfig.getUser().equals(usr) && cusproxyConfig.getPassword().equals(pwd);
                log.info("{}-{}正在认证，认证结果{}。", usr, pwd, match);
                return match;
            }
        });

        HttpProxyServer httpProxyServer = new HttpProxyServer()
                .serverConfig(config);

        if (cusproxyConfig.getProxyType() != null) {
            httpProxyServer.proxyConfig(new ProxyConfig(cusproxyConfig.getProxyType(), cusproxyConfig.getProxyHost(), cusproxyConfig.getProxyPort()));
        }

        httpProxyServer.proxyInterceptInitializer(new HttpProxyInterceptInitializer() {
            @Override
            public void init(HttpProxyInterceptPipeline pipeline) {
                pipeline.addLast(new FullResponseIntercept() {

                    @Override
                    public boolean match(HttpRequest httpRequest, HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
                        return true;
                    }

                    @Override
                    public void handleResponse(HttpRequest httpRequest, FullHttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) {
                        log.info(httpResponse.toString());
                        log.info(httpResponse.content().toString(Charset.defaultCharset()));
                    }
                });
            }
        });

        httpProxyServer.start(cusproxyConfig.getPort());
    }
}
