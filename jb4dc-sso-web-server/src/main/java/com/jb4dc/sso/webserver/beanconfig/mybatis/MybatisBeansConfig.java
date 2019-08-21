package com.jb4dc.sso.webserver.beanconfig.mybatis;

import com.github.pagehelper.PageInterceptor;
import com.jb4dc.base.dbaccess.dynamic.GeneralMapper;
import com.jb4dc.base.service.exenum.EnableTypeEnum;
import com.jb4dc.base.dbaccess.exenum.UniversalIntEnumHandler;
import com.jb4dc.base.ymls.DBYaml;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

@Configuration
/*@MapperScan(basePackages = "com.jbuild4d.base")*/
public class MybatisBeansConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisBeansConfig.class);

    /*@Bean(destroyMethod="close")*/
    @Bean
    public ComboPooledDataSource dataSourceBean() throws PropertyVetoException {
        String driverName= DBYaml.getDriverName();
        ComboPooledDataSource comboPooledDataSource=new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(driverName);
        comboPooledDataSource.setJdbcUrl(DBYaml.getValue("Url"));
        comboPooledDataSource.setUser(DBYaml.getValue("User"));
        comboPooledDataSource.setPassword(DBYaml.getValue("Password"));
        //ComboPooledDataSource validationquery
        comboPooledDataSource.setPreferredTestQuery("SELECT 1");
        comboPooledDataSource.setAutomaticTestTable("TestConn");
        comboPooledDataSource.setIdleConnectionTestPeriod(60);
        //comboPooledDataSource.sett
        return comboPooledDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSourceBean());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSourceBean());
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PageInterceptor pageInterceptor(){
        PageInterceptor pageInterceptor=new PageInterceptor();
        Properties properties=new Properties();
        properties.getProperty("params","value1");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(PageInterceptor pageInterceptor) throws PropertyVetoException, IOException {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceBean());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatismappers/**/*.xml"));
        //sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatismappers/*.xml"));
        TypeHandler[] typeHandlers = {new UniversalIntEnumHandler(EnableTypeEnum.class)};
        sessionFactory.setTypeHandlers(typeHandlers);
        Interceptor[] interceptors={pageInterceptor};
        sessionFactory.setPlugins(interceptors);
        return sessionFactory;
    }

    @Bean
    public GeneralMapper generalMapper(SqlSessionTemplate sqlSessionTemplate) {
        return sqlSessionTemplate.getMapper(GeneralMapper.class);
    }
}
