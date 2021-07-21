package com.liangqiang.manager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DialReq {
    private String proxyHost;
    private Integer proxyPort;

    public DialReq() {
    }

    public DialReq(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
}
