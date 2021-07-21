package com.liangqiang.proxy.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HeartReq {
    private String host;
    private Integer port;
    private String proxyHost;
    private Integer proxyPort;
}
