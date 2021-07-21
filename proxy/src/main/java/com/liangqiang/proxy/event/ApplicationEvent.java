package com.liangqiang.proxy.event;

import com.liangqiang.proxy.core.HeartManager;
import com.liangqiang.proxy.core.HttpProxyServerManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ApplicationEvent implements ApplicationRunner {
    private final HttpProxyServerManager httpProxyServerManager;
    private final HeartManager heartManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("代理服务器正在启动");
        httpProxyServerManager.start();
        log.info("代理服务器启动完成");

        log.info("心跳服务正在启动");
        heartManager.start();
        log.info("心跳服务启动完成");
    }
}
