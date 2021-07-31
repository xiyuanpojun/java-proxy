package com.liangqiang.manager.controller;

import com.liangqiang.manager.anno.ResubmitCheck;
import com.liangqiang.manager.core.HeartManager;
import com.liangqiang.manager.dto.DialReq;
import com.liangqiang.manager.dto.HeartReq;
import com.liangqiang.manager.utils.Result;
import com.liangqiang.manager.utils.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("对外暴露API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("api")
public class ApiController {
    private final HeartManager heartManager;

    @ResubmitCheck
    @ApiOperation("心跳API")
    @PostMapping("v1/heart")
    public String heart(@Validated @RequestBody HeartReq req) {
        heartManager.heart(req);
        return System.currentTimeMillis() + "";
    }

    @ResubmitCheck(expire = 10)
    @ApiOperation("对代理下发拨号指令")
    @GetMapping("v1/dial")
    public String dial(DialReq req) {
        return heartManager.dial(req);
    }

    @ApiOperation("获取系统内所有代理")
    @GetMapping("v1/get_all")
    public Result<List<String>> getAll() {
        return ResultCode.SUCCESS.bindResult(heartManager.getAll());
    }
}
