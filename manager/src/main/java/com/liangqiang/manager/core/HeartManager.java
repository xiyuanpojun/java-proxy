package com.liangqiang.manager.core;

import com.liangqiang.manager.dto.DialReq;
import com.liangqiang.manager.dto.HeartReq;
import com.liangqiang.manager.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class HeartManager {
    private final static ConcurrentHashMap<String, HeartReq> HEART_MAP = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Long> HEART_TIME_MAP = new ConcurrentHashMap<>();

    static {
        new Thread(() -> {
            while (true) {
                try {
                    Set<Map.Entry<String, Long>> entries = HEART_TIME_MAP.entrySet();
                    Iterator<Map.Entry<String, Long>> iterator = entries.stream().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Long> entry = iterator.next();
                        String key = entry.getKey();
                        Long value = entry.getValue();
                        if (System.currentTimeMillis() - value >= 1000L * 60) {
                            HeartReq req = HEART_MAP.remove(key);
                            log.info("移除无效代理 {}", req.toString());
                            HEART_TIME_MAP.remove(key);
                        }
                    }
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }).start();
    }

    public void heart(HeartReq req) {
        log.info("heart {}", req);
        String key = buildKey(req.getProxyHost(), req.getProxyPort());
        HEART_MAP.put(key, req);
        HEART_TIME_MAP.put(key, System.currentTimeMillis());
    }

    public String dial(DialReq req) {
        log.info("dial {}", req);
        String key = buildKey(req.getProxyHost(), req.getProxyPort());
        HeartReq heartReq = HEART_MAP.get(key);
        if (heartReq == null) {
            return "代理服务器不存在";
        }
        String url = String.format("http://%s:%s/api/v1/dial", heartReq.getHost(), heartReq.getPort());
        return RestUtils.get(url, String.class);
    }

    public List<String> getAll() {
        return new ArrayList<>(HEART_MAP.keySet());
    }

    private String buildKey(String proxyHost, Integer proxyPort) {
        return proxyHost + ":" + proxyPort;
    }
}
