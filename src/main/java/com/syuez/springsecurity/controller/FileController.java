package com.syuez.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {
    /**
     * 影片详情页
     */
    @GetMapping("/detail/{type}/{path}")
    public String toDetail(@PathVariable("type") String type, @PathVariable("path") String path) {
        return "detail/" + type + "/" + path;
    }

    /**
     * 跳转到登录页面
     * Spring Security 默认采用 Get 方式的 "/login" 请求用于向登录页面跳转
     * 使用 Post 方式的 "/login" 请求用于对登录后的数据处理
     */
    @GetMapping("/userLogin")
    public String toLoginPage() {
        return "login/login";
    }
}
