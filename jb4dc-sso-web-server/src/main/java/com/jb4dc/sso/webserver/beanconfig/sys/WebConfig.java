package com.jb4dc.sso.webserver.beanconfig.sys;

import com.jb4dc.sso.core.ISSOLoginStore;
import com.jb4dc.sso.webserver.rest.interceptor.BuildJB4DCSessionInterceptor;
import com.jb4dc.sso.webserver.rest.interceptor.LoginedInterceptor;
import com.jb4dc.sso.webserver.rest.interceptor.SSOLoginedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: zhuangrb
 * @Date: 2017/12/11
 * @Description:
 * @Version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ISSOLoginStore issoLoginStore;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BuildJB4DCSessionInterceptor(issoLoginStore)).excludePathPatterns("/Js/**","/Themes/**");
        registry.addInterceptor(new SSOLoginedInterceptor()).excludePathPatterns("/Js/**","/Themes/**");
        registry.addInterceptor(new LoginedInterceptor()).excludePathPatterns("/Js/**","/Themes/**");
    }
}
