# Spring Security 简介

使用 Spring Boot 与 Spring MVC 进行 Web 开发时，如果项目引入 spring-boot-starter-security
依赖启动器，MVC Security 安全管理功能就会自动生效，其默认的安全配置是在 SecurityAutoConfiguration 和
UserDetailsServiceAutoConfiguration 中实现的。其中，SecurityAutoConfiguration 会导入并自动化配置
SpringBootWebSecurityConfiguration 用于启动 Web 安全管理，UserDetailsServiceAutoConfiguration
则用于配置身份信息。

通过自定义 WebSecurityConfigurerAdapter(已经弃用，不推荐使用)类型的 Bean 组件，可以完全关闭 Security
提供的 Web 应用默认安全配置，但是不会关闭 UserDetailsService 用户信息自动配置类。如果要关闭 UserDetailsService
默认用户信息配置，可以自定义 UserDetailsService、AuthenticationProvider 或 AuthenticationManager
类型的 Bean 组件。另外，可以通过自定义 WebSecurityConfigurerAdapter 类型的 Bean 组件覆盖默认访问规则。
