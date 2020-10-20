package com.jb4dc.sso.webserver;

import com.jb4dc.sso.client.provide.ClientSessionProvide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/4
 * To change this template use File | Settings | File Templates.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.jb4dc",excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ClientSessionProvide.class}) )
@EnableFeignClients("com.jb4dc")
@EnableDiscoveryClient
public class ApplicationSSOWebServer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationSSOWebServer.class, args);
    }
}
