package com.syuez.springsecurity.service;

import com.syuez.springsecurity.entity.Authority;
import com.syuez.springsecurity.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
/*
* UserDetailsService 是 Security 提供的用于封装认证用户信息的接口，该接口提供的
* loadUserByUsername(String s) 方法用于通过用户名加载用户信息。使用 UserDetailsService 进
* 行身份认证时，自定义一个 UserDetailsService 接口的实现类，通过 loadUserByUsername(String s)
* 方法封装用户详情信息并返回 UserDetails 对象供 Security 认证使用
* */

/**
 * 自定义一个 UserDetailsService 接口实现类进行用户认证信息封装
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private CustomerService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 通过业务方法获取用户及权限
        Customer customer = service.getCustomer(username);
        List<Authority> authorities = service.getCustomerAuthority(username);

        // 对用户权限进行封装
        List<SimpleGrantedAuthority> list = authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
        // 返回封装的 UserDetails 用户详情类
        if(customer != null) {
            return new User(customer.getUsername(), customer.getPassword(), list);
        } else {
            // 如果查询的用户不存在（用户名不存在），必须抛出异常
            throw new UsernameNotFoundException("当前用户不存在!");
        }
    }
}
