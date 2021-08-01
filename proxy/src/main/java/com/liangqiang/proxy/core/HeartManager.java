package com.liangqiang.proxy.core;

import com.liangqiang.proxy.config.CusheartConfig;
import com.liangqiang.proxy.dto.HeartReq;
import com.liangqiang.proxy.utils.IPUtils;
import com.liangqiang.proxy.utils.RestUtils;
import com.liangqiang.proxy.utils.SpringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class HeartManager {
    private final CusheartConfig cusheartConfig;

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    log.info("...heart s...");
                    process();
                    log.info("...heart e...");
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }).start();
    }

    private void process() {
        HeartReq req = HeartReq.builder()
                .host(IPUtils.getLocalHost())
                .port(SpringUtils.getRunTimePort())
                .proxyHost(IPUtils.getLocalHost())
                .proxyPort(SpringUtils.getProperty("cusproxy.port", Integer.class))
                .build();
        String post = RestUtils.post(cusheartConfig.getUrl(), String.class, req, null);
        log.info("heart {} {}", req.toString(), post);
    }
}
