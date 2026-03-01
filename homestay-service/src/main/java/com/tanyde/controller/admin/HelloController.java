package com.tanyde.controller.admin;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tanyde.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @GetMapping
    @SaCheckPermission("employee:query")
    public Result<String> hello(){
        return Result.success("hello");
    }
}
