package com.syuez.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CSRFController {
    /**
     * 向用户修改页跳转
     */
    @GetMapping("/toUpdate")
    public String toUpdate() {
        return "csrf/csrfTest";
    }

    /**
     * 用户修改提交处理
     */
    @ResponseBody
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        System.out.println(username);
        System.out.println(password);
        String csrfToken = request.getParameter("_csrf");
        System.out.println(csrfToken);
        return "OK";
    }
}
