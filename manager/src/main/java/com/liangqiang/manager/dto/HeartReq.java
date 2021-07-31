package com.liangqiang.manager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("心跳请求模型")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartReq {

    @ApiModelProperty("本机通信地址")
    @NotBlank
    private String host;

    @ApiModelProperty("本机通信端口")
    @NotNull
    private Integer port;

    @ApiModelProperty("本机代理地址")
    @NotBlank
    private String proxyHost;

    @ApiModelProperty("本机代理端口")
    @NotNull
    private Integer proxyPort;
}
