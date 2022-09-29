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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig  {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private DataSource dataSource;
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
                .antMatchers("/","/getUserBySession").permitAll()
                // 需要对 static 文件夹下静态资源进行统一放行
                .antMatchers("/login/**").permitAll()
                // hasRole 匹配用户是否有某一个角色，也可以用 hasAnyRole 来匹配多个角色
                .antMatchers("/detail/common/**").hasRole("common")
                .antMatchers("/detail/vip/**").hasRole("vip")
                // 匹配任何请求，并匹配已经登录认证的用户
                // 这里表示除了上述的请求外，其他请求则要求用户必须先进行登录认证
                .anyRequest().authenticated();

        // 自定义用户登录控制
        http.formLogin()
                // 指定了向自定义登录页面跳转的请求路径，并对其无条件放行
                .loginPage("/userLogin").permitAll()
                // 用来接收提交的用户名和密码
                .usernameParameter("username").passwordParameter("password")
                // 登录成功后默认跳转的路径
                .defaultSuccessUrl("/")
                // 登录失败后跳转的路径
                .failureUrl("/userLogin?error");

        // 自定义用户退出控制
        http.logout()
                .logoutUrl("/mylogout")
                .logoutSuccessUrl("/");

        // 定制 Remember-me 记住我功能
        http.rememberMe()
                .rememberMeParameter("rememberme")
                .tokenValiditySeconds(200)
                // 对 Cookie 信息进行持久化管理
                .tokenRepository(tokenRepository());

        return http.build();
    }

    /**
     * 持久化 Token 存储
     */
    @Bean
    public JdbcTokenRepositoryImpl tokenRepository() {
        JdbcTokenRepositoryImpl jr = new JdbcTokenRepositoryImpl();
        jr.setDataSource(dataSource);
        return jr;
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
