package com.jb4dc.sso.client.remote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/24
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class SSOClientFeignClientConfig {

    @Bean
    FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
