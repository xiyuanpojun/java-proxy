package com.liangqiang.proxy.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class DialManager {
    public void dial() {
        log.info("开始拨号");
        process();
        log.info("拨号完成");
    }

    private void process() {

    }
}
