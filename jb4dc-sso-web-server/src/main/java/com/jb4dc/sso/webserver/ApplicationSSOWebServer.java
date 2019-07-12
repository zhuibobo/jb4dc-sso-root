package com.jb4dc.sso.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/4
 * To change this template use File | Settings | File Templates.
 */
@SpringBootApplication
@ComponentScan("com.jb4dc")
public class ApplicationSSOWebServer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationSSOWebServer.class, args);
    }
}
