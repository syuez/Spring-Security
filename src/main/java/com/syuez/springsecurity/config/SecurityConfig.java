package com.syuez.springsecurity.config;

import com.syuez.springsecurity.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig  {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    /*
    * 从 Spring Security 5 开始，自定义用户认证必须设置密码编码器用于保护密码，否则会报错。
    * Spring Security 提供了多种密码编码器，包括 BCryptPasswordEncoder、Pbkdf2PasswordEncoder、ScryptPasswordEncoder 等。
    * */
    @Bean(name = "bCryptPasswordEncoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    * UserDetailsService 身份认证
    * */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
    /*
    * 自定义用户访问控制
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 开启基于 HttpServletRequest 请求访问限制
        http.authorizeHttpRequests()
                // 开启 Ant 风格的路径匹配，并且对于 "/" 路径的请求无条件进行放行
                .antMatchers("/").permitAll()
                // hasRole 匹配用户是否有某一个角色，也可以用 hasAnyRole 来匹配多个角色
                .antMatchers("/detail/common/**").hasRole("common")
                .antMatchers("/detail/vip/**").hasRole("vip")
                // 匹配任何请求，并匹配已经登录认证的用户
                // 这里表示除了上述的请求外，其他请求则要求用户必须先进行登录认证
                .anyRequest().authenticated()
                // 功能连接符
                .and()
                // 开启基于表单的用户登录
                .formLogin();

        return http.build();
    }
    /*
    * 内存身份认证
    * 主要用于 Security 安全认证体验和测试
    * */
//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
//        // 使用内存用户信息，作为测试使用
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        /*
//        * 自定义用户认证时，可以定义用户角色 roles，也可以定义用户权限 authorities。
//        * 在进行赋值时，权限通常是在角色值的基础上添加 "ROLE_" 前缀。例如，authorities("ROLE_common")
//        * 和 roles("common")是等效的。
//        * */
//        manager.createUser(User.withUsername("shitou")
//                .password(bCryptPasswordEncoder.encode("123456"))
//                .roles("common")
//                .build());
//        /*
//        * 自定义用户认证时，可以为某用户一次指定多个角色或权限，例如 roles("common","vip")或者 authorities("ROLE_common","ROLE_vip")。
//        * */
//        manager.createUser(User.withUsername("李四")
//                .password(bCryptPasswordEncoder.encode("123456"))
//                .roles("vip")
//                .build());
//
//        return manager;
//    }
}
