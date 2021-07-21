package com.liangqiang.proxy.api;

import com.liangqiang.proxy.core.DialManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("api")
public class ApiController {
    private final DialManager dialManager;

    @GetMapping("v1/dial")
    public String dial() {
        dialManager.dial();
        return "OK";
    }
}
