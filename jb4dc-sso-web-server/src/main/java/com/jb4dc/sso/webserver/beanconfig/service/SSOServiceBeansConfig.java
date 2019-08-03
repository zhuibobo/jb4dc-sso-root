package com.jb4dc.sso.webserver.beanconfig.service;

import com.jb4dc.base.service.IOperationLogService;
import com.jb4dc.sso.dao.systemsetting.OperationLogMapper;
import com.jb4dc.sso.service.systemsetting.impl.OperationLogServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/31
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class SSOServiceBeansConfig {
    @Bean
    public IOperationLogService operationLogService(OperationLogMapper _defaultBaseMapper){
        return new OperationLogServiceImpl(_defaultBaseMapper);
    }
}
