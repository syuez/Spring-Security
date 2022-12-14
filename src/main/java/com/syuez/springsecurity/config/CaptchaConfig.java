package com.syuez.springsecurity.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
/**
 * 配置一个kaptcha的实例
 */
@Configuration
public class CaptchaConfig {
    @Bean
    public Producer captcha(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.images.width", "150");
        properties.setProperty("kaptcha.images.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "abcdefghijklmnopqrstuvwxyz0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "5");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
