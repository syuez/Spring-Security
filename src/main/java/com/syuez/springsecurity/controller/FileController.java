package com.syuez.springsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

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

    /**
     * 使用 HttpSession 获取用户信息
     */
    @GetMapping("/getUserBySession")
    @ResponseBody
    public void getUser(HttpSession session) {
        // 从当前 HttpSession 获取绑定到此会话的所有对象的名称
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()){
            // 获取HttpSession中会话名称
            String element = names.nextElement();
            // 获取HttpSession中的应用上下文
            SecurityContextImpl attribute = (SecurityContextImpl) session.getAttribute(element);
            System.out.println("element: "+element);
            System.out.println("attribute: "+attribute);
            // 获取用户相关信息
            Authentication authentication = attribute.getAuthentication();
            UserDetails principal = (UserDetails)authentication.getPrincipal();
            System.out.println(principal);
            System.out.println("username: "+principal.getUsername());
        }
    }

    /**
     * 使用 SecurityContextHolder 获取用户信息
     */
    @GetMapping("/getUserByContext")
    @ResponseBody
    public void getUser2() {
        // 获取应用上下文
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println("userDetails: " + context);
        // 获取用户相关信息
        Authentication authentication = context.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        System.out.println(principal);
        System.out.println("username: "+principal.getUsername());
    }
}
