package com.liangqiang.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartReq {
    private String host;
    private Integer port;
    private String proxyHost;
    private Integer proxyPort;
}
