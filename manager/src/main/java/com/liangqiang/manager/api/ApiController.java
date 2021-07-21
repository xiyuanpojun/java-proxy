package com.liangqiang.manager.api;

import com.liangqiang.manager.core.HeartManager;
import com.liangqiang.manager.dto.DialReq;
import com.liangqiang.manager.dto.HeartReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("api")
public class ApiController {
    private final HeartManager heartManager;

    @PostMapping("v1/heart")
    public String heart(@RequestBody HeartReq req) {
        heartManager.heart(req);
        return "b";
    }

    @GetMapping("v1/dial")
    public String dial(DialReq req) {
        return heartManager.dial(req);
    }

    @GetMapping("v1/get_all")
    public Object getAll() {
        return heartManager.getAll();
    }
}
