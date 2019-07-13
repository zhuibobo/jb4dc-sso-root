package com.jb4dc.sso.webserver.beanconfig.mapper;

import com.jb4dc.system.setting.dao.SettingMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/11
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@MapperScan(basePackages = "com.jb4dc.**.dao.**")
public class MapperBeansConfig {

}
