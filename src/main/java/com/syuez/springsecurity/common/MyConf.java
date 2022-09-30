package com.syuez.springsecurity.common;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * Spring Security 自定义错误信息（中文）
 */
@Component
public class MyConf {
    /*
    * http://www.sqber.com/articles/spring-security-custom-error-message.html
    * 拷贝 spring-security-core-x.x.x.jar 包下的 messages_zh_CN.properties 文件到项目的 resources 目录下，然后注册 MessageSource 即可
    * */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
